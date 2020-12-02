package com.abidjalionel.massguest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class welcomehome extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private static final int PICK_IMAGE_REQUEST=1;
    private Button chooseimage,uploadimage;
    private TextView showuploads;
    private EditText eventname,eventorganiser,eventlocation,eventdate,eventseat;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri imageUri;
    private StorageReference storageref;
    private DatabaseReference dataref;
    private StorageTask uploadtask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomehome);
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        chooseimage=findViewById(R.id.btn_upload);
        uploadimage=findViewById(R.id.create);
        showuploads=findViewById(R.id.show_upload);
        eventname=findViewById(R.id.event_name);
        eventorganiser=findViewById(R.id.event_owner);
        eventlocation=findViewById(R.id.event_location);
        eventdate=findViewById(R.id.event_dates);
        eventseat=findViewById(R.id.event_seats);
        imageView=findViewById(R.id.image_area);
        progressBar=findViewById(R.id.progress_bar);

        storageref= FirebaseStorage.getInstance().getReference("uploads");
        dataref= FirebaseDatabase.getInstance().getReference("uploads");


        chooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name,owner,location,date,seat;
                name=eventname.getText().toString().trim();
                owner=eventorganiser.getText().toString().trim();
                location=eventlocation.getText().toString().trim();
                date=eventdate.getText().toString().trim();
                seat=eventseat.getText().toString().trim();
                if (name.isEmpty()){
                    eventname.setError("Please, Enter an event  Name");
                }
                else if (owner.isEmpty()){
                    eventorganiser.setError("Please, Enter an organiser");
                }
                else if (location.isEmpty()){
                    eventlocation.setError("Please,Enter an event location");
                }
                else if (date.isEmpty()){
                    eventdate.setError("Please,Enter an event time");
                }
                else if (seat.isEmpty()){
                    eventseat.setError("Please,Enter an event seats");
                }
                else {

                    if (uploadtask != null && uploadtask.isInProgress()) {
                        Toast.makeText(welcomehome.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                    } else {

                        uploadFile();
                    }
                }

            }
        });
        showuploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private void openFileChooser(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!= null && data.getData()!= null)
        {

            imageUri=data.getData();
            imageView.setImageURI(imageUri);
        }
    }
    private String getFileExtension(Uri Uri)
    {

        ContentResolver cR= getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(Uri));
    }
    private void uploadFile(){
       if (imageUri != null){
         StorageReference filereference= storageref.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
          uploadtask=filereference.putFile(imageUri)
                  .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          Thread delay = new Thread(){
                              @Override
                              public void run() {
                                  try {
                                      sleep(500);
                                  } catch (InterruptedException e) {
                                      e.printStackTrace();
                                  }
                              }
                          };
                          delay.start();
                          Toast.makeText(welcomehome.this,"Upload Successful", Toast.LENGTH_SHORT).show();
                          Upload upload=new Upload(eventname.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString(),
                                  eventorganiser.getText().toString().trim(),eventlocation.getText().toString().trim(),eventdate.getText().toString().trim(),eventseat.getText().toString().trim(),
                                  String.valueOf(System.currentTimeMillis()));

                          String UploadId= dataref.push().getKey();
                          dataref.child(UploadId).setValue(upload);

                      }
                  })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(welcomehome.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                      }
                  })
                  .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                          double progress=(100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                          progressBar.setProgress((int) progress);
                      }
                  });
       } else {

           Toast.makeText(this, "No File selected", Toast.LENGTH_SHORT).show();
       }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            firebaseAuth= FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        else if (id == R.id.exit)
        {

            final AlertDialog.Builder builder= new AlertDialog.Builder(welcomehome.this);
            builder.setTitle("Exiting App");
            builder.setMessage("Are you sure? You want to exit app?");
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(1);
                }
            });
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }



}