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
import com.example.webprog26.fragmentstask.interfaces.OnArticleReadyToStoreListener;
import com.example.webprog26.fragmentstask.models.Article;
import com.example.webprog26.fragmentstask.providers.DBProvider;

/**
 * Created by webprog26 on 22.11.2016.
 */

public class FragmentEditor extends Fragment{

    private static final String TAG = "FragmentEditor";

    private EditText mEtArticleTitle, mEtArticleText;
    private OnArticleReadyToStoreListener mArticleReadyToStoreListener;
    private long mArticleId;

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

        Log.i(TAG, "Article id " + mArticleId);

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

                if(mArticleReadyToStoreListener != null){
                    mArticleReadyToStoreListener.onArticleReady(builder.build());
                }
                getActivity().finish();
            }
        });
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new ButtonOnClickHandler(getActivity()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Nulling reference, previosly saved in onAttach() to avoid possible memory leaks
        this.mArticleReadyToStoreListener = null;
    }

    private boolean areTextFieldsNotEmpty(EditText firstField, EditText secondField){
        return firstField.length() > 0 && secondField.length() > 0;
    }

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
}
