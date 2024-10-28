package EECS3311.test;

import EECS3311.model.PBKDF2Hasher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HasherTests {
    private PBKDF2Hasher hasher;

    @Before
    public void init() {
        hasher = new PBKDF2Hasher();
    }

    @Test
    public void testFunctionality() {
        String hash1 = hasher.hash("Hi #% /*-+ ][ }{ Hello why".toCharArray());
        String hash2 = hasher.hash("Hi 1\"23456789 ^@!`~ './\\".toCharArray());

        Assert.assertNotEquals("Hi #% /*-+ ][ }{ Hello why", hash1);
        Assert.assertNotEquals("Hi 1\"23456789 ^@!`~ './\\", hash2);

        Assert.assertFalse(hasher.checkPassword("Hi 1\"23456789 ^@!`~ './\\".toCharArray(), hash1));
        Assert.assertFalse(hasher.checkPassword("Hi #% /*-+ ][ }{ Hello why".toCharArray(), hash2));

        Assert.assertTrue(hasher.checkPassword("Hi #% /*-+ ][ }{ Hello why".toCharArray(), hash1));
        Assert.assertTrue(hasher.checkPassword("Hi 1\"23456789 ^@!`~ './\\".toCharArray(), hash2));
        Assert.assertTrue(hasher.checkPassword("Hi #% /*-+ ][ }{ Hello why", hash1));
        Assert.assertTrue(hasher.checkPassword("Hi 1\"23456789 ^@!`~ './\\", hash2));
    }
}
