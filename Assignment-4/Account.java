import java.util.concurrent.locks.Condition;

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
	private int id;
	private int balance;
	private int transactions;
	private Condition condition;

	// It may work out to be handy for the account to
	// have a pointer to its Bank.Bank.
	// (a suggestion, not a requirement)
	private Bank bank;
	
	public Account(Bank bank, int id, int balance) {
		this.bank = bank;
		this.id = id;
		this.balance = balance;
		transactions = 0;
	}

	public synchronized void withdraw(int amount) {
		balance -= amount;
		transactions++;
	}

	public synchronized void deposit(int amount) {
		balance += amount;
		transactions++;
	}

	public int getBalance() {
		return balance;
	}

	@Override
	public String toString() {
		return "acct:" + id + " bal:" + balance + " trans:" + transactions;
	}
}
