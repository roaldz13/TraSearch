package com.example.taquio.trasearch.Guest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Samok.CrawledData;
import com.example.taquio.trasearch.Samok.Spider;
import com.example.taquio.trasearch.Samok.SpiderDataAdapter;
import com.example.taquio.trasearch.SearchLogic.VideoData;
import com.example.taquio.trasearch.SearchLogic.VideoDataAdapter;
import com.example.taquio.trasearch.SearchLogic.VideoHTTPRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GuestVideosFragment extends Fragment {
  private static final String TAG = "GuestVideoFragment";
  private String searchQuery;
  private boolean search_method;
  private List<VideoData> videoDataList = new LinkedList<>();
  private List<CrawledData> crawledDataList = new LinkedList<>();
  private RecyclerView recyclerView;
  private VideoDataAdapter mVideoAdapter;
  private SpiderDataAdapter mCrawledAdapter;


  public GuestVideosFragment() {}
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_guest_videos_fragment, container, false);
    GuestHome activity = (GuestHome)getActivity();
    Bundle result = activity.sendVideoData();
    searchQuery = result.getString("searchQuery");
    search_method = result.getBoolean("searchMethod");
    if (!searchQuery.isEmpty()) {
      if (!search_method) {
        videoDataList = getVideos(searchQuery);
        recyclerView = (RecyclerView) view.findViewById(R.id.guestVideoRecyclerView);
        mVideoAdapter = new VideoDataAdapter(videoDataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mVideoAdapter);
      }
      else {
//                Log.d(TAG, "onCreateView: GuestVideoFragment Crawler " + search_method);
        crawledDataList = getCrawledVideos(searchQuery);
        recyclerView = (RecyclerView) view.findViewById(R.id.guestVideoRecyclerView);
        mCrawledAdapter = new SpiderDataAdapter(crawledDataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mCrawledAdapter);
      }
    }
    return view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate: ");
  }

  public List<VideoData> getVideos(final String searchQuery) {
    ExecutorService executor = Executors.newCachedThreadPool();
    List<VideoData> video = new LinkedList<>();
    Future<List<VideoData>> future = executor.submit(new Callable<List<VideoData>>() {
      @Override
      public List<VideoData> call() throws Exception {
        VideoHTTPRequest request = new VideoHTTPRequest();
        return request.sendGet(searchQuery);
      }
    });
    executor.shutdown();
    try {
      VideoHTTPRequest req = new VideoHTTPRequest();
      HashMap<Integer, VideoData> videoData;
//            StringBuilder sb = new StringBuilder();
//            videoData = future.get();
      videoDataList =  future.get();
//            for (VideoData video: this.videoDataList) {
//                String title = video.getTitle();
//                sb.append(title + "\n");
//            }
//            for (Integer index: videoDatas.keySet()) {
//                VideoData value = videoDatas.get(index);
//                String title = value.getTitle() + "\n";
//                sb.append(title);
//            }
////            Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
//            Log.d(TAG, "getVideos: " + sb .toString());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return videoDataList;
  }

  public List<CrawledData> getCrawledVideos(final String searchQuery) {
    ExecutorService executor = Executors.newCachedThreadPool();
    List<CrawledData> video = new LinkedList<>();
    final Future<List<CrawledData>> future = executor.submit(new Callable<List<CrawledData>>() {
      @Override
      public List<CrawledData> call() throws Exception {
        Spider request = new Spider();
        return request.searchVideo(searchQuery);
      }
    });
    executor.shutdown();

    try {
//            Spider req = new Spider();
//            HashMap<Integer, CrawledData> videoData;
//            StringBuilder sb = new StringBuilder();
//            videoData = future.get();
      crawledDataList = future.get();
//            for (CrawledData v: this.crawledDataList) {
//                String title = v.getTitle();
//                sb.append(title + "\n");
//            }
//            Log.d(TAG, "getCrawledVideos: CrawledData" + sb .toString());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return crawledDataList;
  }
}