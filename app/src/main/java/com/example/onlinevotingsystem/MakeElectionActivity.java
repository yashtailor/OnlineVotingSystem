package com.example.onlinevotingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MakeElectionActivity extends AppCompatActivity {
    private EditText electionName,startDate,endDate;
    private Button makeElectionBtn,backToMainPage;
    private DatePicker startElectionDate,endElectionDate;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_election);
        electionName = findViewById(R.id.electionName);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        makeElectionBtn = findViewById(R.id.makeElectionBtn);
        startElectionDate = findViewById(R.id.startDatePicker);
        endElectionDate = findViewById(R.id.endDatePicker);
        backToMainPage = findViewById(R.id.backToInfoPageFromCreateElectionECA);
        backToMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ECAMainActivity.class));
            }
        });
        makeElectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = startElectionDate.getDayOfMonth();
                int month = startElectionDate.getMonth();
                int year = startElectionDate.getYear();
                String startElectionDateFormatted = String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                int dayE = endElectionDate.getDayOfMonth();
                int monthE = endElectionDate.getMonth();
                int yearE = endElectionDate.getYear();
                String endElectionDateFormatted = String.valueOf(dayE)+"/"+String.valueOf(monthE+1)+"/"+String.valueOf(yearE);
                DatabaseReference electionsRef = FirebaseDatabase.getInstance().getReference();
                Election curElection = new Election(electionName.getText().toString(),startElectionDateFormatted,endElectionDateFormatted,"","false");
                electionsRef.child("elections").child(electionName.getText().toString()).setValue(curElection).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Election added successfully",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),ECAMainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
