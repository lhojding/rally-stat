package se.uc.stat.web.webtypes;

/**
 * A number of formatting methods for the GUI.
 * 
 * @author Anders Persson (konx40)
 */
public class GUIFormatter {
    /**
     * Get the printable string representation of the given value including hard
     * spaces (&amp;nbsp;) instead of usual spaces and minus (&amp;minus;)
     * instead of -.
     * 
     * @param s The string to convert.
     * 
     * @return The string s or "<unknown>" if s is <code>null</code>.
     */
    public static String toStringNoBreak(String s) {
        if (s == null) {
            return "<unknown>";
        }
        final StringBuilder sb = new StringBuilder();
        for (int pos = 0; pos < s.length(); pos++) {
            final char c = s.charAt(pos);
            if (c == ' ') {
                sb.append("&nbsp;");
            }
            else if (c == '-') {
                sb.append("&minus;");
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
