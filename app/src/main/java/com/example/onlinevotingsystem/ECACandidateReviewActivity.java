package com.example.onlinevotingsystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class ECACandidateReviewActivity extends AppCompatActivity {
    private DatabaseReference candidatesDbRef;
    private ListView voters;
    private TextView firstName,lastName,aadharNum,gender,dob,partyName;
    private LinearLayout upparLayout,done;
    private EditText message;
    private Button cAccpet,cDecline,backToMain,backToMain2;
    private ArrayList<String> candidatesId = new ArrayList<String>();
    private int idx=0;
    private ImageView cAadharImg;
    protected  void onCreate(Bundle savedInstateState){
        super.onCreate(savedInstateState);
        setContentView(R.layout.activity_eca_ec_review);
        //voters = findViewById(R.id.voters);
        backToMain = findViewById(R.id.backToMainFromReviewCandidate);
        backToMain2 = findViewById(R.id.backToMainFromReviewCandidate2);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ECAMainActivity.class));
            }
        });
        backToMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ECAMainActivity.class));
            }
        });
        cAccpet = findViewById(R.id.cAccept);
        cDecline = findViewById(R.id.cDecline);
        firstName = findViewById(R.id.cFirstName);
        lastName = findViewById(R.id.cLastName);
        aadharNum = findViewById(R.id.cAadharNum);
        gender = findViewById(R.id.cGender);
        dob = findViewById(R.id.cDob);
        message = findViewById(R.id.cReviewMessage);
        cAadharImg = findViewById(R.id.cAadharImg);
        upparLayout = findViewById(R.id.upperLayout);
        done = findViewById(R.id.doneLayout);
        partyName = findViewById(R.id.cProfileName);
        candidatesDbRef = FirebaseDatabase.getInstance().getReference();
        candidatesDbRef = candidatesDbRef.child("election-candidates");
        candidatesDbRef.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot candidateSnapshot:snapshot.getChildren()){
                    if(candidateSnapshot.child("isVerified").getValue().toString().equals("false")) candidatesId.add(candidateSnapshot.getKey());
                    //Toast.makeText(getApplicationContext(),candidateSnapshot.getKey(),Toast.LENGTH_LONG).show();
                }
                /*try{
                    Toast.makeText(getApplicationContext(),String.valueOf(candidatesId.size()),Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }*/
                try{
                    if(candidatesId.size() > 0)updateUser(candidatesId.get(idx));
                    else done();
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
        cAccpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference curCandidateRef = FirebaseDatabase.getInstance().getReference();
                curCandidateRef = curCandidateRef.child("election-candidates").child(candidatesId.get(idx));
                curCandidateRef.child("isVerified").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Updated"+String.valueOf(idx),Toast.LENGTH_LONG).show();
                        idx++;
                        if(idx < candidatesId.size())updateUser(candidatesId.get(idx));
                        else done();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        cDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference curCandidateRef = FirebaseDatabase.getInstance().getReference();
                curCandidateRef = curCandidateRef.child("election-candidates").child(candidatesId.get(idx));
                curCandidateRef.child("message").setValue(message.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Discard Success: "+String.valueOf(candidatesId.size()),Toast.LENGTH_LONG).show();
                        idx++;
                        if(idx < candidatesId.size())updateUser(candidatesId.get(idx));
                        else done();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        //Log.d("Voter Ids",String.valueOf(candidatesId));
    }

    void updateUser(String uid){
        DatabaseReference candidateRef = FirebaseDatabase.getInstance().getReference();
        candidateRef = candidateRef.child("election-candidates");
        candidateRef = candidateRef.child(uid);
        candidateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firstName.setText(snapshot.child("fName").getValue().toString());
                lastName.setText(snapshot.child("lName").getValue().toString());
                aadharNum.setText(snapshot.child("aadharNum").getValue().toString());
                gender.setText(snapshot.child("gender").getValue().toString());
                dob.setText(snapshot.child("dob").getValue().toString());
                partyName.setText(snapshot.child("pName").getValue().toString());
                StorageReference aadharImgRef = FirebaseStorage.getInstance().getReference().child(uid).child("aadhar-file");
                final long ONE_MEGABYTE = 1024 * 1024;
                aadharImgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        cAadharImg.setImageBitmap(bmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void done(){
        done.setVisibility(View.VISIBLE);
        upparLayout.setVisibility(View.GONE);
    }
}
