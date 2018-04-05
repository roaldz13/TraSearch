package com.example.taquio.trasearch.Samok;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taquio.trasearch.R;

import java.util.List;

/**
 * Created by User on 01/04/2018.
 */

public class SpiderDataAdapter extends RecyclerView.Adapter<SpiderDataAdapter.MyViewHolder>{
  //    private String TAG = "Spider Data Adapter";
  private List<CrawledData> videoDataList;
  private Context context;
  public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title, description, channelTitle;
    public LinearLayout videoRow;

    public MyViewHolder(View view) {
      super(view);
      title = (TextView) view.findViewById(R.id.videoListTitle);
      description = (TextView) view.findViewById(R.id.videoListDesc);
      channelTitle = (TextView) view.findViewById(R.id.videoListChannel);
      videoRow = (LinearLayout) view.findViewById(R.id.videoRow);
    }
  }

  public SpiderDataAdapter(List<CrawledData>  videoDataList) {this.videoDataList = videoDataList; }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_row, parent, false);
    return new MyViewHolder(itemView);
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    context = recyclerView.getContext();
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    final CrawledData data = videoDataList.get(position);
    holder.title.setText(data.getTitle());
    holder.description.setText(data.getDesc());
    holder.channelTitle.setText(data.getChannelName());

    holder.title.setTextColor(ContextCompat.getColor(context, R.color.colorLink));
    holder.videoRow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String url = data.getUrl().toString();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        v.getContext().startActivity(i);
      }
    });
  }

  @Override
  public int getItemCount() {
    return videoDataList.size();
  }
}