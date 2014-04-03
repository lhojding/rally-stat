package se.uc.stat.web;

import java.util.ArrayList;
import java.util.List;

import se.uc.stat.web.types.HourOfDay;

/**
 * Internal services for statistics web GUI. These services are not directly
 * exposed but used internally by other services.
 * 
 * @author Anders Persson (konx40)
 */
/* package */ class InternalServices {
    /**
     * Get the hours of the day ordered in a human friendly view order.
     * 
     * @return The hours of the day.
     *         This method never returns <code>null</code>.
     */
    /* package */ List<HourOfDay> getHourOfDayList() {
        final List<HourOfDay> result = new ArrayList<HourOfDay>();
        for (int i = 0; i <= 23; i++) {
            final String hour = Integer.toString(i);
            result.add(new HourOfDay(i, hour + ":00-" + hour + ":59"));
        }
        return result;
    }
}
