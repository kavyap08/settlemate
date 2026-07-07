package com.example.demo.ExpenseParticipant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Groupmembers.GroupMembers;

@Service
public class ExpenseParticipantService {

    @Autowired
    private ExpenseParticipantRepo expenseParticipantRepo;

    public void addParticipant(ExpenseParticipant participant){

        expenseParticipantRepo.save(participant);

    }
    public double getTotalShare(GroupMembers member){

        List<ExpenseParticipant> list =
                expenseParticipantRepo.findByMember(member);

        double total = 0;

        for(ExpenseParticipant p : list){

            if(!p.isSettled()){

                total += p.getShare();

            }

        }

        return total;

    }
    public double getShareByMember(GroupMembers member){

        List<ExpenseParticipant> participants =
                expenseParticipantRepo.findByMember(member);

        double total = 0;

        for(ExpenseParticipant participant : participants){

            if(!participant.isSettled()){

                total += participant.getShare();

            }

        }

        return total;

    }

}