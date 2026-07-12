package com.example.demo.Settlement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Expense.ExpenseService;
import com.example.demo.ExpenseParticipant.ExpenseParticipantService;
import com.example.demo.Groupmembers.GroupMembers;
import com.example.demo.Groupmembers.GroupMembersService;
import com.example.demo.Groups.Groups;

@Service
public class SettlementService {

    @Autowired
    private SettlementRepo settlementRepo;
    
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseParticipantService expenseParticipantService;
    
    @Autowired
    private GroupMembersService groupmembersService;

   
    public List<Settlement> getHistory(Groups group){

        return settlementRepo.findByGroupOrderBySettledOnDesc(group);

    }
    
    public Map<GroupMembers, Double> getMemberBalances(Groups group) {

        List<GroupMembers> members =
                groupmembersService.getMembersByGroup(group);

        Map<GroupMembers, Double> balances =
                new LinkedHashMap<>();

        List<Settlement> history =
                settlementRepo.findByGroupOrderBySettledOnDesc(group);

        for (GroupMembers member : members) {

            double paid =
                    expenseService.getPaidByMember(
                            member.getEmail(),
                            group.getId());

            double share =
                    expenseParticipantService.getShareByMember(member);

            double balance = paid - share;

            // ⭐ THIS WAS MISSING
            for (Settlement settlement : history) {

                if (settlement.getPayer().getId() == member.getId()) {

                    balance += settlement.getAmount();

                }

                if (settlement.getReceiver().getId() == member.getId()) {

                    balance -= settlement.getAmount();

                }

            }

            balances.put(member, balance);

        }

        return balances;
    }
    
    
    public List<SettlementResult> generateSettlements(Groups group) {

        Map<GroupMembers, Double> balances = getMemberBalances(group);

        List<SettlementResult> settlements = new ArrayList<>();

        while (true) {

            GroupMembers creditor = null;
            GroupMembers debtor = null;

            double maxCredit = 0;
            double maxDebt = 0;

            // Find the member who should receive the most
            for (Map.Entry<GroupMembers, Double> entry : balances.entrySet()) {

                if (entry.getValue() > maxCredit) {

                    maxCredit = entry.getValue();
                    creditor = entry.getKey();

                }

                if (entry.getValue() < maxDebt) {

                    maxDebt = entry.getValue();
                    debtor = entry.getKey();

                }

            }

            // No more settlements needed
            if (creditor == null || debtor == null) {

                break;

            }

            double amount = Math.min(maxCredit, Math.abs(maxDebt));

            SettlementResult result = new SettlementResult();

            result.setPayer(debtor);

            result.setReceiver(creditor);

            result.setAmount(amount);

            settlements.add(result);

            // Update balances
            balances.put(
                    creditor,
                    balances.get(creditor) - amount
            );

            balances.put(
                    debtor,
                    balances.get(debtor) + amount
            );

        }

        return settlements;
    }
    
    public void settleAmount(Groups group,
            GroupMembers payer,
            GroupMembers receiver,
            double amount){

Settlement settlement = new Settlement();

settlement.setGroup(group);

settlement.setPayer(payer);

settlement.setReceiver(receiver);

settlement.setAmount(amount);

settlement.setSettledOn(LocalDateTime.now());

settlementRepo.save(settlement);

}
    
    public List<Settlement> getSettlementHistory(Groups group){

        return settlementRepo.findByGroupOrderBySettledOnDesc(group);

    }

}