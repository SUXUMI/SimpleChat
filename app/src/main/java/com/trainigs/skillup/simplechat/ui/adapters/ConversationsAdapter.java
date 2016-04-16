package com.trainigs.skillup.simplechat.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import com.trainigs.skillup.simplechat.R;

/**
 * Created by Irakli on 4/16/2016
 */
public class ConversationsAdapter extends RecyclerViewCursorAdapter<ConversationsAdapter.MyViewHolder> {


    /**
     * Constructor.
     *
     * @param context The Context the Adapter is displayed in.
     */
    protected ConversationsAdapter(Context context) {
        super(context);

        setupCursorAdapter(null, 0, R.layout.conversations_list_item, false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);

        setViewHolder(holder);

        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }

    public static class MyViewHolder extends RecyclerViewCursorViewHolder {

        /**
         * Constructor.
         *
         * @param view The root view of the ViewHolder.
         */
        public MyViewHolder(View view) {
            super(view);
        }

        @Override
        public void bindCursor(Cursor cursor) {

        }
    }
}