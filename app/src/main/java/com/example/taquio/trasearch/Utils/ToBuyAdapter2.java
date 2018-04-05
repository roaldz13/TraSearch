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
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Samok.UserJunkShopView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Edward on 27/03/2018.
 */

public class ToBuyAdapter2 extends RecyclerView.Adapter<ToBuyAdapter2.ViewHolder>{

    private static final String TAG = "Enter BUy Adapter" ;
    private List<ToBuy> mybuyMaterials;
    private Context mcontext;

    public ToBuyAdapter2(ArrayList<ToBuy> tobuyList, Context context) {
        mybuyMaterials = tobuyList;
        mcontext = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView material;
        public ViewHolder(View itemView) {
            super(itemView);

            material = itemView.findViewById(R.id.tvBuy);
        }

    }
        @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tobuy_list, parent, false);
            
            return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ToBuy buy = mybuyMaterials.get(position);
        Log.d(TAG, "onBindViewHolder: ");
        holder.material.setText(buy.getMatname());

    }

    @Override
    public int getItemCount() {
        return mybuyMaterials.size();
    }
}
