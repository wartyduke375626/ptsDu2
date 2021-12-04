package database;

public class Queries {

    public static String GET_STOP_NAME_QUERY = """
            SELECT s.sname
            FROM stop s
            WHERE s.sname = ?
            """;

    public static String GET_STOP_LINES_QUERY = """
            SELECT l.lname
            FROM stop s, line l, stop_line sl
            WHERE s.sname = ? AND s.sid = sl.sid AND l.lid = sl.lid
            """;

    public static String GET_LINE_FIRST_STOP_AND_LID_QUERY = """
            SELECT s.sname, l.lid
            FROM line l, stop s
            WHERE l.lname = ? AND s.sid = l.firststop
            """;

    public static String GET_LINE_SEGMENTS_DATA_QUERY = """
            SELECT s.sname, ls.timeDiff, ls.sIndex
            FROM lineSegment ls, stop s
            WHERE ls.lid = ? AND s.sid = ls.nextStop
            """;

    public static String GET_BUSES_DATA_AND_BIDS_QUERY = """
            SELECT b.startTime, b.capacity, b.bid
            FROM line l, bus b
            WHERE l.lname = ? AND b.lid = l.lid AND b.startTime <= ? AND b.startTime >= ?
            """;

    public static String GET_BUS_SEGMENTS_PASSENGERS_QUERY = """
            SELECT bs.passengers, ls.sIndex
            FROM busSegment bs, lineSegment ls
            WHERE bs.bid = ? AND ls.lsid = bs.lsid
            """;

    public static String GET_BID_AND_LSID_QUERY = """
            SELECT b.bid, ls.lsid
            FROM lineSegment ls, line l, bus b
            WHERE l.lid = ls.lid AND b.lid = l.lid AND b.startTime = ? AND l.lname = ? AND ls.sIndex = ?
            """;

    public static String UPDATE_BUS_SEGMENT_QUERY = """
            UPDATE busSegment
            SET passengers = ?
            WHERE bid = ? AND lsid = ?
            """;

    public static String RESET_PASSENGERS_QUERY = """
            UPDATE busSegment
            SET passengers = 0
            """;
}
