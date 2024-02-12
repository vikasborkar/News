package com.vikas.android.news.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NewsRepository {

  private static final String TAG = "NewsRepository";

  private static final String JSON_FILE_NAME = "news.json";

  private static Context sContext = null;
  private static NewsRepository sInstance = null;

  private ArrayList<Article> mArticles = new ArrayList<>();

  public static NewsRepository getInstance(Context applicationContext) {
    if (sInstance != null) {
      return sInstance;
    }

    sInstance = new NewsRepository(applicationContext);
    return sInstance;

  }

  private NewsRepository(Context applicationContext) {
    sContext = applicationContext;
  }

  public List<Article> getArticles() {

    if (mArticles.isEmpty()) {
      mArticles =  parseArticles();
    }

    return mArticles;
  }

  public List<Article> queryArticles(String query) {

    if (mArticles.isEmpty()) {
      return mArticles;
    }

    List<Article> resultArticles = new ArrayList();
    for (int i = 0; i < mArticles.size(); i++) {
      Article article = mArticles.get(i);

      if (article.getTitle().toLowerCase().contains(query.toLowerCase())){
        resultArticles.add(article);
      }
    }

    return resultArticles;
  }


  private ArrayList<Article> parseArticles() {

    ArrayList<Article> articles = new ArrayList<>();

    try {
      String jsonString = loadJSONFromAsset();
      JSONObject jsonObject = new JSONObject(jsonString);
      JSONArray jsonArray = jsonObject.getJSONArray("articles");

      for (int i = 0; i < jsonArray.length(); i++) {
        try {
          Article article = new Article(jsonArray.getJSONObject(i));
          articles.add(article);
        } catch (Exception e) {
          //article skipped
          Log.w(TAG, "Error in parsing article at index " + i, e);
        }

      }

    } catch (Exception e) {
      //Error in reading/parsing json
      Log.e(TAG, "Error in parsing the json", e);
    }

    articles.sort(new Comparator<Article>() {
      @Override
      public int compare(Article lhs, Article rhs) {
        return lhs.getPublishedAt().compareTo(rhs.getPublishedAt());
      }
    });

    return articles;
  }


  private String loadJSONFromAsset() throws IOException {
    String json = null;

    InputStream is = sContext.getAssets().open(JSON_FILE_NAME);
    int size = is.available();
    byte[] buffer = new byte[size];
    is.read(buffer);
    is.close();
    json = new String(buffer, "UTF-8");

    return json;
  }

}
