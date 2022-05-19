package com.example.onlinevotingsystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ElectionActivity extends AppCompatActivity {
    private Button chooseElectionCandidateBtn,goToAllElections;
    private String curElectionName;
    private DatabaseReference candidateDbRef,electionDbRef;
    private FirebaseUser curUser;
    private String uid,result;
    private RadioGroup electionCandidatesRadioGroup;
    private TextView electionTxtView;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election);
        curElectionName = getIntent().getStringExtra("electionName");
        chooseElectionCandidateBtn = findViewById(R.id.chooseElectionCandidateBtn);
        electionCandidatesRadioGroup = findViewById(R.id.electionCandidatesRadioGroup);
        electionTxtView = findViewById(R.id.electionTxtView);
        electionTxtView.setText(curElectionName);
        candidateDbRef = FirebaseDatabase.getInstance().getReference();
        electionDbRef = FirebaseDatabase.getInstance().getReference().child("elections");
        candidateDbRef = candidateDbRef.child("election-candidates");
        goToAllElections = findViewById(R.id.goToAllElections);
        goToAllElections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ShowAllCurrentElections.class));
            }
        });
        candidateDbRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot candidateSnapshot:snapshot.getChildren()){
                    RadioButton newCandidate = new RadioButton(getApplicationContext());
                    newCandidate.setTextColor(Color.WHITE);
                    newCandidate.setId(View.generateViewId());
                    if(candidateSnapshot.child("isVerified").getValue().toString().equals("true")) {
                        String partyName = candidateSnapshot.child("pName").getValue().toString();
                        String candidateFName = candidateSnapshot.child("fName").getValue().toString();
                        String candidateLName = candidateSnapshot.child("lName").getValue().toString();
                        newCandidate.setText(partyName+" - "+candidateFName+" "+candidateLName);
                        newCandidate.setTextColor(Color.WHITE);
                        electionCandidatesRadioGroup.addView(newCandidate);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chooseElectionCandidateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) findViewById(electionCandidatesRadioGroup.getCheckedRadioButtonId());
                String candidate = rb.getText().toString().split(" ")[0];
                FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
                electionDbRef = electionDbRef.child(curElectionName).child("results").child(curUser.getUid());
                electionDbRef.setValue(candidate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Candidate Selected",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),ShowAllCurrentElections.class));
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
