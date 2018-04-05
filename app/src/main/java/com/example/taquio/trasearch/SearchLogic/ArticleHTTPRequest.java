package com.example.taquio.trasearch.SearchLogic;

/**
 * Created by Del Mar on 3/14/2018.
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ArticleHTTPRequest {

  private HashMap<Integer, ArticleData> unfilteredData = new HashMap<Integer, ArticleData>();
  private HashMap<Integer, ArticleData> filteredData = new HashMap<Integer, ArticleData>();
  private List<ArticleData> filteredArticle = new LinkedList<>();

  public List<ArticleData> sendGet(String querySearch) throws Exception {
    String urlSource = "https://kgsearch.googleapis.com/v1/entities:search?languages=en&limit=30&types=WebSite&types=Book&types=EducationalOrganization&types=Organization&key=AIzaSyDUvqxt9hpw0CFfJG0fqsyoGbc96-h7hFk&query=";
    String url = urlSource.concat(querySearch.replace(' ', '+'));

    StringBuilder sb = new StringBuilder();
    URL conn = new URL(url);
    HttpURLConnection urlConnection = (HttpURLConnection) conn.openConnection();
    try {
      InputStream in = new BufferedInputStream(urlConnection.getInputStream());
      BufferedReader bin = new BufferedReader(new InputStreamReader(in));

      String inputLine;
      while((inputLine = bin.readLine()) != null) {
        sb.append(inputLine);
      }
      this.jsonParser(sb.toString());
      this.filterData();

      return this.filteredArticle;
    }
    finally {
      urlConnection.disconnect();
    }
  }

  public void jsonParser(String apiOutput) throws Exception {
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(apiOutput);

    JSONArray items = (JSONArray) jsonObject.get("itemListElement");

    int index = 0;
    for(int x = 0; x < items.size(); x++) {
      JSONObject itemObject = (JSONObject) jsonParser.parse(items.get(x).toString());
      JSONObject objectResult = (JSONObject) jsonParser.parse(itemObject.get("result").toString());
      JSONArray resultType = (JSONArray) objectResult.get("@type");

      ArticleData articleData = new ArticleData();
      articleData.setName(objectResult.get("name").toString());
      if (objectResult.containsKey("description")) {
        articleData.setDescription(objectResult.get("description").toString());
      }
      else {

        articleData.setDescription("Not Available");
      }

      articleData.setResultType(resultType.toArray());

      if (objectResult.containsKey("detailedDescription")) {
        JSONObject objectDetailedDesc = (JSONObject) jsonParser.parse(objectResult.get("detailedDescription").toString());
        articleData.setArticleBody(objectDetailedDesc.get("articleBody").toString());
        articleData.setArticleURL(objectDetailedDesc.get("url").toString());
      }
      else {
        articleData.setArticleBody("Not Available");
        articleData.setArticleURL("Not Available");
      }

      if (objectResult.containsKey("url")) {
        articleData.setUrl(objectResult.get("url").toString());
      }
      else {
        articleData.setUrl("Not Available");
      }
      this.unfilteredData.put(index, articleData);
      index++;
    }

  }

  public void filterData() {
    DataFilter filters = new DataFilter();
    String[] words = filters.getWordFilters();
    List<ArticleData> unfilteredData = new LinkedList<>();
    unfilteredData = convertToList(this.unfilteredData);

    for (Iterator<ArticleData> iterator = unfilteredData.listIterator(); iterator.hasNext();){
      ArticleData value = iterator.next();

      for (int wordsIndex = 0; wordsIndex < words.length; wordsIndex++) {
        if (value.getName().toLowerCase().contains(words[wordsIndex])) {
          this.filteredArticle.add(value);
          iterator.remove();
          break;
        }
        else if (!(value.getArticleBody().equalsIgnoreCase("Not Available"))) {
          if (value.getArticleBody().toLowerCase().contains(words[wordsIndex])) {
            this.filteredArticle.add(value);
            iterator.remove();
            break;
          }
        }
        else if (!(value.getDescription().equalsIgnoreCase("Not Available"))) {
          if (value.getDescription().toLowerCase().contains(words[wordsIndex])) {
            this.filteredArticle.add(value);
            iterator.remove();
            break;
          }
        }
      }
    }

//        int wordsIndex = 0;
//        int filteredDataIndex = 0;
//        do {
//            for (Integer index: this.unfilteredData.keySet()) {
//                ArticleData data = unfilteredData.get(index);
//
//                if (data.getName().toLowerCase().contains(words[wordsIndex])) {
//                    ArticleData filteredArticle = new ArticleData();
//                    filteredArticle.setName(data.getName());
//                    filteredArticle.setDescription(data.getDescription());
//                    filteredArticle.setArticleBody(data.getArticleBody());
//                    filteredArticle.setArticleURL(data.getArticleURL());
//                    filteredArticle.setUrl(data.getUrl());
//                    filteredArticle.setResultType(data.getResultType());
//
//                    this.filteredData.put(filteredDataIndex, filteredArticle);
//                    this.unfilteredData.remove(index);
//                    break;
//                }
//                else if (!(data.getArticleBody().equalsIgnoreCase("Not Available"))) {
//                    if (data.getArticleBody().toLowerCase().contains(words[wordsIndex])) {
//                        ArticleData filteredArticle = new ArticleData();
//                        filteredArticle.setName(data.getName());
//                        filteredArticle.setDescription(data.getDescription());
//                        filteredArticle.setArticleBody(data.getArticleBody());
//                        filteredArticle.setArticleURL(data.getArticleURL());
//                        filteredArticle.setUrl(data.getUrl());
//                        filteredArticle.setResultType(data.getResultType());
//
//                        this.filteredData.put(filteredDataIndex, filteredArticle);
//                        this.unfilteredData.remove(index);
//                        break;
//                    }
//                }
//                else if (!(data.getArticleBody().equalsIgnoreCase("Not Available"))) {
//                    if (data.getDescription().toLowerCase().contains(words[wordsIndex])) {
//                        ArticleData filteredArticle = new ArticleData();
//                        filteredArticle.setName(data.getName());
//                        filteredArticle.setDescription(data.getDescription());
//                        filteredArticle.setArticleBody(data.getArticleBody());
//                        filteredArticle.setArticleURL(data.getArticleURL());
//                        filteredArticle.setUrl(data.getUrl());
//                        filteredArticle.setResultType(data.getResultType());
//                        System.out.println("True in this word" + words[wordsIndex]);
//
//                        this.filteredData.put(filteredDataIndex, filteredArticle);
//                        this.unfilteredData.remove(index);
//                        break;
//                    }
//                }
//
//            }
//            filteredDataIndex++;
//            wordsIndex++;
//        }while(wordsIndex < words.length);
  }

  public HashMap<Integer, ArticleData> getUnfilteredData() {
    return this.unfilteredData;
  }

  public HashMap<Integer, ArticleData> getFilteredData() {
    return this.filteredData;
  }

  public List<ArticleData> convertToList(HashMap<Integer, ArticleData> filteredData) {
    List<ArticleData> articleList = new LinkedList<>();
    for(Integer index: filteredData.keySet()) {
      ArticleData value = filteredData.get(index);
      articleList.add(value);
    }
    return articleList;
  }
}