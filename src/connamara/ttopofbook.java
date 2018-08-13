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
public class ttopofbook 
{
    private final String tableName;
    private final ArrayList colData;
    private final Properties prop;
    private final boolean option;

    public ttopofbook(String tableName, ArrayList colData, Properties prop, boolean option) 
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
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        
        UnixtoStandardTime time = new UnixtoStandardTime();
        String tag52 = time.convert(Long.parseLong(colData.get(1).toString().substring(0, 13)));
        String tag60 = time.convert(Long.parseLong(colData.get(4).toString().substring(0, 13)));

        String query = "INSERT INTO connamara_mgex_test.ttopofbook_119 (security_id,"
                        + "trade_date,bid_px,bid_qty,bid_entry_time_utc,"
                        + "bid_event_time_utc,bid_is_implied,bid_is_exchange_best,"
                        + "bid_sending_time_utc,rpt_seq) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        String query1 = "INSERT INTO connamara_mgex_test.ttopofbook_119 (security_id,"
                        + "trade_date,offer_px,offer_qty,offer_entry_time_utc,"
                        + "offer_event_time_utc,offer_is_implied,offer_is_exchange_best,"
                        + "offer_sending_time_utc,rpt_seq) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        String query2 = "INSERT INTO connamara_mgex_test.ttopofbook_118 (security_id,"
                        + "trade_date,bid_px,bid_qty,bid_entry_time_utc,"
                        + "bid_event_time_utc,bid_is_implied,bid_is_exchange_best,"
                        + "bid_sending_time_utc,rpt_seq) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        String query3 = "INSERT INTO connamara_mgex_test.ttopofbook_118 (security_id,"
                        + "trade_date,offer_px,offer_qty,offer_entry_time_utc,"
                        + "offer_event_time_utc,offer_is_implied,offer_is_exchange_best,"
                        + "offer_sending_time_utc,rpt_seq) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?)";
                
        try 
        {
            
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(query);
            stmt1 = conn.prepareStatement(query1);
            stmt2 = conn.prepareStatement(query2);
            stmt3 = conn.prepareStatement(query3);
            
            for(int i = 6; i < colData.size(); i = i + 8)
            {
                int bid = Integer.parseInt(colData.get(i + 7).toString());

                if(option)
                {
                    if(bid == 0 || bid == 2)
                    {
                        stmt.setInt(1, Integer.parseInt(colData.get(i + 2).toString()));
                        stmt.setInt(2, Integer.parseInt(tag60.substring(0, 8)));
                        stmt.setDouble(3, Double.parseDouble(colData.get(i + 0).toString()));
                        stmt.setInt(4, Integer.parseInt(colData.get(i + 1).toString()));
                        stmt.setInt(5, Integer.parseInt(tag52.substring(8, 14)));
                        stmt.setInt(6, Integer.parseInt(tag60.substring(8, 14)));
                        stmt.setInt(7, Integer.parseInt(colData.get(i + 5).toString()));
                        stmt.setInt(8, Integer.parseInt(colData.get(i + 5).toString()));
                        stmt.setInt(9, Integer.parseInt(colData.get(i + 3).toString()));
                        stmt.setLong(10, Long.parseLong(tag52));

                        stmt.addBatch();
                    }
                    else if(bid == 1 || bid == 3)
                    {
                        stmt1.setInt(1, Integer.parseInt(colData.get(i + 2).toString()));
                        stmt1.setInt(2, Integer.parseInt(tag60.substring(0, 8)));
                        stmt1.setDouble(3, Double.parseDouble(colData.get(i + 0).toString()));
                        stmt1.setInt(4, Integer.parseInt(colData.get(i + 1).toString()));
                        stmt1.setInt(5, Integer.parseInt(tag52.substring(8, 14)));
                        stmt1.setInt(6, Integer.parseInt(tag60.substring(8, 14)));
                        stmt1.setInt(7, Integer.parseInt(colData.get(i + 5).toString()));
                        stmt1.setInt(8, Integer.parseInt(colData.get(i + 5).toString()));
                        stmt1.setInt(9, Integer.parseInt(colData.get(i + 3).toString()));
                        stmt1.setLong(10, Long.parseLong(tag52));
                        
                        stmt1.addBatch();
                    }
                }
                else
                {
                    if(bid == 0 || bid == 2)
                    {
                        stmt2.setInt(1, Integer.parseInt(colData.get(i + 2).toString()));
                        stmt2.setInt(2, Integer.parseInt(tag60.substring(0, 8)));
                        stmt2.setDouble(3, Double.parseDouble(colData.get(i + 0).toString()));
                        stmt2.setInt(4, Integer.parseInt(colData.get(i + 1).toString()));
                        stmt2.setInt(5, Integer.parseInt(tag52.substring(8, 14)));
                        stmt2.setInt(6, Integer.parseInt(tag60.substring(8, 14)));
                        stmt2.setInt(7, Integer.parseInt(colData.get(i + 5).toString()));
                        stmt2.setInt(8, Integer.parseInt(colData.get(i + 5).toString()));
                        stmt2.setInt(9, Integer.parseInt(colData.get(i + 3).toString()));
                        stmt2.setLong(10, Long.parseLong(tag52));
                        
                        stmt2.addBatch();
                    }
                    else if(bid == 1 || bid == 3)
                    {
                        stmt3.setInt(1, Integer.parseInt(colData.get(i + 2).toString()));
                        stmt3.setInt(2, Integer.parseInt(tag60.substring(0, 8)));
                        stmt3.setDouble(3, Double.parseDouble(colData.get(i + 0).toString()));
                        stmt3.setInt(4, Integer.parseInt(colData.get(i + 1).toString()));
                        stmt3.setInt(5, Integer.parseInt(tag52.substring(8, 14)));
                        stmt3.setInt(6, Integer.parseInt(tag60.substring(8, 14)));
                        stmt3.setInt(7, Integer.parseInt(colData.get(i + 5).toString()));
                        stmt3.setInt(8, Integer.parseInt(colData.get(i + 5).toString()));
                        stmt3.setInt(9, Integer.parseInt(colData.get(i + 3).toString()));
                        stmt3.setLong(10, Long.parseLong(tag52));
                        
                        stmt3.addBatch();
                    }
                }
                
            }
            
            stmt.executeBatch();
            stmt1.executeBatch();
            stmt2.executeBatch();
            stmt3.executeBatch();
            conn.commit();
        } 
        catch (SQLException ex) 
        {
            System.out.println("Class connamara.ttopofbook error: " + ex);
        }
        finally
        {
            try 
            {
                stmt.close();
                stmt1.close();
                stmt2.close();
                stmt3.close();
                conn.close();
            } 
            catch (SQLException ex) 
            {
                System.out.println("Class connamara.ttopofbook error: " + ex);
            }
        }
    }
}
