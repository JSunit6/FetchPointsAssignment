package Model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

//User class to store each users attributes.
public class User {

    private int userId;

    //Users total reward points
    private double rewardPoints;

    //Users points per payer name
    private Map<String, Double> rewardsPointMap;

    //All user transactions sorted by oldest transaction date first.
    private PriorityQueue<Transactions> transactionsQueue;

    public User(int userId) {
        this.userId = userId;
        this.rewardPoints = 0;
        this.rewardsPointMap = new HashMap<>();
        this.transactionsQueue = new PriorityQueue<>(new Comparator<Transactions>() {
            @Override
            public int compare(Transactions o1, Transactions o2) {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        });
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public double getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(double rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public Map<String, Double> getRewardsPointMap() {
        return rewardsPointMap;
    }

    public void setRewardsPointMap(Map<String, Double> rewardsPointMap) {
        this.rewardsPointMap = rewardsPointMap;
    }

    public PriorityQueue<Transactions> getTransactionsQueue() {
        return transactionsQueue;
    }

    public void setTransactionsQueue(PriorityQueue<Transactions> transactionsQueue) {
        this.transactionsQueue = transactionsQueue;
    }
}
