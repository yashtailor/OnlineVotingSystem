package com.example.onlinevotingsystem;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModuleTestStaticClasses {

    @Test
    public void testElection(){
        String electionName = "Election 1";
        String startDate = "04/04/2021";
        String endDate = "05/04/2021";
        String results = "";
        String isDone = "false";
        Election election = new Election(electionName,startDate,endDate,results,isDone);
        assertEquals(electionName,election.getElectionName());
        assertEquals(startDate,election.getStartDate());
        assertEquals(endDate,election.getEndDate());
        assertEquals(results,election.getResults());
        assertEquals(isDone,election.getIsDone());
    }

    @Test
    public void testVoter(){
        String fName = "Yash";
        String lName = "Tailor";
        String gender = "Male";
        String dob = "24/12/2000";
        String aadharNum = "222244445555";
        String hasSubmitted = "false";
        String isVerified = "true";
        String message = "";
        String userId = "";
        User voter = new User(fName,lName,gender,dob,aadharNum,hasSubmitted,isVerified,message,userId);
        assertEquals(fName,voter.getfName());
        assertEquals(lName,voter.getlName());
        assertEquals(gender,voter.getGender());
        assertEquals(aadharNum,voter.getAadharNum());
        assertEquals(hasSubmitted,voter.getHasSubmitted());
        assertEquals(isVerified,voter.getIsVerified());
        assertEquals(message,voter.getMessage());
        assertEquals(userId,voter.getUserId());
    }

    @Test
    public void testCandidate(){
        String fName = "Narendra";
        String lName = "Modi";
        String pName = "BJP";
        String gender = "Male";
        String dob = "17/04/1965";
        String aadharNum = "222244445555";
        String hasSubmitted = "false";
        String isVerified = "true";
        String message = "";
        String userId = "";
        Candidate candidate = new Candidate(fName,lName,gender,pName,dob,aadharNum,hasSubmitted,isVerified,message,userId);
        assertEquals(fName,candidate.getfName());
        assertEquals(lName,candidate.getlName());
        assertEquals(pName,candidate.getpName());
        assertEquals(gender,candidate.getGender());
        assertEquals(aadharNum,candidate.getAadharNum());
        assertEquals(hasSubmitted,candidate.getHasSubmitted());
        assertEquals(isVerified,candidate.getIsVerified());
        assertEquals(message,candidate.getMessage());
        assertEquals(userId,candidate.getUserId());
    }
}
