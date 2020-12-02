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

public class registrationactivity extends AppCompatActivity {
    EditText email,password;
    Button register;
    TextView login;
    ProgressDialog dialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationactivity);
        email=findViewById(R.id.edt_email);
        password=findViewById(R.id.edt_pass);
        register=findViewById(R.id.btn_signup);
        login=findViewById(R.id.tv_signupp);

        firebaseAuth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);
        dialog.setTitle("Registering!!");
        dialog.setMessage("Please wait....!");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail,userpassword;
                useremail=email.getText().toString().trim();
                userpassword=password.getText().toString().trim();
                if (useremail.isEmpty()){
                    email.setError("Please, Enter an email");
                }
               else if (userpassword.isEmpty()){

                   password.setError("Please,Enter password");
                }
               else if (userpassword.length()<6){
                   password.setError("Please,Enter characters that are more than 6");
                }
               else {
                   dialog.show();
                   firebaseAuth.createUserWithEmailAndPassword(useremail,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           dialog.dismiss();
                           if (task.isSuccessful()){
                               Toast.makeText(registrationactivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getApplicationContext(),homeactivity.class));
                           }
                           else{
                               Toast.makeText(registrationactivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
 startActivity(new Intent(getApplicationContext(),registrationactivity.class));
                           }
                       }
                   });
                }
            }
        }
        );
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class) );
                finish();
            }
        });

    }
}