package com.vikas.android.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vikas.android.news.data.Article;
import com.vikas.android.news.data.NewsRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private List<Article> mArticles;
  private RecyclerView rvArticles;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mArticles = NewsRepository.getInstance(this.getApplicationContext()).getArticles();

    setViews();
  }

  private void setViews() {
    rvArticles = (RecyclerView) findViewById(R.id.rvArticles);

    rvArticles.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
    rvArticles.setAdapter(new ArticleAdapter());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
     getMenuInflater().inflate(R.menu.activity_main, menu);

    SearchView searchItem = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();

    searchItem.setOnCloseListener(new SearchView.OnCloseListener() {
      @Override
      public boolean onClose() {
        mArticles =  NewsRepository.getInstance(MainActivity.this.getApplicationContext()).getArticles();
        rvArticles.getAdapter().notifyDataSetChanged();
        return false;
      }
    });

    searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)){
          mArticles =  NewsRepository.getInstance(MainActivity.this.getApplicationContext()).getArticles();
        } else {
          mArticles = NewsRepository.getInstance(MainActivity.this.getApplicationContext()).queryArticles(query);
        }

        Log.d("Vikas", "Result articles: "+ mArticles.size());

        rvArticles.getAdapter().notifyDataSetChanged();
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        //do nothing
        return false;
      }
    });

     return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    return super.onOptionsItemSelected(item);
  }

  private class ArticleAdapter extends RecyclerView.Adapter<ArticleHolder> {

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(MainActivity.this.getApplicationContext());
      View view = inflater.inflate(R.layout.item_news_article, parent, false);
      return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
      holder.bindItem(mArticles.get(position));
    }

    @Override
    public int getItemCount() {
      return mArticles.size();
    }
  }

  private class ArticleHolder extends RecyclerView.ViewHolder {

    private final ImageView ivImage;
    private final TextView tvTitle;
    private final TextView tvDate;

    public ArticleHolder(@NonNull View itemView) {
      super(itemView);

      ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
      tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
      tvDate = (TextView) itemView.findViewById(R.id.tvDate);
    }

    public void bindItem(Article item){

      Picasso.get()
          .load(item.getUrlToImage())
          .resize(100, 100)
          .centerCrop()
          .into(ivImage);

      tvTitle.setText(item.getTitle());

      SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a");
      String currentDate = sdf.format(item.getPublishedAt());
      tvDate.setText(currentDate);
    }

  }

}

