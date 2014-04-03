package se.uc.stat.basestatistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import se.uc.stat.utils.AbstractTestBase;

/**
 * Test class for BaseCollector.
 * 
 * @author Anders Persson (konx40)
 */
public class BaseCollectorTest extends AbstractTestBase {
    /** Store interval used in most tests. */
    private final long STORE_INTERVAL = 100000;
    
    /**
     * Test the constructor and the put and get methods.
     */
    @Test
    public void testPutGet() {
        final long minTime = System.currentTimeMillis();
        BaseCollectorImpl collector = new BaseCollectorImpl(STORE_INTERVAL, 10);
        final long maxTime = System.currentTimeMillis();
        assertEquals("invalid initial cache size", 0, collector.getCacheSize());
        assertEquals("invalid initial storeList size",
                0, collector.getStoreListSize());
        assertNextStore("nextStore is not within expected interval",
                collector, minTime, maxTime, STORE_INTERVAL);

        final BaseKeyImpl key1 = new BaseKeyImpl("s1", "m1", "o1", "p1", "me1");
        final BaseInformationImpl info1 = new BaseInformationImpl(key1, 3);
        collector.put(info1);
        assertEquals("invalid size of cache after one put",
                1, collector.getCacheSize());

        final BaseKeyImpl key2 = new BaseKeyImpl("s1", "m1", "o1", "p1", "me2");
        final BaseInformationImpl info2 = new BaseInformationImpl(key2, 3);
        collector.put(info2);
        assertEquals("invalid size of cache after two put",
                2, collector.getCacheSize());

        final BaseKeyImpl key3 = new BaseKeyImpl("s1", "m1", "o1", "p1", "me3");
        final BaseInformationImpl info3 = new BaseInformationImpl(key3, 3);
        collector.put(info3);
        assertEquals("invalid size of cache after three put",
                3, collector.getCacheSize());

        assertSame("info1 could not be retrieved correct",
                info1, collector.get(key1));
        assertSame("info2 could not be retrieved correct",
                info2, collector.get(key2));
        assertSame("info3 could not be retrieved correct",
                info3, collector.get(key3));

        assertEquals("Invalid cache size after gets",
                3, collector.getCacheSize());
        assertEquals("Invalid storeList size after gets",
                0, collector.getStoreListSize());

        assertNextStore("nextStore is not within expected interval",
                collector, minTime, maxTime, STORE_INTERVAL);
    }
    
    /**
     * Test the allowStore method, no failures. Test the storing part
     * with different scenarios.
     * <p/>
     * See {@link #testAllowStoreTimes()} and
     * {@link #testAllowStoreFailures()} for more test cases.
     * 
     * @throws InterruptedException if the test fails.
     */
    @Test
    public void testAllowStoreCorrect() throws InterruptedException {
        final long INTERVAL = 200;
        BaseCollectorImpl collector = new BaseCollectorImpl(INTERVAL, 10);
        final long startTime = System.currentTimeMillis();
        addCorrectTestData(collector);
        assertEquals("The cache is not correctly populated before allowStore",
                3, collector.getCacheSize());
        assertEquals("The number of sequences are not correctly " +
                "populated before allowStore",
                3, collector.getNumSequences());
        collector.allowStore();
        assertEquals("Calling allowStore before it is time affected cache",
                3, collector.getCacheSize());
        assertEquals("Calling allowStore before it is time affected sequences",
                3, collector.getNumSequences());
        
        while (System.currentTimeMillis() <= startTime + INTERVAL) {
            Thread.sleep(10);
        }
        final long minTime = System.currentTimeMillis();
        collector.allowStore();
        final long maxTime = System.currentTimeMillis();
        assertNextStore("nextStore is not within expected interval " +
                "after allowStore", collector, minTime, maxTime, INTERVAL);
        assertEquals("The cache is not empty after allowStore",
                0, collector.getCacheSize());
        assertEquals("The storeList is not empty after allowStore",
                0, collector.getStoreListSize());
        assertEquals("The number of sequences are not correct after allowStore",
                0, collector.getNumSequences());
    }

