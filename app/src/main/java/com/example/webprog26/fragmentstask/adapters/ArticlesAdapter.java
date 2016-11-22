package com.example.webprog26.fragmentstask.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.webprog26.fragmentstask.R;
import com.example.webprog26.fragmentstask.interfaces.ItemTouchHelperViewHolder;
import com.example.webprog26.fragmentstask.interfaces.OnArticleDeletedListener;
import com.example.webprog26.fragmentstask.interfaces.OnArticleListClickListener;
import com.example.webprog26.fragmentstask.models.Article;
import com.example.webprog26.fragmentstask.threads.ArticleDeleteThread;

import java.util.Collections;
import java.util.List;

/**
 * Created by webprog26 on 22.11.2016.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder> {

    private static final String TAG = "ArticlesAdapter";

    public class ArticlesViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{

        @Override
        public void onItemSelected() {}

        @Override
        public void onItemClear() {}

        private TextView mTvArticleTitle;

        public ArticlesViewHolder(View itemView) {
            super(itemView);
            mTvArticleTitle = (TextView) itemView.findViewById(R.id.tvArticleTitle);
        }

        public void bind(final Article article, final OnArticleListClickListener listener){

            mTvArticleTitle.setText(article.getArticleTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onArticleListClick(article);
                }
            });
        }
    }

    private List<Article> mArticleList;
    private OnArticleListClickListener mListener;
    private RelativeLayout mListContainer;
    private Activity mActivity;

    public ArticlesAdapter(Activity activity, List<Article> mArticleList, OnArticleListClickListener mListener, RelativeLayout listContainer) {
        this.mActivity = activity;
        this.mArticleList = mArticleList;
        this.mListener = mListener;
        this.mListContainer = listContainer;
    }

    @Override
    public ArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticlesViewHolder holder, int position) {
        holder.bind(mArticleList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

    public void updateList(List<Article> data){
        mArticleList = data;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if(viewHolder.getItemViewType() != target.getItemViewType()){
                    return false;
                }
                onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int adapterPosition = viewHolder.getAdapterPosition();
                final Article article = mArticleList.get(adapterPosition);

                Snackbar snackbar = Snackbar.make(mListContainer, R.string.delete_item,
                        Snackbar.LENGTH_LONG).setAction(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mArticleList.add(adapterPosition, article);
                        notifyItemInserted(adapterPosition);
                    }
                }).setActionTextColor(Color.RED);
                snackbar.show();
                new ArticleDeleteThread(mActivity, article, new OnArticleDeletedListener() {
                    @Override
                    public void onArticleDeleted(Article article) {
                        mArticleList.remove(article);
                        notifyItemRemoved(adapterPosition);
                    }
                }).start();
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
                    if(viewHolder instanceof ItemTouchHelperViewHolder){
                        ItemTouchHelperViewHolder itemTouchHelperViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                        itemTouchHelperViewHolder.onItemSelected();

                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setAlpha(1.0F);
                if (viewHolder instanceof ItemTouchHelperViewHolder) {
                    ItemTouchHelperViewHolder itemTouchHelperViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                    itemTouchHelperViewHolder.onItemClear();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if(fromPosition < toPosition){
            for(int i = fromPosition; i < toPosition; i++){
                Collections.swap(mArticleList, i, i + 1);
            }
        } else {
            for(int i = fromPosition; i > toPosition; i--){
                Collections.swap(mArticleList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

}
