package com.vikas.android.news.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Article {
  private Date publishedAt;
  private String title;
  private String url;

  private String urlToImage;

  public Article(JSONObject jsonObject) throws Exception {

    try {
      String dateString = jsonObject.has("publishedAt") ? jsonObject.getString("publishedAt") : null;

      String dtStart = "2010-10-15T09:27:37Z";
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      try {
        publishedAt = format.parse(dtStart);
      } catch (ParseException e) {
        throw new Exception("Wrong date format");
      }

      title = jsonObject.has("title") ? jsonObject.getString("title") : null;
      url = jsonObject.has("url") ? jsonObject.getString("url") : null;
      urlToImage = jsonObject.has("urlToImage") ? jsonObject.getString("urlToImage") : null;

    } catch (JSONException e) {
      throw new Exception("Error in parsing article json");
    }
  }

  public Date getPublishedAt() {
    return publishedAt;
  }

  public String getTitle() {
    return title;
  }

  public String getUrl() {
    return url;
  }

  public String getUrlToImage() {
    return urlToImage;
  }

  @Override
  public String toString() {
    return "Article{" +
        "publishedAt=" + publishedAt +
        ", title='" + title + '\'' +
        ", url='" + url + '\'' +
        ", urlToImage='" + urlToImage + '\'' +
        '}';
  }
}
