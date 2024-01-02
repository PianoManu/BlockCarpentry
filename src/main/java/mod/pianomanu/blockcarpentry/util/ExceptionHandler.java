package mod.pianomanu.blockcarpentry.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Pretty simple way of unified handling of exceptions. Primary goal is to
 * avoid log spam of similar or identical exceptions.
 *
 * @author PianoManu
 * @version 1.0 01/02/24
 */
public class ExceptionHandler {
    private static Map<String, Integer> exceptionOccurences = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger();

    public static void handleException(Exception e) {
        String key = e.toString();
        if (exceptionOccurences.containsKey(key)) {
            exceptionOccurences.put(key, exceptionOccurences.get(key) + 1);
        } else {
            exceptionOccurences.put(key, 1);
        }

        if (exceptionOccurences.get(key) < 20) {
            e.printStackTrace();
        } else if (exceptionOccurences.get(key) == 20) {
            LOGGER.warn("Exception stacktrace spam detected. Suppressing all following exceptions of \"" + key + "\"...");
        }
    }
}
//========SOLI DEO GLORIA========//