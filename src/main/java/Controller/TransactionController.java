package Controller;

import Exceptions.PointsDeductionException;
import Exceptions.TransactionCannotBeAddedException;
import Exceptions.UserNotFoundException;
import Model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import Model.Transactions;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1.0")
public class TransactionController {

    private static int autoIncrement = 0;
    private static final Map<Integer, User> usersMap = new HashMap<>();

    //Get all transactions of all users
    //endpoint: https://localhos:8080/api/v1.0/transactions
    //Returns: A Map<User ID, Priority Queue<User Transaction>>
    @GetMapping("/transactions")
    public Map<Integer, PriorityQueue<Transactions>> getAllUserTransactions() {
        Map<Integer, PriorityQueue<Transactions>> allUserTransactionMap = new HashMap<>();

        for(Map.Entry<Integer, User> userEntry : usersMap.entrySet()) {
            allUserTransactionMap.put(userEntry.getKey(), userEntry.getValue().getTransactionsQueue());
        }
        return allUserTransactionMap;
    }

    //Get transaction by userId
    //endpoint: https://localhos:8080/api/v1.0/transactions/{id}
    //Returns: Priority Queue<User Transactions> for user with id {id}
    @GetMapping("/transactions/{id}")
    public ResponseEntity<PriorityQueue<Transactions>> getTransactionByUserId(@Validated @PathVariable(name = "id") int id) throws UserNotFoundException {

        if(!usersMap.containsKey(id)) {
            throw new UserNotFoundException("User id not present.");
        }

        PriorityQueue<Transactions> transactionListPerUser = usersMap.get(id).getTransactionsQueue();
        return ResponseEntity.ok().body(transactionListPerUser);
    }

    // add user points for each transaction
    //endpoint: https://localhos:8080/api/v1.0/addpoints/{id}
    /*
        Conditions to check while adding:
        1. If points to add are positive:
            a. Add the transaction to user's transaction queue.
        2. If points to add are negative:
            a. If the transaction payer name exists in user's transaction queue:
                1. If queue transaction.points + current transaction points > 0:
                    a. Update queue transaction points.
                2. If queue transaction.points + current transaction points = 0:
                    a. Remove the transaction from user's queue.
                3. If queue transaction.points + current transaction points < 0:
                    a. Error: Cannot add transaction since points negative.
            b. First transaction for payer name cannot be negative, throw exception.
        3. Update user's total reward points.
        4. Update user's rewards map<Payer Name, Payer Points>.
     */
    //Return 200 OK with the details of added transaction
    @PostMapping("/addpoints/{id}")
    public ResponseEntity<Transactions> addPoints(@Validated @PathVariable int id,
            @Validated @RequestBody Transactions transaction) throws TransactionCannotBeAddedException {

        User user = null;

        // Create a new user if not present and store it in a map.
        if(!usersMap.containsKey(id)) {
            user = new User(id);
            usersMap.put(user.getUserId(), user);
        }
        else {
            user = usersMap.get(id);
        }

        // Increment TransactionID counter.
        ++autoIncrement;
        transaction.setTransactionId(autoIncrement);
        // Set Current time stamp.
        transaction.setTimestamp(LocalDateTime.now());

        // get reward points map of the user.
        Map<String, Double> map = user.getRewardsPointMap();

        // get transaction queue of the user.
        PriorityQueue<Transactions> queue = user.getTransactionsQueue();

        String currentPayerName = transaction.getPayer();
        double currentTransactionPoints = transaction.getPoints();

        if(currentTransactionPoints > 0) {
            queue.offer(transaction);
        }
        else if (currentTransactionPoints < 0 ) {
            if (map.containsKey(currentPayerName)) {
                double payerPtsBalance = map.get(currentPayerName) + currentTransactionPoints;

                /*Get the transaction from user's transaction queue
                 where queue transaction payer name equals current transaction payer name */
                Transactions transactionFromQueue
                        = queue.stream()
                        .filter(transactions -> transactions.getPayer().equals(transaction.getPayer()))
                        .findFirst()
                        .orElse(null);

                if(payerPtsBalance > 0) {
                    transactionFromQueue.setPoints(payerPtsBalance);
                }
                else if(payerPtsBalance == 0) {
                    queue.remove(transactionFromQueue);
                }
                else {
                    throw new TransactionCannotBeAddedException("Invalid transaction");
                }
            }
            else {
                throw new TransactionCannotBeAddedException("Invalid transaction.");
            }
        }

        //Update user's total points.
        user.setRewardPoints(user.getRewardPoints() + currentTransactionPoints);

        //Update user's rewards points map<Payer Name, Payer Points>
        map.put(transaction.getPayer(), (map.getOrDefault(currentPayerName, 0d) + currentTransactionPoints));

        return ResponseEntity.ok().body(transaction);
    }

