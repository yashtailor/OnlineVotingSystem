package com.example.onlinevotingsystem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

        public class ResultsActivity extends AppCompatActivity {
    private LinearLayout showAllDoneElections;
    private DatabaseReference electionDbRef,electionCandidatesRef;
    private FirebaseUser curUser;
    private ArrayList<String> candidates = new ArrayList<>();
    private Button goBackToInfoPageResults;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));
        //Toast.makeText(getApplicationContext(),LoginActivity.curUserType,Toast.LENGTH_LONG).show();
        goBackToInfoPageResults = findViewById(R.id.backToInfoPageFromResults);
        goBackToInfoPageResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginActivity.curUserType == "ec"){
                    startActivity(new Intent(getApplicationContext(),ECInfoActivity.class));
                }else if(LoginActivity.curUserType == "eca"){
                    startActivity(new Intent(getApplicationContext(),ECAMainActivity.class));
                }else if(LoginActivity.curUserType == "voter"){
                    startActivity(new Intent(getApplicationContext(),InfoActivity.class));
                }
            }
        });
        showAllDoneElections = findViewById(R.id.allDoneElections);
        electionDbRef = FirebaseDatabase.getInstance().getReference();
        electionCandidatesRef = electionDbRef.child("election-candidates");
        electionCandidatesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot candidateSnapshot : snapshot.getChildren()){
                    candidates.add(candidateSnapshot.child("pName").getValue().toString());
                }
                //Toast.makeText(getApplicationContext(),candidates.toString(),Toast.LENGTH_LONG).show();
                DatabaseReference electionDbRef2 = electionDbRef.child("elections").getRef();
                //Toast.makeText(getApplicationContext(),electionDbRef.toString(),Toast.LENGTH_LONG).show();
                //curUser = FirebaseAuth.getInstance().getCurrentUser();
                electionDbRef2.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Toast.makeText(getApplicationContext(),snapshot.toString(),Toast.LENGTH_LONG).show();
                        Integer count = 0;
                        for (DataSnapshot electionSnapshot : snapshot.getChildren()) {
                            //Toast.makeText(getApplicationContext(),electionSnapshot.toString(),Toast.LENGTH_LONG).show();
                            if(electionSnapshot.child("isDone").getValue().toString().equals("false")){
                                //don't add
                            }else{
                                CardView newCard = new CardView(getApplicationContext());
                                PieChart pieChart = new PieChart(getApplicationContext());
                                pieChart.setId(View.generateViewId());
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
                                pieChart.setLayoutParams(layoutParams);
                                pieChart.getDescription().setEnabled(false);
                                pieChart.setRotationEnabled(true);
                                pieChart.setDragDecelerationFrictionCoef(0.9f);
                                pieChart.setRotationAngle(0);
                                pieChart.setHighlightPerTapEnabled(true);
                                //adding animation so the entries pop up from 0 degree
                                pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                                //setting the color of the hole in the middle, default white
                                pieChart.setHoleColor(Color.parseColor("#0f0f0f"));
                                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                                String label = "Candidates";
                                Map<String, Integer> typeAmountMap = new HashMap<>();
                                LinearLayout newLinearLayout = new LinearLayout(getApplicationContext());
                                newLinearLayout.setOrientation(LinearLayout.VERTICAL);
                                newCard.setCardElevation(15);
                                newCard.setContentPadding(15,15,15,15);
                                newCard.setRadius(10);
                                newCard.setPreventCornerOverlap(true);
                                newCard.setUseCompatPadding(true);
                                newCard.setMaxCardElevation(20);
                                TextView electionNameTxt = new TextView(getApplicationContext());
                                electionNameTxt.setGravity(Gravity.CENTER_HORIZONTAL);
                                String electionName = electionSnapshot.child("electionName").getValue().toString();
                                electionNameTxt.setText(electionName);
                                electionNameTxt.setTextSize(30);
                                newLinearLayout.addView(electionNameTxt);
                                TableLayout tableLayout = new TableLayout(getApplicationContext());
                                tableLayout.setPadding(10,0,10,0);
                                TableLayout.LayoutParams layoutParamsTab = new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT
                                );
                                layoutParamsTab.setMargins(10,10,10,10);
                                tableLayout.setLayoutParams(layoutParamsTab);
                                tableLayout.setStretchAllColumns(true);
                                TableRow.LayoutParams linearLay = new TableRow.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        1.0f
                                );
                                TableRow tableRowHeader = new TableRow(getApplicationContext());
                                tableRowHeader.setPadding(5,5,5,5);
                                tableRowHeader.setBackgroundColor(Color.parseColor("#0079D6"));
                                tableRowHeader.setLayoutParams(linearLay);
                                TextView candidateNameViewHeader = new TextView(getApplicationContext());
                                candidateNameViewHeader.setText("Party Name");
                                candidateNameViewHeader.setGravity(Gravity.CENTER);
                                candidateNameViewHeader.setTextSize(15);
                                candidateNameViewHeader.setTypeface(null, Typeface.BOLD);
                                TextView candidateVotesViewHeader = new TextView(getApplicationContext());
                                candidateVotesViewHeader.setText("Votes");
                                candidateVotesViewHeader.setGravity(Gravity.CENTER);
                                candidateVotesViewHeader.setTextSize(15);
                                candidateVotesViewHeader.setTypeface(null,Typeface.BOLD);
                                tableRowHeader.addView(candidateNameViewHeader);
                                tableRowHeader.addView(candidateVotesViewHeader);
                                tableLayout.addView(tableRowHeader);
                                //Toast.makeText(getApplicationContext(),candidates.size(),Toast.LENGTH_SHORT).show();
                                for(int i=0;i<candidates.size();i++){
                                    TableRow tableRow = new TableRow(getApplicationContext());
                                    tableRow.setPadding(5,5,5,5);
                                    tableRow.setBackgroundColor(Color.parseColor("#DAE8FC"));
                                    tableRow.setLayoutParams(linearLay);
                                    TextView candidateNameTxtView = new TextView(getApplicationContext());
                                    candidateNameTxtView.setText(candidates.get(i));
                                    candidateNameTxtView.setGravity(Gravity.CENTER);
//                                    candidateNameTxtView.setLayoutParams(linearLay);
                                    //Toast.makeText(getApplicationContext(),candidates.get(i),Toast.LENGTH_SHORT).show();
//                                    TextView candidateTxtView = new TextView(getApplicationContext());
                                    String votes;
                                    if(electionSnapshot.child("candidate-results").hasChild(candidates.get(i))){
                                        votes = electionSnapshot.child("candidate-results").child(candidates.get(i)).getValue().toString();
                                        votes = String.valueOf(Integer.parseInt(votes));
                                    }else{
                                        votes = "0";
                                    }
//                                    candidateTxtView.setText(candidates.get(i) + " -"+votes);
                                    TextView candidateVotesTxtView = new TextView(getApplicationContext());
                                    candidateVotesTxtView.setText(votes);
                                    candidateVotesTxtView.setGravity(Gravity.CENTER);
//                                    candidateVotesTxtView.setLayoutParams(linearLay);
                                    tableRow.addView(candidateNameTxtView);
                                    tableRow.addView(candidateVotesTxtView);
                                    tableLayout.addView(tableRow);
                                    if(!votes.contentEquals("0")) {
                                        typeAmountMap.put(candidates.get(i), Integer.parseInt(votes));
                                        //collecting the entries with label name
                                        PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
                                        //setting text size of the value
                                        pieDataSet.setValueTextSize(12f);
                                        //providing color list for coloring different entries
                                        pieDataSet.setColors(colors);
                                        //grouping the data set from entry to chart
                                        PieData pieData = new PieData(pieDataSet);
                                        //showing the value of the entries, default true if not set
                                        pieData.setDrawValues(true);
                                        pieChart.setData(pieData);
                                        pieChart.invalidate();
                                    }
//                                    candidateTxtView.setTextSize(20);
//                                    newLinearLayout.addView(candidateTxtView);
                                }
                                for(String type: typeAmountMap.keySet()){
                                    pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
                                }
                                newLinearLayout.addView(tableLayout);
                                newLinearLayout.addView(pieChart);
                                newLinearLayout.setPadding(5,5,5,5);
                                newCard.addView(newLinearLayout);
                                newLinearLayout.setBackgroundColor(Color.parseColor("#ffc979"));
                                showAllDoneElections.addView(newCard);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
