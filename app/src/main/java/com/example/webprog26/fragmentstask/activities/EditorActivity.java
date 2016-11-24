package com.example.webprog26.fragmentstask.activities;

import android.support.v4.app.Fragment;

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
        return FragmentEditor.newInstance(getIntent().getLongExtra(FragmentEditor.ARTICLE_ID, FragmentEditor.NEW_ARTICLE));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_editor;
    }

    @Override
    public void onArticleReady(Article article) {
        if(article.getArticleId() == FragmentEditor.NEW_ARTICLE){
            //Since articleId matches FragmentEditor.NEW_ARTICLE
            //we store this article as a new one using separate java.lang.Thread
            new ArticleStoringThread(article, this).start();
        } else {
            //Since articleId matches some article, that already exists
            //in the database, we're editing this article java.lang.Thread
            new ArticleUpdateThread(article, this).start();
        }
        finish();
    }
}

