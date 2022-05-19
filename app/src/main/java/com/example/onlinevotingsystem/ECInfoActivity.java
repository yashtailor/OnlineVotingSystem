package com.example.onlinevotingsystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ECInfoActivity extends AppCompatActivity {
    private Button goToEditPage,goToResultsPage,logout;
    private TextView messageText;
    private CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ec_info);
        goToEditPage = findViewById(R.id.goToMainPage);
        goToResultsPage = findViewById(R.id.goToResultsPage);
        messageText = findViewById(R.id.messageTxt);
        cardView = findViewById(R.id.reviewTextCandidate);
        FirebaseUser curCandidate = FirebaseAuth.getInstance().getCurrentUser();
        logout = findViewById(R.id.logoutCandidate);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        DatabaseReference voterRef = FirebaseDatabase.getInstance().getReference();
        voterRef = voterRef.child("election-candidates").child(curCandidate.getUid());
        voterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("isVerified").getValue().toString().equals("true")){
                    messageText.setText("Profile verified. Authorized for enrolling in elections.");
                    cardView.setCardBackgroundColor(Color.GREEN);
                    messageText.setTextColor(Color.BLACK);
                }else if(snapshot.child("message").getValue().toString().equals("")){
                    //do nothing
                }else{
                    messageText.setText("Status is Declined. "+snapshot.child("message").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        goToEditPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ECMainActivity.class));
            }
        });

        goToResultsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResultsActivity.class));
            }
        });
    }
}