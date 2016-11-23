package com.example.webprog26.fragmentstask.threads;

import android.app.Activity;
import android.util.Log;

import com.example.webprog26.fragmentstask.models.Article;
import com.example.webprog26.fragmentstask.providers.DBProvider;

/**
 * Created by webprog26 on 22.11.2016.
 */

public class ArticleUpdateThread extends Thread {

    private static final String TAG = "ArticleUpdateThread";

    private DBProvider mDbProvider;
    private Article mArticle;

    public ArticleUpdateThread(Activity activity, Article mArticle) {
        this.mDbProvider = new DBProvider(activity);
        this.mArticle = mArticle;
    }

    @Override
    public void run() {
        super.run();
        updateArticle();
        if(!isInterrupted()){
            interrupt();
        }
    }

    /**
     * Updates existing {@link Article} in database via {@link DBProvider} method updateArticle(Article article)
     */
    private void updateArticle(){
        Log.i(TAG, "text: " + mArticle.getArticleText());
        mDbProvider.updateArticle(mArticle);
    }
}
