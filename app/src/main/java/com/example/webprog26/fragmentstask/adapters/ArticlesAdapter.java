package com.example.webprog26.fragmentstask.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.webprog26.fragmentstask.R;
import com.example.webprog26.fragmentstask.interfaces.ItemDeletingListener;
import com.example.webprog26.fragmentstask.interfaces.ItemTouchHelperViewHolder;
import com.example.webprog26.fragmentstask.interfaces.OnArticleDeletedListener;
import com.example.webprog26.fragmentstask.interfaces.OnArticleListClickListener;
import com.example.webprog26.fragmentstask.models.Article;
import com.example.webprog26.fragmentstask.threads.ArticleDeleteThread;
import com.example.webprog26.fragmentstask.threads.ArticleUpdateThread;

import java.util.Collections;
import java.util.List;

/**
 * Created by webprog26 on 22.11.2016.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder> implements ItemDeletingListener {

    private static final String TAG = "ArticlesAdapter";

    public class ArticlesViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{

        @SuppressWarnings("deprecation")
        @Override
        public void onItemSelected() {
            mDefaultDrawable = itemView.getBackground();
            itemView.findViewById(R.id.cardView).setBackground(mActivity.getResources().getDrawable(R.drawable.selected_item));
        }

        @Override
        public void onItemClear() {
            itemView.findViewById(R.id.cardView).setBackground(mDefaultDrawable);
        }

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
    private Drawable mDefaultDrawable;
    private boolean isItemDeleted = false;

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
                isItemDeleted = true;
                Log.i(TAG, "isItemDeleted swiped " + isItemDeleted);
                final int adapterPosition = viewHolder.getAdapterPosition();
                final Article article = mArticleList.get(adapterPosition);

                Snackbar snackbar = Snackbar.make(mListContainer, R.string.delete_item,
                        Snackbar.LENGTH_LONG).setAction(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mArticleList.add(adapterPosition, article);
                        notifyItemInserted(adapterPosition);
                        isItemDeleted = false;
                        Log.i(TAG, "isItemDeleted canceled " + isItemDeleted);
                    }
                }).setActionTextColor(Color.RED);
                snackbar.show();
                snackbar.setCallback(new Snackbar.Callback() {
                    /**
                     * Called when the given {@link Snackbar} has been dismissed, either through a time-out,
                     * having been manually dismissed, or an action being clicked.
                     *
                     * @param snackbar The snackbar which has been dismissed.
                     * @param event    The event which caused the dismissal. One of either:
                     *                 {@link #DISMISS_EVENT_SWIPE}, {@link #DISMISS_EVENT_ACTION},
                     *                 {@link #DISMISS_EVENT_TIMEOUT}, {@link #DISMISS_EVENT_MANUAL} or
                     *                 {@link #DISMISS_EVENT_CONSECUTIVE}.
                     * @see Snackbar#dismiss()
                     */
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        deleteItem(isItemDeleted, article, adapterPosition);
                    }
                });

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
//                viewHolder.itemView.setAlpha(1.0F);
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

    @Override
    public void deleteItem(boolean isDeleted, Article article, final int adapterPosition) {
        Log.i(TAG, "isDeleted in method " + isDeleted);
        if(isDeleted){
            new ArticleDeleteThread(mActivity, article, new OnArticleDeletedListener() {
                @Override
                public void onArticleDeleted(Article article) {
                    mArticleList.remove(article);
                    notifyItemRemoved(adapterPosition);
                }
            }).start();
        }
    }
}
