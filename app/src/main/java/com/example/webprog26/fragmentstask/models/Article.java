package com.example.webprog26.fragmentstask.models;

/**
 * Created by webprog26 on 21.11.2016.
 */

public class Article {

    private long mArticleId;
    private String mArticleTitle;
    private String mArticleText;

    public long getArticleId() {
        return mArticleId;
    }

    public String getArticleTitle() {
        return mArticleTitle;
    }

    public String getArticleText() {
        return mArticleText;
    }

    public static Builder newBuilder(){
        return new Article(). new Builder();
    }

    public class Builder{

        public Builder setArticleId(long articleId){
            Article.this.mArticleId = articleId;
            return this;
        }

        public Builder setArticleTitle(String articleTitle){
            Article.this.mArticleTitle = articleTitle;
            return this;
        }

        public Builder setArticleText(String articleText){
            Article.this.mArticleText = articleText;
            return this;
        }

        public Article build(){
            return Article.this;
        }
    }
}
