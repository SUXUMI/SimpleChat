package com.trainigs.skillup.simplechat.paho;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.trainigs.skillup.simplechat.db.ChatContract;
import com.trainigs.skillup.simplechat.models.Conversation;
import com.trainigs.skillup.simplechat.models.Message;
import com.trainigs.skillup.simplechat.ui.MyApplication;
import com.trainigs.skillup.simplechat.utils.Constants;
import com.trainigs.skillup.simplechat.utils.Utils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Connection {

    enum ConnectionStatus {
        /**
         * Client is Connecting
         **/
        CONNECTING,
        /**
         * Client is Connected
         **/
        CONNECTED,
        /**
         * Client is Disconnecting
         **/
        DISCONNECTING,
        /**
         * Client is Disconnected
         **/
        DISCONNECTED,
        /**
         * Client has encountered an Error
         **/
        ERROR,
        /**
         * Status is unknown
         **/
        NONE
    }

    private static Connection instance;

    private String clientHandle = null;

    private String clientId = null;

    private String host = null;

    private int port = 0;

    private ConnectionStatus status = ConnectionStatus.NONE;

    private MqttAndroidClient client = null;

//    private ArrayList<PropertyChangeListener> listeners = new ArrayList<>();

    private Context context = null;

    private MqttConnectOptions conOpt;

    private boolean sslConnection = false;

    private static long currentConversationId;

    synchronized public static Connection getInstance(Context context) {
        if (instance == null) {
            instance = new Connection(context);
        }
        return instance;
    }

    synchronized public static void deleteInstance() {
        if (instance != null) {
//            if (instance.listeners != null)
//                instance.listeners.clear();
            instance = null;
        }
    }

    public long getCurrentConversationId() {
        return currentConversationId;
    }

    public void setCurrentConversationId(long currentConversationId) {
        this.currentConversationId = currentConversationId;
    }

    MqttCallback clientCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable throwable) {
            Log.i("SimpleChat", "connectionLost");
            changeConnectionStatus(ConnectionStatus.DISCONNECTED);
            if (Utils.isNetworkAvailable()) {
                Connection.getInstance(MyApplication.getInstance()).connect();
            }
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            try {
                JSONObject jsonObject = new JSONObject(mqttMessage.toString());
                String number = jsonObject.getString("number");
                String content = jsonObject.getString("content");
                Conversation conversation = ChatContract.Conversation.getConversationByNumber(MyApplication.getInstance(), number);
                if (conversation == null) {
                    conversation = new Conversation();
                    conversation.setLastMessage(content);
                    conversation.setOwner(number);
                    conversation.setLastMessageDate(new Date());
                    conversation.setTitle(number);

                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
                    Cursor cursor = context.getContentResolver().query(uri,
                            new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI}, null, null,
                            ContactsContract.PhoneLookup.DISPLAY_NAME + " ASC");
                    if (cursor != null) {
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            conversation.setTitle(cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)));
                            conversation.setImageUri(cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI)));
                        }
                        cursor.close();
                    }

                    int id = ChatContract.Conversation.save(MyApplication.getInstance(), conversation);
                    conversation.setId(id);
                }
                Message message = new Message();
                message.setContent(content);
                message.setOwner(number);
                message.setConversationId(conversation.getId());
                message.setType(0);
                ChatContract.Message.save(MyApplication.getInstance(), message);

            } catch (JSONException ignore) {
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            Log.i("SimpleChat", "deliveryComplete");
        }
    };

    private Connection(Context context) {
        //generate the client handle from its hash code

        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        this.context = context;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy mm:hh");
        clientHandle = dateFormat.format(new Date());
        clientId = clientHandle;
        host = Constants.MESSAGES_HOST;
        port = 1883;
        conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(false);
        status = ConnectionStatus.DISCONNECTED;
        createNewClient();
    }

    public void subscribe() {
        try {
            client.subscribe(MyApplication.getInstance().getNumber(), 1, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Log.i("SimpleChat", "subscribe Success " + MyApplication.getInstance().getNumber());
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    //                                    if (Utils.isNetworkAvailable() && MyApplication.getInstance().getValue(Utils.SIGNED_PREFERENCE_KEY, false))
                    //                                        connect();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    IMqttActionListener connectActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken iMqttToken) {
            Log.i("SimpleChat", "MQTT Connected");
            client.setCallback(clientCallback);
            subscribe();
            status = ConnectionStatus.CONNECTED;
        }

        @Override
        public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
            status = ConnectionStatus.DISCONNECTED;
            Log.i("SimpleChat", "MQTT Failed");
        }
    };

    private void createNewClient() {
        String uri = "tcp://" + Constants.MESSAGES_HOST + ":1883";
        client = new MqttAndroidClient(context, uri, clientId);
        client.setTraceCallback(new MqttTraceCallback());
    }

    public void disconnect() {
        try {
            if (status != ConnectionStatus.DISCONNECTED) {
                changeConnectionStatus(ConnectionStatus.DISCONNECTED);
                client.disconnect(null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken iMqttToken) {
//                        Connection.getInstance(MyApplication.getInstance()).changeConnectionStatus(ConnectionStatus.DISCONNECTED);
//                        Connection.deleteInstance();
//                        client.close();
                    }

                    @Override
                    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

                    }
                });
            }
            Connection.getInstance(MyApplication.getInstance()).changeConnectionStatus(ConnectionStatus.DISCONNECTED);
