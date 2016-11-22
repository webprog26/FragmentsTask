package com.example.webprog26.fragmentstask.activities;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.webprog26.fragmentstask.R;
import com.example.webprog26.fragmentstask.fragments.FragmentEditor;
import com.example.webprog26.fragmentstask.interfaces.OnArticleReadyToStoreListener;
import com.example.webprog26.fragmentstask.models.Article;
import com.example.webprog26.fragmentstask.threads.ArticleStoringThread;
import com.example.webprog26.fragmentstask.threads.ArticleUpdateThread;

public class EditorActivity extends SingleFragmentActivity implements OnArticleReadyToStoreListener{

    private static final String TAG = "EditorActivity_TAG";

    @Override
    protected Fragment createFragment() {
        return new FragmentEditor();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_editor;
    }

    @Override
    public void onArticleReady(Article article) {
        Log.i(TAG, "article.getArticleId(): " + article.getArticleId());
        if(article.getArticleId() == FragmentEditor.NEW_ARTICLE){
            new ArticleStoringThread(article, this).start();
        } else {
            new ArticleUpdateThread(this, article).start();
        }
    }
}

