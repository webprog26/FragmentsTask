package com.example.webprog26.fragmentstask.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.example.webprog26.fragmentstask.R;
import com.example.webprog26.fragmentstask.fragments.FragmentEditor;
import com.example.webprog26.fragmentstask.fragments.FragmentList;
import com.example.webprog26.fragmentstask.interfaces.OnArticleListUpdatedListener;
import com.example.webprog26.fragmentstask.interfaces.OnArticleToEditListener;
import com.example.webprog26.fragmentstask.models.Article;

public class MainActivity extends SingleFragmentActivity implements OnArticleToEditListener, OnArticleListUpdatedListener {

    private static final String TAG = "MainActivity_TAG";

    @Override
    protected Fragment createFragment() {
        return new FragmentList();
    }

    @Override
    protected int getLayoutResId() {
//        return R.layout.activity_main;
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onEditArticle(Article article) {
        //On articles list click event received. Depending on the device orientation:
        //portrait or landscape we're calling EditorActivity (portrait orientation, single-panel GUI),
        // with 0 (constant FragmentEditor.NEW_ARTICLE = 0) as a parameter, thus FragmentEditor will be opened in a new window
        if(findViewById(R.id.fragmentEditor) == null){
            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra(FragmentEditor.ARTICLE_ID, article.getArticleId());
            startActivity(intent);
        } else {
            //If orientation is landscape, FragmentEditor will be opened in the same window
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragmentEditor = fragmentManager.findFragmentById(R.id.fragmentEditor);
            Fragment newFragmentEditor = FragmentEditor.newInstance(article.getArticleId());
            if(fragmentEditor != null){
                fragmentTransaction.remove(fragmentEditor);
            }

            fragmentTransaction.add(R.id.fragmentEditor, newFragmentEditor);
            fragmentTransaction.commit();
        }
    }

    /**
     * Used in two-panels GUI only. Implemented by host (parent) activity ({@link com.example.webprog26.fragmentstask.activities.MainActivity}),
     * calls fragment to update it's UI by updating list with newly-added Article
     */
    @Override
    public void onArticleListUpdated() {
        FragmentList fragmentList = (FragmentList) getSupportFragmentManager().findFragmentById(R.id.fragmentList);
        fragmentList.updateUI();
    }
}
