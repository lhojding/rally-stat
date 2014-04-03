package se.uc.stat.dimension;

/**
 * Tests the LayerDimension class.
 * 
 * @author Anders Persson (konx40)
 */
public class LayerDimensionTest extends BaseTestDimension<String> {
    /**
     * Create this class.
     */
    public LayerDimensionTest() {
        DimensionTestConfig<String> config = getConfig();
        config.dimension = Dimensions.getLayerDimension();
        config.tableName = "LAYER_INFO";
        config.idColumn = "LAYER_ID";
        config.keyInvalid = "1234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890";
        
        // Keys without pre processing
        config.addKeyConfig("testDimension",
                "LAYER_NAME = 'testDimension'", true, null);
        config.sqlInsertKey0 = "insert into " + config.tableName +
                "(" + config.idColumn + ", LAYER_NAME) " +
                "values(LAYER_ID_SEQ.nextval, 'testDimension')";
        config.addKeyConfig("testDimension45678901234567890",
                "LAYER_NAME = 'testDimension45678901234567890'",
                true, null);
        // Keys with pre processing
        config.addKeyConfig("1234567890123456789012345678901",
                "LAYER_NAME = '123456789012345678901234567890'",
                true, null);
        config.addKeyConfig("   testDimension   ",
                "LAYER_NAME = 'testDimension'", true, "testDimension");
        config.addKeyConfig(null, "LAYER_NAME = '(tom)'", false, "");
        config.addKeyConfig("", "LAYER_NAME = '(tom)'", false, null);
        config.addKeyConfig("    ", "LAYER_NAME = '(tom)'", false, "");
    }
}
