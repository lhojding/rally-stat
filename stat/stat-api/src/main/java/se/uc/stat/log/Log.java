package se.uc.stat.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Class performing the log formatting and writing.
 * <p/>
 * All methods in this class is constructed in a way that prevents them from
 * throwing an exception. The reasons for this is:
 * <ol>
 * <li>The log should always log an error if it is possible in any way.
 *     That is the reason for example having defined backup routines if
 *     for example the name of the log is set to <code>null</code>
 *     (instead of failing the call).</li>
 * <li>The log should never fail the call. That is the reason to for example
 *     handle <code>toString</code> methods that throws exception.</li>
 * </ol>
 *
 * @author Anders Persson (konx40)
 */
public class Log {

    /**
     * Regular expression used to escape line breaks. Finds all occurences of
     * <code>\r\n</code> and/or <code>\n</code>
     */
    private static final String ESCAPE_LINE_BREAK = "(\\r|\\n)+";

    /**
     * Format of dates to be displayed in log messages.
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /** Synchronized map that holds all the loggers by their name */
    private static final Map<String, Log> loggers =
            new HashMap<String, Log>();

    /** The logger used to log error messages */
    private final Logger logger;

    /** Perform log configuration. */
    static {
        LogConfigurer.configureLogging();
    }
    
    /**
     * Create this class. The method is intentionally private becaues
     * instances can only be created using the static method in this class.
     *
     * @param name The name of the log. Must not be <code>null</code>.
     */
    private Log(String name) {
        logger = LoggerFactory.getLogger(name);
    }

    /**
     * Get the log instance with the given name. Usually, the method
     * {@link #getLog(Class)} is used instead.
     *
     * @param name The name of the log. If <code>null</code>, the logger
     *             coupled to this (Log) class will be used and an error message
     *             will be written to the log.
     *
     * @return The log object with the given name. This method never returns
     *         <code>null</code>.
     */
    public static Log getLog(String name) {
        Log log;
        String logName;
        if (name != null) {
            logName = name;
        } else {
            logName = Log.class.getName();
        }
        synchronized (loggers) {
            log = loggers.get(logName);
            if (log == null) {
                log = new Log(logName);
                loggers.put(logName, log);
            }
        }
        if (name == null) {
            log.error("Tried to call getLog() with null log name.", null,
                    null);
        }
        return log;
    }

    /**
     * Get the log instance for the given class.
     *
     * @param clazz The class of the log. If <code>null</code>, the logger
     *              coupled to this (Log) class will be used and an
     *              error message will be written to the log.
     *
     * @return The log object for the given class. This method never returns
     *         <code>null</code>.
     */
    public static Log getLog(Class clazz) {
        if (clazz == null) {
            return getLog((String)null);
        }
        return getLog(clazz.getName());
    }

    /**
     * Format a log message to be written. The reason for having this method
     * static and with package visibility is to be able to test it
     * extensively. Note that:
     * <ul>
     * <li><code>parameter.toString()</code> may fail which should result
     *     in a log record at error level and a note about the failure in
     *     the string returned.</li>.
     * <li><code>parameter.getCallInfo()</code> holds information that should
     *     be part of the message
     *     (using <code>parameter.getCallInfo().toString()</code>).</li>.
     * <li><code>exception.toString()</code> may fail which should result
     *     in a log record at error level and a note about the failure in
     *     the string returned.</li>.
     * <li><code>logInfo.toString()</code> may fail which should result
     *     in a log record at error level and a note about the failure in
     *     the string returned.</li>.
     * <ul>
     *
     * @param message   The message for the log record. <code>null</code>
     *                  if no message exists.
     * @param exception The exception to log. <code>null</code> if no
     *                  exception exists.
     * @param logInfo   Extra information to log. The <code>toString</code>
     *                  method will be used to convert the object to a string.
     *                  <code>null</code> if no extra information exists.
     *
     * @return The log record to write.
     */
    /* package */ static String format(String message,
            Throwable exception, Object logInfo) {
        String messageToString = safeToString(message);

        String exceptionToString = safeToString(exception);
        String causeToString = null;
        if (exception != null) {
            causeToString = safeToString(exception.getCause());
        }
        String logInfoToString = safeToString(logInfo);

        StringBuffer buf = new StringBuffer();
        buf.append("<message:").append(messageToString).append("> <exception:")
                .append(exceptionToString).append("> <cause:")
                .append(causeToString).append("> <logInfo:")
                .append(logInfoToString).append(">");
        return buf.toString().replaceAll(ESCAPE_LINE_BREAK, " ");
    }

