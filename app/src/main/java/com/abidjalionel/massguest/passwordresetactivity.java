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
import com.google.firebase.auth.FirebaseAuth;

public class passwordresetactivity extends AppCompatActivity {
    EditText email;
    Button reset;
    TextView login;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordresetactivity);
        email=findViewById(R.id.edit_email);
        reset=findViewById(R.id.btn_signup);
        login=findViewById(R.id.tv_signupp);

        firebaseAuth= FirebaseAuth.getInstance();
        dialog= new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Resetting is loading, Please wait");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail;
                useremail=email.getText().toString().trim();
                if (useremail.isEmpty()){
                    email.setError("Please, Enter email");
                }
                else {

                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                Toast.makeText(passwordresetactivity.this, "Password reset sent to"+email, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(passwordresetactivity.this, "Password reset has failed", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),passwordresetactivity.class));
                            }
                        }
                    });
                    }
            }

        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}