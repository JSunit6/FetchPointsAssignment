package Model;

import java.time.LocalDateTime;

public class Transactions {

    private int transactionId;

    private String payer;

    private double points;

    private LocalDateTime timestamp;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Transactions() {

    }

    public Transactions(int id, String payer, int points, LocalDateTime timestamp) {
        this.transactionId = id;
        this.payer = payer;
        this.points = points;
        this.timestamp = timestamp;
    }

}
