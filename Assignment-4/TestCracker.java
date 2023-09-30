import junit.framework.TestCase;

import java.util.ArrayList;

public class TestCracker extends TestCase {
    public void testGeneration() {
        assertTrue(Cracker.generationMode("molly").
                equals("4181eecbd7a755d19fdf73887c54837cbecf63fd"));
        assertTrue(Cracker.generationMode("a").
                equals("86f7e437faa5a7fce15d1ddcb9eaeaea377667b8"));
        assertTrue(Cracker.generationMode("fm").
                equals("adeb6f2a18fe33af368d91b09587b68e3abcb9a7"));
        assertTrue(Cracker.generationMode("java").
                equals("23524be9dba14bc2f1975b37f95c3381771595c8"));
    }

    public void testCrackerMode() {
        Cracker cracker = new Cracker("4181eecbd7a755d19fdf73887c54837cbecf63fd",
                5, 4);
        cracker.await();
        ArrayList<String> passwords = cracker.getPasswords();
        assertTrue(passwords.get(0).equals("molly"));

        cracker = new Cracker("596727c8a0ea4db3ba2ceceedccbacd3d7b371b8",
                4, 8);
        cracker.await();
        passwords = cracker.getPasswords();
        assertTrue(passwords.get(0).equals("jack"));

        cracker = new Cracker("88759b3bcd4bf09fb7fbfe1699080f8bcdb7ca96",
                10, 2);
        cracker.await();
        passwords = cracker.getPasswords();
        assertTrue(passwords.get(0).equals("dostoevsky"));

        cracker = new Cracker("3c2ab713498836d8390a6cbb4ed70aaca24d25ce",
                15, 3);
        cracker.await();
        passwords = cracker.getPasswords();
        assertTrue(passwords.get(0).equals("tbilisi"));

    }
}
