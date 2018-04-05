package com.example.taquio.trasearch.Samok;

import com.example.taquio.trasearch.SearchLogic.DataFilter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Taquio on 2/23/2018.
 */

public class Spider {
  private Set<String> pagesVisited = new HashSet<String>(); // Links visited that contains the searchWord || source of the Links of video
  private List<String> pagesToVisit = new LinkedList<String>(); // Links found to be visited || Links of video
  private List<String> searchedWord = new LinkedList<String>();
  private HashMap<Integer, ArticleCrawledData> unfilteredArticleData = new HashMap<Integer, ArticleCrawledData>();
  private HashMap<Integer, CrawledData> unfilteredVideoData = new HashMap<Integer, CrawledData>();
  private List<CrawledData> filteredVideo = new LinkedList<CrawledData>();
  private List<ArticleCrawledData> filteredArticle = new LinkedList<ArticleCrawledData>();
//    private Object[] paganation;

  private String nextUrl() {
    String nextUrl;
    do {
      nextUrl = this.pagesToVisit.remove(0);
    }while(this.pagesVisited.contains(nextUrl));
    this.pagesVisited.add(nextUrl);
    return nextUrl;
  }

//    public HashMap<Integer, CrawledData> searchEngine(String searchWord) {
//        SpiderLeg leg = new SpiderLeg();
//        String urlSource = "https://www.youtube.com/results?search_query=";
//        String url = urlSource + searchWord.replace(" ", "+");
//        while (leg.checkConnection(url)){
//            String currentUrl;
//
//            if(this.pagesToVisit.isEmpty()) {
//                currentUrl = url;
//                this.pagesVisited.add(url);
//            }
//            else {
//                currentUrl = this.nextUrl();
//            }
//
//            if (this.searchedWord.isEmpty()) {
//                this.searchedWord.add(searchWord);
//            }
//            else {
//                this.searchedWord.add(searchWord);
//            }
//
//            leg.crawl(currentUrl);
//            this.unfiltered = leg.getVideoData();
//            filterData();
//
////			for(Integer index: unfiltered.keySet()){
////				Integer key = index;
////				CrawledData value = unfiltered.get(key);
//////				System.out.println("Key: " + key);
////				System.out.println("Value Title: " + value.getTitle());
//////				System.out.println("Value Url: " + value.getUrl() + "\n");
////			}
//
//            setPaganation(leg.getnextLinks().toArray());
////			for (Integer index: video.keySet()) {
////				CrawledData data = new CrawledData();
////
////			}
////			int x = 0;
////			do{
////				System.out.println("Page " + (x+1) + ": " + paganation[x]);
////				x++;
////			}while(x < paganation.length);
//
//            boolean success = leg.searchForWord(searchWord);
//            if(success) {
////				System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
//                break;
//            }
//            this.pagesToVisit.addAll(leg.getLinks());
//        }
////		System.out.println(String.format("**Done** Visited %s web page(s)", this.pagesVisited.size()));
//        return this.filtered;
//    }
//
//    public void filterData() {
//        DataFilter filters = new DataFilter();
//        String[] words = filters.getWordFilters();
//
////		System.out.println("HashMapSize: " + this.unfilteredData.size());
//
//        int wordsIndex = 0;
//        int filteredDataIndex = 0;
//        do {
//            for (Integer index: this.unfiltered.keySet()) {
//                CrawledData data = unfiltered.get(index);
//                if  (data.getTitle().toLowerCase().contains(words[wordsIndex])) {
//                    CrawledData filteredData = new CrawledData();
//                    filteredData.setTitle(data.getTitle());
//                    filteredData.setUrl(data.getUrl());
//                    this.filtered.put(filteredDataIndex, data);
//                    this.unfiltered.remove(index);
//                    break;
//                }
//            }
//            filteredDataIndex++;
//            wordsIndex++;
//        }while(wordsIndex < words.length);
//    }
//
//
//    public Set<String> getPagesVisited() {
//        return this.pagesVisited;
//    }
//    public List<String> getPagesToVisit() {
//        return this.pagesToVisit;
//    }
//
//    public List<String> getSearchedWord() {
//        return this.searchedWord;
//    }
//
//    public Object[] getPaganation() {
//        return paganation;
//    }
//
//    public void setPaganation(Object[] paganation) {
//        this.paganation = paganation;
//    }

  public void filterArticleData () {
    DataFilter filters = new DataFilter();
    String[] words = filters.getWordFilters();
    List<ArticleCrawledData> unfilteredData = new LinkedList<>();
    unfilteredData = convertArticleToList(this.unfilteredArticleData);


//		for(int dataIndex = 0; dataIndex < unfilteredData.size(); dataIndex++) {
//			CrawledData value = unfilteredData.get(dataIndex);
//			System.out.println(value.getTitle());
//		}
//		System.out.println();
    for (Iterator<ArticleCrawledData> iterator = unfilteredData.listIterator(); iterator.hasNext();) {
      ArticleCrawledData value = iterator.next();
//			System.out.println(value.getTitle());
      for (int wordsIndex = 0; wordsIndex < words.length; wordsIndex++) {
//				System.out.println(value.getTitle() + " " + words[wordsIndex]);
//				System.out.println(value.getChannelName() + " " + words[wordsIndex]);
//				System.out.println(value.getDesc() + " " + words[wordsIndex]);
        if (value.getTitle().toLowerCase().contains(words[wordsIndex])) {
//					System.out.println(value.getTitle() + " " + words[wordsIndex]);
          this.filteredArticle.add(value);
//					this.filteredVideoData.put(wordsIndex, value);
          iterator.remove();
          break;
        }

        if (value.getDesc().toLowerCase().contains(words[wordsIndex])) {
//					System.out.println(value.getDesc() + " " + words[wordsIndex]);
          this.filteredArticle.add(value);
          iterator.remove();
          break;
        }
      }
    }

  }

