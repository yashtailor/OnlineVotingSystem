package com.example.onlinevotingsystem;

public class Election {
    String electionName,startDate,endDate,results,isDone;

    public Election(String electionName, String startDate, String endDate,String results,String isDone) {
        this.electionName = electionName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.results = results;
        this.isDone = isDone;
    }

    public String getElectionName() {
        return electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }
}
