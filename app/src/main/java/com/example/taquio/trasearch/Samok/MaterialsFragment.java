package com.example.taquio.trasearch.Samok;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialsFragment extends Fragment {
    private View mMainView;
    private DatabaseReference mMaterials;
    private RecyclerView mMaterialList;
    private static final String TAG = "MaterialsFragment";
    private EditText materials_inputName;
    private Button materials_add;
    private Materials materials;
    private boolean flag = false;

    public MaterialsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater
                .inflate(R.layout.fragment_materials,container,false);
        mMaterialList = mMainView.findViewById(R.id.materialView);
        materials_inputName = mMainView.findViewById(R.id.materials_inputName);
        materials_add = mMainView.findViewById(R.id.materials_add);
        Log.d(TAG, "onCreateView: Started");

        mMaterials = FirebaseDatabase.getInstance().getReference().child("Materials");
        mMaterialList.setLayoutManager(new LinearLayoutManager(getContext()));


        materials_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputName = materials_inputName.getText().toString();
                if (TextUtils.isEmpty(inputName))
                {
                    Log.d(TAG, "onClick: InputName is Empty");
                    Toast.makeText(getContext(),"Input Material can't be EMPTY",Toast.LENGTH_SHORT).show();
                    materials_inputName.setSelection(0);
                }
                else
                {
                    Log.d(TAG, "onClick: InputName is not Empty");
                    mMaterials.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                            {
                                Log.d(TAG, "onDataChange: "+dataSnapshot1.getChildren());
                                if(dataSnapshot1.child("name").getValue().toString().equalsIgnoreCase(inputName))
                                {
                                    Log.d(TAG, "onDataChange: Duplicate Entry");
                                    flag = true;
                                }
                                else
                                {
                                    Log.d(TAG, "onDataChange: No duplicate Entry");
                                }
                            }
                            if(!flag)
                            {
                                mMaterials.push().child("name").setValue(inputName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        materials_inputName.setText("");
                                        materials_inputName.setSelection(0);
                                    }
                                });
                            }else{
                                Toast.makeText(getContext(),"Duplicate Entry",Toast.LENGTH_SHORT).show();
                                flag=false;
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        
        Log.d(TAG, "onCreateView: Done Return");

        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Started");
        
        final FirebaseRecyclerAdapter<Materials,MaterialsViewHolder> materialsAdapter = new FirebaseRecyclerAdapter<Materials, MaterialsViewHolder>(
                Materials.class,
                R.layout.materials_single,
                MaterialsViewHolder.class,
                mMaterials.orderByChild("name")

        ) {

            @Override
            protected void populateViewHolder(final MaterialsViewHolder viewHolder, Materials model, final int position) {

                final String materialsPushIDs = getRef(viewHolder.getAdapterPosition()).getKey();

                Log.d(TAG, "populateViewHolder: Started");

                mMaterials.child(materialsPushIDs).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(final DataSnapshot dataSnapshot) {
                       Log.d(TAG, "onDataChange: Started");

                           viewHolder.setName(dataSnapshot.child("name").getValue().toString());
                           viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(final View v) {
                                   Log.d(TAG, "onClick: Clicked");
                                   AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                   alertDialog.setTitle("UPDATE");
                                   alertDialog.setMessage(dataSnapshot.child("name").getValue().toString());

                                   final EditText MaterialName = new EditText(getContext());
                                   LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                           LinearLayout.LayoutParams.MATCH_PARENT,
                                           LinearLayout.LayoutParams.MATCH_PARENT);
                                   MaterialName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                                   MaterialName.setHint(dataSnapshot.child("name").getValue().toString());
                                   MaterialName.setLayoutParams(lp);
                                   alertDialog.setView(MaterialName);
//                                   alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                                       @Override
//                                       public void onClick(DialogInterface dialog, int which) {
//                                           Log.d(TAG, "onClick: Deleted");
//                                           DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
//                                           firstChild.getRef().removeValue();
////                                           mMaterials.child(materialsPushIDs).child("name").removeValue();
//                                       }
//                                   });
                                   alertDialog.setNegativeButton("Update", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           Log.d(TAG, "onClick: Update");
                                           String updated = MaterialName.getText().toString();
                                           mMaterials.child(materialsPushIDs).child("name").setValue(updated);
                                       }
                                   });
                                   alertDialog.show();
                               }
                           });

                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });
            }

        };
        mMaterialList.setAdapter(materialsAdapter);
        materialsAdapter.notifyDataSetChanged();

    }

    public static class MaterialsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public MaterialsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName (String name)
        {
            TextView materialsName = itemView.findViewById(R.id.material_name);
            materialsName.setText(name);
        }




    }
}
