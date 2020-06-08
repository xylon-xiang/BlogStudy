package com.studyblog.blog.model;

import lombok.Data;

import java.util.Date;


/*
* @Author Xylon-xiang
* @Date 2020.6.8
* */



@Data
public class Article {
    //main key to tip the article, and it is unique
    private int articleId;

    private String articleTitle;

    private String authorName;

    private Date publishDate;

    private Date refreshDate;

    //the abstract of the article, always being the fist 20 chars of the context
    private String summary;

    //the whole context of the article
    private String context;

    //the number of the people who like the article
    private int likeIt;


    //the number of the people who collect it
    //this function will be added as soon
    //private int stars;
}
