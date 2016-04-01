/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTextArea;

/**
 *
 * @author Administrator
 */
class WritetoDatabase implements Runnable 
{
    private final Properties prop;
    private final LinkedList<DatabaseObject> decodedData;
    private final ReentrantLock decodedDataLock;
    private final JTextArea screen;
    private final DatabaseTemplate template;
    private final boolean option;

    public WritetoDatabase(Properties prop, LinkedList<DatabaseObject> decodedData, ReentrantLock decodedDataLock, JTextArea screen, boolean option) 
    {
        this.prop = prop;
        this.decodedData = decodedData;
        this.decodedDataLock = decodedDataLock;
        this.screen = screen;
        this.option = option;
        this.template = new DatabaseTemplate(prop, screen, option);
    }
    
    @Override
    public synchronized void run()
    {
        while(true)
        {
            if(decodedData.isEmpty())
            {
                try 
                {
                    Thread.sleep(5000);
                } 
                catch (InterruptedException ex) 
                {
                    System.out.println("Decode thread sleep error: " + ex);
                }
            }
            else
            {
                try
                {
                    decodedDataLock.lock();
                    template.template(decodedData.remove()); 
                }
                finally
                {
                    decodedDataLock.unlock();  
                }
            }
        }
    }
    
}
