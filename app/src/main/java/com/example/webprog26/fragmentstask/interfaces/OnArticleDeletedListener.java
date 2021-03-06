package com.example.webprog26.fragmentstask.interfaces;

import com.example.webprog26.fragmentstask.models.Article;

/**
 * Created by webprog26 on 22.11.2016.
 */

public interface OnArticleDeletedListener {

    /**
     * Used in anonymous {@link com.example.webprog26.fragmentstask.threads.ArticleDeleteThread}
     * in {@link com.example.webprog26.fragmentstask.adapters.ArticlesAdapter}
     * removes swiped {@link Article} from list, calls notifyItemRemoved() method of adapter
     * @param article {@link Article}
     */
    void onArticleDeleted(Article article);
}
