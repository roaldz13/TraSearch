package com.example.taquio.trasearch.SearchLogic;

/**
 * Created by Del Mar on 3/14/2018.
 */
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class VideoHTTPRequest {

  private HashMap<Integer, VideoData> unfilteredData = new HashMap<Integer, VideoData>();
  private HashMap<Integer, VideoData> filteredData = new HashMap<Integer, VideoData>();
  private List<VideoData> filteredVideo = new LinkedList<>();

  public List<VideoData> sendGet(String querySearch) throws Exception {
    // Youtube Data API link with API Key
    String urlSource = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=40&type=video&key=AIzaSyDUvqxt9hpw0CFfJG0fqsyoGbc96-h7hFk&q=";
    String url = urlSource.concat(querySearch.replace(' ', '+'));
//		System.out.println(url);
//        String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    StringBuilder sb = new StringBuilder();
    URL conn = new URL(url);
    HttpURLConnection  urlConnection = (HttpURLConnection) conn.openConnection();
    try {
      InputStream in = new BufferedInputStream(urlConnection.getInputStream());
      BufferedReader bin = new BufferedReader(new InputStreamReader(in));

      String inputLine;
      while ((inputLine = bin.readLine()) != null) {
        sb.append(inputLine);
      }
//            System.out.println(sb);
      this.jsonParser(sb.toString());
      this.filterData();
      return this.filteredVideo;
//            return unfilteredData;
    }
    finally {
//			response.close();
      urlConnection.disconnect();
    }

  }

  public void jsonParser(String apiOutput) throws Exception {
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(apiOutput);

    JSONArray items = (JSONArray) jsonObject.get("items");
//		System.out.println("Array Size: " + items.size());
    int index = 0;
    for(int x = 0; x < items.size(); x++) {
      // itemObject Parse each object of index "x" inside the Items Array of Object of JSON
      JSONObject itemObject = (JSONObject) jsonParser.parse(items.get(x).toString());
      // objectSnippet Parse each snippet object inside the itemObject
      // to get title, channelTitle, and description of each item
      JSONObject objectSnippet = (JSONObject) jsonParser.parse(itemObject.get("snippet").toString());
      // objectID Parse each id object inside the itemObject
      // to get the videoId
      JSONObject objectID = (JSONObject) jsonParser.parse(itemObject.get("id").toString());
      JSONObject objectTumbnails = (JSONObject) jsonParser.parse(objectSnippet.get("thumbnails").toString());
      JSONObject defaultTumbnails = (JSONObject) jsonParser.parse(objectTumbnails.get("default").toString());

      // Created a Object videoData
      VideoData videoData = new VideoData();
      videoData.setTitle(objectSnippet.get("title").toString());
      videoData.setDescription(objectSnippet.get("description").toString());
      videoData.setChannelTitle(objectSnippet.get("channelTitle").toString());
      videoData.setVideoId(objectID.get("videoId").toString());
      videoData.setDefaultThumbUrl(defaultTumbnails.get("url").toString());

      // videoData is added to HashMap unfilteredData with index as a key
      this.unfilteredData.put(index, videoData);
      // index is incremented for next item
      index++;
    }

  }

  public void filterData() {
    DataFilter filters = new DataFilter();
    String[] words = filters.getWordFilters();
    List<VideoData> unfilteredData = new LinkedList<VideoData>();
    unfilteredData = convertToList(this.unfilteredData);

    for (Iterator<VideoData> iterator = unfilteredData.listIterator(); iterator.hasNext();){
      VideoData value = iterator.next();

      for (int wordsIndex = 0; wordsIndex < words.length; wordsIndex++) {
        if (value.getTitle().toLowerCase().contains(words[wordsIndex])) {
          this.filteredVideo.add(value);
          iterator.remove();
          break;
        }
        if (value.getChannelTitle().toLowerCase().contains(words[wordsIndex])) {
          this.filteredVideo.add(value);
          iterator.remove();
          break;
        }
        if (value.getDescription().toLowerCase().contains(words[wordsIndex])) {
          this.filteredVideo.add(value);
          iterator.remove();
          break;
        }
      }
    }

//		System.out.println("HashMapSize: " + this.unfilteredData.size());

//        int wordsIndex = 0;
//        int filteredDataIndex = 0;
//        do {
//            for (Integer index: this.unfilteredData.keySet()) {
//                Integer key = index;
//                VideoData data = unfilteredData.get(key);
//
//                if (data.getTitle().toLowerCase().contains(words[wordsIndex])) {
//                    VideoData filteredVideo = new VideoData();
//                    filteredVideo.setTitle(data.getTitle());
//                    filteredVideo.setDescription(data.getDescription());
//                    filteredVideo.setChannelTitle(data.getChannelTitle());
//                    filteredVideo.setVideoId(data.getVideoId());
//                    filteredVideo.setDefaultThumbUrl(data.getDefaultThumbUrl());
//
//                    this.filteredData.put(filteredDataIndex, filteredVideo);
//                    this.unfilteredData.remove(key);
//                    break;
//                }
//                else if (data.getDescription().toLowerCase().contains(words[wordsIndex])) {
//                    VideoData filteredVideo = new VideoData();
//                    filteredVideo.setTitle(data.getTitle());
//                    filteredVideo.setDescription(data.getDescription());
//                    filteredVideo.setChannelTitle(data.getChannelTitle());
//                    filteredVideo.setVideoId(data.getVideoId());
//                    filteredVideo.setDefaultThumbUrl(data.getDefaultThumbUrl());
//
//                    this.filteredData.put(filteredDataIndex, filteredVideo);
//                    this.unfilteredData.remove(key);
//                    break;
//                }
//            }
//            filteredDataIndex++;
//            wordsIndex++;
//        }while(wordsIndex < words.length);
  }

  public HashMap<Integer, VideoData> getUnfilteredData() {
    return this.unfilteredData;
  }

  public HashMap<Integer, VideoData> getFilteredData() { return this.filteredData; }

  public List<VideoData> convertToList(HashMap<Integer, VideoData> filteredData) {
    List<VideoData> videoList = new LinkedList<>();
    for(Integer index: filteredData.keySet()) {
      VideoData value = filteredData.get(index);
      videoList.add(value);
    }
    return videoList;
  }
}