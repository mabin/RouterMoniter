package org.rm.core;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* A class encapsulated a prepared statement. Set values by this can let you get a complete sql. <br />
* Usage: <br />
* <code>
* Connection conn = .......
* String sql = "UPDATE TABLE SET A=? WHERE B=?";
* PreparedStatement ps = PreparedStatementEx.getInstance(conn, sql);
* ps.setString(1, "A");
* ps.setString(2, "B");
* System.out.println(PreparedStatementEx.getSql(ps)); // this line is just for test.
* .... // some code to execute the statement and process the data.
* </code>
* <br />
* This class is for 1.6
* @author Wang Yuan
*/
public class PreparedStatementEx implements PreparedStatement {
   
    private final static String EMPTY_SQL = "";
    private final static String IN_PARAM_ESC = "?";
    private final static String NULL_VALUE = "NULL";
    private final static String DATA_ERROR = "* DATA RETRIEVE ERROR *";
    private final static String INCOMPLETE = "...";
   
    private PreparedStatement pStmt;
    private String origSql;
   
    private static boolean fullSql = true;
   
    private boolean showHexByte = true;
    private int maxParamValueLength = UNLIMITED_LENGTH;
    private int maxSqlLength = UNLIMITED_LENGTH;
    private String dataFormatPattern = "''yyyy-MM-dd''";
    private String timeFormatPattern = "''HH:mm:ss''";
    private String timestampFormatPattern = "''yyyy-MM-dd-HH.mm.ss.SSSSSS''";
   
    private SimpleDateFormat sdf;
   
    private Map<Integer, Object> paramMap = new HashMap<Integer, Object>();
   
    /**
     * constant for set max param value length or max sql length.
     * this value means the length is unlimited.
     */
    public final static int UNLIMITED_LENGTH = -1;
   
    /**
     * Construct a new PreparedStatementEx.
     * @param preparedStatement the prepared statement.
     * @param sql the sql that create the prepared statement.
     */
    PreparedStatementEx(PreparedStatement preparedStatement, String sql) {
        this.pStmt = preparedStatement;
        this.origSql = (null == sql) ? EMPTY_SQL : sql;
    }
   
