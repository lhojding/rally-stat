package se.uc.stat.customerstatistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

import se.uc.stat.utils.AbstractTestBase;

/**
 * Test the CustomerInformation class.
 * 
 * @author Anders Persson (konx40)
 */
public class CustomerInformationTest extends AbstractTestBase {
    /** Testkey to use. */
    private static final CustomerStatisticsKey KEY = new CustomerStatisticsKey(
            "s", "m", "o", "p", "me", "c", new Date());
    
    /**
     * Test the constructor, getDayOfWeek and the initial values.
     */
    @Test
    public void testConstructor() {
        CustomerInformation info = new CustomerInformation(KEY, 3);
        assertEquals("Invalid initial value of getDayOfWeek",
                3, info.getDayOfWeek());
        assertEquals("Invalid initial value of getNumCorrectCalls",
                0, info.getNumCorrectCalls());
        assertEquals("Invalid initial value of getNumInvalidCalls",
                0, info.getNumInvalidCalls());
        assertEquals("Invalid initial value of getNumFailedCalls",
                0, info.getNumFailedCalls());
        assertSame("Invalid initial value of getKey", KEY, info.getKey());
        
        // Test limits in input
        info = new CustomerInformation(KEY, 0);
        info = new CustomerInformation(KEY, 6);
        // Test invalid input
        try {
            info = new CustomerInformation(KEY, -1);
            fail("dayOfWeek -1 did not fail");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
        try {
            info = new CustomerInformation(KEY, 7);
            fail("dayOfWeek 7 did not fail");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
        try {
            info = new CustomerInformation(null, 3);
            fail("key null did not fail");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
    }

    /**
     * Test registerCorrectCall and getNumCorrectCalls.
     */
    @Test
    public void testCorrectCalls() {
        CustomerInformation info = new CustomerInformation(KEY, 0);
        info.registerCorrectCall();
        assertEquals("Invalid value after first registration",
                1, info.getNumCorrectCalls());
        info.registerCorrectCall();
        assertEquals("Invalid value after second registration",
                2, info.getNumCorrectCalls());
        info.registerCorrectCall();
        assertEquals("Invalid value after third registration",
                3, info.getNumCorrectCalls());
    }

    /**
     * Test registerInvalidCall and getNumInvalidCalls.
     */
    @Test
    public void testInvalidCalls() {
        CustomerInformation info = new CustomerInformation(KEY, 0);
        info.registerInvalidCall();
        assertEquals("Invalid value after first registration",
                1, info.getNumInvalidCalls());
        info.registerInvalidCall();
        assertEquals("Invalid value after second registration",
                2, info.getNumInvalidCalls());
        info.registerInvalidCall();
        assertEquals("Invalid value after third registration",
                3, info.getNumInvalidCalls());
    }

    /**
     * Test registerFailedCall and getNumFailedCalls.
     */
    @Test
    public void testFailedCalls() {
        CustomerInformation info = new CustomerInformation(KEY, 0);
        info.registerFailedCall();
        assertEquals("Invalid value after first registration",
                1, info.getNumFailedCalls());
        info.registerFailedCall();
        assertEquals("Invalid value after second registration",
                2, info.getNumFailedCalls());
        info.registerFailedCall();
        assertEquals("Invalid value after third registration",
                3, info.getNumFailedCalls());
    }
    
    /**
     * Test the subtract method for correct calls.
     */
    @Test
    public void testCorrectSubtract() {
        int invalid = 13;
        int failed = 17;
        testCorrectSubtract(0, 0, invalid++, failed++);
        testCorrectSubtract(1, 0, invalid++, failed++);
        testCorrectSubtract(1, 1, invalid++, failed++);
        testCorrectSubtract(2, 0, invalid++, failed++);
        testCorrectSubtract(2, 1, invalid++, failed++);
        testCorrectSubtract(2, 2, invalid++, failed++);
        testCorrectSubtract(3, 0, invalid++, failed++);
        testCorrectSubtract(3, 1, invalid++, failed++);
        testCorrectSubtract(3, 2, invalid++, failed++);
        testCorrectSubtract(3, 3, invalid++, failed++);
    }

    /**
     * Test a subtraction of correct calls. This method fails in a JUnit way
     * if the test fails.
     * 
     * @param initial  The initial value of number of correct calls.
     * @param subtract The number of correct calls in the subtracted instance.
     * @param invalid  The number of invalid calls in both instances.
     * @param failed   The number of failed calls in both instances.
     */
    private void testCorrectSubtract(int initial, int subtract,
            int invalid, int failed) {
        final String testCase = "Correct calls, initial=" + initial +
                ", subtract=" + subtract + ". ";
        final CustomerInformation info = new CustomerInformation(KEY, 3);
        final CustomerInformation toSubtract = new CustomerInformation (KEY, 6);
        for (int i = 0; i < invalid; i++) {
            info.registerInvalidCall();
            toSubtract.registerInvalidCall();
        }
        for (int i = 0; i < failed; i++) {
            info.registerFailedCall();
            toSubtract.registerFailedCall();
        }
        // Prepare initial.
        for (int i = 0; i < initial; i++) {
            info.registerCorrectCall();
        }
        assertEquals(testCase + "Initial getNumCorrectCalls",
                initial, info.getNumCorrectCalls());
        assertEquals(testCase + "Initial getNumInvalidCalls",
                invalid, info.getNumInvalidCalls());
        assertEquals(testCase + "Initial getNumFailedCalls",
                failed, info.getNumFailedCalls());
        assertCreateClone(info);
        // Prepare subtract
        for (int i = 0; i < subtract; i++) {
            toSubtract.registerCorrectCall();
        }
        assertEquals(testCase + "Initial toSubtract getNumCorrectCalls",
                subtract, toSubtract.getNumCorrectCalls());
        assertEquals(testCase + "Initial toSubtract getNumInvalidCalls",
                invalid, toSubtract.getNumInvalidCalls());
        assertEquals(testCase + "Initial toSubtract getNumFailedCalls",
                failed, toSubtract.getNumFailedCalls());
        
        // Perform test
        if (initial == subtract) {
            assertTrue(testCase + "subtract result", info.subtract(toSubtract));
        } else {
            assertFalse(testCase + "subtract result", info.subtract(toSubtract));
        }
        assertEquals(testCase + "getNumCorrectCalls after operation",
                initial - subtract, info.getNumCorrectCalls());
        assertEquals(testCase + "getNumInvalidCalls after operation",
                0, info.getNumInvalidCalls());
        assertEquals(testCase + "getNumFailedCalls after operation",
                0, info.getNumFailedCalls());

        assertEquals(testCase + "toSubtract after operation getNumCorrectCalls",
                subtract, toSubtract.getNumCorrectCalls());
        assertEquals(testCase + "toSubtract after operation getNumInvalidCalls",
                invalid, toSubtract.getNumInvalidCalls());
        assertEquals(testCase + "toSubtract after operation getNumFailedCalls",
                failed, toSubtract.getNumFailedCalls());
    }
    
    /**
     * Test the subtract method for invalid calls.
     */
    @Test
    public void testInvalidSubtract() {
        int correct = 13;
        int failed = 17;
        testInvalidSubtract(0, 0, correct++, failed++);
        testInvalidSubtract(1, 0, correct++, failed++);
        testInvalidSubtract(1, 1, correct++, failed++);
        testInvalidSubtract(2, 0, correct++, failed++);
        testInvalidSubtract(2, 1, correct++, failed++);
        testInvalidSubtract(2, 2, correct++, failed++);
        testInvalidSubtract(3, 0, correct++, failed++);
        testInvalidSubtract(3, 1, correct++, failed++);
        testInvalidSubtract(3, 2, correct++, failed++);
        testInvalidSubtract(3, 3, correct++, failed++);
    }

    /**
     * Test a subtraction of invalid calls. This method fails in a JUnit way
     * if the test fails.
     * 
     * @param initial  The initial value of number of invalid calls.
     * @param subtract The number of invalid calls in the subtracted instance.
     * @param correct  The number of correct calls in both instances.
     * @param failed   The number of failed calls in both instances.
     */
    private void testInvalidSubtract(int initial, int subtract,
            int correct, int failed) {
        final String testCase = "Invalid calls, initial=" + initial +
                ", subtract=" + subtract + ". ";
        final CustomerInformation info = new CustomerInformation(KEY, 3);
        final CustomerInformation toSubtract = new CustomerInformation (KEY, 6);
        for (int i = 0; i < correct; i++) {
            info.registerCorrectCall();
            toSubtract.registerCorrectCall();
        }
        for (int i = 0; i < failed; i++) {
            info.registerFailedCall();
            toSubtract.registerFailedCall();
        }
        // Prepare initial.
        for (int i = 0; i < initial; i++) {
            info.registerInvalidCall();
        }
        assertEquals(testCase + "Initial getNumCorrectCalls",
                correct, info.getNumCorrectCalls());
        assertEquals(testCase + "Initial getNumInvalidCalls",
                initial, info.getNumInvalidCalls());
        assertEquals(testCase + "Initial getNumFailedCalls",
                failed, info.getNumFailedCalls());
        assertCreateClone(info);
        // Prepare subtract
        for (int i = 0; i < subtract; i++) {
            toSubtract.registerInvalidCall();
        }
        assertEquals(testCase + "Initial toSubtract getNumCorrectCalls",
                correct, toSubtract.getNumCorrectCalls());
        assertEquals(testCase + "Initial toSubtract getNumInvalidCalls",
                subtract, toSubtract.getNumInvalidCalls());
        assertEquals(testCase + "Initial toSubtract getNumFailedCalls",
                failed, toSubtract.getNumFailedCalls());
        
        // Perform test
        if (initial == subtract) {
            assertTrue(testCase + "subtract result", info.subtract(toSubtract));
        } else {
            assertFalse(testCase + "subtract result", info.subtract(toSubtract));
        }
        assertEquals(testCase + "getNumCorrectCalls after operation",
                0, info.getNumCorrectCalls());
        assertEquals(testCase + "getNumInvalidCalls after operation",
                initial - subtract, info.getNumInvalidCalls());
        assertEquals(testCase + "getNumFailedCalls after operation",
                0, info.getNumFailedCalls());

        assertEquals(testCase + "toSubtract after operation getNumCorrectCalls",
                correct, toSubtract.getNumCorrectCalls());
        assertEquals(testCase + "toSubtract after operation getNumInvalidCalls",
                subtract, toSubtract.getNumInvalidCalls());
        assertEquals(testCase + "toSubtract after operation getNumFailedCalls",
                failed, toSubtract.getNumFailedCalls());
    }
    
    /**
     * Test the subtract method for failed calls.
     */
    @Test
    public void testFailedSubtract() {
        int correct = 13;
        int invalid = 17;
        testFailedSubtract(0, 0, correct++, invalid++);
        testFailedSubtract(1, 0, correct++, invalid++);
        testFailedSubtract(1, 1, correct++, invalid++);
        testFailedSubtract(2, 0, correct++, invalid++);
        testFailedSubtract(2, 1, correct++, invalid++);
        testFailedSubtract(2, 2, correct++, invalid++);
        testFailedSubtract(3, 0, correct++, invalid++);
        testFailedSubtract(3, 1, correct++, invalid++);
        testFailedSubtract(3, 2, correct++, invalid++);
        testFailedSubtract(3, 3, correct++, invalid++);
    }

    /**
     * Test a subtraction of failed calls. This method fails in a JUnit way
     * if the test fails.
     * 
     * @param initial  The initial value of number of invalid calls.
     * @param subtract The number of invalid calls in the subtracted instance.
     * @param correct  The number of correct calls in both instances.
     * @param invalid  The number of invalid calls in both instances.
     */
    private void testFailedSubtract(int initial, int subtract,
            int correct, int invalid) {
        final String testCase = "Invalid calls, initial=" + initial +
                ", subtract=" + subtract + ". ";
        final CustomerInformation info = new CustomerInformation(KEY, 3);
        final CustomerInformation toSubtract = new CustomerInformation (KEY, 6);
        for (int i = 0; i < correct; i++) {
            info.registerCorrectCall();
            toSubtract.registerCorrectCall();
        }
        for (int i = 0; i < invalid; i++) {
            info.registerInvalidCall();
            toSubtract.registerInvalidCall();
        }
        // Prepare initial.
        for (int i = 0; i < initial; i++) {
            info.registerFailedCall();
        }
        assertEquals(testCase + "Initial getNumCorrectCalls",
                correct, info.getNumCorrectCalls());
        assertEquals(testCase + "Initial getNumInvalidCalls",
                invalid, info.getNumInvalidCalls());
        assertEquals(testCase + "Initial getNumFailedCalls",
                initial, info.getNumFailedCalls());
        assertCreateClone(info);
        // Prepare subtract
        for (int i = 0; i < subtract; i++) {
            toSubtract.registerFailedCall();
        }
        assertEquals(testCase + "Initial toSubtract getNumCorrectCalls",
                correct, toSubtract.getNumCorrectCalls());
        assertEquals(testCase + "Initial toSubtract getNumInvalidCalls",
                invalid, toSubtract.getNumInvalidCalls());
        assertEquals(testCase + "Initial toSubtract getNumFailedCalls",
                subtract, toSubtract.getNumFailedCalls());
        
        // Perform test
        if (initial == subtract) {
            assertTrue(testCase + "subtract result", info.subtract(toSubtract));
        } else {
            assertFalse(testCase + "subtract result", info.subtract(toSubtract));
        }
        assertEquals(testCase + "getNumCorrectCalls after operation",
                0, info.getNumCorrectCalls());
        assertEquals(testCase + "getNumInvalidCalls after operation",
                0, info.getNumInvalidCalls());
        assertEquals(testCase + "getNumFailedCalls after operation",
                initial - subtract, info.getNumFailedCalls());

        assertEquals(testCase + "toSubtract after operation getNumCorrectCalls",
                correct, toSubtract.getNumCorrectCalls());
        assertEquals(testCase + "toSubtract after operation getNumInvalidCalls",
                invalid, toSubtract.getNumInvalidCalls());
        assertEquals(testCase + "toSubtract after operation getNumFailedCalls",
                subtract, toSubtract.getNumFailedCalls());
    }
    
    /**
     * Perform test of subtract where all numbers are not equals.
     */
    @Test
    public void testComplexSubtract() {
        CustomerInformation info = new CustomerInformation(KEY, 3);
        CustomerInformation subtract = new CustomerInformation(KEY, 6);

        info.registerCorrectCall();
        info.registerCorrectCall();
        info.registerInvalidCall();
        info.registerInvalidCall();
        info.registerInvalidCall();
        info.registerFailedCall();
        info.registerFailedCall();
        info.registerFailedCall();
        info.registerFailedCall();
        
        subtract.registerCorrectCall();
        subtract.registerInvalidCall();
        subtract.registerInvalidCall();
        subtract.registerFailedCall();
        subtract.registerFailedCall();
        subtract.registerFailedCall();
        
        assertFalse("Invalid result when all numbers should be 1",
                info.subtract(subtract));
        assertEquals("getNumCorrectCalls", 1, info.getNumCorrectCalls());
        assertEquals("getNumInvalidCalls", 1, info.getNumInvalidCalls());
        assertEquals("getNumFailedCalls", 1, info.getNumFailedCalls());
    }
    
    /**
     * Assert that the createClone method works.
     * 
     * @param expected Expected values, object to clone.
     */
    private void assertCreateClone(CustomerInformation expected) {
        CustomerInformation actual = expected.createClone();
        assertEquals("getDayOfWeek",
                expected.getDayOfWeek(), actual.getDayOfWeek());
        assertSame("getKey",
                expected.getKey(), actual.getKey());
        assertEquals("getNumCorrectCalls",
                expected.getNumCorrectCalls(), actual.getNumCorrectCalls());
        assertEquals("getNumInvalidCalls",
                expected.getNumInvalidCalls(), actual.getNumInvalidCalls());
        assertEquals("getNumFailedCalls",
                expected.getNumFailedCalls(), actual.getNumFailedCalls());
    }
}
