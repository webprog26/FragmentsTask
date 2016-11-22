package com.example.webprog26.fragmentstask.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by webprog26 on 21.11.2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper_TAG";

    private static final String DB_NAME = "fragmens_task_db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_ARTICLES = "table_articles";
    public static final String ARTICLE_ID = "_id";
    public static final String ARTICLE_TITLE = "article_title";
    public static final String ARTICLE_TEXT = "article_text";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_ARTICLES + "("
        + ARTICLE_ID + " integer primary key autoincrement, "
        + ARTICLE_TITLE + " varchar(100), "
        + ARTICLE_TEXT + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //empty
    }
}
