package com.example.webprog26.fragmentstask.interfaces;

import com.example.webprog26.fragmentstask.models.Article;

/**
 * Created by webprog26 on 22.11.2016.
 */

public interface OnArticleToEditListener {

    /**
     * Implemented in host (parent) {@link com.example.webprog26.fragmentstask.activities.MainActivity}.
     * Using is caused by orientation. In portrait orientation (in other words single-panel GUI)
     * calls {@link com.example.webprog26.fragmentstask.activities.EditorActivity} with article's id
     * as a parameter, thus {@link com.example.webprog26.fragmentstask.activities.EditorActivity} can
     * transfer article id to {@link com.example.webprog26.fragmentstask.fragments.FragmentEditor},
     * which loads editable {@link Article} from database by received id
     * @param article
     */
    void onEditArticle(Article article);
}
