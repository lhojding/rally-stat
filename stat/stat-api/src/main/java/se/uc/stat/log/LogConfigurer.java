package se.uc.stat.log;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.io.File;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * Configures logging using an external config file instead of using the
 * logback.xml specified on the classpath. If no external config file is
 * available, the logback.xml on the classpath will be used.
 *
 * @author Jonas Paro (konx77)
 */
public class LogConfigurer {
    /** Default config file location */
    private final static String CONFIG_FILE_LOCATION =
            "/opt/uc/etc/logconfig/media/stat.xml";

    /**
     * Configures the logging to behave according to the file specified by the
     * configFileLocation. If the specified file is missing the config specified
     * in logback.xml will be used.
     */
    /* package */ static void configureLogging() {
        File logConfigFile = new File(CONFIG_FILE_LOCATION);
        if (logConfigFile.exists()) {
            LoggerContext lc =
                    (LoggerContext) LoggerFactory.getILoggerFactory();

            try {
                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(lc);
                lc.reset();
                configurator.doConfigure(CONFIG_FILE_LOCATION);
            } catch (JoranException je) {
                throw new IllegalStateException("Cannot configure logback", je);
            }
            LoggerFactory.getLogger(LogConfigurer.class).info(
                    CONFIG_FILE_LOCATION +
                    " will be used to configure logging.");
        } else {
            LoggerFactory.getLogger(LogConfigurer.class).info(
                    "No external log config found on: " + CONFIG_FILE_LOCATION +
                    ". Will use logback.xml from classpath.");
        }
    }
}
