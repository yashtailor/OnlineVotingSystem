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
import java.util.Map;

public class ElectionCalculateResults extends AppCompatActivity {
    private LinearLayout showAllRunningElectionsToCalculate;
    private DatabaseReference electionDbRef;
    private FirebaseUser curUser;
    private Button backToMainPage;
    private LinearLayout linearLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_elections);
        showAllRunningElectionsToCalculate = findViewById(R.id.showAllRunningElectionsToCalculate);
        electionDbRef = FirebaseDatabase.getInstance().getReference();
        electionDbRef = electionDbRef.child("elections");
        curUser = FirebaseAuth.getInstance().getCurrentUser();
        backToMainPage = findViewById(R.id.backToInfoPageFromMainECA);
        linearLayout = findViewById(R.id.noOngoingElectionsToEndLayout);
        backToMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ECAMainActivity.class));
            }
        });
        electionDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int numElections = 0;
                for (DataSnapshot candidateSnapshot : snapshot.getChildren()) {
                    if(candidateSnapshot.child("isDone").getValue().toString().equals("true")){
                        //don't add
                    }else{
                        numElections++;
                        Button curElectionBtn = new Button(getApplicationContext());
                        curElectionBtn.setId(View.generateViewId());
                        String electionName = candidateSnapshot.child("electionName").getValue().toString();
                        curElectionBtn.setText(electionName);
                        curElectionBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String,Integer> results = new HashMap<>();
                                candidateSnapshot.getRef().child("results").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot innerSnapshot) {
                                        for(DataSnapshot resultSnapshot:innerSnapshot.getChildren()){
                                            String partyName = resultSnapshot.getValue().toString();
                                            if(results.containsKey(partyName)){
                                                int count = results.get(partyName);
                                                results.put(partyName,count+1);
                                            }else{
                                                results.put(partyName,1);
                                            }
                                        }
                                        for(Map.Entry result:results.entrySet()){
                                            candidateSnapshot.getRef()
                                                    .child("candidate-results")
                                                    .child(result.getKey().toString())
                                                    .setValue(result.getValue().toString());
                                        }
                                        Toast.makeText(getApplicationContext(),"Calculated",Toast.LENGTH_LONG).show();
                                        curElectionBtn.setVisibility(View.GONE);
                                        candidateSnapshot.child("isDone").getRef().setValue("true");
                                        finish();
                                        startActivity(getIntent());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                        showAllRunningElectionsToCalculate.addView(curElectionBtn);
                    }
                }
                if(numElections==0)linearLayout.setVisibility(View.VISIBLE);
                else linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}