/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connamara;

import database.GetConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class tlasttrade 
{
    private final String tableName;
    private final ArrayList colData;
    private final Properties prop;
    private final boolean option;

    public tlasttrade(String tableName, ArrayList colData, Properties prop, boolean option) 
    {
        this.tableName = tableName;
        this.colData = colData;
        this.prop = prop;
        this.option = option;
    }
    
    public void execute() 
    {
        GetConnection connect = new GetConnection();
        connect.setJDBC_CONNECTION_URL(prop.getProperty("JDBC_CONNECTION_URL"));
        connect.setMySQLDB_URL(prop.getProperty("MySQLDB_URL"));
        connect.setMySQLUser(prop.getProperty("MySQLUser"));
        connect.setMySQLPassword(prop.getProperty("MySQLPassword"));
        
        Connection conn = connect.getConnection();
        PreparedStatement stmt = null;
        
        UnixtoStandardTime time = new UnixtoStandardTime();
        String tag52 = time.convert(Long.parseLong(colData.get(1).toString().substring(0, 13)));
        String tag60 = time.convert(Long.parseLong(colData.get(4).toString().substring(0, 13)));
        
        String query;
        
        if(option)
        {
            query = "INSERT INTO connamara_mgex_test.tlasttrade_119 (seq_num,"
                    + "security_id,trade_px,trade_qty,bb_px,bb_qty,bo_px,bo_qty,"
                    + "trade_date,trade_time,sending_time,rpt_seq) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        else
        {
            query = "INSERT INTO connamara_mgex_test.tlasttrade_118 (seq_num,"
                    + "security_id,trade_px,trade_qty,bb_px,bb_qty,bo_px,bo_qty,"
                    + "trade_date,trade_time,sending_time,rpt_seq) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        
        
        try 
        {
            
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(query);
            
            for(int i = 6; i < colData.size(); i = i + 8)
            {
                stmt.setInt(1, Integer.parseInt(colData.get(0).toString()));
                stmt.setInt(2, Integer.parseInt(colData.get(i + 2).toString()));
                stmt.setDouble(3, Double.parseDouble(colData.get(i + 0).toString()));
                stmt.setInt(4, Integer.parseInt(colData.get(i + 1).toString()));
                stmt.setDouble(5, 0.0);
                stmt.setInt(6, 0);
                stmt.setDouble(7, 0.00);
                stmt.setInt(8, 0);
                stmt.setInt(9, Integer.parseInt(tag60.substring(0, 8)));
                stmt.setInt(10, Integer.parseInt(tag60.substring(8, 14)));
                stmt.setLong(11, Long.parseLong(tag52));
                stmt.setInt(12, Integer.parseInt(colData.get(i + 3).toString()));
                
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            conn.commit();
        } 
        catch (SQLException ex) 
        {
            System.out.println("Class connamara.tlasttrade error: " + ex);
        }
        finally
        {
            try 
            {
                stmt.close();
                conn.close();
            } 
            catch (SQLException ex) 
            {
                System.out.println("Class connamara.tlasttrade error: " + ex);
            }
        }
    }
}
