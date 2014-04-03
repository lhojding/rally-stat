package se.uc.stat.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import se.uc.stat.web.types.DayOfWeek;
import se.uc.stat.web.types.LayerInfo;
import se.uc.stat.web.types.MethodInfo;
import se.uc.stat.web.types.MediaInfo;
import se.uc.stat.web.types.OriginInfo;

/**
 * Database services for statistics web GUI. These services are not directly
 * exposed but used internally by other services.
 * 
 * @author Anders Persson (konx40)
 */
/* package */ class DBServices {
    /**
     * SQL to select method. No parameters. Result:
     * Column 1: id
     * Column 2: service name
     * Column 3: method name
     * Order by: service name ascending, method name ascending
     */
    private final String GET_METHOD_INFO =
        "select METHOD_ID, SERVICE_NAME, METHOD_NAME " +
        "from METHOD_INFO " +
        "order by SERVICE_NAME, METHOD_NAME";
    
    /**
     * SQL to select day of week. No parameters. Result:
     * Column 1: id
     * Column 2: name
     * Order by: id ascending
     */
    private final String GET_WEEK_DAYS =
        "select DAY_OF_WEEK_ID, DAY_OF_WEEK_NAME " +
        "from DAY_OF_WEEK " +
        "order by DAY_OF_WEEK_ID";
    
    /**
     * SQL to select media. No parameters. Result:
     * Column 1: id
     * Column 2: name
     * Order by: name ascending
     */
    private final String GET_MEDIA_INFO =
        "select MEDIA_ID, MEDIA_NAME " +
        "from MEDIA_INFO " +
        "order by MEDIA_NAME";
    
    /**
     * SQL to select origin. No parameters. Result:
     * Column 1: id
     * Column 2: name
     * Order by: name ascending
     */
    private final String GET_ORIGIN_INFO =
        "select ORIGIN_ID, ORIGIN_NAME " +
        "from ORIGIN_INFO " +
        "order by ORIGIN_NAME";
    
    /**
     * SQL to select layer. No parameters. Result:
     * Column 1: id
     * Column 2: name
     * Order by: name ascending
     */
    private final String GET_LAYER_INFO =
        "select LAYER_ID, LAYER_NAME " +
        "from LAYER_INFO " +
        "order by LAYER_NAME";
    
    /**
     * Get the methods ordered in a human friendly view order.
     * 
     * @return The methods.
     *         This method never returns <code>null</code>.
     * 
     * @throws SQLException if there is a problem retrieving the information.
     */
    /* package */ List<MethodInfo> getMethodInfoList() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtilities.createConnection();
            ps = conn.prepareStatement(GET_METHOD_INFO);
            rs = ps.executeQuery();
            final List<MethodInfo> result = new ArrayList<MethodInfo>();
            while (rs.next()) {
                result.add(new MethodInfo(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            return result;
        } finally {
            DBUtilities.close(conn, ps, rs);
        }
    }
    
    /**
     * Get the days of the week ordered in a human friendly view order.
     * 
     * @return The days of the week.
     *         This method never returns <code>null</code>.
     * 
     * @throws SQLException if there is a problem retrieving the information.
     */
    /* package */ List<DayOfWeek> getDayOfWeekList() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtilities.createConnection();
            ps = conn.prepareStatement(GET_WEEK_DAYS);
            rs = ps.executeQuery();
            final List<DayOfWeek> result = new ArrayList<DayOfWeek>();
            while (rs.next()) {
                result.add(new DayOfWeek(rs.getInt(1), rs.getString(2)));
            }
            return result;
        } finally {
            DBUtilities.close(conn, ps, rs);
        }
    }

    /**
     * Get the medias ordered in a human friendly view order.
     * 
     * @return The medias.
     *         This method never returns <code>null</code>.
     * 
     * @throws SQLException if there is a problem retrieving the information.
     */
    /* package */ List<MediaInfo> getMediaInfoList() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtilities.createConnection();
            ps = conn.prepareStatement(GET_MEDIA_INFO);
            rs = ps.executeQuery();
            final List<MediaInfo> result = new ArrayList<MediaInfo>();
            while (rs.next()) {
                result.add(new MediaInfo(rs.getInt(1), rs.getString(2)));
            }
            return result;
        } finally {
            DBUtilities.close(conn, ps, rs);
        }
    }

    /**
     * Get the origins ordered in a human friendly view order.
     * 
     * @return The origins.
     *         This method never returns <code>null</code>.
     * 
     * @throws SQLException if there is a problem retrieving the information.
     */
    /* package */ List<OriginInfo> getOriginInfoList() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtilities.createConnection();
            ps = conn.prepareStatement(GET_ORIGIN_INFO);
            rs = ps.executeQuery();
            final List<OriginInfo> result = new ArrayList<OriginInfo>();
            while (rs.next()) {
                result.add(new OriginInfo(rs.getInt(1), rs.getString(2)));
            }
            return result;
        } finally {
            DBUtilities.close(conn, ps, rs);
        }
    }

    /**
     * Get the layers ordered in a human friendly view order.
     * 
     * @return The layers.
     *         This method never returns <code>null</code>.
     * 
     * @throws SQLException if there is a problem retrieving the information.
     */
    /* package */ List<LayerInfo> getLayerInfoList() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtilities.createConnection();
            ps = conn.prepareStatement(GET_LAYER_INFO);
            rs = ps.executeQuery();
            final List<LayerInfo> result = new ArrayList<LayerInfo>();
            while (rs.next()) {
                result.add(new LayerInfo(rs.getInt(1), rs.getString(2)));
            }
            return result;
        } finally {
            DBUtilities.close(conn, ps, rs);
        }
    }
}
