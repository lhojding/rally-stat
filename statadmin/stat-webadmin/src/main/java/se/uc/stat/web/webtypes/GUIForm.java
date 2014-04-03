package se.uc.stat.web.webtypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import se.uc.stat.web.BaseContainer;
import se.uc.stat.web.types.ColumnInfo;

/**
 * Represents the full form with all its attributes.
 * 
 * @author Anders Persson (konx40)
 */
public class GUIForm implements GUIRequestParameters {
    /** The default number of max rows. */
    private final static int MAX_ROWS_DEFAULT = 100;
    /** The max number of max rows. */
    private final static int MAX_ROWS_MAX = 50000;
    
    /** The container with the information this form is built on. */
    private final BaseContainer container;
    /** The parameters to this request. */
    private final Map<String, String> parameters =
        new HashMap<String, String>();
    /** The sort part of the form. */
    private final GUISort sort;
    /** The max number of rows. */
    private final int maxRows;
    /** The customer to search for. */
    private final String customer;
    /** The product to search for. */
    private final String product;
    /** The from date. */
    private final Date fromDate;
    /** The to date. */
    private final Date toDate;
    
    /**
     * Create this class.
     * 
     * @param container         The parent container with the information this
     *                          form is built on.
     *                          Must not be <code>null</code>.
     * @param requestParameters The parameters to this request.
     *                          Must not be <code>null</code>.
     *                          
     * @throws IllegalArgumentException if any of the conditions above
     *         is not met.
     */
    public GUIForm(BaseContainer container,
            Map<String, String[]> requestParameters) {
        if (container == null) {
            throw new IllegalArgumentException("container must not be null");
        }
        this.container = container;
        if (parameters == null) {
            throw new IllegalArgumentException("parameters must not be null");
        }
        preprocessParameters(requestParameters);
        sort = new GUISort(this, container.getSortParameterInfoList().size());
        maxRows = parseMaxRows();
        customer = parameters.get(ColumnInfo.CUSTOMER.getName());
        product = parameters.get(ColumnInfo.PRODUCT.getName());
        fromDate = parseDate(parameters.get("fromDate"));
        toDate = parseDate(parameters.get("toDate"));
        // Post process GUI lists
        for (GUIList list : container.getGUIListList()) {
            list.setRequestParameters(this);
        }
    }

    /**
     * Pre process parameters to a format more suitable for this class.
     * Fill <code>parameters</code> with values.
     * 
     * @param requestParameters The parameters to this request.
     *                          The type is chosen to match the ServletRequest.
     */
    private void preprocessParameters(@SuppressWarnings("rawtypes") Map requestParameters) {
        @SuppressWarnings("unchecked")
        Map<String, String[]> params = requestParameters;
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            final String key = entry.getKey();
            final String values[] = entry.getValue();
            if (values != null && values.length != 0) {
                final String value = values[0];
                if (value != null && value.length() != 0) {
                    final String trimmed = value.trim();
                    if (trimmed.length() != 0) {
                        parameters.put(key, trimmed);
                    }
                }
            }
        }
    }

    /**
     * Get the value of the parameter with the given name.
     * 
     * @param name The name of the parameter.
     * 
     * @return The value of the parameter or <code>null</code> if the
     *         parameter has not been specified. This method never returns
     *         an empty string. The value is always trimmed.
     */
    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }
    
    /**
     * Get the container with all information to publish.
     * 
     * @return The container with all information to publish.
     *         This method never returns <code>null</code>
     */
    /* package */ BaseContainer getContainer() {
        return container;
    }
    
    /**
     * Get the sort rows.
     * 
     * @return The sort rows.
     *         This method never returns <code>null</code>
     */
    public GUISort getSort() {
        return sort;
    }

    /**
     * Parse the maxumum number of rows.
     * 
     * @return The given maximum number of rows.
     */
    private int parseMaxRows() {
        final String param = parameters.get("maxRows");
        if (param == null) {
            return MAX_ROWS_DEFAULT;
        }
        try {
            final int value = Integer.parseInt(param);
            if (value < 1) {
                return MAX_ROWS_DEFAULT;
            }
            if (value > MAX_ROWS_MAX) {
                return MAX_ROWS_MAX;
            }
            return value;
        } catch (NumberFormatException e) {
            return MAX_ROWS_DEFAULT;
        }
    }
    
    /**
     * Get the maximum number of rows.
     * 
     * @return The maximum number of rows.
     */
    public int getMaxRows() {
        return maxRows;
    }
    
    /**
     * Get the customer.
     * 
     * @return The customer or <code>null</code> if not specified.
     */
    public String getCustomer() {
        return customer;
    }
    
    /**
     * Get the product.
     * 
     * @return The product or <code>null</code> if not specified.
     */
    public String getProduct() {
        return product;
    }
    
    /**
     * Get the from date.
     * 
     * @return The from date or <code>null</code> if not specified.
     */
    public Date getFromDate() {
        return fromDate;
    }
    
    /**
     * Get the from date as a string.
     * 
     * @return The from date as a string or empty string if not specified.
     */
    public String getFromDateString() {
        return toString(fromDate);
    }
    
    /**
     * Get the to date.
     * 
     * @return The to date or <code>null</code> if not specified.
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * Get the to date as a string.
     * 
     * @return The to date as a string or empty string if not specified.
     */
    public String getToDateString() {
        return toString(toDate);
    }
    
    /**
     * Convert the date to a string.
     * yyyyMMdd or yyyyMMdd HHmm or empty string.
     * 
     * @param date The date to convert.
     * 
     * @return The converted date. This method never returns <code>null</code>.
     */
    private String toString(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    /**
     * Convert the string to a date. The string will be stripped from all other
     * than digits and then parsed as yyyyMMdd or yyyyMMddHHmm depending on
     * length. If there are problems or if the string is empty,
     * <code>null</code> will be returned.
     * 
     * @param dateString The string to parse
     * 
     * @return The converted date or <code>null</code> if the string is empty
     *         or there is a problem parsing the string.
     */
    private Date parseDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        StringBuilder numbers = new StringBuilder();
        for (char c : dateString.toCharArray()) {
            if (Character.isDigit(c)) {
                numbers.append(c);
            }
        }
        if (numbers.length() == 8) {
            numbers.append("0000");
        }
        if (numbers.length() != 12) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            return sdf.parse(numbers.toString());
        } catch (ParseException e) {
            return null;
        }
    }
}