//            Connection.deleteInstance();
        } catch (MqttException ignore) {
        }
    }

    public void connect() {
        try {
            if (status == ConnectionStatus.DISCONNECTED) {
                status = ConnectionStatus.CONNECTING;
                client.connect(conOpt, null, connectActionListener);
            }
        } catch (MqttException ignore) {
        }
    }

    public void sendMessage(String mqttTopicText, String mqttMessageText, IMqttActionListener iMqttActionListener) {
        try {
            if (status == ConnectionStatus.CONNECTED && client != null)
                client.publish(mqttTopicText, mqttMessageText.getBytes(), 1, false, null, iMqttActionListener);
        } catch (MqttException ignore) {
        }
    }

    public String handle() {
        return clientHandle;
    }

    public boolean isConnected() {
        return status == ConnectionStatus.CONNECTED;
    }

    public void changeConnectionStatus(ConnectionStatus connectionStatus) {
        status = connectionStatus;
    }

//    @Override
//    public String toString() {
//        StringBuffer sb = new StringBuffer();
//        sb.append(clientId);
//        sb.append("\n ");
//
//        switch (status) {
//
//            case CONNECTED:
//                sb.append(context.getString(R.string.connectedto));
//                break;
//            case DISCONNECTED:
//                sb.append(context.getString(R.string.disconnected));
//                break;
//            case NONE:
//                sb.append(context.getString(R.string.no_status));
//                break;
//            case CONNECTING:
//                sb.append(context.getString(R.string.connecting));
//                break;
//            case DISCONNECTING:
//                sb.append(context.getString(R.string.disconnecting));
//                break;
//            case ERROR:
//                sb.append(context.getString(R.string.connectionError));
//        }
//        sb.append(" ");
//        sb.append(host);
//
//        return sb.toString();
//    }

    public boolean isHandle(String handle) {
        return clientHandle.equals(handle);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Connection)) {
            return false;
        }

        Connection c = (Connection) o;

        return clientHandle.equals(c.clientHandle);

    }

    public String getId() {
        return clientId;
    }

    public String getHostName() {
        return host;
    }

    public boolean isConnectedOrConnecting() {
        return (status == ConnectionStatus.CONNECTED) || (status == ConnectionStatus.CONNECTING);
    }

    public boolean noError() {
        return status != ConnectionStatus.ERROR;
    }

    public MqttAndroidClient getClient() {
        return client;
    }

    public void addConnectionOptions(MqttConnectOptions connectOptions) {
        conOpt = connectOptions;
    }

    public MqttConnectOptions getConnectionOptions() {
        return conOpt;
    }

    public int getPort() {
        return port;
    }

    public int isSSL() {
        return sslConnection ? 1 : 0;
    }
}