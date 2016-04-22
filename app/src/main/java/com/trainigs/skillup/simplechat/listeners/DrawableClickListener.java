package com.trainigs.skillup.simplechat.listeners;

/**
 * Created by Irakli on 4/22/2016
 */
public interface DrawableClickListener {
    public static enum DrawablePosition {TOP, BOTTOM, LEFT, RIGHT}

    ;

    public void onClick(DrawablePosition target);
}