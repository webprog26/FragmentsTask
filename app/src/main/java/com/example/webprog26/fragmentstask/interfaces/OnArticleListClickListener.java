package com.example.webprog26.fragmentstask.interfaces;

import com.example.webprog26.fragmentstask.models.Article;

/**
 * Created by webprog26 on 22.11.2016.
 */

public interface OnArticleListClickListener {

    /**
     * Implemented in {@link com.example.webprog26.fragmentstask.fragments.FragmentList},
     * used in adapter to handle articles list onClick events
     * @param article {@link Article}
     */
    public void onArticleListClick(Article article);
}