  public void filterVideoData () {
    DataFilter filters = new DataFilter();
    String[] words = filters.getWordFilters();
    List<CrawledData> unfilteredData = new LinkedList<>();
    unfilteredData = convertVideoToList(this.unfilteredVideoData);


//		for(int dataIndex = 0; dataIndex < unfilteredData.size(); dataIndex++) {
//			CrawledData value = unfilteredData.get(dataIndex);
//			System.out.println(value.getTitle());
//		}
//		System.out.println();
    for (Iterator<CrawledData> iterator = unfilteredData.listIterator(); iterator.hasNext();) {
      CrawledData value = iterator.next();
//			System.out.println(value.getTitle());
      for (int wordsIndex = 0; wordsIndex < words.length; wordsIndex++) {
//				System.out.println(value.getTitle() + " " + words[wordsIndex]);
//				System.out.println(value.getChannelName() + " " + words[wordsIndex]);
//				System.out.println(value.getDesc() + " " + words[wordsIndex]);
        if (value.getTitle().toLowerCase().contains(words[wordsIndex])) {
//					System.out.println(value.getTitle() + " " + words[wordsIndex]);
          this.filteredVideo.add(value);
//					this.filteredVideoData.put(wordsIndex, value);
          iterator.remove();
          break;
        }

        if (value.getChannelName().toLowerCase().contains(words[wordsIndex])) {
//					System.out.println(value.getChannelName() + " " + words[wordsIndex]);
          this.filteredVideo.add(value);
          iterator.remove();
          break;
        }

        if (value.getDesc().toLowerCase().contains(words[wordsIndex])) {
//					System.out.println(value.getDesc() + " " + words[wordsIndex]);
          this.filteredVideo.add(value);
          iterator.remove();
          break;
        }
      }
    }

  }

  public List<CrawledData> convertVideoToList(HashMap<Integer, CrawledData> filteredVideoData) {
    List<CrawledData> videoList = new LinkedList<>();
    for(Integer index: filteredVideoData.keySet()) {
      CrawledData value = filteredVideoData.get(index);
      videoList.add(value);
    }
    return videoList;
  }

  public List<ArticleCrawledData> convertArticleToList(HashMap<Integer, ArticleCrawledData> filteredArticleData) {
    List<ArticleCrawledData> articleList = new LinkedList<>();
    for(Integer index: filteredArticleData.keySet()) {
      ArticleCrawledData value = filteredArticleData.get(index);
      articleList.add(value);
    }
    return articleList;
  }



  public List<ArticleCrawledData> searchArticle (String searchWord) {
    SpiderLeg spider = new SpiderLeg();
    String urlSource = "https://www.bing.com/search?q=";
    String query = searchWord.replace(" ", "+");
    String url = urlSource.concat(query);
    System.out.println(url);
    HashMap<Integer, ArticleCrawledData> data = new HashMap<Integer, ArticleCrawledData>();
    while(spider.checkConnection(url)) {
      String currentUrl;

      if(this.pagesToVisit.isEmpty()) {
        currentUrl = url;
        this.pagesVisited.add(url);
      }
      else {
        currentUrl = this.nextUrl();
      }

      if (this.searchedWord.isEmpty()) {
        this.searchedWord.add(searchWord);
      }
      else {
        this.searchedWord.add(searchWord);
      }

      data = spider.crawlArticle(url);
      this.unfilteredArticleData = data;
      this.filterArticleData();

      boolean success = spider.searchForWord(searchWord);
      if (success) {
        break;
      }
    }
    return this.filteredArticle;
  }

  public List<CrawledData> searchVideo (String searchWord) {
    SpiderLeg leg = new SpiderLeg();
    String urlSource = "https://www.youtube.com/results?search_query=";
    String url = urlSource + searchWord.replace(" ", "+");
    HashMap<Integer, CrawledData> data = new HashMap<Integer, CrawledData>();
    while (leg.checkConnection(url)) {
      String currentUrl;

      if(this.pagesToVisit.isEmpty()) {
        currentUrl = url;
        this.pagesVisited.add(url);
      }
      else {
        currentUrl = this.nextUrl();
      }

      if (this.searchedWord.isEmpty()) {
        this.searchedWord.add(searchWord);
      }
      else {
        this.searchedWord.add(searchWord);
      }

      data = leg.crawlVideo(url);
      this.unfilteredVideoData = data;
      this.filterVideoData();

      System.out.println();
//			this.filteredVideoData = filterVideoData(this.unfilteredVideoData);
//			for(int x = 0; x < this.filteredList.size(); x++) {
//				CrawledData dataList = this.filteredList.get(x);
//				System.out.println(dataList.getTitle());
//			}

      boolean success = leg.searchForWord(searchWord);
      if(success) {
        break;
      }
    }

    return this.filteredVideo;
  }
  public List<CrawledData> convertToList(HashMap<Integer, CrawledData> filteredData) {
    List<CrawledData> videoList = new LinkedList<>();
    for(Integer index: filteredData.keySet()) {
      CrawledData value = filteredData.get(index);
      videoList.add(value);
    }
    return videoList;
  }
}