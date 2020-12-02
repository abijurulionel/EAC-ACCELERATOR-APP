package com.abidjalionel.massguest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class firstpagee extends AppCompatActivity{
private RecyclerView mRecycler;
private ImageAdapter madapter;
private FirebaseStorage mstorage;
private DatabaseReference mDatabaseRef;
private ValueEventListener mDBlistener;
private List<Upload> muploads;
private ProgressBar mprogressbar;
private ProgressDialog dialog;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpagee);

        mRecycler=findViewById(R.id.recyclacle_view);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mprogressbar=findViewById(R.id.progress_bar);
        muploads= new ArrayList<>();
        madapter= new ImageAdapter(this,muploads);
        dialog= new ProgressDialog(this);
        dialog.setTitle("loading");
        dialog.setMessage("Please Loading");
        mstorage= FirebaseStorage.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");
         dialog.show();
        mDBlistener= mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                muploads.clear();
                for (DataSnapshot postsnapshot: dataSnapshot.getChildren()){
                    Upload upload= postsnapshot.getValue(Upload.class);
                    upload.setKey(postsnapshot.getKey());
                    muploads.add(upload);
                }
                madapter.notifyDataSetChanged();
                mprogressbar.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                Collections.reverse(muploads);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                mprogressbar.setVisibility(View.INVISIBLE);
            }
        });
        mRecycler.setAdapter(madapter);
    }
}