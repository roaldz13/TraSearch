package com.example.taquio.trasearch.Samok;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Taquio on 2/23/2018.
 */

public class SpiderLeg {
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

  //    private HashMap<String, String> video = new HashMap<String, String>();
//    private HashMap<Integer, CrawledData> video2 = new HashMap<Integer, CrawledData>();
//
//    private List<String> links = new LinkedList<String>();
//    private List<String> linksTitle = new LinkedList<String>();
//    private List<String> nextLinks = new LinkedList<String>();
//
//    private List<CrawledData> videoList = new LinkedList<CrawledData>();
  private Document htmlDocument;

  private HashMap<Integer, ArticleCrawledData> articles = new HashMap<Integer, ArticleCrawledData>();
  private HashMap<Integer, CrawledData> videos = new HashMap<Integer, CrawledData>();
  public boolean checkConnection(String url) {
    try {
      Connection connection = Jsoup.connect(url).userAgent(USER_AGENT).timeout(30000);
//			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
      Document htmlDocument = connection.get();
      this.htmlDocument = htmlDocument;

      if (!connection.response().contentType().contains("text/html")) {
        return false;
      }

      if (connection.response().statusCode() == 200) {
        return true;
      }

      return true;
    }
    catch(IOException ioe) {
      System.out.println(ioe);
      return false;
    }
  }

//    public boolean crawl(String url) {
//        try {
//            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
//            Document htmlDocument = connection.get();
//            this.htmlDocument = htmlDocument;
//            if (this.checkConnection(url)) {
////				System.out.println("\n**Visiting** Received web page at " + url);
//
//                Elements linksOnPage = htmlDocument.select("h3 > a[href]");
////                System.out.println("Found (" + linksOnPage.size() + ") links");
//
//                int linkIndex = 0;
//                for(Element link : linksOnPage) {
//                    this.links.add(link.absUrl("href"));
//
//                    CrawledData videoData = new CrawledData();
//                    videoData.setTitle(link.attr("title"));
//                    videoData.setUrl(link.absUrl("href"));
//
//                    this.linksTitle.add(link.attr("title"));
//                    this.videoList.add(videoData);
//                    this.video.put(link.attr("title"), link.absUrl("href"));
//
//                    this.video2.put(linkIndex, videoData);
//                    linkIndex++;
//                }// Crawler for the Links and Title then Saved to Video HashMap
//
////				Elements channel = htmlDocument.select("div.yt-lockup-byline > a[href].spf-link");
////				for (Element link : channel ) {
////					System.out.println("channel Links: " + link.absUrl("href").toString());
////					System.out.println("Channel Name: " + link.text().toString());
////				}
//
//                Elements nextPageLink = htmlDocument.select("a[href].vve-check");
////                System.out.println("Found (" + nextPageLink.size() + ") next page links");
//                for(Element nextLink: nextPageLink) {
//                    this.nextLinks.add(nextLink.absUrl("href"));
//                }// Crawler for the Next Page Links
//                return true;
//            }
//            else {
////				System.out.println("**Failure** Retrieved something other than HTML");
//                return false;
//            }
//        }
//        catch(IOException ioe) {
////			System.out.println("Error in out HTTP request " + ioe);
//            return false;
//        }
//    }
//
//
//    public boolean searchForWord(String searchWord){
//        if (this.htmlDocument == null) {
////            System.out.println("Error! Call crawl() before performing analysis on the document");
//            return false;
//        }
//
////        System.out.println("Searching for the word " + searchWord + "...");
//        String bodyText = this.htmlDocument.body().text();
//        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
//    }
//
//
//    public List<String> getLinks(){
//        return this.links;
//    }
//
//    public List<String> getLinksTitle(){
//        return this.linksTitle;
//    }
//
//    public List<String> getnextLinks(){
//        return this.nextLinks;
//    }
//
//    public HashMap<String, String> getVideos(){
//        return this.video;
//    }
//
//    public HashMap<Integer, CrawledData> getVideoData() {
//        return this.video2;
//    }

  public HashMap<Integer, ArticleCrawledData> crawlArticle (String url) {
    try {
      Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
      Document htmlDocument = connection.get();
      this.htmlDocument = htmlDocument;

      if (this.checkConnection(url)) {

        System.out.println();
        Elements data = htmlDocument.select("li.b_algo");
        int articleIndex = 0;
        for (Element desc: data) {
          ArticleCrawledData articleData = new ArticleCrawledData();
          articleData.setTitle(desc.child(0).child(1).text());
          articleData.setUrl(desc.child(0).child(1).child(0).absUrl("href"));
          articleData.setDesc(desc.child(1).select("p").text());

          articles.put(articleIndex, articleData);
          articleIndex++;
        }
      }
    }
    catch(IOException ioe) {
    }
    return articles;
  }

  public HashMap<Integer, CrawledData> crawlVideo(String url) {
    try {
      Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
      Document htmlDocument = connection.get();
      this.htmlDocument = htmlDocument;
      if (this.checkConnection(url)) {
        Elements videoData = htmlDocument.select("li > div.yt-lockup-video");
//				System.out.println(videoData);
        int videoIndex = 0;
        for (Element data: videoData) {
          CrawledData crawledVideoData = new CrawledData();
          crawledVideoData.setTitle(data.child(0).child(1).child(0).child(0).attr("title"));
          crawledVideoData.setUrl(data.child(0).child(1).child(0).child(0).absUrl("href"));
          crawledVideoData.setChannelName(data.child(0).child(1).child(1).child(0).text());
          crawledVideoData.setChannelUrl(data.child(0).child(1).child(1).child(0).absUrl("href"));
//					String[] numViews = data.child(0).child(1).child(2).child(0).child(1).text().toString().split("\\s");
//					String num = numViews[0].concat(" Views");
//					crawledVideoData.setViews(data.child(0).child(1).child(2).child(0).child(1).text());
          crawledVideoData.setViews("Not Available");
          if (data.child(0).child(1).childNodeSize() > 3) {
            crawledVideoData.setDesc(data.child(0).child(1).child(3).text());
          }
          else {
            crawledVideoData.setDesc("Not Available");
          }

          videos.put(videoIndex, crawledVideoData);
          videoIndex++;
        }
      }
    }
    catch(IOException ioe) {
    }
    return videos;
  }

  public boolean searchForWord(String searchWord){
    if (this.htmlDocument == null) {
      System.out.println("Error! Call crawl() before performing analysis on the document");
      return false;
    }

//		System.out.println("Searching for the word " + searchWord + "...");
    String bodyText = this.htmlDocument.body().text();
    return bodyText.toLowerCase().contains(searchWord.toLowerCase());
  }

}