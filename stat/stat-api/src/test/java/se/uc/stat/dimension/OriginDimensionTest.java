package se.uc.stat.dimension;

/**
 * Tests the OriginDimension class.
 * 
 * @author Anders Persson (konx40)
 */
public class OriginDimensionTest extends BaseTestDimension<String> {
    /**
     * Create this class.
     */
    public OriginDimensionTest() {
        DimensionTestConfig<String> config = getConfig();
        config.dimension = Dimensions.getOriginDimension();
        config.tableName = "ORIGIN_INFO";
        config.idColumn = "ORIGIN_ID";
        config.keyInvalid = "1234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890";
        
        // Keys without pre processing
        config.addKeyConfig("testDimension",
                "ORIGIN_NAME = 'testDimension'", true, null);
        config.sqlInsertKey0 = "insert into " + config.tableName +
                "(" + config.idColumn + ", ORIGIN_NAME) " +
                "values(ORIGIN_ID_SEQ.nextval, 'testDimension')";
        config.addKeyConfig("testDimension45678901234567890",
                "ORIGIN_NAME = 'testDimension45678901234567890'",
                true, null);
        // Keys with pre processing
        config.addKeyConfig("1234567890123456789012345678901",
                "ORIGIN_NAME = '123456789012345678901234567890'",
                true, null);
        config.addKeyConfig("   testDimension   ",
                "ORIGIN_NAME = 'testDimension'", true, "testDimension");
        config.addKeyConfig(null, "ORIGIN_NAME = '(tom)'", false, "");
        config.addKeyConfig("", "ORIGIN_NAME = '(tom)'", false, null);
        config.addKeyConfig("    ", "ORIGIN_NAME = '(tom)'", false, "");
    }
}
