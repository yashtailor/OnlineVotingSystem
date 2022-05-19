package com.example.onlinevotingsystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.content.Intent.ACTION_GET_CONTENT;
import static android.content.Intent.createChooser;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private String uid;
    private Button submitBtn;
    private EditText firstName,lastName,aadharNumber;
    private RadioButton genderMale,genderFemale,genderOthers;
    private RadioGroup genderGrp;
    private DatePicker datePicker;
    private Button chooseFileBtn;
    private Button goBackToInfoPage;
    private static final int REQUEST_CODE_FILES = 1;
    private Uri file;
    private DatabaseReference dbRef;
    private StorageReference storageRef;
    private  FirebaseUser user;
    private UploadTask uploadTask;
    private int count = 0;
    private LinearLayout cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference();
        if(user != null){
            uid = user.getUid();
        }
        setContentView(R.layout.activity_main);
        submitBtn = findViewById(R.id.submitBtn);
        goBackToInfoPage = findViewById(R.id.goBackToInfoPage);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        aadharNumber = findViewById(R.id.aadharNumber);
        genderMale = findViewById(R.id.male);
        genderFemale = findViewById(R.id.female);
        genderOthers = findViewById(R.id.others);
        datePicker = findViewById(R.id.datePicker);
        chooseFileBtn = findViewById(R.id.chooseFileBtn);
        genderGrp = findViewById(R.id.genderGrp);
        cardView = findViewById(R.id.uploadVoter);
        try{
            DatabaseReference subDbRef = dbRef.child("user-data").child(uid);
            //Toast.makeText(getApplicationContext(),subDbRef.toString(),Toast.LENGTH_LONG).show();
            subDbRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Toast.makeText(getApplicationContext(),snapshot.child("hasSubmitted").getValue().toString(),Toast.LENGTH_LONG).show();
                    if(snapshot.hasChild("hasSubmitted")){
                        if(snapshot.child("hasSubmitted").getValue().toString().equals("true")){
                            String fName = snapshot.child("fName").getValue().toString();
                            String lName = snapshot.child("lName").getValue().toString();
                            String dob = snapshot.child("dob").getValue().toString();
                            String aadharNum = snapshot.child("aadharNum").getValue().toString();
                            String gender = snapshot.child("gender").getValue().toString();

                            firstName.setText(fName);
                            lastName.setText(lName);
                            genderGrp.clearCheck();
                            if(gender.equals("male"))genderMale.setChecked(true);
                            else if(gender.equals("female"))genderFemale.setChecked(true);
                            else genderOthers.setChecked(true);
                            aadharNumber.setText(aadharNum);
                            datePicker.updateDate(2000,11,24);
                            goBackToInfoPage.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        chooseFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(ACTION_GET_CONTENT);
                startActivityForResult(createChooser(intent, "Select File"), REQUEST_CODE_FILES);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String aadharNum = aadharNumber.getText().toString();
                String gender = null;
                if(genderMale.isChecked())gender = "male";
                else if(genderFemale.isChecked())gender = "female";
                else gender ="others";
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                String dob = String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);

                if(fName.trim().equals("")){
                    Toast.makeText(getApplicationContext(),"First Name Cannot Be Empty",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(lName.trim().equals("")){
                    Toast.makeText(getApplicationContext(),"Last Name Cannot Be Empty",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(fName.trim().contains("12")){
                    Toast.makeText(getApplicationContext(),"First Name cannot have a number",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(lName.trim().contains("12")){
                    Toast.makeText(getApplicationContext(),"Last Name cannot have a number",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(fName.trim().contains("\"")){
                    Toast.makeText(getApplicationContext(),"First Name cannot have \"",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(lName.trim().contains("\"")){
                    Toast.makeText(getApplicationContext(),"Last Name cannot have \"",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(aadharNum.trim().length() != 12){
                    Toast.makeText(getApplicationContext(),"Aadhar Number must be a 12 digit number",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    User curUser = new User(fName,lName,gender,dob,aadharNum,"true","false","",uid);
                    dbRef.child("user-data").child(uid).setValue(curUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Submitted Sucessfully! Under Review!",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),InfoActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        });
        // initialising all views through id defined above

        goBackToInfoPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),InfoActivity.class));
            }
        });
    }

    public void putFileInStorage(final Uri file) {
        String uid = user.getUid();
        final StorageReference upload = storageRef.child(uid).child("aadhar-file");
        uploadTask = (UploadTask) upload.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "Aadhar Image Upload Successfully!", Toast.LENGTH_SHORT).show();
                cardView.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                cardView.setVisibility(View.GONE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FILES && resultCode == RESULT_OK && data != null && data.getData() != null) {
            file = data.getData();
//            if(count==0){
//                Toast.makeText(getApplicationContext(), "PDF Type Not supported", Toast.LENGTH_SHORT).show();
//                count += 1;
//                return;
//            }
//            if(count==1){
//                Toast.makeText(getApplicationContext(), "Doc Type Not supported", Toast.LENGTH_SHORT).show();
//                count += 1;
//                return;
//            }
            cardView.setVisibility(View.VISIBLE);
            putFileInStorage(file);
        }
    }
}