    // Deduct points from payers of a user
    //endpoint: https://localhos:8080/api/v1.0/deduct/{userid}/{points to deduct}
    /*
        Conditions to check:
        1. If user id is present:
            a. User,s total reward point is less than deduct points:
                1. Throw Points deduction exception; cannot deduct points.
            b. User,s total reward point is greater than or equal to deduct points:
                1. Continue till deduct points > 0:
                    a. Get the transaction from user's transaction queue:
                        1. Deduct appropriate points from each eligible payer.
                        2. Update user's total points.
                        3. Update user's rewards map<Payer Name, Payer points>
                        4. Add eligible deduction to list.
        2. Throw user not found exception.
     */
    //Returns: List<StringBuilder> of all the eligible payer names and value that can be deducted.
    @GetMapping("/deduct/{id}/{deduct}")
    public ResponseEntity<List<StringBuilder>> deduct(@Validated @PathVariable int id,
            @Validated @PathVariable double deduct) throws TransactionCannotBeAddedException, PointsDeductionException, UserNotFoundException {

        if(usersMap.containsKey(id)) {

            User currentUser = usersMap.get(id);

            if(currentUser.getRewardPoints() < deduct) {
                throw new PointsDeductionException("Points balance insufficient.");
            }

            else {
                List<StringBuilder> deductList = new ArrayList<>();
                PriorityQueue<Transactions> transactionQ = currentUser.getTransactionsQueue();
                Map<String, Double> userRewardPoints = currentUser.getRewardsPointMap();

                while(deduct > 0 && !transactionQ.isEmpty()) {
                    Transactions transaction = transactionQ.poll();
                    double currentPoints = transaction.getPoints();
                    double val;

                    if(currentPoints <= deduct) {
                        val = deduct - (deduct - currentPoints);
                    }
                    else {
                        val = currentPoints - (currentPoints - deduct);
                    }

                    deductList.add(new StringBuilder(transaction.getPayer()).append(" -"+(val)+" "+("Now")));
                    currentUser.setRewardPoints(currentUser.getRewardPoints() - val);
                    userRewardPoints.put(transaction.getPayer(),
                                        userRewardPoints.getOrDefault(transaction.getPayer(),0d) - val);
                    System.out.println("List:"+deductList);
                    deduct -= val;
                    System.out.println("\tDeduct Val"+deduct);
                    System.out.println("TOTAL USER Points:"+currentUser.getRewardPoints());
                }
                return ResponseEntity.ok().body(deductList);
            }
        }
        else {
         throw new UserNotFoundException("User id not present.");
        }
    }

    // Show user's all payer names and their points
    //endpoint: https://localhos:8080/api/v1.0/deduct/{userid}/{points to deduct}
    /*
        Conditions to check:
        1. If user present:
            a. Show user's reward points map<Payer name, Payer points>.
        2. Throw user not found exception.
     */
    //Returns: A List<StringBuilder> of user's current points per payer name.
    @GetMapping("show/{id}")
    public ResponseEntity<List<StringBuilder>> showTransactionPerUser(@Validated @PathVariable int id) throws UserNotFoundException {

        List<StringBuilder> rewardPointsList;

        if(usersMap.containsKey(id)) {
            rewardPointsList  = new ArrayList<>();
            User user = usersMap.get(id);

            for(Map.Entry<String, Double> payerRewards: user.getRewardsPointMap().entrySet()) {
                rewardPointsList.add(new StringBuilder(payerRewards.getKey()+", "+payerRewards.getValue()+" points"));
            }
        }
        else {
            throw new UserNotFoundException("User id not present.");
        }
        return ResponseEntity.ok().body(rewardPointsList);
    }

}
