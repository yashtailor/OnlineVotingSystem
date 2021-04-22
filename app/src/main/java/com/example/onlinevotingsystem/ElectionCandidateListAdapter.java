package com.example.onlinevotingsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ElectionCandidateListAdapter extends RecyclerView.Adapter<ElectionCandidateListAdapter.ElectionCandidateListViewHolder> {
    private String[] candidateNames;
    public ElectionCandidateListAdapter(String[] candidateNames){
        this.candidateNames = candidateNames;
//        this.candidateParties = candidateParty;
    }
    @NonNull
    @Override
    public ElectionCandidateListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_candidate_layout,parent,false);
        return new ElectionCandidateListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ElectionCandidateListViewHolder holder, int position) {
        String candidateName = candidateNames[position];
//        String candidateParty = candidateParties[position];
        holder.candidateName.setText(candidateName);
//        holder.candidateParty.setText(candidateParty);
    }

    @Override
    public int getItemCount() {
        return candidateNames.length;
    }

    public class ElectionCandidateListViewHolder extends RecyclerView.ViewHolder{
        TextView candidateName,candidateParty;
        RadioButton rdCandidateBtn;
        public ElectionCandidateListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
