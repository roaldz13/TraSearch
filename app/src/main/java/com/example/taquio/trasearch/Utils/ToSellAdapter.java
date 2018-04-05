package com.example.taquio.trasearch.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taquio.trasearch.Models.ToBuy;
import com.example.taquio.trasearch.Models.ToSell;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Samok.UserJunkShopView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Edward on 27/03/2018.
 */

public class ToSellAdapter extends RecyclerView.Adapter<ToSellAdapter.ViewHolder>{

    private static final String TAG = "Enter Sell Adapter" ;
    private List<ToSell> mysellMaterials;
    private Context mcontext;

    public ToSellAdapter(ArrayList<ToSell> tosellList, UserJunkShopView userJunkShopView) {

        mysellMaterials = tosellList;
        mcontext = userJunkShopView;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView material;
        public ViewHolder(View itemView) {
            super(itemView);

            material = itemView.findViewById(R.id.tvSell);
        }

    }
        @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tosell_list, parent, false);
            
            return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ToSell sell = mysellMaterials.get(position);
        Log.d(TAG, "onBindViewHolder: ");
        holder.material.setText(sell.getMatname());

    }

    @Override
    public int getItemCount() {
        return mysellMaterials.size();
    }
}