    /**
     * Test the flush method, no failures.
     */
    @Test
    public void testFlushCorrect() {
        // Long time between stores means no looping within the flush method.
        BaseCollectorImpl collector = new BaseCollectorImpl(STORE_INTERVAL, 2);
        addCorrectTestData(collector);
        assertEquals("The cache is not correctly populated before flush",
                3, collector.getCacheSize());
        assertEquals("The number of sequences are not correctly " +
                "populated before flush",
                3, collector.getNumSequences());
        final long minTime = System.currentTimeMillis();
        collector.flush();
        final long maxTime = System.currentTimeMillis();
        assertNextStore("nextStore is not within expected interval after flush",
                collector, minTime, maxTime, STORE_INTERVAL);
        assertEquals("The cache is not empty after flush",
                0, collector.getCacheSize());
        assertEquals("The storeList is not empty after flush",
                0, collector.getStoreListSize());
        assertEquals("The number of sequences are not correct after flush",
                0, collector.getNumSequences());

        // Short time between stores means loops within the flush method.
        collector = new BaseCollectorImpl(1, 2);
        addCorrectTestData(collector);
        assertEquals("The cache is not correctly populated before flush",
                3, collector.getCacheSize());
        assertEquals("The number of sequences are not correctly " +
                "populated before flush",
                3, collector.getNumSequences());
        collector.flush();
        assertEquals("The cache is not empty after flush",
                0, collector.getCacheSize());
        assertEquals("The storeList is not empty after flush",
                0, collector.getStoreListSize());
        assertEquals("The number of sequences are not correct after flush",
                0, collector.getNumSequences());
    }

    /**
     * Add testdata, all three correct cases and the corresponding sequences.
     * 
     * @param collector The collector to populate. 
     */
    private void addCorrectTestData(BaseCollectorImpl collector) {
        int numCalls = 1;
        
        // update true
        final BaseKeyImpl key1 = new BaseKeyImpl("s1", "m1", "o1", "p1", "me1");
        final BaseInformationImpl info1 = new BaseInformationImpl(key1, 3);
        collector.put(info1);
        info1.registerCorrectCalls(numCalls++);
        info1.registerInvalidCalls(numCalls++);
        info1.registerFailedCalls(numCalls++);
        Sequence sequence1 = new Sequence("update true", info1);
        sequence1.addMethodCall(Sequence.METHOD_UPDATE, Sequence.RESULT_TRUE);
        collector.addSequence(sequence1);
        
        // update false, insert true
        final BaseKeyImpl key2 = new BaseKeyImpl("s2", "m1", "o1", "p1", "me1");
        final BaseInformationImpl info2 = new BaseInformationImpl(key2, 5);
        collector.put(info2);
        info2.registerCorrectCalls(numCalls++);
        info2.registerInvalidCalls(numCalls++);
        info2.registerFailedCalls(numCalls++);
        Sequence sequence2 = new Sequence("update false, insert true", info2);
        sequence2.addMethodCall(Sequence.METHOD_UPDATE, Sequence.RESULT_FALSE);
        sequence2.addMethodCall(Sequence.METHOD_INSERT, Sequence.RESULT_TRUE);
        collector.addSequence(sequence2);
        
        // update false, insert false, update true
        final BaseKeyImpl key3 = new BaseKeyImpl("s3", "m1", "o1", "p1", "me1");
        final BaseInformationImpl info3 = new BaseInformationImpl(key3, 3);
        collector.put(info3);
        info3.registerCorrectCalls(numCalls++);
        info3.registerInvalidCalls(numCalls++);
        info3.registerFailedCalls(numCalls++);
        Sequence sequence3 = new Sequence(
                "update false, insert false, update true", info3);
        sequence3.addMethodCall(Sequence.METHOD_UPDATE, Sequence.RESULT_FALSE);
        sequence3.addMethodCall(Sequence.METHOD_INSERT, Sequence.RESULT_FALSE);
        sequence3.addMethodCall(Sequence.METHOD_UPDATE, Sequence.RESULT_TRUE);
        collector.addSequence(sequence3);
    }
    
