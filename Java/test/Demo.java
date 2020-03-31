import io.violas.sdk.*;
import javafx.util.Pair;

public class Demo {
    public static void main(String[] argv) {
        System.out.println("Run Violas SDK Java Demo !");

        try {

            runViolasSdk();

        } catch (Exception e) {
            System.out.println("Demo has a exception, error : " + e.getMessage());
        }
    }

    static void runViolasSdk() {
        String scripts_path = "../../cppSdk/scripts/";

        Client client = new Client("18.220.66.235", // "125.39.5.57",
                (short) 40001, "../../cppSdk/testnet/mint_test.key", false, "", "mnenonic");

        client.test_validator_connection();

        Pair<Long, byte[]> account0 = client.createNextAccount();
        System.out.println("createNextAccount account 0");

        Pair<Long, byte[]> account1 = client.createNextAccount();
        System.out.println("createNextAccount account 1");

        for (Client.Account account : client.getAllAccounts()) {
            System.out.println("index=" + account.index);
        }
        System.out.println("getAllAccounts ...");

        client.mint((long) 0, (long) 100);

        double balance = client.getBalance(0);

        System.out.println("the account 0's balance = " + balance);

        client.mint(1, 50);

        balance = client.getBalance(account1.getValue());

        System.out.println("the account 0's balance = " + balance);

        client.transfer(0, account1.getValue(), 1 * Client.MICRO_LIBRA_COIN);

        balance = client.getBalance(account1.getValue());

        System.out.println("the account 1's balance = " + balance);

        long sequence = client.getSequenceNumber(0);

        // var (txn, event) = client.getCommittedTxnsByAccSeq(1.toLong(), sequence -
        // 1.toLong());
        Pair<String, String> txn_event = client.getCommittedTxnsByAccSeq(0, sequence - 1);
        System.out.println("the transaction is " + txn_event.getKey() + "\n" + "the event is " + txn_event.getValue());

        Pair<String, String>[] txnEvents = client.getCommitedTxnByRange(100, 100, true);
        for (Pair<String, String> x : txnEvents) {
            System.out.println("the transaction is " + x.getKey() + "\n" + "the event is " + x.getValue());
        }

        client.close();
        /*
         * client.compile( 0, scripts_path + "token.mvir", true, "");
         * 
         * client.publishModule(0, scripts_path + "token.mv");
         * 
         * client.compile( 0, scripts_path + "publish.mvir", false, "");
         * 
         * client.executeScript(0, scripts_path+"publish.mv", new String[0]);
         * 
         * client.executeScript(1, scripts_path+"publish.mv", new String[0]); 
         *           
         * 
         * // // test Token class // Token token = new Token(client,
         * account1.getValue(), "Token1", scripts_path, "");
         * 
         * token.deploy(1);
         * 
         * token.publish(1);
         * 
         * token.publish(0);
         * 
         * token.mint(1, account0.getValue(), 1000);
         * 
         * long token_balance = token.getBalance(0);
         * 
         * System.out.println("the balance of Stable Coin of account 0  = " +
         * token_balance);
         */
    }
}
