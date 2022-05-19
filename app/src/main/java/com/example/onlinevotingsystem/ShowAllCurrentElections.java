package com.example.onlinevotingsystem;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ShowAllCurrentElections extends AppCompatActivity {
    private LinearLayout showAllRunningElections,noElectionsLayout;
    private DatabaseReference electionDbRef;
    private FirebaseUser curUser;
    private Button goBackToInfoPageFromAllElections;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_elections);
        showAllRunningElections = findViewById(R.id.showAllRunningElections);
        electionDbRef = FirebaseDatabase.getInstance().getReference();
        electionDbRef = electionDbRef.child("elections");
        curUser = FirebaseAuth.getInstance().getCurrentUser();
        goBackToInfoPageFromAllElections = findViewById(R.id.backToInfoPageFromAllElections);
        noElectionsLayout = findViewById(R.id.noOngoingElectionsLayout);
        goBackToInfoPageFromAllElections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),InfoActivity.class));
            }
        });
        electionDbRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int numElections = 0;
                for (DataSnapshot candidateSnapshot : snapshot.getChildren()) {
                    if(candidateSnapshot.child("results").hasChild(curUser.getUid()) || candidateSnapshot.child("isDone").equals("true")){
                        //don't add
                    }else{
                        numElections++;
                        Button curElectionBtn = new Button(getApplicationContext());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            curElectionBtn.setElevation(2);
                        }
                        curElectionBtn.setId(View.generateViewId());
                        String electionName = candidateSnapshot.child("electionName").getValue().toString();
                        curElectionBtn.setText(electionName);
                        curElectionBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(),ElectionActivity.class);
                                intent.putExtra("electionName",electionName);
                                startActivity(intent);
                            }
                        });
                        showAllRunningElections.addView(curElectionBtn);
                    }
                }
                if(numElections==0)noElectionsLayout.setVisibility(View.VISIBLE);
                else noElectionsLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}
