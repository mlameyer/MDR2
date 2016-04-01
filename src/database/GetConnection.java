/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class GetConnection 
{
    private String JDBC_CONNECTION_URL;
    private String MySQLDB_URL;
    private String MySQLUser;
    private String MySQLPassword;
    
    public void setJDBC_CONNECTION_URL(String newJDBC_CONNECTION_URL)
    {
        JDBC_CONNECTION_URL = newJDBC_CONNECTION_URL;
    }
    public void setMySQLDB_URL(String newMySQLDB_URL)
    {
        MySQLDB_URL = newMySQLDB_URL;
    }
    public void setMySQLUser(String newMySQLUser)
    {
        MySQLUser = newMySQLUser;
    }
    public void setMySQLPassword(String newMySQLPassword)
    {
        MySQLPassword = newMySQLPassword;
    }
    
    public Connection getConnection() {
        Connection connect = null;
	try 
        {
            Class.forName(JDBC_CONNECTION_URL);
            connect = DriverManager.getConnection(MySQLDB_URL,MySQLUser,MySQLPassword);       
	} 
        catch (ClassNotFoundException | SQLException ex) 
        {
            System.out.println("GetConnection class error: " + ex);
        }
        return connect;
    }
}