    /**
     * Return a PreparedStatementEx by specified connection and sql.
     * @param conn
     * @param sql
     * @return a PreparedStatement helper, which encapsulate a prepared statement created by specified connection and sql.
     * @throws SQLException
     */
    public static PreparedStatementEx getInstance(Connection conn, String sql) throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement(sql);
        return new PreparedStatementEx(pStmt, sql);
    }
   
    /**
     * Return the prepared statement encapsulated.
     * @return the prepared statement encapsulated.
     */
    public PreparedStatement getPreparedStatement() {
        return this.pStmt;
    }
   
    /**
     * Return the sql that created the prepared statement
     * @return the sql that created the prepared statement
     */
    public String getPreparedSql() {
        return this.origSql;
    }
   
    protected void setParameter4Sql(int parameterIndex, Object x) {
        this.paramMap.put(new Integer(parameterIndex), x);
    }
   
    /**
     * Set the value of fullSql flag. The value of fullSql flag is <tt>true</tt> by default. <br />
     * getSql() method return the full sql statement when the fullSql flag is true,
     * and return the sql used to create the PreparedStatementEx instance if the fullSql falg is false.
     * @param flag the value to set the fullSql flag.
     */
    public static void setFullSql(boolean flag) {
        fullSql = flag;
    }
   
    /**
     * Return the value of fullSql flag. The value of fullSql flag is <tt>true</tt> by default. <br />
     * getSql() method return the full sql statement when the fullSql flag is true,
     * and return the sql used to create the PreparedStatementEx instance if the fullSql falg is false.
     * @return The value of fullSql flag.
     */
    public static boolean isFullSql() {
        return fullSql;
    }
   
    /**
     * If the fullSql flag is true return the complete sql of specified PrepareStatementEx instance.
     * Otherwise return the sql with '?' used to create the specified PreparedStatementEx instantce.
     * Return null when the PreparedStatementEx instance is null.
     * @param pse the PreparedStatementEx instance to get sql.
     * @return the complete sql of specified PreparedStatementEx instance.
     * Return null when the PreparedStatementEx instance is null.
     */
  
    public static String getSql(PreparedStatementEx pse) {
        if (null == pse) {
            return null;
        } else {
            if (fullSql) {
                return pse.getSql();
            } else {
                return pse.getPreparedSql();
            }
        }
    }
   
    /**
     * Return the sql used to create the specified PrepareStatementEx instance.
     * Return null when the PreparedStatementEx instance is null.
     * @param pse the PreparedStatementEx instance to get sql.
     * @return the complete sql of specified PreparedStatementEx instance.
     * Return null when the PreparedStatementEx instance is null.
     */
    public static String getPreparedSql(PreparedStatementEx pse) {
        if (null == pse) {
            return null;
        } else {
            return pse.getPreparedSql();
        }
    }

    /**
     * Always return the complete sql of specified PrepareStatementEx instance.
     * Return null when the PreparedStatementEx instance is null.
     * @param pse the PreparedStatementEx instance to get sql.
     * @return the complete sql of specified PreparedStatementEx instance.
     * Return null when the PreparedStatementEx instance is null.
     */
    
    public static String getFullSql(PreparedStatementEx pse) {
        if (null == pse) {
            return null;
        } else {
            return pse.getSql();
        }
    }

    /**
     * Return the complete sql.
     * @return the complete sql.
     * @deprecated The current PreparedStatementEx instance maybe <tt>null</tt> because of createStatement exceptions, please use the static method - PreparedStatementEx.getSql(PreparedStatementEx pse).
     */
    public String getSql() {
        String[] splitSql = this.origSql.split("\\Q" + IN_PARAM_ESC + "\\E");
        StringBuffer compSql = new StringBuffer(splitSql[0]);
        Integer key = null;
        String value = IN_PARAM_ESC;
        for (int i = 1; i < splitSql.length && this.inMaxSqlLength(compSql.length()); i++) {
            key = new Integer(i);
            value = IN_PARAM_ESC;
            if (this.paramMap.containsKey(key)) {
                Object x = this.paramMap.get(key);
                value = getSqlString(x);
            }
            compSql.append(value);
            compSql.append(splitSql[i]);
        }
        key = new Integer(splitSql.length);
        value = this.origSql.lastIndexOf(IN_PARAM_ESC) == (this.origSql.length() - 1) ?
                IN_PARAM_ESC : "";
        if (this.paramMap.containsKey(key) && this.inMaxSqlLength(compSql.length())) {
            Object x = this.paramMap.get(key);
            value = getSqlString(x);
        }
        compSql.append(value);
       
        if (!this.inMaxSqlLength(compSql.length())) {
            compSql.delete(this.maxSqlLength, compSql.length());
            compSql.append(INCOMPLETE);
        }
       
        return compSql.toString();
    }
   
    private boolean inMaxSqlLength(int length) {
        if (this.maxSqlLength < 0) {
            return true;
        } else {
            return length <= this.maxSqlLength;
        }
    }
   
    private boolean inMaxParamValueLength(long length) {
        if (this.maxParamValueLength < 0) {
            return true;
        } else {
            return length <= this.maxParamValueLength;
        }
    }
   
    private byte[] convInputStream2Bytes(InputStream x, int length) {
        byte[] empty = new byte[0];
        try {
            if (x.markSupported()) {
                int available = Math.min(x.available(), length);
                x.mark(available);
                byte[] data = new byte[available];
                x.read(data, 0, available);
                x.reset();
                return data;
            } else {
                return empty;
            }
        } catch(IOException e) {
            return empty;
        }
    }
   
    private String convReader2String(Reader x, int length) {
        try {
            if (x.markSupported()) {
                int available = length;
                x.mark(available);
                char[] data = new char[available];
                x.read(data, 0, available);
                x.reset();
                return new String(data);
            } else {
                return "";
            }
        } catch (IOException e) {
            return "";
        }
    }
   
    protected String getSqlString(Object x) {
        String ret;
        if (null == x) {
            ret = NULL_VALUE;
        } else {
            if (x instanceof String) {
                ret = this.stringSql((String) x);
            } else if (x instanceof Array) {
                ret = this.arraySql((Array) x);
            } else if (x instanceof Date) {
                ret = this.dateSql((Date) x);
            } else if (x instanceof Time) {
                ret = this.timeSql((Time) x);
            } else if (x instanceof Timestamp) {
                ret = this.timestampSql((Timestamp) x);
            } else if (x instanceof Clob) {
                ret = this.clobSql((Clob) x);
            } else if (x instanceof Blob) {
                ret = this.blobSql((Blob) x);
            } else if (x instanceof byte[]) {
                ret = this.byteArraySql((byte[]) x);
            } else {
                ret = this.defaultSql(x);
            }
        }
       
        if (!this.inMaxParamValueLength(ret.length())) {
            ret = ret.substring(0, this.maxParamValueLength + 1);
            ret += INCOMPLETE;
        }
       
        return ret;
    }
   
    protected String stringSql(String x) {
        // ' -> ''
        StringBuffer r = new StringBuffer(x.replaceAll("\\Q\'\\E", "\'\'"));
        final char q = '\'';
        r.insert(0, q); 
        r.append(q);
       
        return r.toString();
    }
   
    protected String arraySql(Array x) {
        List<Object> list = null;
        try {
            list = Arrays.asList((Object[]) x.getArray());
        } catch (SQLException e) {
        }
        return null == list ? DATA_ERROR : list.toString();
    }
   
    protected String byteArraySql(byte[] x) {
        final int mark = 0xFF;
        final String sep = ", ";
        if (null == x) {
            return DATA_ERROR;
        }
        StringBuffer r = new StringBuffer("[");
        for (int i = 0; i < x.length && this.inMaxParamValueLength(r.length() - 1); i++) {
            String v;
            if (this.showHexByte) {
                v = Integer.toHexString(mark & x[i]);
                if (v.length() < 2) {
                    v = "0" + v;
                }
            } else {
                v = Integer.toString(x[i]);
            }
            r.append(v);
            r.append(sep);
        }
        if (r.length() > sep.length()) {
            r.delete(r.length() - sep.length(), r.length());
        }
        r.append(']');
       
        return r.toString();
    }
   
    protected String dateSql(Date x) {
        if (null == sdf) {
            sdf = new SimpleDateFormat();
        }
        sdf.applyPattern(this.dataFormatPattern);
        java.util.Date date = new java.util.Date(x.getTime());
        return sdf.format(date);
    }
   
    protected String timeSql(Time x) {
        if (null == sdf) {
            sdf = new SimpleDateFormat();
        }
        sdf.applyPattern(this.timeFormatPattern);
        java.util.Date time = new java.util.Date(x.getTime());
        return sdf.format(time);
    }
   
    protected String timestampSql(Timestamp x) {
        Pattern p = Pattern.compile("[S]{4,}");
        Matcher m = p.matcher(this.timestampFormatPattern);
        DecimalFormat df = new DecimalFormat("000000000");
        String nanoString = df.format(x.getNanos());
        StringBuffer fp = new StringBuffer(this.timestampFormatPattern);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            for (int i = start; i < end; i++) {
                char replacement = '0';
                int index = i - start;
                if (nanoString.length() > index) {
                    replacement = nanoString.charAt(index);
                }
                fp.setCharAt(i, replacement);
            }
        }
       
        if (null == sdf) {
            sdf = new SimpleDateFormat();
        }
        sdf.applyPattern(fp.toString());
        return sdf.format(x);
    }
   
    protected String clobSql(Clob x) {
        try {
            final String prefix = "CLOB(\'";
            final String suffix = "\')";
            long xLength = x.length();
            int length = Integer.MAX_VALUE < xLength ? Integer.MAX_VALUE : (int) xLength;
            if (!this.inMaxParamValueLength(length + prefix.length() + suffix.length())) {
                length = this.maxParamValueLength - prefix.length() - suffix.length();
            }
            StringBuffer r = new StringBuffer(prefix);
            if (length > 0) {
                r.append(x.getSubString(1, length));
            }
            r.append(suffix);
            return r.toString();
        } catch (SQLException e) {
            return DATA_ERROR;
        }
    }
   
    protected String blobSql(Blob x) {
        try {
            long xLength = x.length();
            int length = Integer.MAX_VALUE < xLength ? Integer.MAX_VALUE : (int) xLength;
            if (!this.inMaxParamValueLength(length << 2)) {
                length = this.maxParamValueLength >> 2;
            }
            byte[] data = x.getBytes(1, length);
            return this.byteArraySql(data);
        } catch (SQLException e) {
            return DATA_ERROR;
        }
    }
   
    protected String defaultSql(Object x) {
        return x.toString();
    }
   
    /**
     * Return the value of showHexByte. Default value is true.
     * @return the showHexByte
     */
    public boolean isShowHexByte() {
        return showHexByte;
    }

    /**
     * Set the value of showHexByte.
     * @param showHexByte the showHexByte to set
     */
    public void setShowHexByte(boolean showHexByte) {
        this.showHexByte = showHexByte;
    }

    /**
     * Return the value of maxParamValueLength
     * @return the maxParamValueLength
     */
    public int getMaxParamValueLength() {
        return maxParamValueLength;
    }

    /**
     * Set the value of maxParamValueLength.
     * @param maxParamValueLength the maxParamValueLength to set
     */
    public void setMaxParamValueLength(int maxParamValueLength) {
        this.maxParamValueLength = maxParamValueLength;
    }

    /**
     * Return the value of maxSqlLength
     * @return the maxSqlLength
     */
    public int getMaxSqlLength() {
        return maxSqlLength;
    }

    /**
     * Set the value of maxSqlLength.
     * @param maxSqlLength the maxSqlLength to set
     */
    public void setMaxSqlLength(int maxSqlLength) {
        this.maxSqlLength = maxSqlLength;
    }

    /**
     * Return the value of dataFormatPattern
     * @return the dataFormatPattern
     */
    public String getDataFormatPattern() {
        return dataFormatPattern;
    }

    /**
     * Set the value of dataFormatPattern.
     * @param dataFormatPattern the dataFormatPattern to set
     */
    public void setDataFormatPattern(String dataFormatPattern) {
        this.dataFormatPattern = dataFormatPattern;
    }

    /**
     * Return the value of timeFormatPattern
     * @return the timeFormatPattern
     */
    public String getTimeFormatPattern() {
        return timeFormatPattern;
    }

    /**
     * Set the value of timeFormatPattern.
     * @param timeFormatPattern the timeFormatPattern to set
     */
    public void setTimeFormatPattern(String timeFormatPattern) {
        this.timeFormatPattern = timeFormatPattern;
    }

    /**
     * Return the value of timestampFormatPattern
     * @return the timestampFormatPattern
     */
    public String getTimestampFormatPattern() {
        return timestampFormatPattern;
    }

    /**
     * Set the value of timestampFormatPattern.
     * @param timestampFormatPattern the timestampFormatPattern to set
     */
    public void setTimestampFormatPattern(String timestampFormatPattern) {
        this.timestampFormatPattern = timestampFormatPattern;
    }

    /**
     *
     * @throws SQLException
     *
     * (non-Javadoc)
     * @see java.sql.PreparedStatement#clearParameters()
     */
    public void clearParameters() throws SQLException {
        this.paramMap.clear();
        this.pStmt.clearParameters();
    }

    /**
     *
     * @param i
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
     */
    public void setArray(int i, Array x) throws SQLException {
        this.setParameter4Sql(i, x);
        this.pStmt.setArray(i, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @param length
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
     */
    public void setAsciiStream(int parameterIndex, InputStream x, int length)
            throws SQLException {
        this.setParameter4Sql(parameterIndex, this.convInputStream2Bytes(x, length));
        this.pStmt.setAsciiStream(parameterIndex, x, length);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
     */
    public void setBigDecimal(int parameterIndex, BigDecimal x)
            throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.pStmt.setBigDecimal(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @param length
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
     */
    public void setBinaryStream(int parameterIndex, InputStream x, int length)
            throws SQLException {
        this.setParameter4Sql(parameterIndex, this.convInputStream2Bytes(x, length));
        this.pStmt.setBinaryStream(parameterIndex, x, length);
    }

    /**
     *
     * @param i
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
     */
    public void setBlob(int i, Blob x) throws SQLException {
        this.setParameter4Sql(i, x);
        this.pStmt.setBlob(i, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setBoolean(int, boolean)
     */
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        this.setParameter4Sql(parameterIndex, new Boolean(x));
        this.pStmt.setBoolean(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setByte(int, byte)
     */
    public void setByte(int parameterIndex, byte x) throws SQLException {
        this.setParameter4Sql(parameterIndex, new Byte(x));
        this.pStmt.setByte(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setBytes(int, byte[])
     */
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.pStmt.setBytes(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param reader
     * @param length
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
     */
    public void setCharacterStream(int parameterIndex, Reader reader, int length)
            throws SQLException {
        this.setParameter4Sql(parameterIndex, this.convReader2String(reader, length));
        this.pStmt.setCharacterStream(parameterIndex, reader, length);
    }

    /**
     *
     * @param i
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
     */
    public void setClob(int i, Clob x) throws SQLException {
        this.setParameter4Sql(i, x);
        this.pStmt.setClob(i, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
     */
    public void setDate(int parameterIndex, Date x) throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.pStmt.setDate(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @param cal
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
     */
    public void setDate(int parameterIndex, Date x, Calendar cal)
            throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.pStmt.setDate(parameterIndex, x, cal);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setDouble(int, double)
     */
    public void setDouble(int parameterIndex, double x) throws SQLException {
        this.setParameter4Sql(parameterIndex, new Double(x));
        this.pStmt.setDouble(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setFloat(int, float)
     */
    public void setFloat(int parameterIndex, float x) throws SQLException {
        this.setParameter4Sql(parameterIndex, new Float(x));
        this.pStmt.setFloat(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setInt(int, int)
     */
    public void setInt(int parameterIndex, int x) throws SQLException {
        this.setParameter4Sql(parameterIndex, new Integer(x));
        this.pStmt.setInt(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setLong(int, long)
     */
    public void setLong(int parameterIndex, long x) throws SQLException {
        this.setParameter4Sql(parameterIndex, new Long(x));
        this.pStmt.setLong(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param sqlType
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setNull(int, int)
     */
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        this.setParameter4Sql(parameterIndex, null);
        this.pStmt.setNull(parameterIndex, sqlType);
    }

    /**
     *
     * @param paramIndex
     * @param sqlType
     * @param typeName
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
     */
    public void setNull(int paramIndex, int sqlType, String typeName)
            throws SQLException {
        this.setParameter4Sql(paramIndex, null);
        this.pStmt.setNull(paramIndex, sqlType, typeName);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
     */
    public void setObject(int parameterIndex, Object x) throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.pStmt.setObject(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @param targetSqlType
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType)
            throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.setObject(parameterIndex, x, targetSqlType);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @param targetSqlType
     * @param scale
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType,
            int scale) throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.setObject(parameterIndex, x, targetSqlType, scale);
    }

    /**
     *
     * @param i
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
     */
    public void setRef(int i, Ref x) throws SQLException {
        this.setParameter4Sql(i, x);
        this.pStmt.setRef(i, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setShort(int, short)
     */
    public void setShort(int parameterIndex, short x) throws SQLException {
        this.setParameter4Sql(parameterIndex, new Short(x));
        this.pStmt.setShort(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setString(int, java.lang.String)
     */
    public void setString(int parameterIndex, String x) throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.pStmt.setString(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
     */
    public void setTime(int parameterIndex, Time x) throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.pStmt.setTime(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @param cal
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
     */
    public void setTime(int parameterIndex, Time x, Calendar cal)
            throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.pStmt.setTime(parameterIndex, x, cal);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(int parameterIndex, Timestamp x)
            throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.pStmt.setTimestamp(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @param cal
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp, java.util.Calendar)
     */
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
            throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.setTimestamp(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
     */
    public void setURL(int parameterIndex, URL x) throws SQLException {
        this.setParameter4Sql(parameterIndex, x);
        this.pStmt.setURL(parameterIndex, x);
    }

    /**
     *
     * @param parameterIndex
     * @param x
     * @param length
     * @throws SQLException
     * @deprecated
     *
     *
     * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream, int)
     */
    public void setUnicodeStream(int parameterIndex, InputStream x, int length)
            throws SQLException {
        this.setParameter4Sql(parameterIndex, this.convInputStream2Bytes(x, length));
        this.pStmt.setUnicodeStream(parameterIndex, x, length);
    }

    /**
     *
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#addBatch()
     */
    public void addBatch() throws SQLException {
        this.pStmt.addBatch();
    }

    /**
     *
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#execute()
     */
    public boolean execute() throws SQLException {
        return this.pStmt.execute();
    }

    /**
     *
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() throws SQLException {
        return this.pStmt.executeQuery();
    }

    /**
     *
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException {
        return this.pStmt.executeUpdate();
    }

    /**
     *
     * @throws SQLException
     *
     *
     * @see java.sql.PreparedStatement#getMetaData()
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.pStmt.getMetaData();
    }

    /**
     *
     *
     * @see java.sql.PreparedStatement#getParameterMetaData()
     */
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return this.pStmt.getParameterMetaData();
    }

    /**
     *
     *
     * @see java.sql.Statement#addBatch(java.lang.String)
     */
    public void addBatch(String sql) throws SQLException {
        this.pStmt.addBatch(sql);
    }

    /**
     *
     * @throws SQLException
     *
     *
     * @see java.sql.Statement#cancel()
     */
    public void cancel() throws SQLException {
        this.pStmt.cancel();
    }

    /**
     *
     * @see java.sql.Statement#clearBatch()
     */
    public void clearBatch() throws SQLException {
        this.pStmt.clearBatch();
    }

    /**
     *
     * @see java.sql.Statement#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        this.pStmt.clearWarnings();
    }

    /**
     *
     * @see java.sql.Statement#close()
     */
    public void close() throws SQLException {
        this.pStmt.close();
    }

    /**
     *
     * @see java.sql.Statement#execute(java.lang.String)
     */
    public boolean execute(String sql) throws SQLException {
        return this.pStmt.execute(sql);
    }

    /**
     *
     * @see java.sql.Statement#execute(java.lang.String, int)
     */
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return this.pStmt.execute(sql, autoGeneratedKeys);
    }

    /**
     *
     * @see java.sql.Statement#execute(java.lang.String, int[])
     */
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return this.pStmt.execute(sql, columnIndexes);
    }

    /**
     *
     * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
     */
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return this.pStmt.execute(sql, columnNames);
    }

    /**
     *
     * @see java.sql.Statement#executeBatch()
     */
    public int[] executeBatch() throws SQLException {
        return this.pStmt.executeBatch();
    }

    /**
     *
     * @see java.sql.Statement#executeQuery(java.lang.String)
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        return this.pStmt.executeQuery(sql);
    }

    /**
     *
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     */
    public int executeUpdate(String sql) throws SQLException {
        return this.pStmt.executeUpdate(sql);
    }

    /**
     *
     * @see java.sql.Statement#executeUpdate(java.lang.String, int)
     */
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return this.pStmt.executeUpdate(sql, autoGeneratedKeys);
    }

    /**
     *
     * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
     */
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return this.pStmt.executeUpdate(sql, columnIndexes);
    }

    /**
     *
     * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
     */
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return this.pStmt.executeUpdate(sql, columnNames);
    }

    /**
     *
     * @see java.sql.Statement#getConnection()
     */
    public Connection getConnection() throws SQLException {
        return this.pStmt.getConnection();
    }

    /**
     *
     * @see java.sql.Statement#getFetchDirection()
     */
    public int getFetchDirection() throws SQLException {
        return this.pStmt.getFetchDirection();
    }

    /**
     *
     * @see java.sql.Statement#getFetchSize()
     */
    public int getFetchSize() throws SQLException {
        return this.pStmt.getFetchSize();
    }

    /**
     *
     * @see java.sql.Statement#getGeneratedKeys()
     */
    public ResultSet getGeneratedKeys() throws SQLException {
        return this.pStmt.getGeneratedKeys();
    }

    /**
     *
     * @see java.sql.Statement#getMaxFieldSize()
     */
    public int getMaxFieldSize() throws SQLException {
        return this.pStmt.getMaxFieldSize();
    }

    /**
     *
     * @see java.sql.Statement#getMaxRows()
     */
    public int getMaxRows() throws SQLException {
        return this.pStmt.getMaxRows();
    }

    /**
     *
     * @see java.sql.Statement#getMoreResults()
     */
    public boolean getMoreResults() throws SQLException {
        return this.pStmt.getMoreResults();
    }

    /**
     *
     * @see java.sql.Statement#getMoreResults(int)
     */
    public boolean getMoreResults(int current) throws SQLException {
        return this.pStmt.getMoreResults(current);
    }

    /**
     *
     * @see java.sql.Statement#getQueryTimeout()
     */
    public int getQueryTimeout() throws SQLException {
        return this.pStmt.getQueryTimeout();
    }

    /**
     *
     * @see java.sql.Statement#getResultSet()
     */
    public ResultSet getResultSet() throws SQLException {
        return this.pStmt.getResultSet();
    }

    /**
     *
     * @see java.sql.Statement#getResultSetConcurrency()
     */
    public int getResultSetConcurrency() throws SQLException {
        return this.pStmt.getResultSetConcurrency();
    }

    /**
     *
     * @see java.sql.Statement#getResultSetHoldability()
     */
    public int getResultSetHoldability() throws SQLException {
        return this.pStmt.getResultSetHoldability();
    }

    /**
     *
     * @see java.sql.Statement#getResultSetType()
     */
    public int getResultSetType() throws SQLException {
        return this.pStmt.getResultSetType();
    }

    /**
     *
     * @see java.sql.Statement#getUpdateCount()
     */
    public int getUpdateCount() throws SQLException {
        return this.pStmt.getUpdateCount();
    }

    /**
     *
     * @see java.sql.Statement#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        return this.pStmt.getWarnings();
    }

    /**
     *
     * @see java.sql.Statement#setCursorName(java.lang.String)
     */
    public void setCursorName(String name) throws SQLException {
        this.pStmt.setCursorName(name);
    }

    /**
     *
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    public void setEscapeProcessing(boolean enable) throws SQLException {
        this.pStmt.setEscapeProcessing(enable);
    }

    /**
     *
     * @see java.sql.Statement#setFetchDirection(int)
     */
    public void setFetchDirection(int direction) throws SQLException {
        this.pStmt.setFetchDirection(direction);
    }

    /**
     *
     * @see java.sql.Statement#setFetchSize(int)
     */
    public void setFetchSize(int rows) throws SQLException {
        this.pStmt.setFetchSize(rows);
    }

    /**
     *
     * @see java.sql.Statement#setMaxFieldSize(int)
     */
    public void setMaxFieldSize(int max) throws SQLException {
        this.pStmt.setMaxFieldSize(max);
    }

    /**
     *
     * @see java.sql.Statement#setMaxRows(int)
     */
    public void setMaxRows(int max) throws SQLException {
        this.pStmt.setMaxRows(max);
    }

    /**
     *
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    public void setQueryTimeout(int seconds) throws SQLException {
        this.pStmt.setQueryTimeout(seconds);
    }

    /**
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
     */
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        this.pStmt.setAsciiStream(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, long)
     */
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        this.pStmt.setAsciiStream(parameterIndex, x, length);
    }

    /**
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
     */
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        this.pStmt.setBinaryStream(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, long)
     */
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        this.pStmt.setBinaryStream(parameterIndex, x, length);
    }

    /**
     * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
     */
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        this.pStmt.setBlob(parameterIndex, inputStream);
    }

    /**
     * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
     */
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        this.pStmt.setBlob(parameterIndex, inputStream, length);
    }

    /**
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
     */
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        this.pStmt.setCharacterStream(parameterIndex, reader);
    }

    /**
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, long)
     */
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        this.pStmt.setCharacterStream(parameterIndex, reader, length);
    }

    /**
     * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
     */
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        this.pStmt.setClob(parameterIndex, reader);
    }

    /**
     * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
     */
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        this.pStmt.setClob(parameterIndex, reader, length);
    }

    /**
     * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
     */
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        this.pStmt.setNCharacterStream(parameterIndex, value);
    }

    /**
     * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader, long)
     */
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        this.pStmt.setNCharacterStream(parameterIndex, value, length);
    }

    /**
     * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
     */
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        this.pStmt.setNClob(parameterIndex, value);
    }

    /**
     * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
     */
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        this.pStmt.setNClob(parameterIndex, reader);
    }

    /**
     * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
     */
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        this.pStmt.setNClob(parameterIndex, reader, length);
    }

    /**
     * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
     */
    public void setNString(int parameterIndex, String value) throws SQLException {
        this.pStmt.setNString(parameterIndex, value);
    }

    /**
     * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
     */
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        this.pStmt.setRowId(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
     */
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        this.pStmt.setSQLXML(parameterIndex, xmlObject);
    }

    /**
     * @see java.sql.Statement#isClosed()
     */
    public boolean isClosed() throws SQLException {
        return this.pStmt.isClosed();
    }

    /**
     * @see java.sql.Statement#isPoolable()
     */
    public boolean isPoolable() throws SQLException {
        return this.pStmt.isPoolable();
    }

    /**
     * @see java.sql.Statement#setPoolable(boolean)
     */
    public void setPoolable(boolean poolable) throws SQLException {
        this.pStmt.setPoolable(poolable);
    }

    /**
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.pStmt.isWrapperFor(iface);
    }

    /**
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.pStmt.unwrap(iface);
    }

	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


}