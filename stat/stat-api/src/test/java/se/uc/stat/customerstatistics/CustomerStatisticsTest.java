package se.uc.stat.customerstatistics;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Test;

import se.uc.stat.utils.TimeRepresentation;
import se.uc.stat.utils.TimeSource;
import se.uc.stat.utils.TimeUtils;

/**
 * Test the CustomerStatistics class.
 * This test is intended to be run manually for longer time periods (an hour)
 * to get an idea of the thread safety.
 * 
 * @author Anders Persson (konx40)
 */
public class CustomerStatisticsTest extends CustomerTestBase {
    /** The number of threads to use. */
    private static final int NUM_THREADS = 10;
    /** The run time in milliseconds. */
    private static final long RUN_TIME = 10000;
    
    /** Test keys. Note that the time is not of interest. */
    private final ArrayList<CustomerStatisticsKey> keys =
            new ArrayList<CustomerStatisticsKey>();
    /** The registered statistics. */
    private final HashMap<CustomerStatisticsKey, CustomerInformation> infos =
            new HashMap<CustomerStatisticsKey, CustomerInformation>();
    /** Lock object for accessing infos and to update objects within the map. */
    private final Object infosLock = new Object();
    
    /**
     * Test the healthCheck method.
     * 
     * @throws SQLException if the test fails.
     */
    @Test
    public void testHealthCheck() throws SQLException {
        CustomerStatistics.healthCheck();
    }
    
    /**
     * Perform a test using the number of threads specified by
     * <code>NUM_THREADS</code> running for <code>RUN_TIME</code> milliseconds.
     * Evaluate the result, each record in the database.
     * 
     * @throws SQLException if the test fails.
     * @throws InterruptedException if the test fails.
     */
    @Test
    public void performTest() throws SQLException, InterruptedException {
        int totalNumCorrect = 0;
        int totalNumInvalid = 0;
        int totalNumFailed = 0;
        cleanCustomerStatDb();
        setupTestKeys();
        assertTrue("Not enough test keys are added", keys.size() > 50);
        final long stopTime = System.currentTimeMillis() + RUN_TIME;
        AddStat threads[] = new AddStat[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new AddStat(stopTime, keys, infos, infosLock);
            threads[i].start();
        }
        // Threads are working.
        boolean failingThread = false;
        for (int i = 0; i < NUM_THREADS; i++) {
            AddStat thread = threads[i];
            thread.join();
            if (thread.throwable != null) {
                failingThread = true;
            }
            totalNumCorrect += thread.totalNumCorrect;
            totalNumInvalid += thread.totalNumInvalid;
            totalNumFailed += thread.totalNumFailed;
        }
        assertFalse("Thread failed with exception", failingThread);
        CustomerStatistics.flush();
        assertTrue("The total number of correct calls are 0, bad test data",
                totalNumCorrect > 0);
        assertTrue("The total number of invalid calls are 0, bad test data",
                totalNumInvalid > 0);
        assertTrue("The total number of failed calls are 0, bad test data",
                totalNumFailed > 0);
        assertTrue("Not enough information objects are added",
                infos.size() > 50);
        for (CustomerInformation info : infos.values()) {
            this.assertInfoInDb(info.getKey().toString(), info);
        }
        cleanCustomerStatDb();
    }
    
