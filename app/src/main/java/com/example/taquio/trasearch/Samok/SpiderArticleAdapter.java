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
import com.example.taquio.trasearch.SearchLogic.ArticleData;
import com.example.taquio.trasearch.SearchLogic.ArticleDataAdapter;

import java.util.List;

/**
 * Created by User on 05/04/2018.
 */

public class SpiderArticleAdapter extends RecyclerView.Adapter<SpiderArticleAdapter.MyViewHolder> {
    private List<ArticleCrawledData> articleDataList;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description;
        public LinearLayout articleRow;

        public MyViewHolder (View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.articleListName);
            description = (TextView) view.findViewById(R.id.articleListBody);
            articleRow = (LinearLayout) view.findViewById(R.id.articleRow);
        }
    }

    public SpiderArticleAdapter(List<ArticleCrawledData> articleDataList) {
        this.articleDataList = articleDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        try {
            context = recyclerView.getContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ArticleCrawledData article = articleDataList.get(position);
        holder.name.setText(article.getTitle());
        holder.description.setText(article.getDesc());

        holder.name.setTextColor(ContextCompat.getColor(context, R.color.colorLink));
        holder.articleRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = article.getUrl().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  articleDataList.size();
    }
}