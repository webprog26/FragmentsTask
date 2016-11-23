package com.example.webprog26.fragmentstask.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.webprog26.fragmentstask.R;
import com.example.webprog26.fragmentstask.adapters.ArticlesAdapter;
import com.example.webprog26.fragmentstask.click_handlers.ButtonOnClickHandler;
import com.example.webprog26.fragmentstask.interfaces.OnArticleListClickListener;
import com.example.webprog26.fragmentstask.interfaces.OnArticleToEditListener;
import com.example.webprog26.fragmentstask.models.Article;
import com.example.webprog26.fragmentstask.providers.DBProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by webprog26 on 22.11.2016.
 */

public class FragmentList extends Fragment implements OnArticleListClickListener{

    private static final String TAG = "FragmentList";

    private RecyclerView mArticlesRecyclerView;
    private List<Article> mArticles;
    private ArticlesAdapter mAdapter;
    private RelativeLayout mListContainer;

    private OnArticleToEditListener mOnArticleToEditListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArticles = new ArrayList<>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnArticleToEditListener){
            mOnArticleToEditListener = (OnArticleToEditListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //In this moment of Fragment's lifecycle we're loading list of Articles
        //to show it's titles in A RecyclerView via ArticlesAdapter
        new AsyncTitlesLoadingTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListContainer = (RelativeLayout) view.findViewById(R.id.listContainer);

        mAdapter = new ArticlesAdapter(getActivity(), mArticles, FragmentList.this, mListContainer);

        mArticlesRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mArticlesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mArticlesRecyclerView.setHasFixedSize(true);
        mArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton btnAdArticle = (FloatingActionButton) view.findViewById(R.id.btnAddArticle);
        btnAdArticle.setOnClickListener(new ButtonOnClickHandler(getActivity()));
        Log.i(TAG, "onViewCreated " + FragmentList.this.getClass().getSimpleName());

        mArticlesRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onArticleListClick(Article article) {
        if(mOnArticleToEditListener != null){
            mOnArticleToEditListener.onEditArticle(article);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Nulling reference, previosly saved in onAttach() to avoid possible memory leaks
        this.mOnArticleToEditListener = null;
    }

    //This class loads existing Articles from database asynchronously & transfers it
    //to ArticlesAdapter
    private class AsyncTitlesLoadingTask extends AsyncTask<Void, Void, List<Article>>{

        @Override
        protected List<Article> doInBackground(Void... voids) {
            DBProvider dbProvider = new DBProvider(getActivity());
            return dbProvider.getArticles();
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            super.onPostExecute(articles);
            mArticles = articles;
            mAdapter.updateList(mArticles);
        }
    }
}