    /**
     * Test the allowStore method, failures. Test the storing part
     * with different scenarios.
     * <p/>
     * See {@link #testAllowStoreTimes()} and
     * {@link #testAllowStoreFailures()} for more test cases.
     * 
     * @throws InterruptedException if the test fails.
     */
    @Test
    public void testAllowStoreFailures() throws InterruptedException {
        final int INITIAL_NUM_CASES = 1000;
        final long INTERVAL = 500;
        for (int testNum = 0; testNum < 4; testNum++) {
            BaseCollectorImpl collector =
                    new BaseCollectorImpl(INTERVAL, 500);
            long startTime = System.currentTimeMillis();
            addFailingTestData(collector, testNum);
            assertEquals("The cache is not correctly populated before " +
                    "allowStore", INITIAL_NUM_CASES, collector.getCacheSize());
            assertEquals("The number of sequences are not correctly " +
                    "populated before allowStore",
                    INITIAL_NUM_CASES, collector.getNumSequences());
            while (System.currentTimeMillis() <= startTime + INTERVAL) {
                Thread.sleep(10);
            }
            long previousMinTime = System.currentTimeMillis();
            long minTime = System.currentTimeMillis();
            long maxTime;
            int lastStoreListSize;
            do {
                lastStoreListSize = collector.getStoreListSize();
                previousMinTime = minTime;
                minTime = System.currentTimeMillis();
                collector.allowStore();
                maxTime = System.currentTimeMillis();
            } while (lastStoreListSize != collector.getStoreListSize());
            assertNextStore("nextStore is not within expected interval after " +
                    "allowStore", collector, previousMinTime, maxTime, INTERVAL);
            assertTrue("At least one sequence is expected to be run",
                    collector.getNumSequences() < INITIAL_NUM_CASES);
            // May happen if the failing case accidently comes last in the 
            // storeList.
            assertTrue("All sequence are not expected to be run fully. " +
                    "There is however a small statistical possibility that " +
                    "this may happen even if there is no failure in the code.",
                    collector.getNumSequences() > 0);
            // Note that the failing case should not be deleted even if the
            // the sequence has run correctly.
            assertEquals("The cache has not lost the correct number of " +
                    "objects.", collector.getNumSequences() + 1,
                    collector.getCacheSize());
            assertEquals("The storeList is not empty after allowStore.",
                    0, collector.getStoreListSize());
        }
    }
    
    /**
     * Test the flush method, failures.
     */
    @Test
    public void testFlushFailures() {
        final int INITIAL_NUM_CASES = 1000;
        final long INTERVAL = INITIAL_NUM_CASES * 10;
        for (int testNum = 0; testNum < 4; testNum++) {
            BaseCollectorImpl collector =
                    new BaseCollectorImpl(INTERVAL, 2);
            addFailingTestData(collector, testNum);
            assertEquals("The cache is not correctly populated before flush",
                    INITIAL_NUM_CASES, collector.getCacheSize());
            assertEquals("The number of sequences are not correctly " +
                    "populated before flush",
                    INITIAL_NUM_CASES, collector.getNumSequences());
            final long minTime = System.currentTimeMillis();
            collector.flush();
            final long maxTime = System.currentTimeMillis();
            assertNextStore("nextStore is not within expected interval " +
                    "after flush", collector, minTime, maxTime, INTERVAL);
            assertTrue("At least one sequence is expected to be run",
                    collector.getNumSequences() < INITIAL_NUM_CASES);
            // May happen if the failing case accidently comes last in the 
            // storeList.
            assertTrue("All sequence are not expected to be run fully. " +
                    "There is however a small statistical possibility that " +
                    "this may happen even if there is no failure in the code.",
                    collector.getNumSequences() > 0);
            // Note that the failing case should not be deleted even if the
            // the sequence has run correctly.
            assertEquals("The cache has not lost the correct number of " +
                    "objects.", collector.getNumSequences() + 1,
                    collector.getCacheSize());
            assertEquals("The storeList is not empty after flush.",
                    0, collector.getStoreListSize());
        }
    }

