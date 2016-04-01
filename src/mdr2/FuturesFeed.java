/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTextArea;

/**
 *
 * @author Administrator
 */
class FuturesFeed implements Runnable
{
    private final Properties prop;
    private String channelName, ip, socket;
    private final String threadName = "Futures Feed";
    private JTextArea screen;
    private final boolean option = false;
    private final LinkedList<byte[]> rawData = new LinkedList<>();
    private static final ReentrantLock rawDataLock = new ReentrantLock();
    private final LinkedList<DatabaseObject> decodedData = new LinkedList<>();
    private static final ReentrantLock decodedDataLock = new ReentrantLock();

    FuturesFeed(Properties prop, JTextArea screen) 
    {
        this.prop = prop;
        this.screen = screen;
        channelName = prop.getProperty("ChannelName2");
        ip = prop.getProperty("ConnectionID460IAIP");
        socket = prop.getProperty("ConnectionID460IASocket");
    }

    @Override
    public void run()
    {
        screen.append("Reader Futures A Feed Started "  + new Date() + "\n\n");
        screen.append("Channel " + channelName + " " + new Date() + "\n"
                        + "Channel IP: " + ip + "\nChannel Socket: " + socket + "\n\n");
        screen.append("Thread " + threadName + " running\n\n");
        ExecutorService executor = Executors.newFixedThreadPool(25);
        for(int i = 0; i < 1; i++)
        {
            Runnable reader = new UDPConnection(ip, socket, rawData, rawDataLock, screen, threadName);
            executor.execute(reader);
        }  
       
        for(int i = 0; i < 1; i++)
        {
            Runnable reader = new Decode(decodedData, decodedDataLock, rawData, rawDataLock, screen, option);
            executor.execute(reader);
        }
      
        for(int i = 0; i < 1; i++)
        {
            Runnable reader = new WritetoDatabase(prop, decodedData, decodedDataLock, screen, option);
            executor.execute(reader);
        }

    }
    
}
