package se.uc.stat.timestatistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

import se.uc.stat.utils.AbstractTestBase;

/**
 * Test the TimeInformation class.
 * 
 * @author Anders Persson (konx40)
 */
public class TimeInformationTest extends AbstractTestBase {
    /** Testkey to use. */
    private static final TimeStatisticsKey KEY = new TimeStatisticsKey(
            "s", "m", "o", "p", "me", "l", new Date());
    /** The place in the expected array for the counter 10. */
    private final static int NUM_PLACE_10 = 0;
    /** The place in the expected array for the counter 20. */
    private final static int NUM_PLACE_20 = 1;
    /** The place in the expected array for the counter 50. */
    private final static int NUM_PLACE_50 = 2;
    /** The place in the expected array for the counter 100. */
    private final static int NUM_PLACE_100 = 3;
    /** The place in the expected array for the counter 200. */
    private final static int NUM_PLACE_200 = 4;
    /** The place in the expected array for the counter 500. */
    private final static int NUM_PLACE_500 = 5;
    /** The place in the expected array for the counter 1000. */
    private final static int NUM_PLACE_1000 = 6;
    /** The place in the expected array for the counter 2000. */
    private final static int NUM_PLACE_2000 = 7;
    /** The place in the expected array for the counter 5000. */
    private final static int NUM_PLACE_5000 = 8;
    /** The place in the expected array for the counter 10000. */
    private final static int NUM_PLACE_10000 = 9;
    /** The place in the expected array for the counter 20000. */
    private final static int NUM_PLACE_20000 = 10;
    /** The place in the expected array for the counter over 20000. */
    private final static int NUM_PLACE_OVER_20000 = 11;
    /** The total number of places in expected array. */
    private final static int NUM_PLACES = 12;
    
