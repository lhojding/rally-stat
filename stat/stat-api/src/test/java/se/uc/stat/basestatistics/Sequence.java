package se.uc.stat.basestatistics;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Class representing a sequence of expected method calls.
 * Used by {@link BaseCollectorTest} to perform the tests.
 * 
 * @author Anders Persson (konx40)
 */
/* package */ class Sequence {
    /** The method update. */
    /* package */ final static int METHOD_UPDATE = 0;
    /** The method insert. */
    /* package */ final static int METHOD_INSERT = 1;
    /** The result <code>true</code>. */
    /* package */ final static int RESULT_TRUE = 100;
    /** The result <code>false</code>. */
    /* package */ final static int RESULT_FALSE = 101;
    /** The result <code>SQLException</code>. */
    /* package */ final static int RESULT_SQLEXCEPTION = 102;
    
    /** The name of the test case. */
    private final String testCase;
    /** The information this sequence is coupled to. */
    private final BaseInformationImpl info;
    /** The steps in this sequence. */
    private final ArrayList<Step> steps = new ArrayList<Step>();
    /** The expected next step. */
    private int nextStep = 0;
    
    /**
     * Create this class.
     * 
     * @param testCase The name of the test case. Used to get better error
     *                 messages.
     * @param info     The information this sequence is coupled to.
     */
    /* package */ Sequence(String testCase, BaseInformationImpl info) {
        this.testCase = testCase;
        this.info = info;
    }

    /**
     * Add an expected method call.
     * 
     * @param method The expected method, one of the constants METHOD_*.
     * @param result The method result, one of the constants RESULT_*.
     */
    /* package */ void addMethodCall(int method, int result) {
        steps.add(new Step(method, result));
    }

    /**
     * Get the information object for this sequence.
     * 
     * @return The information object for this sequence.
     */
    /* package */ BaseInformationImpl getInfo() {
        return info;
    }

    /**
     * Checks if this sequence has run to the end.
     * 
     * @return <code>true</code> if the sequence has run to the end.
     *         <code>false</code> if the sequence has steps left.
     */
    /* package */ boolean hasRunToEnd() {
        return (nextStep >= steps.size());
    }
    
    /**
     * Called when a call to updateInformation has been performed to assert
     * this call is the expected call.
     * The method fails in a JUnit way if the method is not expected to be
     * called or if other problems are encountered.
     * 
     * @param actualInfo The information object to update.
     * @param actualMethod The method called. One of the constants METHOD_*.
     * 
     * @return The value the method updateInformation is expected to return.
     *  
     * @throws SQLException if the method updateInformation is expected to
     *         throw an SQLException.
     */
    /* package */ boolean methodCall(BaseInformationImpl actualInfo,
            int actualMethod) throws SQLException {
        assertEquals(testCase + "Invalid key. This is a programming error " +
        		"in the test class.", info.getKey(), actualInfo.getKey());
        assertTrue(testCase + ". No more calls were expected. " + nextStep +
                " calls has been done.", nextStep < steps.size());
        assertTrue(testCase + ". The info object has not been copied. " +
                "It is the same object instance.", info != actualInfo);
        assertEquals(testCase + ". Invalid number of correct calls.",
                info.getNumCorrectCalls(), actualInfo.getNumCorrectCalls());
        assertEquals(testCase + ". Invalid number of invalid calls.",
                info.getNumInvalidCalls(), actualInfo.getNumInvalidCalls());
        assertEquals(testCase + ". Invalid number of failed calls.",
                info.getNumFailedCalls(), actualInfo.getNumFailedCalls());
        assertEquals(testCase + ". Invalid day of week.",
                info.getDayOfWeek(), actualInfo.getDayOfWeek());
        
        final Step step = steps.get(nextStep);
        if (actualMethod == METHOD_UPDATE) {
            assertEquals(testCase + ". The method updateInformation is not " +
                    "expected to be called", METHOD_UPDATE, step.method);
        } else if (actualMethod == METHOD_INSERT) {
            assertEquals(testCase + ". The method insertInformation is not " +
                    "expected to be called", METHOD_INSERT, step.method);
        } else {
            fail("The method given as actual is invalid. This is an error in " +
                  "the test class" + actualMethod);
        }
        nextStep++;
        if (step.result == RESULT_TRUE) {
            return true;
        } else if (step.result == RESULT_FALSE) {
            return false;
        } else if (step.result == RESULT_SQLEXCEPTION) {
            throw new SQLException("updateInformation fails with " +
                    "SQLException. This is triggered by the test case and " +
                    "is expected and correct behavious.");
        }
        fail(testCase + ". Invalid expected result. This is a programming " +
                "error in the test class " + step.result);
     // This will never happens, but the compiler does not realize that
        return true; 
    }
    
    /**
     * Class representing one step in the sequence.
     */
    private static class Step {
        /** The expected method, one of the constants METHOD_*. */
        /* package */ final int method;
        /**The method result, one of the constants RESULT_*. */
        /* package */ final int result;
        
        /**
         * Create this instance.
         * 
         * @param method The expected method, one of the constants METHOD_*.
         * @param result The method result, one of the constants RESULT_*.
         */
        /* package */ Step(int method, int result) {
            this.method = method;
            this.result = result;
        }
    }
}
