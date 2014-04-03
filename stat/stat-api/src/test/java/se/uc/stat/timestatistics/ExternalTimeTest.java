package se.uc.stat.timestatistics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.uc.stat.utils.AbstractTestBase;

/**
 * Test the class ExternalTime.
 * 
 * @author Anders Persson (konx40)
 */
public class ExternalTimeTest extends AbstractTestBase {
    /**
     * Test the constructor and get method.
     */
    @Test
    public void testConstructorGet() {
        ExternalTime ext = new ExternalTime(17);
        assertEquals("Invalid response from getTime", 17, ext.getTime());
    }
}
