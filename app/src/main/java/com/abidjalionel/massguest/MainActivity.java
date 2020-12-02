package com.abidjalionel.massguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    Button login;
    TextView reset,signup;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.edit_email);
        password=findViewById(R.id.edt_pass);
        login=findViewById(R.id.btn_signup);
        signup=findViewById(R.id.tv_signupp);
        reset=findViewById(R.id.tv_reset);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),registrationactivity.class));
            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please, wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        FirebaseUser user= firebaseAuth.getCurrentUser();
        if (user!=null)
        {
            firebaseAuth= FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
        }
        else{
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String useremail,userpassword;
                    useremail=email.getText().toString().trim();
                    userpassword=password.getText().toString().trim();
                     if (useremail.isEmpty()){
                        email.setError("Please, Enter an email");
                     }
                     else if (userpassword.isEmpty()){
                         password.setError("Please, Enter a password");
                     }
                     else if (userpassword.length()<6){
                         password.setError("Please,Enter password characters that are up to 6");
                     }
                     else{

                        progressDialog.show();
                        firebaseAuth.signInWithEmailAndPassword(useremail,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (useremail.equals("abijurulionel250@gmail.com")) {
                                        Toast.makeText(MainActivity.this, "Welcome Admin" + useremail, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),welcomehome.class));
                                    } else {
                                        Toast.makeText(MainActivity.this, "Log In Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),firstpagee.class));
                                    }
                                }

                                else {
                                    Toast.makeText(MainActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                            }
                        });
                     }
                }
            });
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),passwordresetactivity.class) );
                }
            });
        }


    }
}