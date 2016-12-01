package com.example.webprog26.fragmentstask.click_handlers;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.example.webprog26.fragmentstask.R;
import com.example.webprog26.fragmentstask.activities.EditorActivity;
import com.example.webprog26.fragmentstask.fragments.FragmentEditor;


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
                //Calling EditorActivity from MainActivity to open FragmentEditor
                    Intent intent = new Intent(mActivity, EditorActivity.class);
                    intent.putExtra(FragmentEditor.ARTICLE_ID, FragmentEditor.NEW_ARTICLE);
                    mActivity.startActivity(intent);
                break;
            case R.id.btnCancel:
                //Finishing EditorActivity. Returning to Main Activity
                    mActivity.finish();
                break;
        }
    }
}
