package se.uc.stat.basestatistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.Assert.assertNotNull;

/**
 * Implementation of BaseCollector for test purposes.
 * 
 * @author Anders Persson (konx40)
 */
public class BaseCollectorImpl extends BaseCollector<BaseKeyImpl,
        BaseInformationImpl> {
    /** The expected sequences. */
    private final HashMap<BaseKeyImpl, Sequence> sequences =
            new HashMap<BaseKeyImpl, Sequence>();
    
    /**
     * Create this instance.
     * 
     * @param storeInterval       The time interval for storing information.
     *                            This is the time in milliseconds between the
     *                            last information object is stored and the
     *                            next start to store information.
     * @param maxNumberOfStorages The maximum number of objects a single thread
     *                            may have to store before it is released.
     */
    /* package */ BaseCollectorImpl(long storeInterval,
            int maxNumberOfStorages) {
        super(storeInterval, maxNumberOfStorages, "", "");
    }

    /**
     * Add an expected sequence to this collector.
     * 
     * @param sequence The sequence to add.
     */
    /* package */ void addSequence(Sequence sequence) {
        sequences.put(sequence.getInfo().getKey(), sequence);
    }
    
    /**
     * Get the number of sequences that has not been fully run.
     * 
     * @return The number of sequences that has not run to an end.
     */
    /* package */ int getNumSequences() {
        int result = 0;
        for (Sequence sequence : sequences.values()) {
            if (!sequence.hasRunToEnd()) {
                result++;
            }
        }
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    /* package */ boolean insertInformation(Connection connection,
            BaseInformationImpl info) throws SQLException {
        final Sequence sequence = sequences.get(info.getKey());
        assertNotNull("Try to call not existing object " + info.getKey(),
                sequence);
        return sequence.methodCall(info, Sequence.METHOD_INSERT);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    /* package */ boolean updateInformation(Connection connection,
            BaseInformationImpl info) throws SQLException {
        final Sequence sequence = sequences.get(info.getKey());
        assertNotNull("Try to call not existing object " + info.getKey(),
                sequence);
        return sequence.methodCall(info, Sequence.METHOD_UPDATE);
    }
    
    /**
     * Not used in this test.
     * {@inheritDoc}.
     */
    @Override
    protected void populateInsert(PreparedStatement ps, BaseInformationImpl info)
            throws SQLException {
        // Nothing to do.
    }

    /**
     * Not used in this test.
     * {@inheritDoc}.
     */
    @Override
    protected void populateUpdate(PreparedStatement ps, BaseInformationImpl info)
            throws SQLException {
        // Nothing to do.
    }
}
