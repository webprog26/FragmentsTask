package com.example.webprog26.fragmentstask.managers;

import android.database.Cursor;

import com.example.webprog26.fragmentstask.db.DBHelper;
import com.example.webprog26.fragmentstask.models.Article;

/**
 * Created by webprog26 on 24.11.2016.
 */

public class CursorManager {

    public static Article getArticleFromDataBase(Cursor cursor){
        if(cursor == null){
            return null;
        }

        Article.Builder builder = Article.newBuilder();
            builder.setArticleId(cursor.getLong(cursor.getColumnIndex(DBHelper.ARTICLE_ID)))
                    .setArticleTitle(cursor.getString(cursor.getColumnIndex(DBHelper.ARTICLE_TITLE)))
                    .setArticleText(cursor.getString(cursor.getColumnIndex(DBHelper.ARTICLE_TEXT)));
        return builder.build();
    }
}
