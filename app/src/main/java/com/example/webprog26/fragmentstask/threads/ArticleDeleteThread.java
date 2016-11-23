package com.example.webprog26.fragmentstask.threads;

import android.app.Activity;
import android.util.Log;

import com.example.webprog26.fragmentstask.interfaces.OnArticleDeletedListener;
import com.example.webprog26.fragmentstask.models.Article;
import com.example.webprog26.fragmentstask.providers.DBProvider;

/**
 * Created by webprog26 on 22.11.2016.
 */

public class ArticleDeleteThread extends Thread {

    private static final String TAG = "ArticleDeleteThread";

    private DBProvider mDbProvider;
    private Article mArticle;
    private OnArticleDeletedListener mOnArticleDeletedListener;

    public ArticleDeleteThread(Activity activity, Article article, OnArticleDeletedListener onArticleDeletedListener) {
        this.mDbProvider = new DBProvider(activity);
        this.mArticle = article;
        this.mOnArticleDeletedListener = onArticleDeletedListener;
    }

    @Override
    public void run() {
        super.run();
        deleteArticle();
        if(!isInterrupted()){
            interrupt();
        }
    }

    /**
     * Deletes {@link Article} in database via {@link DBProvider} method deleteArticleById(long articleId)
     */
    private void deleteArticle(){
        Log.i(TAG, "Deleting article with Id: " + mArticle.getArticleId());
        mDbProvider.deleteArticleById(mArticle.getArticleId());
        mOnArticleDeletedListener.onArticleDeleted(mArticle);
    }
}
