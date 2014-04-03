package se.uc.stat.dimension;

import se.uc.stat.utils.AbstractTestBase;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the MethodKey class.
 * 
 * @author Anders Persson (konx40)
 */
public class MethodKeyTest extends AbstractTestBase {
    /**
     * Test the constructor and the get and toString methods.
     */
    @Test
    public void testConstructorGetToString() {
        MethodKey key = new MethodKey("s1", "m1");
        assertEquals("Test 1, invalid getServiceName",
                "s1", key.getServiceName());
        assertEquals("Test 1, invalid getMethodName",
                "m1", key.getMethodName());
        assertEquals("Test 1, invalid toString",
                "{serviceName=s1, methodName=m1}", key.toString());

        key = new MethodKey("s2", null);
        assertEquals("Test 2, invalid getServiceName",
                "s2", key.getServiceName());
        assertEquals("Test 2, invalid getMethodName",
                null, key.getMethodName());
        assertEquals("Test 2, invalid toString",
                "{serviceName=s2, methodName=null}", key.toString());

        key = new MethodKey(null, "m3");
        assertEquals("Test 3, invalid getServiceName",
                null, key.getServiceName());
        assertEquals("Test 3, invalid getMethodName",
                "m3", key.getMethodName());
        assertEquals("Test 3, invalid toString",
                "{serviceName=null, methodName=m3}", key.toString());

        key = new MethodKey(null, null);
        assertEquals("Test 4, invalid getServiceName",
                null, key.getServiceName());
        assertEquals("Test 4, invalid getMethodName",
                null, key.getMethodName());
        assertEquals("Test 4, invalid toString",
                "{serviceName=null, methodName=null}", key.toString());
    }

    /**
     * Test the equals and hashCode methods.
     */
    @Test
    public void testEqualsHashCode() {
        MethodKey keys1[] = {
                new MethodKey("s1", "m1"),
                new MethodKey("s2", "m1"),
                new MethodKey("s1", "m3"),
                new MethodKey("s1", null),
                new MethodKey("s2", null),
                new MethodKey(null, "m1"),
                new MethodKey(null, "m3"),
                new MethodKey(null, null)
        };
        MethodKey keys2[] = copyObject(keys1);
        for (int i1 = 0; i1 < keys1.length; i1++) {
            final MethodKey key1 = keys1[i1];
            for (int i2 = 0; i2 < keys2.length; i2++) {
                final MethodKey key2 = keys2[i2];
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
        MethodKey key = new MethodKey("s", "m");
        assertFalse("equals null", key.equals(null));
        assertFalse("equals to other object type", key.equals("s"));
    }
}
