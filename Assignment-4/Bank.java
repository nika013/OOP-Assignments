/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Bank {
	public static final int ACCOUNTS = 20;	 // number of accounts
	private BlockingQueue<Transaction> blockingQueue = new ArrayBlockingQueue<>(ACCOUNTS);
	private static ArrayList<Transaction> transactions = new ArrayList<>();
	private final Transaction nullTrans = new Transaction(-1,0,0);
	private Worker[] workers;
	private Account[] accounts;
	class Worker extends Thread {
		@Override
		public void run() {
			try {
				while (!this.currentThread().isInterrupted()) {
					Transaction transaction = blockingQueue.take();
					if (isPoison(transaction)) {
						interruptOthers(workers);
						break;
					}
					accounts[transaction.from].withdraw(transaction.amount);
					accounts[transaction.to].deposit(transaction.amount);
				}
			} catch (InterruptedException e) {
				this.currentThread().interrupt();
			}
		}

		private boolean isPoison(Transaction transaction) {
			return transaction.from == -1
					&& transaction.to == 0
					&& transaction.amount == 0;
		}

		private void interruptOthers(Worker[] workers) {
			for (Worker workerThread : workers) {
				if (workerThread != Thread.currentThread()) {
					workerThread.interrupt();
				}
			}
		}
	}
	
	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */
	public void readFile(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);
			
			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
				int from = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int to = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;
				
				// Use the from/to/amount
				
				// YOUR CODE HERE
				Transaction transaction = new Transaction(from, to, amount);
				blockingQueue.put(transaction);
			}
			blockingQueue.put(nullTrans);

		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	public void processFile(Bank bank, String file, int numWorkers) {
		initialize(bank, numWorkers);
		startWorkers(numWorkers);
		readFile(file);

		try {
			for (int i = 0; i < numWorkers; i++) {
				workers[i].join();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		printAccounts(accounts);
	}
	public Account[] getAccounts() {
		return accounts;
	}
	private void printAccounts(Account[] accounts) {
		for (Account account : accounts) {
			System.out.println(account.toString());
		}
	}
	private void startWorkers(int numWorkers) {
		for (int i = 0; i < numWorkers; i++) {
			workers[i].start();
		}
	}
	private void initialize(Bank bank, int numWorkers) {
		accounts = new Account[ACCOUNTS];
		for (int i = 0; i < ACCOUNTS; i++) {
			accounts[i] = new Account(bank, i, 1000);
		}
		workers = new Worker[numWorkers];
		for (int i = 0; i < numWorkers; i++) {
			workers[i] = new Worker();
		}
	}
}