    /**
     * Test the constructor, getDayOfWeek, getHourOfDay and the initial values.
     */
    @Test
    public void testConstructor() {
        TimeInformation info = new TimeInformation(KEY, 3, 7);
        assertEquals("Invalid initial value of getDayOfWeek",
                3, info.getDayOfWeek());
        assertEquals("Invalid initial value of getHourOfDay",
                7, info.getHourOfDay());
        assertEquals("Invalid initial value of getNumCorrectCalls",
                0, info.getNumCorrectCalls());
        assertEquals("Invalid initial value of getNumInvalidCalls",
                0, info.getNumInvalidCalls());
        assertEquals("Invalid initial value of getNumFailedCalls",
                0, info.getNumFailedCalls());
        assertEquals("Invalid initial value of getTotalTimeCorrectCalls",
                0, info.getTotalTimeCorrectCalls());
        assertEquals("Invalid initial value of getTotalTimeInvalidCalls",
                0, info.getTotalTimeInvalidCalls());
        assertEquals("Invalid initial value of getTotalTimeFailedCalls",
                0, info.getTotalTimeFailedCalls());
        assertEquals("Invalid initial value of getNum10",
                0, info.getNum10());
        assertEquals("Invalid initial value of getNum20",
                0, info.getNum20());
        assertEquals("Invalid initial value of getNum50",
                0, info.getNum50());
        assertEquals("Invalid initial value of getNum100",
                0, info.getNum100());
        assertEquals("Invalid initial value of getNum200",
                0, info.getNum200());
        assertEquals("Invalid initial value of getNum500",
                0, info.getNum500());
        assertEquals("Invalid initial value of getNum1000",
                0, info.getNum1000());
        assertEquals("Invalid initial value of getNum2000",
                0, info.getNum2000());
        assertEquals("Invalid initial value of getNum5000",
                0, info.getNum5000());
        assertEquals("Invalid initial value of getNum10000",
                0, info.getNum10000());
        assertEquals("Invalid initial value of getNum20000",
                0, info.getNum20000());
        assertEquals("Invalid initial value of getNumOver20000",
                0, info.getNumOver20000());
        assertSame("Invalid initial value of getKey", KEY, info.getKey());
        
        // Test limits in input
        info = new TimeInformation(KEY, 0, 7);
        info = new TimeInformation(KEY, 6, 7);
        info = new TimeInformation(KEY, 3, 0);
        info = new TimeInformation(KEY, 3, 23);
        // Test invalid input
        try {
            info = new TimeInformation(KEY, -1, 7);
            fail("dayOfWeek -1 did not fail");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
        try {
            info = new TimeInformation(KEY, 7, 7);
            fail("dayOfWeek 7 did not fail");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
        try {
            info = new TimeInformation(KEY, 3, -1);
            fail("hourOfDay -1 did not fail");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
        try {
            info = new TimeInformation(KEY, 3, 24);
            fail("hourOfDay 24 did not fail");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
        try {
            info = new TimeInformation(null, 3, 7);
            fail("key null did not fail");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
    }

    /**
     * Test registerCorrectCall and getNumXxx.
     */
    @Test
    public void testCorrectCalls() {
        final TimeInformation info = new TimeInformation(KEY, 0, 0);
        final int[] expected = new int[NUM_PLACES];
        final IntervalTestData[] testDatas = getTimeIntervalTestData();
        int num = 0;
        long totalTime = 0;
        assertNumbers("Initial check", expected, info);
        for (int i = 0; i < 3; i++) {
            for (IntervalTestData testData : testDatas) {
                String testCase = "After time=" + testData.callTime;
                info.registerCorrectCall(testData.callTime);
                expected[testData.numPlace]++;
                assertNumbers(testCase, expected, info);
                num++;
                totalTime += testData.callTime;
                assertEquals(testCase + ": getNumCorrectCalls",
                        num, info.getNumCorrectCalls());
                assertEquals(testCase + ": getNumInvalidCalls",
                        0, info.getNumInvalidCalls());
                assertEquals(testCase + ": getNumFailedCalls",
                        0, info.getNumFailedCalls());
                assertEquals(testCase + ": getTotalTimeCorrectCalls",
                        totalTime, info.getTotalTimeCorrectCalls());
                assertEquals(testCase + ": getTotalTimeInvalidCalls",
                        0, info.getTotalTimeInvalidCalls());
                assertEquals(testCase + ": getTotalTimeFailedCalls",
                        0, info.getTotalTimeFailedCalls());
            }
        }
    }

    /**
     * Test registerInvalidCall and getNumXxx.
     */
    @Test
    public void testInvalidCalls() {
        final TimeInformation info = new TimeInformation(KEY, 0, 0);
        final int[] expected = new int[NUM_PLACES];
        final IntervalTestData[] testDatas = getTimeIntervalTestData();
        int num = 0;
        long totalTime = 0;
        assertNumbers("Initial check", expected, info);
        for (int i = 0; i < 3; i++) {
            for (IntervalTestData testData : testDatas) {
                String testCase = "After time=" + testData.callTime;
                info.registerInvalidCall(testData.callTime);
                expected[testData.numPlace]++;
                assertNumbers(testCase, expected, info);
                num++;
                totalTime += testData.callTime;
                assertEquals(testCase + ": getNumCorrectCalls",
                        0, info.getNumCorrectCalls());
                assertEquals(testCase + ": getNumInvalidCalls",
                        num, info.getNumInvalidCalls());
                assertEquals(testCase + ": getNumFailedCalls",
                        0, info.getNumFailedCalls());
                assertEquals(testCase + ": getTotalTimeCorrectCalls",
                        0, info.getTotalTimeCorrectCalls());
                assertEquals(testCase + ": getTotalTimeInvalidCalls",
                        totalTime, info.getTotalTimeInvalidCalls());
                assertEquals(testCase + ": getTotalTimeFailedCalls",
                        0, info.getTotalTimeFailedCalls());
            }
        }
    }

    /**
     * Test registerFailedCall and getNumXxx.
     */
    @Test
    public void testFailedCalls() {
        final TimeInformation info = new TimeInformation(KEY, 0, 0);
        final int[] expected = new int[NUM_PLACES];
        final IntervalTestData[] testDatas = getTimeIntervalTestData();
        int num = 0;
        long totalTime = 0;
        assertNumbers("Initial check", expected, info);
        for (int i = 0; i < 3; i++) {
            for (IntervalTestData testData : testDatas) {
                String testCase = "After time=" + testData.callTime;
                info.registerFailedCall(testData.callTime);
                expected[testData.numPlace]++;
                assertNumbers(testCase, expected, info);
                num++;
                totalTime += testData.callTime;
                assertEquals(testCase + ": getNumCorrectCalls",
                        0, info.getNumCorrectCalls());
                assertEquals(testCase + ": getNumInvalidCalls",
                        0, info.getNumInvalidCalls());
                assertEquals(testCase + ": getNumFailedCalls",
                        num, info.getNumFailedCalls());
                assertEquals(testCase + ": getTotalTimeCorrectCalls",
                        0, info.getTotalTimeCorrectCalls());
                assertEquals(testCase + ": getTotalTimeInvalidCalls",
                        0, info.getTotalTimeInvalidCalls());
                assertEquals(testCase + ": getTotalTimeFailedCalls",
                        totalTime, info.getTotalTimeFailedCalls());
            }
        }
    }

    /**
     * Asserts that the number of calls (result of getNumXxx) equals to
     * the number of expected calls. Fail in a JUnit way if there are
     * errors.
     *  
     * @param testCase Description of the test case to simplify debugging.
     * @param expected The expected number of calls from the different
     *                 methods.
     * @param actual   The actual values.
     */
    private void assertNumbers(String testCase, int[] expected,
            TimeInformation actual) {
        assertEquals(testCase + ": getNum10",
                expected[NUM_PLACE_10], actual.getNum10());
        assertEquals(testCase + ": getNum20",
                expected[NUM_PLACE_20], actual.getNum20());
        assertEquals(testCase + ": getNum50",
                expected[NUM_PLACE_50], actual.getNum50());
        assertEquals(testCase + ": getNum100",
                expected[NUM_PLACE_100], actual.getNum100());
        assertEquals(testCase + ": getNum200",
                expected[NUM_PLACE_200], actual.getNum200());
        assertEquals(testCase + ": getNum500",
                expected[NUM_PLACE_500], actual.getNum500());
        assertEquals(testCase + ": getNum1000",
                expected[NUM_PLACE_1000], actual.getNum1000());
        assertEquals(testCase + ": getNum2000",
                expected[NUM_PLACE_2000], actual.getNum2000());
        assertEquals(testCase + ": getNum5000",
                expected[NUM_PLACE_5000], actual.getNum5000());
        assertEquals(testCase + ": getNum10000",
                expected[NUM_PLACE_10000], actual.getNum10000());
        assertEquals(testCase + ": getNum20000",
                expected[NUM_PLACE_20000], actual.getNum20000());
        assertEquals(testCase + ": getNumOver20000",
                expected[NUM_PLACE_OVER_20000], actual.getNumOver20000());
    }
    
    /**
     * Get test data for time interval test.
     * 
     * @return Test data for time interval test.
     */
    private IntervalTestData[] getTimeIntervalTestData() {
        IntervalTestData result[] = new IntervalTestData[] {
                new IntervalTestData(0, NUM_PLACE_10),
                new IntervalTestData(5, NUM_PLACE_10),
                new IntervalTestData(10, NUM_PLACE_10),
                new IntervalTestData(11, NUM_PLACE_20),
                new IntervalTestData(15, NUM_PLACE_20),
                new IntervalTestData(20, NUM_PLACE_20),
                new IntervalTestData(21, NUM_PLACE_50),
                new IntervalTestData(40, NUM_PLACE_50),
                new IntervalTestData(50, NUM_PLACE_50),
                new IntervalTestData(51, NUM_PLACE_100),
                new IntervalTestData(70, NUM_PLACE_100),
                new IntervalTestData(100, NUM_PLACE_100),
                new IntervalTestData(101, NUM_PLACE_200),
                new IntervalTestData(150, NUM_PLACE_200),
                new IntervalTestData(200, NUM_PLACE_200),
                new IntervalTestData(201, NUM_PLACE_500),
                new IntervalTestData(400, NUM_PLACE_500),
                new IntervalTestData(500, NUM_PLACE_500),
                new IntervalTestData(501, NUM_PLACE_1000),
                new IntervalTestData(700, NUM_PLACE_1000),
                new IntervalTestData(1000, NUM_PLACE_1000),
                new IntervalTestData(1001, NUM_PLACE_2000),
                new IntervalTestData(1500, NUM_PLACE_2000),
                new IntervalTestData(2000, NUM_PLACE_2000),
                new IntervalTestData(2001, NUM_PLACE_5000),
                new IntervalTestData(3000, NUM_PLACE_5000),
                new IntervalTestData(5000, NUM_PLACE_5000),
                new IntervalTestData(5001, NUM_PLACE_10000),
                new IntervalTestData(7000, NUM_PLACE_10000),
                new IntervalTestData(10000, NUM_PLACE_10000),
                new IntervalTestData(10001, NUM_PLACE_20000),
                new IntervalTestData(15000, NUM_PLACE_20000),
                new IntervalTestData(20000, NUM_PLACE_20000),
                new IntervalTestData(20001, NUM_PLACE_OVER_20000),
                new IntervalTestData(30000, NUM_PLACE_OVER_20000)
        };
        return result;
    }

    /**
     * Test the subtract method for correct calls.
     */
    @Test
    public void testCorrectSubtract() {
        for (SubtractTestData testData : getSubtractionTestData()) {
            // numbers and time are equals.
            testCorrectSubtract(testData, 0, 0, 0, 0, true);
            // numbers are not equals and time are not equals.
            testCorrectSubtract(testData, 1, 0, 0, 0, false);
            testCorrectSubtract(testData, 1, 1, 0, 0, true);
            // numbers are equals and time are not equals.
            testCorrectSubtract(testData, 1, 1, testData.extraCallTime, 0, false);
            testCorrectSubtract(testData, 2, 0, 0, 0, false);
            testCorrectSubtract(testData, 2, 1, 0, 0, false);
            testCorrectSubtract(testData, 2, 2, 0, 0, true);
            testCorrectSubtract(testData, 2, 2, testData.extraCallTime, 0, false);
            testCorrectSubtract(testData, 3, 0, 0, 0, false);
            testCorrectSubtract(testData, 3, 1, 0, 0, false);
            testCorrectSubtract(testData, 3, 2, 0, 0, false);
            // numbers are not equals but time is equal.
            assertEquals("Invalid test data, callTime=" + testData.callTime,
                    3 * testData.callTime,
                    2 * (testData.callTime + testData.extraCallTime));
            testCorrectSubtract(testData, 3, 2, 0, testData.extraCallTime, false);
            testCorrectSubtract(testData, 3, 3, 0, 0, true);
            testCorrectSubtract(testData, 3, 3, testData.extraCallTime, 0, false);
        }
    }

    /**
     * Test a subtraction of correct calls. This method fails in a JUnit way
     * if the test fails.
     * 
     * @param testData          The test data to use for the test.
     * @param initial           The initial value of number of correct calls.
     * @param subtract          The number of correct calls in the subtracted
     *                          instance.
     * @param extraInitialTime  Extra call time to add to the initial
     *                          registration calls.
     * @param extraSubtractTime Extra call time to add to the initial
     *                          registration calls.
     * @param expectedResult    The expected result of the subtract operation.
     */
    private void testCorrectSubtract(SubtractTestData testData, int initial,
            int subtract, long extraInitialTime, long extraSubtractTime,
            boolean expectedResult) {
        final String testCase = "Correct calls, time= " + testData.callTime +
        ", initial=" + initial + ", subtract=" + subtract +
        ", extraInitialTime=" + extraInitialTime + ", extraSubtractTime=" +
        extraSubtractTime + ". ";
        final TimeInformation info = new TimeInformation(KEY, 3, 7);
        final TimeInformation toSubtract = new TimeInformation(KEY, 6, 9);
        final int[] expectedInfo = new int[NUM_PLACES];
        final long infoTime = (testData.callTime + extraInitialTime) * initial;
        final long toSubtractTime = (testData.callTime + extraSubtractTime) *
                subtract;

        // Prepare initial.
        for (int i = 0; i < initial; i++) {
            final long time = testData.callTime + extraInitialTime;
            info.registerCorrectCall(time);
        }
        assertCreateClone(info);
        // Prepare subtract
        for (int i = 0; i < subtract; i++) {
            final long time = testData.callTime + extraSubtractTime;
            toSubtract.registerCorrectCall(time);
        }
        
        expectedInfo[testData.numPlace] = initial - subtract;
        
        // Perform test
        if (expectedResult) {
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
                0, toSubtract.getNumInvalidCalls());
        assertEquals(testCase + "toSubtract after operation getNumFailedCalls",
                0, toSubtract.getNumFailedCalls());

        assertEquals(testCase + "getTotalTimeCorrectCalls after operation",
                infoTime - toSubtractTime, info.getTotalTimeCorrectCalls());
        assertEquals(testCase + "getTotalTimeInvalidCalls after operation",
                0, info.getTotalTimeInvalidCalls());
        assertEquals(testCase + "getTotalTimeFailedCalls after operation",
                0, info.getTotalTimeFailedCalls());

        assertEquals(testCase +
                "toSubtract after operation getTotalTimeCorrectCalls",
                toSubtractTime, toSubtract.getTotalTimeCorrectCalls());
        assertEquals(testCase +
                "toSubtract after operation getTotalTimeInvalidCalls",
                0, toSubtract.getTotalTimeInvalidCalls());
        assertEquals(testCase +
                "toSubtract after operation getTotalTimeFailedCalls",
                0, toSubtract.getTotalTimeFailedCalls());
        
        assertNumbers(testCase, expectedInfo, info);
    }

    /**
     * Test the subtract method for invalid calls.
     */
    @Test
    public void testInvalidSubtract() {
        for (SubtractTestData testData : getSubtractionTestData()) {
            // numbers and time are equals.
            testInvalidSubtract(testData, 0, 0, 0, 0, true);
            // numbers are not equals and time are not equals.
            testInvalidSubtract(testData, 1, 0, 0, 0, false);
            testInvalidSubtract(testData, 1, 1, 0, 0, true);
            // numbers are equals and time are not equals.
            testInvalidSubtract(testData, 1, 1, testData.extraCallTime, 0, false);
            testInvalidSubtract(testData, 2, 0, 0, 0, false);
            testInvalidSubtract(testData, 2, 1, 0, 0, false);
            testInvalidSubtract(testData, 2, 2, 0, 0, true);
            testInvalidSubtract(testData, 2, 2, testData.extraCallTime, 0, false);
            testInvalidSubtract(testData, 3, 0, 0, 0, false);
            testInvalidSubtract(testData, 3, 1, 0, 0, false);
            testInvalidSubtract(testData, 3, 2, 0, 0, false);
            // numbers are not equals but time is equal.
            assertEquals("Invalid test data, callTime=" + testData.callTime,
                    3 * testData.callTime,
                    2 * (testData.callTime + testData.extraCallTime));
            testInvalidSubtract(testData, 3, 2, 0, testData.extraCallTime, false);
            testInvalidSubtract(testData, 3, 3, 0, 0, true);
            testInvalidSubtract(testData, 3, 3, testData.extraCallTime, 0, false);
        }
    }

    /**
     * Test a subtraction of invalid calls. This method fails in a JUnit way
     * if the test fails.
     * 
     * @param testData          The test data to use for the test.
     * @param initial           The initial value of number of correct calls.
     * @param subtract          The number of correct calls in the subtracted
     *                          instance.
     * @param extraInitialTime  Extra call time to add to the initial
     *                          registration calls.
     * @param extraSubtractTime Extra call time to add to the initial
     *                          registration calls.
     * @param expectedResult    The expected result of the subtract operation.
     */
    private void testInvalidSubtract(SubtractTestData testData, int initial,
            int subtract, long extraInitialTime, long extraSubtractTime,
            boolean expectedResult) {
        final String testCase = "Correct calls, time= " + testData.callTime +
        ", initial=" + initial + ", subtract=" + subtract +
        ", extraInitialTime=" + extraInitialTime + ", extraSubtractTime=" +
        extraSubtractTime + ". ";
        final TimeInformation info = new TimeInformation(KEY, 3, 7);
        final TimeInformation toSubtract = new TimeInformation(KEY, 6, 9);
        final int[] expectedInfo = new int[NUM_PLACES];
        final long infoTime = (testData.callTime + extraInitialTime) * initial;
        final long toSubtractTime = (testData.callTime + extraSubtractTime) *
                subtract;

        // Prepare initial.
        for (int i = 0; i < initial; i++) {
            final long time = testData.callTime + extraInitialTime;
            info.registerInvalidCall(time);
        }
        assertCreateClone(info);
        // Prepare subtract
        for (int i = 0; i < subtract; i++) {
            final long time = testData.callTime + extraSubtractTime;
            toSubtract.registerInvalidCall(time);
        }
        expectedInfo[testData.numPlace] = initial - subtract;
        
        // Perform test
        if (expectedResult) {
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
                0, toSubtract.getNumCorrectCalls());
        assertEquals(testCase + "toSubtract after operation getNumInvalidCalls",
                subtract, toSubtract.getNumInvalidCalls());
        assertEquals(testCase + "toSubtract after operation getNumFailedCalls",
                0, toSubtract.getNumFailedCalls());

        assertEquals(testCase + "getTotalTimeCorrectCalls after operation",
                0, info.getTotalTimeCorrectCalls());
        assertEquals(testCase + "getTotalTimeInvalidCalls after operation",
                infoTime - toSubtractTime, info.getTotalTimeInvalidCalls());
        assertEquals(testCase + "getTotalTimeFailedCalls after operation",
                0, info.getTotalTimeFailedCalls());

        assertEquals(testCase +
                "toSubtract after operation getTotalTimeCorrectCalls",
                0, toSubtract.getTotalTimeCorrectCalls());
        assertEquals(testCase +
                "toSubtract after operation getTotalTimeInvalidCalls",
                toSubtractTime, toSubtract.getTotalTimeInvalidCalls());
        assertEquals(testCase +
                "toSubtract after operation getTotalTimeFailedCalls",
                0, toSubtract.getTotalTimeFailedCalls());
        
        assertNumbers(testCase, expectedInfo, info);
    }

    /**
     * Test the subtract method for failed calls.
     */
    @Test
    public void testFailedSubtract() {
        for (SubtractTestData testData : getSubtractionTestData()) {
            // numbers and time are equals.
            testFailedSubtract(testData, 0, 0, 0, 0, true);
            // numbers are not equals and time are not equals.
            testFailedSubtract(testData, 1, 0, 0, 0, false);
            testFailedSubtract(testData, 1, 1, 0, 0, true);
            // numbers are equals and time are not equals.
            testFailedSubtract(testData, 1, 1, testData.extraCallTime, 0, false);
            testFailedSubtract(testData, 2, 0, 0, 0, false);
            testFailedSubtract(testData, 2, 1, 0, 0, false);
            testFailedSubtract(testData, 2, 2, 0, 0, true);
            testFailedSubtract(testData, 2, 2, testData.extraCallTime, 0, false);
            testFailedSubtract(testData, 3, 0, 0, 0, false);
            testFailedSubtract(testData, 3, 1, 0, 0, false);
            testFailedSubtract(testData, 3, 2, 0, 0, false);
            // numbers are not equals but time is equal.
            assertEquals("Invalid test data, callTime=" + testData.callTime,
                    3 * testData.callTime,
                    2 * (testData.callTime + testData.extraCallTime));
            testFailedSubtract(testData, 3, 2, 0, testData.extraCallTime, false);
            testFailedSubtract(testData, 3, 3, 0, 0, true);
            testFailedSubtract(testData, 3, 3, testData.extraCallTime, 0, false);
        }
    }

    /**
     * Test a subtraction of failed calls. This method fails in a JUnit way
     * if the test fails.
     * 
     * @param testData          The test data to use for the test.
     * @param initial           The initial value of number of failed calls.
     * @param subtract          The number of failed calls in the subtracted
     *                          instance.
     * @param extraInitialTime  Extra call time to add to the initial
     *                          registration calls.
     * @param extraSubtractTime Extra call time to add to the initial
     *                          registration calls.
     * @param expectedResult    The expected result of the subtract operation.
     */
    private void testFailedSubtract(SubtractTestData testData, int initial,
            int subtract, long extraInitialTime, long extraSubtractTime,
            boolean expectedResult) {
        final String testCase = "Correct calls, time= " + testData.callTime +
        ", initial=" + initial + ", subtract=" + subtract +
        ", extraInitialTime=" + extraInitialTime + ", extraSubtractTime=" +
        extraSubtractTime + ". ";
        final TimeInformation info = new TimeInformation(KEY, 3, 7);
        final TimeInformation toSubtract = new TimeInformation(KEY, 6, 9);
        final int[] expectedInfo = new int[NUM_PLACES];
        final long infoTime = (testData.callTime + extraInitialTime) * initial;
        final long toSubtractTime = (testData.callTime + extraSubtractTime) *
                subtract;

        // Prepare initial.
        for (int i = 0; i < initial; i++) {
            final long time = testData.callTime + extraInitialTime;
            info.registerFailedCall(time);
        }
        assertCreateClone(info);
        // Prepare subtract
        for (int i = 0; i < subtract; i++) {
            final long time = testData.callTime + extraSubtractTime;
            toSubtract.registerFailedCall(time);
        }
        expectedInfo[testData.numPlace] = initial - subtract;
        
        // Perform test
        if (expectedResult) {
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
                0, toSubtract.getNumCorrectCalls());
        assertEquals(testCase + "toSubtract after operation getNumInvalidCalls",
                0, toSubtract.getNumInvalidCalls());
        assertEquals(testCase + "toSubtract after operation getNumFailedCalls",
                subtract, toSubtract.getNumFailedCalls());

        assertEquals(testCase + "getTotalTimeCorrectCalls after operation",
                0, info.getTotalTimeCorrectCalls());
        assertEquals(testCase + "getTotalTimeInvalidCalls after operation",
                0, info.getTotalTimeInvalidCalls());
        assertEquals(testCase + "getTotalTimeFailedCalls after operation",
                infoTime - toSubtractTime, info.getTotalTimeFailedCalls());

        assertEquals(testCase +
                "toSubtract after operation getTotalTimeCorrectCalls",
                0, toSubtract.getTotalTimeCorrectCalls());
        assertEquals(testCase +
                "toSubtract after operation getTotalTimeInvalidCalls",
                0, toSubtract.getTotalTimeInvalidCalls());
        assertEquals(testCase +
                "toSubtract after operation getTotalTimeFailedCalls",
                toSubtractTime, toSubtract.getTotalTimeFailedCalls());
        
        assertNumbers(testCase, expectedInfo, info);
    }

    /**
     * Get test data for subtraction test.
     * 
     * @return Test data for subtraction test.
     */
    private SubtractTestData[] getSubtractionTestData() {
        SubtractTestData result[] = new SubtractTestData[] {
                // Note that extraCallTime should always be half the value of
                // callTime to ensure all tests will pass.
                new SubtractTestData(4, NUM_PLACE_10, 2),
                new SubtractTestData(12, NUM_PLACE_20, 6),
                new SubtractTestData(22, NUM_PLACE_50, 11),
                new SubtractTestData(52, NUM_PLACE_100, 26),
                new SubtractTestData(102, NUM_PLACE_200, 51),
                new SubtractTestData(202, NUM_PLACE_500, 101),
                new SubtractTestData(502, NUM_PLACE_1000, 251),
                new SubtractTestData(1002, NUM_PLACE_2000, 501),
                new SubtractTestData(2002, NUM_PLACE_5000, 1001),
                new SubtractTestData(5002, NUM_PLACE_10000, 2501),
                new SubtractTestData(10002, NUM_PLACE_20000, 5001),
                new SubtractTestData(20002, NUM_PLACE_OVER_20000, 10001)
        };
        return result;
    }

    
    /**
     * Assert that the createClone method works.
     * 
     * @param expected Expected values, object to clone.
     */
    private void assertCreateClone(TimeInformation expected) {
        TimeInformation actual = expected.createClone();
        assertEquals("getDayOfWeek",
                expected.getDayOfWeek(), actual.getDayOfWeek());
        assertEquals("getHourOfDay",
                expected.getHourOfDay(), actual.getHourOfDay());
        assertSame("getKey",
                expected.getKey(), actual.getKey());
        assertEquals("getNumCorrectCalls",
                expected.getNumCorrectCalls(), actual.getNumCorrectCalls());
        assertEquals("getNumInvalidCalls",
                expected.getNumInvalidCalls(), actual.getNumInvalidCalls());
        assertEquals("getNumFailedCalls",
                expected.getNumFailedCalls(), actual.getNumFailedCalls());
        assertEquals("getTotalTimeCorrectCalls",
                expected.getTotalTimeCorrectCalls(),
                actual.getTotalTimeCorrectCalls());
        assertEquals("getTotalTimeInvalidCalls",
                expected.getTotalTimeInvalidCalls(),
                actual.getTotalTimeInvalidCalls());
        assertEquals("getTotalTimeFailedCalls",
                expected.getTotalTimeFailedCalls(),
                actual.getTotalTimeFailedCalls());
        assertEquals("getNum10", expected.getNum10(), actual.getNum10());
        assertEquals("getNum20", expected.getNum20(), actual.getNum20());
        assertEquals("getNum50", expected.getNum50(), actual.getNum50());
        assertEquals("getNum100", expected.getNum100(), actual.getNum100());
        assertEquals("getNum200", expected.getNum200(), actual.getNum200());
        assertEquals("getNum500", expected.getNum500(), actual.getNum500());
        assertEquals("getNum1000", expected.getNum1000(), actual.getNum1000());
        assertEquals("getNum2000", expected.getNum2000(), actual.getNum2000());
        assertEquals("getNum5000", expected.getNum5000(), actual.getNum5000());
        assertEquals("getNum10000",
                expected.getNum10000(), actual.getNum10000());
        assertEquals("getNum20000",
                expected.getNum20000(), actual.getNum20000());
        assertEquals("getNumOver20000",
                expected.getNumOver20000(), actual.getNumOver20000());
    }

    /**
     * Test data container for time interval tests.
     */
    private static class IntervalTestData {
        /** The callTime. */
        final long callTime;
        /**
         * The place in the expected array where the result is updated.
         * One of the constants NUM_PLACE_X
         */
        final int numPlace;
        
        /**
         * Create this instance
         * 
         * @param callTime The call time to test with.
         * @param numPlace The place in the expected array where the result is
         *                 updated. One of the constants NUM_PLACE_X
         */
        IntervalTestData(long callTime, int numPlace) {
            this.callTime = callTime;
            this.numPlace = numPlace;
        }
    }

    /**
     * Test data container for subtraction test data.
     */
    private static class SubtractTestData {
        /** The callTime. */
        final long callTime;

        /**
         * The place in the expected array where the result is updated.
         * One of the constants NUM_PLACE_X
         */
        final int numPlace;
        
        /**
         * The extra call time that may be added to still be within the
         * interval.
         */
        final long extraCallTime;

        /**
         * Create this instance
         * 
         * @param callTime      The call time to test with.
         * @param numPlace      The place in the expected array where the result
         *                      is updated. One of the constants NUM_PLACE_X
         * @param extraCallTime The extra call time that may be added to still
         *                      be within the the time interval.
         */
        SubtractTestData(long callTime, int numPlace, long extraCallTime) {
            this.callTime = callTime;
            this.numPlace = numPlace;
            this.extraCallTime = extraCallTime;
        }
    }
}
