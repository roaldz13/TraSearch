package com.example.taquio.trasearch.BusinessProfile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.example.taquio.trasearch.R;

/**
 * Created by Del Mar on 3/15/2018.
 */

public class MaterialHolder extends RecyclerView.ViewHolder {

    CheckBox name;

    public MaterialHolder(View itemView) {
        super(itemView);

        name =  (CheckBox) itemView.findViewById(R.id.name);

    }

}