    /**
     * Set the test keys.
     */
    private void setupTestKeys() {
        for (int methodId = 0; methodId < 2; methodId++) {
            for (int originId = 0; originId < 2; originId++) {
                for (int productId = 0; productId < 2; productId++) {
                    for (int mediaId = 0; mediaId < 2; mediaId++) {
                        for (int customerId = 0; customerId < 2; customerId++) {
                            CustomerStatisticsKey key =
                                new CustomerStatisticsKey(SERVICE,
                                "m" + methodId,
                                ORIGIN_PREFIX + originId,
                                "p" + productId,
                                MEDIA_PREFIX + mediaId,
                                "c" + customerId,
                                new Date());
                            keys.add(key);
                            key = new CustomerStatisticsKey("s" + methodId,
                                METHOD,
                                ORIGIN_PREFIX + originId,
                                "p" + productId,
                                MEDIA_PREFIX + mediaId,
                                "c" + customerId,
                                new Date());
                            keys.add(key);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Process to concurrently add statistics to get some sort of thread
     * safety check.
     */
    private static class AddStat extends Thread {
        /** The time to stop running this thread. */
        private final long stopTime;
        /** The keys to use in this thread. */
        private final ArrayList<CustomerStatisticsKey> keys;
        /**
         * The info objects to update. Note that the updates will require
         * a lock on infosLock
         */
        private final HashMap<CustomerStatisticsKey, CustomerInformation> infos;
        /**
         * Lock object for accessing infos and to update objects within the map.
         */
        private final Object infosLock;
        /** The total number of correct calls in this thread. */
        /* package */ int totalNumCorrect = 0;
        /** The total number of invalid calls in this thread. */
        /* package */ int totalNumInvalid = 0;
        /** The total number of failed calls in this thread. */
        /* package */ int totalNumFailed = 0;
        /** The exception if this thread fails. */
        /* package */ Throwable throwable = null;

        /**
         * Create this instance.
         * 
         * @param stopTime  The time to stop executing.
         * @param keys      The keys to use.
         * @param infos     The info objects to update.
         * @param infosLock The lock object require to hold when updating
         *                  <code>infos</code>.
         */
        /* package */ AddStat(long stopTime,
                ArrayList<CustomerStatisticsKey> keys,
                HashMap<CustomerStatisticsKey, CustomerInformation> infos,
                Object infosLock) {
            this.stopTime = stopTime;
            this.keys = keys;
            this.infos = infos;
            this.infosLock = infosLock;
        }
        
        /**
         * Perform the work for the specified time.
         */
        @Override
        public void run() {
            try {
                while (System.currentTimeMillis() < stopTime) {
                    for (CustomerStatisticsKey key : keys) {
                        final double choice = Math.random() * 3;
                        synchronized(infosLock) {
                            CustomerInformation info = getInfo(key);
                            if (choice < 1) {
                                info.registerCorrectCall();
                            } else if (choice < 2) {
                                info.registerInvalidCall();
                            } else {
                                info.registerFailedCall();
                            }
                        }
                        if (choice < 1) {
                            totalNumCorrect++;
                            CustomerStatistics.registerCorrectServiceCall(
                                    key.getService(), key.getMethod(),
                                    key.getOrigin(), key.getProduct(),
                                    key.getMedia(), key.getCustomer());
                        } else if (choice < 2) {
                            totalNumInvalid++;
                            CustomerStatistics.registerInvalidServiceCall(
                                    key.getService(), key.getMethod(),
                                    key.getOrigin(), key.getProduct(),
                                    key.getMedia(), key.getCustomer());
                        } else {
                            totalNumFailed++;
                            CustomerStatistics.registerFailedServiceCall(
                                    key.getService(), key.getMethod(),
                                    key.getOrigin(), key.getProduct(),
                                    key.getMedia(), key.getCustomer());
                        }
                    }
                }
            } catch (Throwable t) {
                throwable = t;
                t.printStackTrace();
            }
        }

        /**
         * Get the info object for the given key. Observe that this time also
         * wait if it is near an hour break and that it use a key with a correct
         * time even if the inputed key does not have a correct time.
         * <p/>
         * This method have to be called when a lock is held on
         * <code>infosLock</code>.
         * 
         * @param originKey The original key to use.
         * 
         * @return The information object to use.
         */
        private CustomerInformation getInfo(CustomerStatisticsKey originKey) {
            TimeSource.haltNearHour();
            TimeRepresentation time = TimeUtils.getTimeRepresentation(
                    System.currentTimeMillis());
            final CustomerStatisticsKey key = new CustomerStatisticsKey(
                    originKey.getService(), originKey.getMethod(),
                    originKey.getOrigin(), originKey.getProduct(),
                    originKey.getMedia(), originKey.getCustomer(),
                    new Date(time.getDate()));
            CustomerInformation info = infos.get(key);
            if (info == null) {
                info = new CustomerInformation(key, time.getDayOfWeek());
                infos.put(key, info);
            }
            return info;
        }
    }
}
