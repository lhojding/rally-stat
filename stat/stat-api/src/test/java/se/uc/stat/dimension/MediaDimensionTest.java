package se.uc.stat.dimension;

/**
 * Tests the MediaDimension class.
 * 
 * @author Anders Persson (konx40)
 */
public class MediaDimensionTest extends BaseTestDimension<String> {
    /**
     * Create this class.
     */
    public MediaDimensionTest() {
        DimensionTestConfig<String> config = getConfig();
        config.dimension = Dimensions.getMediaDimension();
        config.tableName = "MEDIA_INFO";
        config.idColumn = "MEDIA_ID";
        config.keyInvalid = "1234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890";
        
        // Keys without pre processing
        config.addKeyConfig("testDimension",
                "MEDIA_NAME = 'testDimension'", true, null);
        config.sqlInsertKey0 = "insert into " + config.tableName +
                "(" + config.idColumn + ", MEDIA_NAME) " +
                "values(MEDIA_ID_SEQ.nextval, 'testDimension')";
        config.addKeyConfig("testDimension4567890123456789012345678901234567890",
                "MEDIA_NAME = 'testDimension4567890123456789012345678901234567890'",
                true, null);
        // Keys with pre processing
        config.addKeyConfig("123456789012345678901234567890123456789012345678901",
                "MEDIA_NAME = '12345678901234567890123456789012345678901234567890'",
                true, null);
        config.addKeyConfig("   testDimension   ",
                "MEDIA_NAME = 'testDimension'", true, "testDimension");
        config.addKeyConfig(null, "MEDIA_NAME = '(tom)'", false, "");
        config.addKeyConfig("", "MEDIA_NAME = '(tom)'", false, null);
        config.addKeyConfig("    ", "MEDIA_NAME = '(tom)'", false, "");
    }
}
