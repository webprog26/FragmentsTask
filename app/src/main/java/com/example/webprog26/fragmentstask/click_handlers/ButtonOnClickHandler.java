package com.example.webprog26.fragmentstask.click_handlers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.webprog26.fragmentstask.R;
import com.example.webprog26.fragmentstask.activities.EditorActivity;
import com.example.webprog26.fragmentstask.activities.MainActivity;
import com.example.webprog26.fragmentstask.fragments.FragmentEditor;
import com.example.webprog26.fragmentstask.models.Article;

/**
 * Created by webprog26 on 22.11.2016.
 */

public class ButtonOnClickHandler implements View.OnClickListener {

    private static final String TAG = "ButtonOnClickHandler";

    private Activity mActivity;

    public ButtonOnClickHandler(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddArticle:
                Log.i(TAG, "btnAddArticle clicked");
                Intent intent = new Intent(mActivity, EditorActivity.class);
                intent.putExtra(FragmentEditor.ARTICLE_ID, FragmentEditor.NEW_ARTICLE);
                mActivity.startActivity(intent);
                break;
            case R.id.btnCancel:
                mActivity.finish();
                break;
        }
    }
}
