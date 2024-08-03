package transaction;

public class Main {
    public static void main(String[] args) {
        TransactionAuthorizer authorizer = new TransactionAuthorizer();
        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setAccountId("123");
        transaction.setAmount(50.0);
        transaction.setMerchant("UBER EATS                   SAO PAULO BR");
        transaction.setMcc("5811");

        String result = authorizer.authorizeTransaction(transaction);
        System.out.println(result); // {"code": "00"}
    }
}
