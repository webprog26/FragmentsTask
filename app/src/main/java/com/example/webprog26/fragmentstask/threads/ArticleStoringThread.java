package com.example.webprog26.fragmentstask.threads;

import android.app.Activity;
import android.util.Log;

import com.example.webprog26.fragmentstask.models.Article;
import com.example.webprog26.fragmentstask.providers.DBProvider;

/**
 * Created by webprog26 on 22.11.2016.
 */

public class ArticleStoringThread extends Thread {

    private static final String TAG = "ArticleStoringThread";

    private Article mArticle;
    private DBProvider mDbProvider;

    public ArticleStoringThread(Article mArticle, Activity activity) {
        this.mArticle = mArticle;
        this.mDbProvider = new DBProvider(activity);
    }

    @Override
    public void run() {
        storeArticleToDatabase();
        if(!isInterrupted()){
            interrupt();
        }
        super.run();
    }

    /**
     * Inserts newly-created {@link Article} to database via {@link DBProvider} method insertArticleToDB(Article article)
     */
    private void storeArticleToDatabase(){
        Log.i(TAG, this.getName() + " writing: " + mArticle.getArticleTitle());
        mDbProvider.insertArticleToDB(mArticle);
    }
}
