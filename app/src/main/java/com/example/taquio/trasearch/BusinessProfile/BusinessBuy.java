package com.example.taquio.trasearch.BusinessProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.taquio.trasearch.Models.ToBuy;
import com.example.taquio.trasearch.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Del Mar on 2/24/2018.
 */

public class BusinessBuy extends AppCompatActivity {
    private static final String TAG = "BusinessBuy";
    private RecyclerView recyclerView;
    private DatabaseReference ref;
    String materialName;
    ArrayList<Material> mat = new ArrayList<>();
    Button btn;
    FirebaseUser user;
    DatabaseReference db;
    String key, matvalue[];
    boolean flag = false, fake=false;
    int count = 0, k =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_buy_activity);

        db = FirebaseDatabase.getInstance().getReference().child("ToBuy");

        user = FirebaseAuth.getInstance().getCurrentUser();
        btn = (Button) findViewById(R.id.addItem);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ArrayList<String> materials = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference().child("Materials");
        
        FirebaseRecyclerAdapter<Material, MaterialHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Material, MaterialHolder>
                (Material.class, R.layout.business_material_info, MaterialHolder.class, ref) {
            @Override
            protected void populateViewHolder(final MaterialHolder viewHolder, final Material model, final int position) {
                viewHolder.name.setText(model.getName());


                // mao ning checkboxes
                viewHolder.name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {

                        if(compoundButton.isChecked()) {
                            compoundButton.setEnabled(false);
                            materialName = viewHolder.name.getText().toString();
                            FirebaseDatabase.getInstance().getReference().child("ToBuy").child(user.getUid())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot sp : dataSnapshot.getChildren()) {

                                                Log.d(TAG, "kuha: " + sp.child("matname").getValue());
                                                Log.d(TAG, "kuha: " + materialName);
                                                if (materialName.equals(sp.child("matname").getValue())) {
                                                    compoundButton.setEnabled(true);
                                                    compoundButton.setChecked(false);
                                                    Toast.makeText(BusinessBuy.this, materialName + " already displayed", Toast.LENGTH_SHORT).show();
                                                    flag = false;
                                                    fake = true;

                                                }
                                            }
                                            if (!flag) {
                                                if(fake){
                                                    if (materialName != null) {
                                                        materialName = "";
                                                    }
                                                }else {
                                                    materials.add(materialName);
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                        }
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                for (int j=0; j <materials.size(); j++) {
//                    if(matval.get(k).equals(materials.get(j))){
////                    Log.d(TAG, "c1: "+ materialName);
////                    Log.d(TAG, "c2: "+ materials.get(j));
////                    Log.d(TAG, "c3: "+ materials.remove(j));
//                        materials.remove(j);
//                    }
//                    if(k==matval.size()-1){
//                        for (int x=0; x <materials.size(); x++) {
//                            Map map = new HashMap();
//                            map.put("matname", materials.get(x));
//                            key = db.child("ToBuy").child(user.getUid()).push().getKey();
//                            db.child(user.getUid()).child(key).setValue(map);
//                        }
//                    }
//                    if(j==materials.size()-1){
//                        j=0;
//                        if(k<matval.size()){
//                            k+=1;
//                        }
//                    }
//                }

                                    for (int x = 0; x < materials.size(); x++) {
                                        Map map = new HashMap();
                                        map.put("matname", materials.get(x));
                                        key = db.child("ToBuy").child(user.getUid()).push().getKey();
                                        db.child(user.getUid()).child(key).setValue(map);
                                    }
                                    startActivity(new Intent(BusinessBuy.this, BusinessProfile.class));
                                }

                        });


                    }
                });

            }
        };
        recyclerView.setAdapter(recyclerAdapter);

        //mao ning button para ma add ang material
        // ga add sa database TO BUY




    }


}
