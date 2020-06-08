package com.studyblog.blog.mapping;

import com.studyblog.blog.model.Article;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


import java.util.List;

@Mapper
@Repository
public interface ArticleMapping {

    @Select("SELECT articleTitle FROM article ORDER BY articleId desc")
    List<String> ListAllArticleName();

    @Insert("INSERT Article (articleId, articleTitle, authorName, publishDate, refreshDate, summary, context, likeIt)" +
            " VALUE (#{article}, #{articleTitle}, #{authorName}, #{publishDate}, #{refreshDate}, #{summary}, #{context}, #{likeIt})")
    void AddArticle(Article article);

    @Update("UPDATE Article SET articleTitle=#{articleTitle}, refreshDate=#{refreshDate}, " +
            "summary=#{summary}, context=#{context}, likeIt=#{likeIt}")
    void UpdateArticle(Article article);

    @Delete("DELETE FROM Article WHERE articleId=#{articleId}")
    void DeleteArticle(@Param("articleId") int articleId);

}