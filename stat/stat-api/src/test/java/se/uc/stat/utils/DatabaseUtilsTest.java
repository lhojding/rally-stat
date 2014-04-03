package se.uc.stat.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for DatabaseUtils.
 *  
 * @author Anders Persson (konx40)
 */
public class DatabaseUtilsTest extends AbstractTestBase {
    /**
     * Test the pad method.
     */
    @Test
    public void testPad() {
        assertPad("test", 4);
        assertPad("test", 5);
        assertPad("test", 6);
        assertPad("test", 50);
        assertPad("test", 150);
        assertPad("test", 500);
        assertNull("null string", DatabaseUtils.pad(null, 10));
        assertEquals("Truncate string", "tes", DatabaseUtils.pad("test", 3));
    }
    
    /**
     * Perform a call to the pad method and evaluate the result.
     * The method fails in a JUnit way if the test fails.
     * 
     * @param str      The string to pad.
     * @param numChars The expected length.
     */
    private void assertPad(String str, int numChars) {
        final String padded = DatabaseUtils.pad(str, numChars);
        final String testCase = "String '" + str + "', numChars=" + numChars +
                ". ";
        assertEquals(testCase + "Invalid length", numChars, padded.length());
        assertEquals(testCase + "Invalid start of string",
                str, padded.substring(0, str.length()));
        for (int pos = str.length(); pos < numChars; pos++) {
            assertEquals(testCase + "No space at pos" + pos, ' ',
                    padded.charAt(pos));
        }
    }
}
