package se.uc.stat.dimension;

/**
 * Tests the MethodDimension class.
 * 
 * @author Anders Persson (konx40)
 */
public class MethodDimensionTest extends BaseTestDimension<MethodKey> {
    /** String with 101 characters. */
    private final String CHAR_101 = "1234567890123456789012345678901234567890" +
            "1234567890123456789012345678901234567890123456789012345678901";
    /** String with the starting 100 characters among the 101 in CHAR_101. */
    private final String CHAR_100 = "1234567890123456789012345678901234567890" +
            "123456789012345678901234567890123456789012345678901234567890";
    
    /**
     * Create this class.
     */
    public MethodDimensionTest() {
        DimensionTestConfig<MethodKey> config = getConfig();
        config.dimension = Dimensions.getMethodDimension();
        config.tableName = "METHOD_INFO";
        config.idColumn = "METHOD_ID";
        config.keyInvalid = new MethodKey("123456789012345678901234567890" +
        		"12345678901234567890123456789012345678901234567890" +
        		"123456789012345678901", "metod");
        
        // Keys without pre processing
        config.addKeyConfig(new MethodKey("s1", "m1"),
                "SERVICE_NAME = 's1' and METHOD_NAME = 'm1'", true, null);
        config.sqlInsertKey0 = "insert into " + config.tableName +
            "(" + config.idColumn + ", SERVICE_NAME, METHOD_NAME) " +
            "values(METHOD_ID_SEQ.nextval, 's1', 'm1')";
        config.addKeyConfig(new MethodKey("s1", "m3"),
                "SERVICE_NAME = 's1' and METHOD_NAME = 'm3'", true, null);
        config.addKeyConfig(new MethodKey("s2", "m1"),
                "SERVICE_NAME = 's2' and METHOD_NAME = 'm1'", true, null);
        config.addKeyConfig(new MethodKey(CHAR_100, CHAR_100),
                "SERVICE_NAME = '" + CHAR_100 + "' and METHOD_NAME = '" +
                CHAR_100 + "'", true, null);
        // Keys with pre processing
        config.addKeyConfig(new MethodKey(CHAR_101, "m1"),
                "SERVICE_NAME = '" + CHAR_100 + "' " +
                "and METHOD_NAME = 'm1'", true, null);
        config.addKeyConfig(new MethodKey(CHAR_101, null),
                "SERVICE_NAME = '" + CHAR_100 + "' " +
                "and METHOD_NAME = '(tom)'", true, null);
        config.addKeyConfig(new MethodKey(null, CHAR_101),
                "SERVICE_NAME = '(tom)' and METHOD_NAME = " +
                "'" + CHAR_100 + "'",
                true, null);
        config.addKeyConfig(new MethodKey(CHAR_101, CHAR_101),
                "SERVICE_NAME = '" + CHAR_100 + "' and METHOD_NAME = " +
                "'" + CHAR_100 + "'",
                true, new MethodKey(CHAR_100, CHAR_100));
        config.addKeyConfig(new MethodKey("  s1  ", "  m1  "),
                "SERVICE_NAME = 's1' and METHOD_NAME = 'm1'", true,
                new MethodKey("s1", "m1"));

        final MethodKey emptyKey = new MethodKey(null, null);
        final String emptyKeyWhere =
                "SERVICE_NAME = '(tom)' and METHOD_NAME = '(tom)'";
        config.addKeyConfig(emptyKey, emptyKeyWhere, false, null);
        config.addKeyConfig(null, emptyKeyWhere, false, emptyKey);
        config.addKeyConfig(new MethodKey("   ", null),
                emptyKeyWhere, false, emptyKey);
        config.addKeyConfig(new MethodKey(null, "   "),
                emptyKeyWhere, false, emptyKey);
        config.addKeyConfig(new MethodKey("   ", "   "),
                emptyKeyWhere, false, emptyKey);
    }
}
