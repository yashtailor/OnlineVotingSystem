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

public class ECAVoterReviewActivity extends AppCompatActivity {
    private DatabaseReference votersDbRef;
    private ListView voters;
    private TextView firstName,lastName,aadharNum,gender,dob;
    private LinearLayout upparLayout,doneBtn;
    private EditText message;
    private Button vAccpet,vDecline,backToMain,backToMain2;
    private ArrayList<String> voterIds = new ArrayList<String>();
    private int idx=0;
    private ImageView vAadharImg;
    protected  void onCreate(Bundle savedInstateState){
        super.onCreate(savedInstateState);
        setContentView(R.layout.activity_eca_voter_review);
        //voters = findViewById(R.id.voters);
        vAccpet = findViewById(R.id.vAccept);
        vDecline = findViewById(R.id.vDecline);
        firstName = findViewById(R.id.vFirstName);
        lastName = findViewById(R.id.vLastName);
        aadharNum = findViewById(R.id.vAadharNum);
        gender = findViewById(R.id.vGender);
        dob = findViewById(R.id.vDob);
        message = findViewById(R.id.vReviewMessage);
        vAadharImg = findViewById(R.id.vAadharImg);
        upparLayout = findViewById(R.id.upperLayoutVoter);
        backToMain = findViewById(R.id.backToMainFromReviewVoter);
        backToMain2 = findViewById(R.id.backToMainFromReviewVoter2);
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
        doneBtn = findViewById(R.id.doneLayout);
        votersDbRef = FirebaseDatabase.getInstance().getReference();
        votersDbRef = votersDbRef.child("user-data");
        votersDbRef.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot:snapshot.getChildren()){
                    if(userSnapshot.child("isVerified").getValue().toString().equals("false")) voterIds.add(userSnapshot.getKey());
                    //Toast.makeText(getApplicationContext(),userSnapshot.getKey(),Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(getApplicationContext(),voterIds.size(),Toast.LENGTH_LONG).show();
                if(voterIds.size() > 0)updateUser(voterIds.get(idx));
                else done();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
        vAccpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference curVoterRef = FirebaseDatabase.getInstance().getReference();
                curVoterRef = curVoterRef.child("user-data").child(voterIds.get(idx));
                curVoterRef.child("isVerified").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Updated"+String.valueOf(idx),Toast.LENGTH_LONG).show();
                        idx++;
                        if(idx < voterIds.size())updateUser(voterIds.get(idx));
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
        vDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference curVoterRef = FirebaseDatabase.getInstance().getReference();
                curVoterRef = curVoterRef.child("user-data").child(voterIds.get(idx));
                curVoterRef.child("message").setValue(message.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Discard Success : "+String.valueOf(voterIds.size()),Toast.LENGTH_LONG).show();
                        idx++;
                        if(idx < voterIds.size())updateUser(voterIds.get(idx));
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
        //Log.d("Voter Ids",String.valueOf(voterIds));
    }

    void updateUser(String uid){
        DatabaseReference voterRef = FirebaseDatabase.getInstance().getReference();
        voterRef = voterRef.child("user-data");
        voterRef = voterRef.child(uid);
        voterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firstName.setText(snapshot.child("fName").getValue().toString());
                lastName.setText(snapshot.child("lName").getValue().toString());
                aadharNum.setText(snapshot.child("aadharNum").getValue().toString());
                gender.setText(snapshot.child("gender").getValue().toString());
                dob.setText(snapshot.child("dob").getValue().toString());
                StorageReference aadharImgRef = FirebaseStorage.getInstance().getReference().child(uid).child("aadhar-file");
                final long ONE_MEGABYTE = 1024 * 1024;
                aadharImgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        vAadharImg.setImageBitmap(bmp);
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
//        Toast.makeText(getApplicationContext(),"HOLA",Toast.LENGTH_LONG).show();
        doneBtn.setVisibility(View.VISIBLE);
        upparLayout.setVisibility(View.GONE);
    }
}
