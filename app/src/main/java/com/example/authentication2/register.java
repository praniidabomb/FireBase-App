package com.example.authentication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.proto.TargetGlobal;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "TAG";
    EditText regName, regEmail, regPass;
    Button regButt;
    TextView regToLog;
    ProgressBar regProg;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regName = (findViewById(R.id.regName));
        regEmail = (findViewById(R.id.regEmail));
        regPass = (findViewById(R.id.regPass));
        regButt = (findViewById(R.id.regButt));
        regToLog = (findViewById(R.id.regToLog));
        regProg = (findViewById(R.id.regProg));
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
    @Override
    public void onClick(View v) {
        final String email = regEmail.getText().toString().trim();
        final String pass = regPass.getText().toString().trim();
        final String name = regName.getText().toString();

        if(TextUtils.isEmpty(email)){
            regEmail.setError("Invalid Email Address.");
            return;
        }
        if(TextUtils.isEmpty(pass) || pass.length() < 6){
            regPass.setError("Password greater than 6 characters required.");
            return;
        }

        regProg.setVisibility(View.VISIBLE);

        fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    userId = fAuth.getCurrentUser().getUid();
                    DocumentReference dRef = fStore.collection("userId").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Nmae", name);
                    user.put("Email Address", email);
                    user.put("Password", pass);
                    dRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: User created successfully for" + userId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: User not created"+ e.toString());
                        }
                    });
                    
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    regProg.setVisibility(View.INVISIBLE);
                }else {
                    Toast.makeText(register.this, "Registration Unsuccessful" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    regProg.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void toLog(View v){
        Intent intent = new Intent(register.this, login.class);
        startActivity(intent);
    }
}
