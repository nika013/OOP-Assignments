package LoginSystem.src.test.java;

import LoginSystem.src.main.java.org.example.Login.AccountManager;
import junit.framework.TestCase;

public class AccountManagerTest extends TestCase {
    public void testBasic() {
        AccountManager accountManager = new AccountManager();
        assertTrue(accountManager.isAdded("Molly"));
        assertTrue(accountManager.isCorrect("Molly", "FloPup"));
        assertTrue(accountManager.isAdded("Patrick"));
        assertTrue(accountManager.isCorrect("Patrick", "1234"));
    }

    public void testAdd() {
        AccountManager accountManager = new AccountManager();
        accountManager.addAccount("Nick", "1234");
        assertTrue(accountManager.isAdded("Nick"));
        assertTrue(accountManager.isCorrect("Nick", "1234"));
        assertFalse(accountManager.isCorrect("Nick", "1234567"));
        accountManager.addAccount("Nick", "1234567");
        assertTrue(accountManager.isCorrect("Nick", "1234567"));
    }

}
