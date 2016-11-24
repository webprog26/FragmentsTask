package com.example.webprog26.fragmentstask.interfaces;

import com.example.webprog26.fragmentstask.models.Article;

/**
 * Created by webprog26 on 23.11.2016.
 */

public interface ItemDeletingListener {

    /**
     * Implemented by adapter. Processes with deleting article asynchronously
     * @param isDeleted boolean
     * @param article {@link Article}
     * @param adapterPosition int
     */
     void deleteItem(boolean isDeleted, Article article, final int adapterPosition);
}