    /**
     * Add testdata, one of the four failing cases and the corresponding
     * sequences.
     * Also adds 999 correct test data to make it unlikely that the failing
     * test data will be the last in the storeList.
     * The total number of test objects will be 1000.
     * 
     * @param collector The collector to populate. 
     * @param testNum   Integer 0-3 to populate with one of the four failing
     *                  test cases.
     */
    private void addFailingTestData(BaseCollectorImpl collector, int testNum) {
        int numCalls = 1;
        
        switch(testNum) {
        case 0:
            // update throws SQLException
            final BaseKeyImpl key1 = new BaseKeyImpl(
                    "s1", "m1", "o1", "p1", "me1");
            final BaseInformationImpl info1 = new BaseInformationImpl(key1, 3);
            collector.put(info1);
            info1.registerCorrectCalls(numCalls++);
            info1.registerInvalidCalls(numCalls++);
            info1.registerFailedCalls(numCalls++);
            Sequence sequence1 = new Sequence("update SQLException", info1);
            sequence1.addMethodCall(Sequence.METHOD_UPDATE,
                    Sequence.RESULT_SQLEXCEPTION);
            collector.addSequence(sequence1);
            break;
        case 1:
            // update false, insert throws SQLException
            final BaseKeyImpl key2 = new BaseKeyImpl(
                    "s2", "m1", "o1", "p1", "me1");
            final BaseInformationImpl info2 = new BaseInformationImpl(key2, 5);
            collector.put(info2);
            info2.registerCorrectCalls(numCalls++);
            info2.registerInvalidCalls(numCalls++);
            info2.registerFailedCalls(numCalls++);
            Sequence sequence2 = new Sequence(
                    "update false, insert SQLException", info2);
            sequence2.addMethodCall(Sequence.METHOD_UPDATE, 
                    Sequence.RESULT_FALSE);
            sequence2.addMethodCall(Sequence.METHOD_INSERT,
                    Sequence.RESULT_SQLEXCEPTION);
            collector.addSequence(sequence2);
            break;
        case 2:
            // update false, insert false, update throws SQLException
            final BaseKeyImpl key3 = new BaseKeyImpl(
                    "s3", "m1", "o1", "p1", "me1");
            final BaseInformationImpl info3 = new BaseInformationImpl(key3, 3);
            collector.put(info3);
            info3.registerCorrectCalls(numCalls++);
            info3.registerInvalidCalls(numCalls++);
            info3.registerFailedCalls(numCalls++);
            Sequence sequence3 = new Sequence(
                    "update false, insert false, update SQLException", info3);
            sequence3.addMethodCall(Sequence.METHOD_UPDATE, 
                    Sequence.RESULT_FALSE);
            sequence3.addMethodCall(Sequence.METHOD_INSERT, 
                    Sequence.RESULT_FALSE);
            sequence3.addMethodCall(Sequence.METHOD_UPDATE,
                    Sequence.RESULT_SQLEXCEPTION);
            collector.addSequence(sequence3);
            break;
        case 3:
            // update false, insert false, update false
            final BaseKeyImpl key4 = new BaseKeyImpl(
                    "s4", "m1", "o1", "p1", "me1");
            final BaseInformationImpl info4 = new BaseInformationImpl(key4, 3);
            collector.put(info4);
            info4.registerCorrectCalls(numCalls++);
            info4.registerInvalidCalls(numCalls++);
            info4.registerFailedCalls(numCalls++);
            Sequence sequence4 = new Sequence(
                    "update false, insert false, update false", info4);
            sequence4.addMethodCall(Sequence.METHOD_UPDATE, 
                    Sequence.RESULT_FALSE);
            sequence4.addMethodCall(Sequence.METHOD_INSERT, 
                    Sequence.RESULT_FALSE);
            sequence4.addMethodCall(Sequence.METHOD_UPDATE, 
                    Sequence.RESULT_FALSE);
            collector.addSequence(sequence4);
            break;
        default:
            fail("invalid test number");
        }
        // Add correct tests to make it unlikely that the failing one is the
        // last in the storeList.
        for (int extraNum = 0; extraNum < 999; extraNum++) {
            final BaseKeyImpl key = new BaseKeyImpl(
                    "sextra", "m" + extraNum, "o1", "p1", "me1");
            final BaseInformationImpl info = new BaseInformationImpl(key, 3);
            collector.put(info);
            info.registerCorrectCalls(numCalls++);
            info.registerInvalidCalls(numCalls++);
            info.registerFailedCalls(numCalls++);
            Sequence sequence = new Sequence(
                    "update true", info);
            sequence.addMethodCall(Sequence.METHOD_UPDATE,
                    Sequence.RESULT_TRUE);
            collector.addSequence(sequence);
        }
    }

