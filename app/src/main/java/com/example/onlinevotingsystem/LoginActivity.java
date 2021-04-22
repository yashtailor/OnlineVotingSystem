package com.example.onlinevotingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.SharedMemory;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView;
    private Button Btn,btnECA,btnEC,backToRegisterBtn;
    private ProgressBar progressBar;
    public static String curUserType;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
//        if(mAuth.getCurrentUser()!=null){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//        }
        // initialising all views through id defined above
        emailTextView = findViewById(R.id.emailLogin);
        passwordTextView = findViewById(R.id.password);
        Btn = findViewById(R.id.login);
        btnECA = findViewById(R.id.loginECA);
        progressBar = findViewById(R.id.progressBar);
        btnEC = findViewById(R.id.loginEC);
        backToRegisterBtn = findViewById(R.id.backToRegisterBtn);
        // Set on Click Listener on Sign-in button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount("voter");
            }
        });

        btnEC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount("ec");
            }
        });

        btnECA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = emailTextView.getText().toString();
                password = passwordTextView.getText().toString();
                if(email.contains("@") == false){
                    Toast.makeText(getApplicationContext(),"Email must have @ in it",Toast.LENGTH_LONG).show();
                    return;
                }
                if(email.contains(".") == false){
                    Toast.makeText(getApplicationContext(),"Email must have . in it",Toast.LENGTH_LONG).show();
                    return;
                }
                if(email.equals("eca@gov.in") && password.equals("eca123")){
                    curUserType = "eca";
                    Toast.makeText(getApplicationContext(),"Successfully logged in",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),ECAMainActivity.class));
                }
            }
        });

        backToRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
    }

    private void loginUserAccount(String type)
    {

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);
        curUserType = type;
        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "Login successful!!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressBar.setVisibility(View.GONE);
                                    if(type == "ec"){
                                        startActivity(new Intent(getApplicationContext(), ECMainActivity.class));
                                        return;
                                    }
                                    FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
                                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                                    try{
                                        DatabaseReference subDbRef = dbRef.child("user-data").child(curUser.getUid());
                                        subDbRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                //Toast.makeText(getApplicationContext(),snapshot.child("hasSubmitted").getValue().toString(),Toast.LENGTH_LONG).show();
                                                if(snapshot.hasChild("hasSubmitted")){
                                                    if(snapshot.child("hasSubmitted").getValue().toString().equals("true")){
                                                        startActivity(new Intent(getApplicationContext(),InfoActivity.class));
                                                    }else startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                }else startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }catch(Exception e){
                                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                            "Login failed!!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
    }

    public Boolean isCurUserLoggedIn(){
        try{
            System.out.println(mAuth.getCurrentUser().getUid());
        }catch(Exception e){
            return false;
        }
        return true;
    }
    public void logout(){
        mAuth.signOut();
    }
}