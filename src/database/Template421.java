/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class Template421 
{
    private final String tableName;
    private final ArrayList colData;
    private final Properties prop;

    public Template421(String tableName, ArrayList colData, Properties prop) 
    {
        this.tableName = tableName;
        this.colData = colData;
        this.prop = prop;
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
        BigDecimal tag52 = time.convert(colData.get(1).toString());
        BigDecimal tag60 = time.convert(colData.get(4).toString());
        
        try 
        {
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(tableName);
            
            for(int i = 6; i < colData.size(); i = i + 2)
            {
                stmt.setInt(1, Integer.parseInt(colData.get(0).toString()));
                stmt.setBigDecimal(2, tag52);
                stmt.setInt(3, Integer.parseInt(colData.get(2).toString()));
                stmt.setString(4, colData.get(3).toString());
                stmt.setBigDecimal(5, tag60);
                stmt.setString(6, colData.get(5).toString());
                
                if(colData.get(i + 0) == null)//
                {
                    stmt.setLong(7, java.sql.Types.BIGINT);//size
                    stmt.setInt(8, java.sql.Types.INTEGER);//secid
                }
                else
                {
                    stmt.setLong(7, Long.parseLong(colData.get(i + 0).toString()));
                    stmt.setInt(8, Integer.parseInt(colData.get(i + 1).toString()));
                }
                
                stmt.addBatch();
                //System.out.println(stmt);
            }
            
            stmt.executeBatch();
            conn.commit();
        } 
        catch (SQLException ex) 
        {
            System.out.println("Class database.Template421 error: " + ex);
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
                System.out.println("Class database.Template421 error: " + ex);
            }
        }
    }
    
}
