package LoginSystem.src.main.java.org.example.Login;

import java.util.HashMap;

public class AccountManager {
    private HashMap<String, String> accounts;
    public AccountManager() {
        accounts = new HashMap<>();
        accounts.put("Patrick", "1234");
        accounts.put("Molly", "FloPup");
    }

    public void addAccount(String login, String password) {
        accounts.put(login, password);
    }

    public boolean isAdded(String login) {
        return accounts.containsKey(login);
    }

    public boolean isCorrect(String userName, String password) {
        String oldPassword = accounts.get(userName);
        if (oldPassword == null) {
            return false;
        }
        return password.equals(oldPassword);
    }
}
