package com.example.authentication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity implements View.OnClickListener {

    EditText logEmail, logPass;
    Button logButt;
    TextView logToReg;
    FirebaseAuth fAuth;
    ProgressBar logProg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logEmail = (findViewById(R.id.logEmail));
        logPass = (findViewById(R.id.logPass));
        logButt = (findViewById(R.id.logButt));
        logToReg = (findViewById(R.id.logToReg));
        fAuth = FirebaseAuth.getInstance();
        logProg = (findViewById(R.id.logProg));

    }
    @Override
    public void onClick(View v) {

        String email = logEmail.getText().toString().trim();
        String pass = logPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(login.this, "Email Required" , Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass) || pass.length() < 6){
            Toast.makeText(login.this, "Password more than 6 characters required" , Toast.LENGTH_SHORT).show();
            return;
        }
        logProg.setVisibility(View.VISIBLE);

        fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(login.this, "Logged in Successfully" , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    logProg.setVisibility(View.INVISIBLE);
                }else {
                    Toast.makeText(login.this, "Log In Unsuccessful" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    logProg.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public void toReg(View view){
        Intent intent = new Intent(login.this, register.class);
        startActivity(intent);
    }
}