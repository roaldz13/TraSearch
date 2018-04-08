package com.example.taquio.trasearch.SearchLogic;

/**
 * Created by Del Mar on 3/14/2018.
 */

public class VideoData {
  private String title;
  private String videoId;
  private String description;
  private String channelTitle;
  private String defaultThumbUrl;

  @Override
  public String toString() {
    return "VideoData{" +
            "title='" + title + '\'' +
            ", videoId='" + videoId + '\'' +
            ", description='" + description + '\'' +
            ", channelTitle='" + channelTitle + '\'' +
            ", defaultThumbUrl='" + defaultThumbUrl + '\'' +
            '}';
  }

  public VideoData() {
  }
  public VideoData(String title, String videoId, String description, String channelTitle, String defaultThumbUrl) {
    this.title = title;
    this.videoId = videoId;
    this.description = description;
    this.channelTitle = channelTitle;
    this.defaultThumbUrl = defaultThumbUrl;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getVideoId() {
    return videoId;
  }
  public void setVideoId(String videoId) {
    this.videoId = videoId;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getChannelTitle() {
    return channelTitle;
  }
  public void setChannelTitle(String channelTitle) {
    this.channelTitle = channelTitle;
  }

  public String getDefaultThumbUrl() {
    return defaultThumbUrl;
  }
  public void setDefaultThumbUrl(String defaultThumbUrl) {
    this.defaultThumbUrl = defaultThumbUrl;
  }
}