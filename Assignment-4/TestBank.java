import junit.framework.TestCase;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestBank extends TestCase {
    public void testBankSimple() {
        Bank bank = new Bank();
        bank.processFile(bank, "small.txt", 1);
        Account[] accounts = bank.getAccounts();
        assertTrue(accounts[0].toString().equals("acct:0 bal:999 trans:1"));
        assertTrue(accounts[1].toString().equals("acct:1 bal:1001 trans:1"));
        assertTrue(accounts[2].toString().equals("acct:2 bal:999 trans:1"));
        assertTrue(accounts[3].toString().equals("acct:3 bal:1001 trans:1"));
        assertTrue(accounts[4].toString().equals("acct:4 bal:999 trans:1"));
        assertTrue(accounts[5].toString().equals("acct:5 bal:1001 trans:1"));
        assertTrue(accounts[6].toString().equals("acct:6 bal:999 trans:1"));
        assertTrue(accounts[7].toString().equals("acct:7 bal:1001 trans:1"));
        assertTrue(accounts[8].toString().equals("acct:8 bal:999 trans:1"));
        assertTrue(accounts[9].toString().equals("acct:9 bal:1001 trans:1"));
    }

    public void testMultiple() {
        Bank bank = new Bank();
        bank.processFile(bank, "5k.txt", 8);
        Account[] accounts = bank.getAccounts();
        assertTrue(accounts[0].toString().equals("acct:0 bal:1000 trans:518"));
        assertTrue(accounts[1].toString().equals("acct:1 bal:1000 trans:444"));
        assertTrue(accounts[2].toString().equals("acct:2 bal:1000 trans:522"));
        assertTrue(accounts[3].toString().equals("acct:3 bal:1000 trans:492"));
        assertTrue(accounts[4].toString().equals("acct:4 bal:1000 trans:526"));

    }

    public void testCatches() {
        assertThrows(RuntimeException.class, () -> {
            Bank bank = new Bank();
            bank.processFile(bank, "bla.py", 0);
        });

    }
}
