package com.example.webprog26.fragmentstask.providers;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.webprog26.fragmentstask.db.DBHelper;
import com.example.webprog26.fragmentstask.models.Article;

import java.util.ArrayList;

/**
 * Created by webprog26 on 21.11.2016.
 */

public class DBProvider {

    private static final String TAG = "DBProvider";

    private DBHelper mDbHelper;

    public DBProvider(Activity activity) {
        this.mDbHelper = new DBHelper(activity);
    }

    /**
     * Insert Article to data base
     * @param article {@link Article}
     * @return long
     */
    public long insertArticleToDB(Article article){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.ARTICLE_TITLE, article.getArticleTitle());
        contentValues.put(DBHelper.ARTICLE_TEXT, article.getArticleText());

        return mDbHelper.getWritableDatabase().insert(DBHelper.TABLE_ARTICLES, null, contentValues);
    }

    /**
     * Gets all the articles from data base
     * @return ArrayList<Article>
     */
    public ArrayList<Article> getArticles(){
        ArrayList<Article> articlesList = new ArrayList<>();

        Cursor cursor = mDbHelper.getReadableDatabase().query(DBHelper.TABLE_ARTICLES, null, null, null, null, null, DBHelper.ARTICLE_ID);

        while (cursor.moveToNext()){
            Article.Builder builder = Article.newBuilder();
                builder.setArticleId(cursor.getLong(cursor.getColumnIndex(DBHelper.ARTICLE_ID)))
                        .setArticleTitle(cursor.getString(cursor.getColumnIndex(DBHelper.ARTICLE_TITLE)))
                        .setArticleText(cursor.getString(cursor.getColumnIndex(DBHelper.ARTICLE_TEXT)));

            articlesList.add(builder.build());
        }
        cursor.close();
        return articlesList;
    }

    /**
     * Gets {@link Article} with given id from data base
     * @param articleId long
     * @return {@link Article}
     */
    public Article getArticleById(long articleId){
        Article.Builder builder = Article.newBuilder();

        String selection = DBHelper.ARTICLE_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(articleId)};

        Cursor cursor = mDbHelper.getReadableDatabase().query(DBHelper.TABLE_ARTICLES, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()){
            builder.setArticleId(articleId)
                    .setArticleTitle(cursor.getString(cursor.getColumnIndex(DBHelper.ARTICLE_TITLE)))
                    .setArticleText(cursor.getString(cursor.getColumnIndex(DBHelper.ARTICLE_TEXT)));
        }
        cursor.close();
        return builder.build();
    }

    public long updateArticle(Article article){
        String strFilter = DBHelper.ARTICLE_ID + " = " + String.valueOf(article.getArticleId());

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.ARTICLE_TITLE, article.getArticleTitle());
        contentValues.put(DBHelper.ARTICLE_TEXT, article.getArticleText());

        Log.i(TAG, "ID + " + article.getArticleId() + " Title = " + article.getArticleTitle() + ", text = " + article.getArticleText());

        return mDbHelper.getWritableDatabase().update(DBHelper.TABLE_ARTICLES, contentValues, strFilter, null);
    }

    public void deleteArticleById(long articleId){
        String whereClause = DBHelper.ARTICLE_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(articleId)};
        mDbHelper.getWritableDatabase().delete(DBHelper.TABLE_ARTICLES, whereClause, whereArgs);
    }
}
