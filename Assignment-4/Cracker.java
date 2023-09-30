// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	private String hashValue;
	private CountDownLatch latch;
	private int length;
	private int numWorkers;
	private Worker[] workers;
	private ArrayList<String> passwords;

	public Cracker(String hashValue, int length, int numWorkers) {
		this.numWorkers = numWorkers;
		this.hashValue = hashValue;
		this.length = length;
		this.latch = new CountDownLatch(numWorkers);
		passwords = new ArrayList<>();
		workers = new Worker[numWorkers];
		int strIdx = 0;
		int offset = CHARS.length / numWorkers;
		for (int i = 0; i < numWorkers; i++) {
			workers[i] = new Worker(strIdx, strIdx + offset -1);
			if (i == numWorkers - 1
					&& CHARS.length % numWorkers != 0) {
				workers[i].setEndIdx(CHARS.length - 1);
			}
			workers[i].start();
			strIdx += offset;
		}
	}

	public void await() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public ArrayList<String> getPasswords() {
		return passwords;
	}

	class Worker extends Thread {
		private int strIdx;
		private int endIdx;

		public Worker(int strIdx, int endIdx) {
			this.strIdx = strIdx;
			this.endIdx = endIdx;
		}

		public void setEndIdx(int endIdx) {
			this.endIdx = endIdx;
		}

		@Override
		public void run() {
			for (int i = strIdx; i < endIdx; i++) {
				String curPassword = "";
				curPassword += CHARS[i];
				generate(curPassword);
			}
			latch.countDown();
		}
		private void generate(String curPassword) {
			if (curPassword.length() == length
					&& generateHash(curPassword).equals(hashValue)) {
				passwords.add(curPassword);
				System.out.println(curPassword);
			} else if (curPassword.length() < length) {
				for (int i = 0; i < CHARS.length; i++) {
					generate(curPassword + CHARS[i]);
				}
			}
		}

		private String generateHash(String password) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(password.getBytes());
				return hexToString(md.digest());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	private static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}

	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
//	private static byte[] hexToArray(String hex) {
//		byte[] result = new byte[hex.length()/2];
//		for (int i=0; i<hex.length(); i+=2) {
//			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
//		}
//		return result;
//	}

	public static String generationMode(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			byte[] bytes = password.getBytes();
			return Cracker.hexToString(md.digest(bytes));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			String password = args[0];
			System.out.println(Cracker.generationMode(password));
		} else {
			String hashValue = args[0];
			int length = Integer.parseInt(args[1]);
			int numWorkers = Integer.parseInt(args[2]);
			Cracker cracker = new Cracker(hashValue, length, numWorkers);
			cracker.await();
			System.out.println("all done");
		}
	}
}