    /**
     * Test the allowStore method, timing and what happens with updates while
     * only some objects has been stored.
     * <p/>
     * See {@link #testAllowStoreCorrect()} and
     * {@link #testAllowStoreFailures()} for more test cases.
     * 
     * @throws InterruptedException if the test fails.
     */
    @Test
    public void testAllowStoreTimes() throws InterruptedException {
        final long INTERVAL = 2000;
        int numCalls = 1;

        BaseCollectorImpl collector = new BaseCollectorImpl(INTERVAL, 2);
        long startTime = System.currentTimeMillis();

        final BaseKeyImpl key1 = new BaseKeyImpl("s1", "m1", "o1", "p1", "me1");
        final BaseInformationImpl info1 = new BaseInformationImpl(key1, 3);
        collector.put(info1);
        info1.registerCorrectCalls(numCalls++);
        info1.registerInvalidCalls(numCalls++);
        info1.registerFailedCalls(numCalls++);
        Sequence sequence1 = new Sequence("info 1", info1.createClone());
        sequence1.addMethodCall(Sequence.METHOD_UPDATE, Sequence.RESULT_TRUE);
        collector.addSequence(sequence1);
        
        final BaseKeyImpl key2 = new BaseKeyImpl("s2", "m1", "o1", "p1", "me1");
        final BaseInformationImpl info2 = new BaseInformationImpl(key2, 3);
        collector.put(info2);
        info2.registerCorrectCalls(numCalls++);
        info2.registerInvalidCalls(numCalls++);
        info2.registerFailedCalls(numCalls++);
        Sequence sequence2 = new Sequence("info 2", info2.createClone());
        sequence2.addMethodCall(Sequence.METHOD_UPDATE, Sequence.RESULT_TRUE);
        collector.addSequence(sequence2);
        
        final BaseKeyImpl key3 = new BaseKeyImpl("s3", "m1", "o1", "p1", "me1");
        final BaseInformationImpl info3 = new BaseInformationImpl(key3, 3);
        collector.put(info3);
        info3.registerCorrectCalls(numCalls++);
        info3.registerInvalidCalls(numCalls++);
        info3.registerFailedCalls(numCalls++);
        Sequence sequence3 = new Sequence("info 3", info3.createClone());
        sequence3.addMethodCall(Sequence.METHOD_UPDATE, Sequence.RESULT_TRUE);
        collector.addSequence(sequence3);

        assertEquals("The cache is not correctly populated before first " +
                "allowStore", 3, collector.getCacheSize());
        assertEquals("The number of sequences are not correctly " +
                "populated before allowStore",
                3, collector.getNumSequences());
        collector.allowStore();
        assertEquals("Calling allowStore before it is time affected cache",
                3, collector.getCacheSize());
        assertEquals("Calling allowStore before it is time affected sequences",
                3, collector.getNumSequences());
        
        while (System.currentTimeMillis() <= startTime + INTERVAL) {
            Thread.sleep(10);
        }
        // Perform first allowStore. This should store 2 of 3 objects.
        collector.allowStore();
        assertEquals("The cache is not correctly after first allowStore",
                1, collector.getCacheSize());
        assertEquals("The number of sequences are not correctly " +
                "after first allowStore",
                1, collector.getNumSequences());
        
        // Perform updates in cache between the two calls to allowStore.
        //
        // The object that has not been stored will have the numbers
        // increased.
        //
        // The two objects that has been stored will be added again.
        // They will have the keys key4 and key5 because we do not know
        // which two of the three previously added objects that has already
        // been stored and which has not been stored.
        BaseInformationImpl notStoredInfo;
        BaseKeyImpl key4;
        BaseKeyImpl key5;
        if (collector.get(key1) != null) {
            notStoredInfo = info1;
            key4 = key2;
            key5 = key3;
        } else if (collector.get(key2) != null) {
            notStoredInfo = info2;
            key4 = key1;
            key5 = key3;
        } else if (collector.get(key3) != null) {
            notStoredInfo = info3;
            key4 = key1;
            key5 = key2;
        } else {
            fail("Neither of the keys are in the cache");
            return; // Never happens, but the compiler does not know that.
        }
        final BaseInformationImpl info4 = new BaseInformationImpl(key4, 3);
        final BaseInformationImpl info5 = new BaseInformationImpl(key5, 3);
        final BaseInformationImpl notStoredSecondSequenceInfo =
                new BaseInformationImpl(notStoredInfo.getKey(), 3);
        collector.put(info4);
        collector.put(info5);
        notStoredSecondSequenceInfo.registerCorrectCalls(numCalls);
        notStoredInfo.registerCorrectCalls(numCalls++);
        notStoredSecondSequenceInfo.registerInvalidCalls(numCalls);
        notStoredInfo.registerInvalidCalls(numCalls++);
        notStoredSecondSequenceInfo.registerFailedCalls(numCalls);
        notStoredInfo.registerFailedCalls(numCalls++);
        info4.registerCorrectCalls(numCalls++);
        info4.registerInvalidCalls(numCalls++);
        info4.registerFailedCalls(numCalls++);
        info5.registerCorrectCalls(numCalls++);
        info5.registerInvalidCalls(numCalls++);
        info5.registerFailedCalls(numCalls++);
        // Note that these updates should not affect the current storing
        // operation (but the next one).
        
        // Perform second allowStore. This should store the last of 3 objects.
        final long minTime = System.currentTimeMillis();
        collector.allowStore();
        final long maxTime = System.currentTimeMillis();
        startTime = System.currentTimeMillis();
        // Note that updates has been performed.
        assertEquals("The cache is not correctly after second allowStore",
                3, collector.getCacheSize());
        assertEquals("The number of sequences are not correctly " +
                "after second allowStore",
                0, collector.getNumSequences());
        assertNextStore("nextStore is not within expected interval " +
                "after allowStore", collector, minTime,
                maxTime, INTERVAL);

        // Prepare the sequences for the second store.
        Sequence sequence4 = new Sequence("info 4", info4);
        sequence4.addMethodCall(Sequence.METHOD_UPDATE, Sequence.RESULT_TRUE);
        collector.addSequence(sequence4);
        Sequence sequence5 = new Sequence("info 5", info5);
        sequence5.addMethodCall(Sequence.METHOD_UPDATE, Sequence.RESULT_TRUE);
        collector.addSequence(sequence5);
        Sequence sequenceNotStored = new Sequence("info not stored",
                notStoredSecondSequenceInfo);
        sequenceNotStored.addMethodCall(Sequence.METHOD_UPDATE,
                Sequence.RESULT_TRUE);
        collector.addSequence(sequenceNotStored);
        
        while (System.currentTimeMillis() <= startTime + INTERVAL) {
            Thread.sleep(10);
        }
        // Perform third allowStore (first in "second round").
        // This should store 2 of 3 objects 
        collector.allowStore();
        assertEquals("The cache is not correctly after first allowStore",
                1, collector.getCacheSize());
        assertEquals("The number of sequences are not correctly " +
                "after first allowStore",
                1, collector.getNumSequences());

        // Perform fourth allowStore (second in "second round").
        // This should store the last of 3 objects. 
        collector.allowStore();
        assertEquals("The cache is not empty after allowStore",
                0, collector.getCacheSize());
        assertEquals("The storeList is not empty after allowStore",
                0, collector.getStoreListSize());
        assertEquals("The number of sequences are not correct after allowStore",
                0, collector.getNumSequences());
    }

    /**
     * Assert that the <code>nextStore</code> time is within the expected interval.
     * 
     * @param testCase  The name of the test case.
     * @param collector The collector
     * @param minTime   The minimum time (taken before the call updating the
     *                  <code>nextStore</code>).
     * @param maxTime   The maximum time (taken after the call updating the
     *                  <code>nextStore</code>).
     * @param interval  The current interval.
     */
    private void assertNextStore(String testCase, BaseCollectorImpl collector,
            long minTime, long maxTime, long interval) {
        final long nextStore = collector.getNextStore();
        String message = testCase + ". The value of nextStore=" + nextStore +
                " is not within the expected interval " + (minTime + interval) +
                " to " + (maxTime + interval) + ". interval=" + interval;
        assertTrue(message, nextStore >= (minTime + interval) &&
                nextStore <= (maxTime + interval));
    }
}
