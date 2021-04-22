package com.example.onlinevotingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ECAMainActivity extends AppCompatActivity {
    private Button makeElectionBtn,reviewVoterBtn,reviewCandidateBtn,calculateResultsBtn,viewResultsBtn,logout;
    protected  void onCreate(Bundle savedInstateState){
        super.onCreate(savedInstateState);
        setContentView(R.layout.activity_eca_main);
        makeElectionBtn = findViewById(R.id.makeElectionPage);
        reviewVoterBtn = findViewById(R.id.reviewVoterDetails);
        reviewCandidateBtn = findViewById(R.id.reviewCandidateDetails);
        calculateResultsBtn = findViewById(R.id.viewAndCalculateResults);
        viewResultsBtn = findViewById(R.id.viewResultsBtn);
        reviewVoterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ECAVoterReviewActivity.class));
            }
        });
        logout = findViewById(R.id.logoutECA);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        makeElectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MakeElectionActivity.class));
            }
        });
        reviewCandidateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ECACandidateReviewActivity.class));
            }
        });
        calculateResultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ElectionCalculateResults.class));
            }
        });
        viewResultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResultsActivity.class));
            }
        });
    }
}
