package se.uc.stat.customerstatistics;

import java.util.Date;

import se.uc.stat.utils.AbstractTestBase;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test the CustomerStatisticsKey class.
 * 
 * @author Anders Persson (konx40)
 */
public class CustomerStatisticsKeyTest extends AbstractTestBase {
    /**
     * Test the constructor and the get and toString methods.
     */
    @Test
    public void testConstructorGetToString() {
        Date date = new Date();
        CustomerStatisticsKey key = new CustomerStatisticsKey("s1", "m1", "o1",
                "p1", "me1", "c1", date);
        assertEquals("Test 1, invalid getService",
                "s1", key.getService());
        assertEquals("Test 1, invalid getMethod",
                "m1", key.getMethod());
        assertEquals("Test 1, invalid getOrigin",
                "o1", key.getOrigin());
        assertEquals("Test 1, invalid getProduct",
                "p1", key.getProduct());
        assertEquals("Test 1, invalid getMedia",
                "me1", key.getMedia());
        assertEquals("Test 1, invalid getCustomer",
                "c1", key.getCustomer());
        assertEquals("Test 1, invalid getDate",
                date, key.getDate());
        assertEquals("Test 1, invalid toString", "{service=s1, method=m1, " +
        		"origin=o1, product=p1, media=me1, customer=c1, date=" +
        		date + "}", key.toString());

        key = new CustomerStatisticsKey(null, "m2", null, "p2", null, "c2",
                date);
        assertEquals("Test 2, invalid getService",
                null, key.getService());
        assertEquals("Test 2, invalid getMethod",
                "m2", key.getMethod());
        assertEquals("Test 2, invalid getOrigin",
                null, key.getOrigin());
        assertEquals("Test 2, invalid getProduct",
                "p2", key.getProduct());
        assertEquals("Test 2, invalid getMedia",
                null, key.getMedia());
        assertEquals("Test 2, invalid getCustomer",
                "c2", key.getCustomer());
        assertEquals("Test 2, invalid getDate",
                date, key.getDate());
        assertEquals("Test 2, invalid toString", "{service=null, method=m2, " +
                "origin=null, product=p2, media=null, customer=c2, date=" +
                date + "}", key.toString());

        key = new CustomerStatisticsKey("s3", null, "o3",
                null, "me3", null, date);
        assertEquals("Test 3, invalid getService",
                "s3", key.getService());
        assertEquals("Test 3, invalid getMethod",
                null, key.getMethod());
        assertEquals("Test 3, invalid getOrigin",
                "o3", key.getOrigin());
        assertEquals("Test 3, invalid getProduct",
                null, key.getProduct());
        assertEquals("Test 3, invalid getMedia",
                "me3", key.getMedia());
        assertEquals("Test 3, invalid getCustomer",
                null, key.getCustomer());
        assertEquals("Test 3, invalid getDate",
                date, key.getDate());
        assertEquals("Test 3, invalid toString", "{service=s3, method=null, " +
                "origin=o3, product=null, media=me3, customer=null, date=" +
                date + "}", key.toString());
        
        try {
            key = new CustomerStatisticsKey("s1", "m1", "o1", "p1", "me1", "c1",
                    null);
            fail("null date did not throw exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test the equals and hashCode methods.
     */
    @Test
    public void testEqualsHashCode() {
        Date date1 = new Date(System.currentTimeMillis() - 1000);
        Date date2 = new Date(System.currentTimeMillis());
        CustomerStatisticsKey keys1[] = {
                new CustomerStatisticsKey(
                        "s1", "m1", "o1", "p1", "me1", "c1", date1),
                new CustomerStatisticsKey(
                        "s2", "m1", "o1", "p1", "me1", "c1", date1),
                new CustomerStatisticsKey(
                        "s1", "m3", "o1", "p1", "me1", "c1", date1),
                new CustomerStatisticsKey(
                        "s1", "m1", "o4", "p1", "me1", "c1", date1),
                new CustomerStatisticsKey(
                        "s1", "m1", "o1", "p5", "me1", "c1", date1),
                new CustomerStatisticsKey(
                        "s1", "m1", "o1", "p1", "me6", "c1", date1),
                new CustomerStatisticsKey(
                        "s1", "m1", "o1", "p1", "me1", "c7", date1),
                new CustomerStatisticsKey(
                        "s1", "m1", "o1", "p1", "me1", "c1", date2),
                new CustomerStatisticsKey(
                        null, "m1", "o1", "p1", "me1", "c1", date1),
                new CustomerStatisticsKey(
                        "s1", null, "o1", "p1", "me1", "c1", date1),
                new CustomerStatisticsKey(
                        "s1", "m1", null, "p1", "me1", "c1", date1),
                new CustomerStatisticsKey(
                        "s1", "m1", "o1", null, "me1", "c1", date1),
                new CustomerStatisticsKey(
                        "s1", "m1", "o1", "p1", null, "c1", date1),
                new CustomerStatisticsKey(
                        "s1", "m1", "o1", "p1", "me1", null, date1),
        };
        CustomerStatisticsKey keys2[] = copyObject(keys1);
        for (int i1 = 0; i1 < keys1.length; i1++) {
            final CustomerStatisticsKey key1 = keys1[i1];
            for (int i2 = 0; i2 < keys2.length; i2++) {
                final CustomerStatisticsKey key2 = keys2[i2];
                if (i1 == i2) {
                    assertTrue("Key " + key1 + " equals key " + key2 + " fails",
                            key1.equals(key2));
                    assertEquals("Key " + key1 +
                            " hashCode comparison to key " +
                            key2 + " fails", key1.hashCode(), key2.hashCode());
                } else {
                    assertFalse("Key " + key1 + " equals key " + key2 +
                            " fails", key1.equals(key2));
                    assertTrue("Key " + key1 + " hashCode comparison to key " +
                            key2 + " fails",
                            key1.hashCode() != key2.hashCode());
                }
            }
        }
        // Special tests cases
        CustomerStatisticsKey key = new CustomerStatisticsKey("s1", "m1",
                "o1", "p1", "me1", "c1", date1);
        assertFalse("equals null", key.equals(null));
        assertFalse("equals to other object type", key.equals("s"));
    }
}
