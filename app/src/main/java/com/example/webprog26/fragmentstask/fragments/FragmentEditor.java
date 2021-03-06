package com.example.webprog26.fragmentstask.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.webprog26.fragmentstask.R;
import com.example.webprog26.fragmentstask.click_handlers.ButtonOnClickHandler;
import com.example.webprog26.fragmentstask.interfaces.OnArticleListUpdatedListener;
import com.example.webprog26.fragmentstask.interfaces.OnArticleReadyToStoreListener;
import com.example.webprog26.fragmentstask.models.Article;
import com.example.webprog26.fragmentstask.providers.DBProvider;
import com.example.webprog26.fragmentstask.threads.ArticleStoringThread;
import com.example.webprog26.fragmentstask.threads.ArticleUpdateThread;

/**
 * Created by webprog26 on 22.11.2016.
 */

public class FragmentEditor extends Fragment{

    private static final String TAG = "FragmentEditor";

    private EditText mEtArticleTitle, mEtArticleText;
    private OnArticleReadyToStoreListener mArticleReadyToStoreListener;
    private OnArticleListUpdatedListener mOnArticleListUpdatedListener;
    private long mArticleId;

    //Constants to transfer articleId while creating or editing Article via Bundle
    public static final String ARTICLE_ID = "com.example.webprog26.fragmentstask.article_id";
    public static final long NEW_ARTICLE = 0;

    public static FragmentEditor newInstance(long articleId){
        Bundle bundle = new Bundle();
        FragmentEditor fragmentEditor = new FragmentEditor();
        bundle.putLong(ARTICLE_ID, articleId);
        fragmentEditor.setArguments(bundle);

        return fragmentEditor;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mArticleId = getArguments().getLong(ARTICLE_ID, NEW_ARTICLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnArticleReadyToStoreListener){
            mArticleReadyToStoreListener = (OnArticleReadyToStoreListener) context;
        }

        if(context instanceof OnArticleListUpdatedListener){
            mOnArticleListUpdatedListener = (OnArticleListUpdatedListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editor, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEtArticleTitle = (EditText) view.findViewById(R.id.etTitle);
        mEtArticleText = (EditText) view.findViewById(R.id.etText);

        if(mArticleId != NEW_ARTICLE){
            new SingleArticleAsyncLoadingTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, mArticleId);
        }

        Button btnSaveArticle = (Button) view.findViewById(R.id.btnSave);

        btnSaveArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!areTextFieldsNotEmpty(mEtArticleTitle, mEtArticleText)) return;

                Article.Builder builder = Article.newBuilder();
                builder.setArticleId(mArticleId)
                        .setArticleTitle(mEtArticleTitle.getText().toString())
                        .setArticleText(mEtArticleText.getText().toString());
                final Article article = builder.build();


                Log.i(TAG, "Article " + article.getArticleTitle() + ", " + article.getArticleText());

                if(doesActivityImplementsOnArticleReadyToStoreListener()){
                    mArticleReadyToStoreListener.onArticleReady(article);
                } else{
                    new OnArticleReadyToStoreListener(){
                        @Override
                        public void onArticleReady(Article article) {
                            if(article.getArticleId() == FragmentEditor.NEW_ARTICLE){
                                //Since articleId matches FragmentEditor.NEW_ARTICLE
                                //we store this article as a new one using separate java.lang.Thread
                                new ArticleStoringThread(article, getActivity()).start();
                            } else {
                                //Since articleId matches some article, that already exists
                                //in the database, we're editing this article java.lang.Thread
                                new ArticleUpdateThread(article, getActivity()).start();

                            }
                        }
                    }.onArticleReady(article);
                    mOnArticleListUpdatedListener.onArticleListUpdated();
                    mEtArticleTitle.setText("");
                    mEtArticleText.setText("");
                }

            }
        });
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        if(doesActivityImplementsOnArticleReadyToStoreListener()){
            btnCancel.setOnClickListener(new ButtonOnClickHandler(getActivity()));
        } else {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mEtArticleTitle.setText("");
                    mEtArticleText.setText("");
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Nulling reference, previously saved in onAttach() to avoid possible memory leaks
        if(this.mArticleReadyToStoreListener != null){
            this.mArticleReadyToStoreListener = null;
        }

        if(this.mOnArticleListUpdatedListener != null){
            mOnArticleListUpdatedListener = null;
        }
    }

    /**
     * Checks that {@link EditText} fields aren't empty
     * @param firstField {@link EditText}
     * @param secondField {@link EditText}
     * @return boolean
     */
    private boolean areTextFieldsNotEmpty(EditText firstField, EditText secondField){
        return firstField.length() > 0 && secondField.length() > 0;
    }

    //This class loads editable Article title & text from data base asynchronously
    //& places values to EditText fields
    private class SingleArticleAsyncLoadingTask extends AsyncTask<Long, Void, Article>{

        @Override
        protected Article doInBackground(Long... longs) {
            DBProvider dbProvider = new DBProvider(getActivity());
            return dbProvider.getArticleById(longs[0]);
        }

        @Override
        protected void onPostExecute(Article article) {
            super.onPostExecute(article);
            mEtArticleTitle.setText(article.getArticleTitle());
            mEtArticleText.setText(article.getArticleText());
        }
    }

    /**
     * Checks does host (parent) Activity implements OnArticleReadyToStoreListener
     * @return boolean
     */
    private boolean doesActivityImplementsOnArticleReadyToStoreListener(){
        return getActivity() instanceof OnArticleReadyToStoreListener;
    }
}
