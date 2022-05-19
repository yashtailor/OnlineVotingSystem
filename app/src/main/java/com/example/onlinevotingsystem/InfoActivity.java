package com.example.onlinevotingsystem;

import android.annotation.SuppressLint;
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

public class InfoActivity extends AppCompatActivity {
    private Button goToPendingElectionsPage,goToEditPage,vGoToResultsPage,logout;
    private CardView cardView;
    private TextView messageText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        goToPendingElectionsPage = findViewById(R.id.goToPendingElectionsActivity);
        goToEditPage = findViewById(R.id.goToMainPage);
        vGoToResultsPage = findViewById(R.id.vGoToResultsPage);
        messageText = findViewById(R.id.messageTxt);
        logout = findViewById(R.id.logout);
        cardView = findViewById(R.id.reviewTextVoter);
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        DatabaseReference voterRef = FirebaseDatabase.getInstance().getReference();
        voterRef = voterRef.child("user-data").child(curUser.getUid());
        voterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("isVerified").getValue().toString().equals("true")){
                    messageText.setText("Profile verified. Authorized for voting.");
                    cardView.setCardBackgroundColor(Color.parseColor("#4CAF50"));
                }else{
                    String fmsg = snapshot.child("message").getValue().toString();
                    if(fmsg.length()==0)fmsg="Profile is under Review. Not yet authorized for Voting.";
                    else fmsg = "Status is Declined. "+fmsg;
                    messageText.setText(fmsg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        goToPendingElectionsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ShowAllCurrentElections.class));
            }
        });
        goToEditPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        vGoToResultsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResultsActivity.class));
            }
        });
    }
}
