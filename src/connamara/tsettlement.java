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
public class tsettlement 
{
    private final String tableName;
    private final ArrayList colData;
    private final Properties prop;
    private final boolean option;

    public tsettlement(String tableName, ArrayList colData, Properties prop, boolean option) 
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
        String tag60 = time.convert(Long.parseLong(colData.get(4).toString().substring(0, 13)));
        
        String query;
        
        if(option)
        {
            query = "INSERT INTO connamara_mgex_test.tsettlement_119 (seq_num,"
                    + "security_id,settlement_price,settlement_date,settlement_time) "
                    + "VALUES (?,?,?,?,?)";
        }
        else
        {
            query = "INSERT INTO connamara_mgex_test.tsettlement_118 (seq_num,"
                    + "security_id,settlement_price,settlement_date,settlement_time) "
                    + "VALUES (?,?,?,?,?)";
        }
        
        
        try 
        {
            
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(query);
            
            for(int i = 6; i < colData.size(); i = i + 8)
            {
                stmt.setInt(1, Integer.parseInt(colData.get(0).toString()));//seq_num
                stmt.setInt(2, Integer.parseInt(colData.get(i + 2).toString()));//security_id
                stmt.setDouble(3, Double.parseDouble(colData.get(i + 0).toString()));//settlement_price
                stmt.setInt(4, Integer.parseInt(tag60.substring(0, 8)));//settlement_date
                stmt.setInt(5, Integer.parseInt(tag60.substring(8, 14)));//settlement_time
                
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            conn.commit();
        } 
        catch (SQLException ex) 
        {
            System.out.println("Class connamara.tsettlement error: " + ex);
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
                System.out.println("Class connamara.tsettlement error: " + ex);
            }
        }
    }
}
