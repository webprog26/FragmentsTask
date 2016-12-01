package com.example.webprog26.fragmentstask.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.example.webprog26.fragmentstask.R;
import com.example.webprog26.fragmentstask.fragments.FragmentEditor;
import com.example.webprog26.fragmentstask.fragments.FragmentList;

/**
 * Created by webprog26 on 22.11.2016.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private static final String TAG = "SingleFragmentActivity";

    /**
     * Returns {@link Fragment} instance that will be hosted by activity
     * @return {@link Fragment}
     */
    protected abstract Fragment createFragment();

    /**
     * Returns activity's content view Id. Thus any activity, that extends {@link SingleFragmentActivity}
     * can use different content view
     * @return int
     */
    protected abstract int getLayoutResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        //Getting FragmentManager instance
        FragmentManager fragmentManager = getSupportFragmentManager();
        //Getting link to frame container, in other words - fragment placeholder
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentList);

        if(fragment == null){
            //Getting link to current Fragment instance
            fragment = createFragment();
            //Placing current Fragment instance instead placeholder, using Fragment's content view
            fragmentManager.beginTransaction().add(R.id.fragmentList, fragment).commit();
        }

        //Orientation is landscape (two-panels GUI). Empty FragmentEditor will be loaded in the same window
        // as the FragmentList will
        if(findViewById(R.id.fragmentEditor) != null){
            fragmentManager.beginTransaction().add(R.id.fragmentEditor, FragmentEditor.newInstance(FragmentEditor.NEW_ARTICLE)).commit();
        }
    }
}
