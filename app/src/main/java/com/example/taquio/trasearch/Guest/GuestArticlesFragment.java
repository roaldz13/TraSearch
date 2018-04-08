package com.example.taquio.trasearch.Guest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Samok.ArticleCrawledData;
import com.example.taquio.trasearch.Samok.Spider;
import com.example.taquio.trasearch.Samok.SpiderArticleAdapter;
import com.example.taquio.trasearch.SearchLogic.ArticleData;
import com.example.taquio.trasearch.SearchLogic.ArticleDataAdapter;
import com.example.taquio.trasearch.SearchLogic.ArticleHTTPRequest;
import com.example.taquio.trasearch.SearchLogic.VideoData;
import com.example.taquio.trasearch.SearchLogic.VideoHTTPRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GuestArticlesFragment extends Fragment {
  private static final String TAG = "GuestArticlesFragment";
  private String searchQuery;
  private boolean search_method;
  private List<ArticleData> articleDataList = new LinkedList<>();
  private List<ArticleCrawledData> articleCrawledDataList = new LinkedList<>();
  private RecyclerView recyclerView;
  private ArticleDataAdapter mArticleDataAdapter;
  private SpiderArticleAdapter mCrawledArticleAdapter;
  private DatabaseReference articleDatabase;

  public GuestArticlesFragment() {}
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_guest_articles_fragment, container, false);
    GuestHome activity = (GuestHome)getActivity();
    Bundle result = activity.sendVideoData();
    articleDatabase = FirebaseDatabase.getInstance().getReference().child("TraSearch");
    searchQuery = result.getString("searchQuery");
    search_method = result.getBoolean("searchMethod");

    if (!searchQuery.isEmpty()) {
      if (!search_method) {
        articleDataList = getArticles(searchQuery);
        recyclerView = (RecyclerView) view.findViewById(R.id.guestArticleRecyclerView);
        mArticleDataAdapter = new ArticleDataAdapter(articleDataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mArticleDataAdapter);
      }
      else {
//                Log.d(TAG, "onCreateView: GuestArticleFragment Crawler " + search_method);
        articleCrawledDataList = getCrawledArticle(searchQuery);
        recyclerView = (RecyclerView) view.findViewById(R.id.guestArticleRecyclerView);
        mCrawledArticleAdapter = new SpiderArticleAdapter(articleCrawledDataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mCrawledArticleAdapter);
      }
    }
    return view;
  }

  public List<ArticleData> getArticles (final String searchQuery) {
    ExecutorService executor = Executors.newCachedThreadPool();
    List<ArticleData> article = new LinkedList<>();
    Future<List<ArticleData>> future = executor.submit(new Callable<List<ArticleData>>() {
      @Override
      public List<ArticleData> call() throws Exception {
        ArticleHTTPRequest request = new ArticleHTTPRequest();
        return request.sendGet(searchQuery);
      }
    });
    executor.shutdown();
    try {



      ArticleHTTPRequest req = new ArticleHTTPRequest();
      HashMap<Integer, ArticleData> articleDatas;
      StringBuilder sb = new StringBuilder();
//            articleDatas = future.get();
      articleDataList = future.get();
      for (int x = 0; x < articleDataList.size(); x++) {
        ArticleData value = articleDataList.get(x);
        ArticleData tobeSaved = new ArticleData();
        tobeSaved.setArticleBody(value.getArticleBody());
        tobeSaved.setArticleURL(value.getArticleURL());
        tobeSaved.setDescription(value.getDescription());
        tobeSaved.setName(value.getName());
        tobeSaved.setUrl(value.getUrl());
        articleDatabase.child("Articles").child(searchQuery).push().setValue(tobeSaved);
      }



      for (ArticleData value: this.articleDataList) {
        String name = value.getName();
        sb.append(name + "\n");
      }
      Log.d(TAG, "getArticles: " + sb.toString());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return articleDataList;
  }

  public List<ArticleCrawledData> getCrawledArticle (final String searchQuery) {
    ExecutorService executor = Executors.newCachedThreadPool();
//        List<ArticleCrawledData>
    Future<List<ArticleCrawledData>> future = executor.submit(new Callable<List<ArticleCrawledData>>() {
      @Override
      public List<ArticleCrawledData> call() throws Exception {
        Spider req = new Spider();
        return req.searchArticle(searchQuery);
      }
    });
    executor.shutdown();
    try {
      articleCrawledDataList = future.get();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return articleCrawledDataList;
  }
}