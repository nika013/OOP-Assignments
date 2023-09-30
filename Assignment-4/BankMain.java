public class BankMain {
    /*
	 Looks at commandline args and calls Bank.Bank processing.
	*/
    public static void main(String[] args) {
        Bank bank = new Bank();
        // deal with command-lines args
        if (args.length == 0) {
            System.out.println("Args: transaction-file [num-workers [limit]]");
            System.exit(1);
        }

        String file = args[0];

        int numWorkers = 1;
        if (args.length >= 2) {
            numWorkers = Integer.parseInt(args[1]);
        }

        // YOUR CODE HERE
        bank.processFile(bank, file, numWorkers);
    }
}
