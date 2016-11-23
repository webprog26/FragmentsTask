package com.example.webprog26.fragmentstask.interfaces;

import com.example.webprog26.fragmentstask.models.Article;

/**
 * Created by webprog26 on 22.11.2016.
 */

public interface OnArticleReadyToStoreListener {

    /**
     * Implemented by host(parent) {@link com.example.webprog26.fragmentstask.activities.EditorActivity} activity.
     * Processes with article addition/ edition via separate thread
     * @param article {@link Article}
     */
    public void onArticleReady(Article article);
}
