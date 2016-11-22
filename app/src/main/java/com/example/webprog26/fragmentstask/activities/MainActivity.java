package com.example.webprog26.fragmentstask.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;


import com.example.webprog26.fragmentstask.R;
import com.example.webprog26.fragmentstask.fragments.FragmentEditor;
import com.example.webprog26.fragmentstask.fragments.FragmentList;
import com.example.webprog26.fragmentstask.interfaces.OnArticleToEditListener;
import com.example.webprog26.fragmentstask.models.Article;

public class MainActivity extends SingleFragmentActivity implements OnArticleToEditListener {

    private static final String TAG = "MainActivity_TAG";

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart(): " + MainActivity.this.getClass().getSimpleName());
    }

    @Override
    protected Fragment createFragment() {
        Log.i(TAG, "createFragment(): " + MainActivity.this.getClass().getSimpleName());
        return new FragmentList();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void onEditArticle(Article article) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra(FragmentEditor.ARTICLE_ID, article.getArticleId());
        startActivity(intent);
    }
}
