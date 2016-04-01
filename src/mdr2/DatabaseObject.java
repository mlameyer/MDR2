/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class DatabaseObject 
{
    private int template;
    private String tableName;
    private ArrayList colData;
    
    public int getTemplate()
    {
        return template;
    }
    
    public void setTemplate(int newTemplate)
    {
        template = newTemplate;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName(String newTableName)
    {
        tableName = newTableName;
    }
    
    public ArrayList getColData()
    {
        return colData;
    }
    
    public void setColData(ArrayList newColData)
    {
        colData = newColData;
    }
    
    public void print()
    {
        System.out.println(template);
        System.out.println(tableName);
        System.out.println(colData);
    }
}
