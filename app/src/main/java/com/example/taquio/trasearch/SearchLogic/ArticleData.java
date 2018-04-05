package com.example.taquio.trasearch.SearchLogic;

/**
 * Created by Del Mar on 3/14/2018.
 */
public class ArticleData {
  private String name;
  private String description;
  private String articleBody;
  private String articleURL;
  private String url;
  private Object[] resultType;

  public ArticleData() {}

  public ArticleData(String name, String description, String articleBody, String articleURL, String url, Object[] resultType) {
    this.name = name;
    this.description = description;
    this.articleBody = articleBody;
    this.articleURL = articleURL;
    this.url = url;
    this.resultType = resultType;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getArticleBody() {
    return articleBody;
  }
  public void setArticleBody(String articleBody) {
    this.articleBody = articleBody;
  }
  public String getArticleURL() {
    return articleURL;
  }
  public void setArticleURL(String articleURL) {
    this.articleURL = articleURL;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public Object[] getResultType() {
    return resultType;
  }
  public void setResultType(Object[] resultType) {
    this.resultType = resultType;
  }

}