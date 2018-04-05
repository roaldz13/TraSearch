package com.example.taquio.trasearch.Samok;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Utils.UniversalImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 1/1/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private List<Junkshop> mylist;
    private ArrayList<String> mUsers;
    private ArrayList<String> businessUser;
    private DatabaseReference mUserDatabase;
    private Context context, mcontext;

    public RecyclerViewAdapter(List<Junkshop> mjunkshop, Context mcontext) {
        this.mylist = mjunkshop;
        this.mcontext = mcontext;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

         View mview;
         TextView address;
         ImageView businessPhoto;
         Junkshop shop = new Junkshop();
         LinearLayout lin;
        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            address = itemView.findViewById(R.id.image_name);
            businessPhoto = itemView.findViewById(R.id.businessImage);
            lin = itemView.findViewById(R.id.linout);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.layout_listitem, parent, false);
        context = parent.getContext();

        mUsers = new ArrayList<>();
        businessUser = new ArrayList<>();
        mUserDatabase = FirebaseDatabase.getInstance().getReference();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Junkshop junklist = mylist.get(position);

        String busname = junklist.getBsnBusinessName();
           holder.address.setText(busname);

        final String busphoto = junklist.getImage();
//        UniversalImageLoader.setImage(junklist.getImage(),
//                holder.businessPhoto, null, "");

       Picasso.with(mcontext).load(junklist.getImage())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.shop)
                .into(holder.businessPhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mcontext)
                                .load(busphoto)
                                .placeholder(R.drawable.shop)
                                .into(holder.businessPhoto);
                    }
                });

        Query query = mUserDatabase
                .child("Users")
                .orderByChild("userID")
                .equalTo(junklist.getUserID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    holder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "Lets go to Profile!" + holder.shop, Toast.LENGTH_LONG).show();
                            Intent busprofileIntent = new Intent(v.getContext(), UserJunkShopView.class);
                            busprofileIntent.putExtra("busprofile", holder.shop);
                            v.getContext().startActivity(busprofileIntent);
                        }
                    });
                    holder.lin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Toast.makeText(context, "Lets go to Profile!" + holder.shop, Toast.LENGTH_LONG).show();
                            Intent busprofileIntent = new Intent(v.getContext(), UserJunkShopView.class);
                            busprofileIntent.putExtra("busprofile", holder.shop);
                            v.getContext().startActivity(busprofileIntent);
                        }
                    });
//                    holder.businessPhoto.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(context, "Lets go to Profile!" + holder.shop, Toast.LENGTH_LONG).show();
//                            Intent busprofileIntent = new Intent(v.getContext(), UserJunkShopView.class);
//                            busprofileIntent.putExtra("busprofile", holder.shop);
//                            v.getContext().startActivity(busprofileIntent);
//
//                        }
//                    });
                    holder.shop = singleSnapshot.getValue(Junkshop.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }
}

