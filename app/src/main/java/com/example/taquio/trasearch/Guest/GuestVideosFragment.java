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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
  private DatabaseReference videosDatabse;


  public GuestVideosFragment() {}
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_guest_videos_fragment, container, false);
    GuestHome activity = (GuestHome)getActivity();
    Bundle result = activity.sendVideoData();
    videosDatabse = FirebaseDatabase.getInstance().getReference().child("TraSearch");
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
      final HashMap<Integer, VideoData> videoData;
//            StringBuilder sb = new StringBuilder();
//            videoData = future.get();
      videoDataList =  future.get();
      for (int x = 0; x < videoDataList.size(); x++) {
        VideoData value = videoDataList.get(x);
        VideoData tobeSaved = new VideoData();
        tobeSaved.setVideoId(value.getVideoId());
        tobeSaved.setChannelTitle(value.getChannelTitle());
        tobeSaved.setDefaultThumbUrl(value.getDefaultThumbUrl());
        tobeSaved.setDescription(value.getDescription());
        tobeSaved.setTitle(value.getTitle());
        videosDatabse.child("Videos").child(searchQuery).child(value.getVideoId()).setValue(tobeSaved);
      }

//      videosDatabse.child("Videos").child(searchQuery).addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//          for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
//          {
//            VideoData videoData1 = new VideoData();
////            HashMap<String, VideoData> objectMap = (HashMap<String, VideoData>) dataSnapshot1.getValue();
//            HashMap<String, VideoData> objectMap = new HashMap<>();
//            objectMap = (HashMap<String, VideoData>) dataSnapshot.getValue();
//            HashMap<Integer, VideoData> data = new HashMap<>();
////            List<String> key = new LinkedList<>(objectMap.keySet());
//            int x = 0;
//            for (String index: objectMap.keySet()) {
//              VideoData value = objectMap.get(index);
////              VideoData value = VideoData();
////              value.setVideoId(objectMap.get(index));
//              data.put(x, value);
//              x++;
//            }
//            Log.d(TAG, "onDataChange: Object Map: "+objectMap);
////            try {
////              videoData1.setTitle(objectMap.get("title").toString());
////              videoData1.setDescription(objectMap.get("description").toString());
////              videoData1.setDefaultThumbUrl(objectMap.get("defaultThumbUrl").toString());
////              videoData1.setChannelTitle(objectMap.get("channelTitle").toString());
////              videoData1.setVideoId(objectMap.get("videoId").toString());
////            }catch(NullPointerException e){
////              Log.e(TAG, "onDataChange: NullPointerException: " + e.getMessage() );
////            }
////            VideoHTTPRequest videoHTTPRequest = new VideoHTTPRequest();
////            int index = 0;
////            for (String key: objectMap.keySet()) {
////              VideoData value = objectMap.get(key);
////              data.put(index, value);
////              index++;
////            }
//
////            VideoHTTPRequest request = new VideoHTTPRequest();
////            videoDataList = request.convertToListDatabase(objectMap);
//            Log.d(TAG, "onDataChange: "+videoDataList);
//          }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//      });

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