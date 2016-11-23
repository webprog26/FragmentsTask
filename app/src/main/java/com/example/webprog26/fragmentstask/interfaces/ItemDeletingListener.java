package com.example.webprog26.fragmentstask.interfaces;

import com.example.webprog26.fragmentstask.models.Article;

/**
 * Created by webprog26 on 23.11.2016.
 */

public interface ItemDeletingListener {

    public void deleteItem(boolean isDeleted, Article article, final int adapterPosition);
}