    /**
     * Safely converts an object into a string for log messages by calling the
     * <code>toString()</code> method on the specified object.
     * <p/>
     * If the <code>toString()</code> method throws an exception a log record at
     * error level will be created and the returned string will contain
     * information that the <code>toString()</code> failed.
     * <p/>
     * If the object is <code>null</code> this method will return
     * <code>null</code>.
     *
     * @param o an object to safely convert into a log message string.
     *
     * @return the <code>toString()</code> of the specified object or
     *         <code>null</code> if the o is <code>null</code>.
     */
    private static String safeToString(Object o) {
        String safeToString;
        if (o != null) {
            try {
                safeToString = o.toString();
            } catch (Throwable t) {
                safeToString = "Execution of toString() failed on " +
                        o.getClass().getSimpleName() +
                        " with exception=" + t.getClass().getSimpleName();
                boolean safeToLogAsError;
                try {
                    t.toString();
                    safeToLogAsError = true;
                } catch (Throwable toStringExceptionInThrowable) {
                    safeToLogAsError = false;
                }
                if (safeToLogAsError) {
                    getLog(o.getClass())
                            .error(safeToString, t, null);
                } else {
                    getLog(o.getClass())
                            .error(safeToString +
                                    " and toString()" +
                                    " of that exception also threw exception.",
                                    null, null);
                }
            }
        } else {
            safeToString = null;
        }
        return safeToString;
    }

    /**
     * Write a log record at error level.
     *
     * @param message   The message for the log record. <code>null</code>
     *                  if no message exists.
     * @param exception The exception to log. <code>null</code> if no
     *                  exception exists.
     * @param logInfo   Extra information to log. The <code>toString</code>
     *                  method will be used to convert the object to a string.
     *                  <code>null</code> if no extra information exists.
     */
    public void error(String message,
            Throwable exception, Object logInfo) {
        final String logRecord = format(message, exception, logInfo);
        logger.error(logRecord, exception);
    }

    /**
     * Write a log record at warning level.
     *
     * @param message   The message for the log record. <code>null</code>
     *                  if no message exists.
     * @param exception The exception to log. <code>null</code> if no
     *                  exception exists.
     * @param logInfo   Extra information to log. The <code>toString</code>
     *                  method will be used to convert the object to a string.
     *                  <code>null</code> if no extra information exists.
     */
    public void warning(String message,
            Throwable exception, Object logInfo) {
        final String logRecord = format(message, exception, logInfo);
        logger.warn(logRecord, exception);
    }

    /**
     * Write a log record at info level.
     *
     * @param message   The message for the log record. <code>null</code>
     *                  if no message exists.
     * @param exception The exception to log. <code>null</code> if no
     *                  exception exists.
     * @param logInfo   Extra information to log. The <code>toString</code>
     *                  method will be used to convert the object to a string.
     *                  <code>null</code> if no extra information exists.
     */
    public void info(String message,
            Throwable exception, Object logInfo) {
        final String logRecord = format(message, exception, logInfo);
        logger.info(logRecord, exception);
    }

    /**
     * Write a log record at debug level.
     *
     * @param message   The message for the log record. <code>null</code>
     *                  if no message exists.
     * @param exception The exception to log. <code>null</code> if no
     *                  exception exists.
     * @param logInfo   Extra information to log. The <code>toString</code>
     *                  method will be used to convert the object to a string.
     *                  <code>null</code> if no extra information exists.
     */
    public void debug(String message,
            Throwable exception, Object logInfo) {
        final String logRecord = format(message, exception, logInfo);
        logger.debug(logRecord, exception);
    }
}
