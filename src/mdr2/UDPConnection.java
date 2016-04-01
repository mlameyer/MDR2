/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTextArea;

/**
 *
 * @author Administrator
 */
class UDPConnection implements Runnable 
{
    private ReentrantLock rawDataLock;
    private final byte[] bytes = new byte[(int)10000];
    private InetAddress group; 
    private MulticastSocket s;
    private DatagramPacket packet;
    private LinkedList<byte[]> rawData;
    private JTextArea screen;
    private String threadName;
    
    public UDPConnection(String ip, String socket, LinkedList<byte[]> rawData, ReentrantLock rawDataLock, JTextArea screen, String threadName) 
    {
        this.packet = new DatagramPacket(bytes, bytes.length);
        try {
            this.s = new MulticastSocket(Integer.parseInt(socket));
            this.group = InetAddress.getByName(ip);
            this.rawDataLock = rawDataLock;
            this.rawData = rawData;
            this.screen = screen;
            this.threadName = threadName;
        } catch (IOException ex) 
        {
            System.out.println("IOException in UDPConnection Constructor: " + ex);
            screen.append("IOException in UDPConnection Constructor: " + ex + " " + new Date() + "\n");
        }
    }
    
    @Override
    public void run() 
    {
        try
        {
            s.joinGroup(group);
            
            while(true)
            {
                try
                {
                    s.receive(packet);
                    byte[] readData = Arrays.copyOf(packet.getData(), packet.getLength());
                    rawDataLock.lock();
                    rawData.addLast(readData);
                }
                finally
                {
                    rawDataLock.unlock();  
                    if(Thread.currentThread().isAlive() == false)
                    {
                        System.out.println("Thread failure for : " + threadName);
                        screen.append("Thread failure for : " + threadName + " " + new Date() + "\n");
                        //Email email = new Email("Thread Failure Check Market Data Reader: " + group, "Market Data Reader 1 Failure: " + Thread.currentThread().getName());
                        //email.sendMail();
                    }
                }
            }
        }
        catch (IOException ex) {
            System.out.println("IOException in UDPConnection run: " + ex);
            screen.append("IOException in UDPConnection run: " + ex + " " + new Date() + "\n");
        }        
    }
    
}
