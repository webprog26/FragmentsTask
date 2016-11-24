package com.example.webprog26.fragmentstask.interfaces;

import com.example.webprog26.fragmentstask.models.Article;

/**
 * Created by webprog26 on 23.11.2016.
 */

public interface OnArticleListUpdatedListener {
    /**
     * Used in two-panels GUI only. Implemented by host (parent) activity ({@link com.example.webprog26.fragmentstask.activities.MainActivity}),
     * calls fragment to update it's UI by updating list with newly-added Article
     * via calling new instance of {@link com.example.webprog26.fragmentstask.fragments.FragmentList.AsyncTitlesLoadingTask} to execute
     */
    void onArticleListUpdated();
}
