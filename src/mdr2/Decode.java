/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTextArea;

/**
 *
 * @author Administrator
 */
public class Decode implements Runnable
{
    private final LinkedList<DatabaseObject> decodedData;
    private final ReentrantLock decodedDataLock;
    private final LinkedList<byte[]> rawData;
    private final ReentrantLock rawDataLock;
    private final LinkedList<byte[]> parsedData = new LinkedList<>();
    private final JTextArea screen;
    private final boolean option;
    private DecodeMessage decode;
    
    Decode(LinkedList<DatabaseObject> decodedData, ReentrantLock decodedDataLock, LinkedList<byte[]> rawData, ReentrantLock rawDataLock, JTextArea screen, boolean option) 
    {
        this.decodedData = decodedData;
        this.decodedDataLock = decodedDataLock;
        this.rawData = rawData;
        this.rawDataLock = rawDataLock;
        this.screen = screen;
        this.option = option;
    }
    
    @Override
    public synchronized void run()
    {
        decode = new DecodeMessage(decodedData, option);
        
        while(true)
            {
                if(rawData.isEmpty())
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
                        rawDataLock.lock();
                        ParseData(rawData.remove());
                    }
                    finally
                    {
                        rawDataLock.unlock();  
                    }

                    try
                    {
                        decodedDataLock.lock();
                        while(!parsedData.isEmpty())
                        {
                            byte[] data = parsedData.remove();
                            decode.decode(data);                       
                        }

                    }
                    finally
                    {
                        decodedDataLock.unlock();  
                    }
                }
            }
    }
    
    private void ParseData(byte[] data)
    {
        int counter = 1;
        final ByteBuffer buffer = ByteBuffer.wrap(data);
        byte[] newArr = null;
        byte[] headerArr = new byte[12];
        byte[] head;
        byte[] messageCount = new byte[1];
      
        head = Arrays.copyOf(data, 12);
        buffer.get(headerArr);
        
        while (buffer.hasRemaining()) 
        {
                  
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.mark();
            int length = buffer.getShort() & 0xffff;
            if (length == 0) {
                break;
            }
            //System.out.println(length);
            newArr = new byte[length];
            buffer.reset();
            buffer.get(newArr, 0, length);
            
            messageCount[0] = (byte)counter;
            counter++; 
            
            byte[] completeArray;
        
            completeArray = new byte[headerArr.length + messageCount.length + newArr.length];
            for(int i = 0; i < headerArr.length; i++)
            {
              completeArray[i] = headerArr[i];  
            }

            for(int i = headerArr.length, j = 0; j < newArr.length; i++, j++)
            {
              completeArray[i] = newArr[j];  

              if(i == (completeArray.length - 1))
              {
                completeArray[i + 1] = messageCount[0];  
              }
            }
            
            parsedData.add(completeArray);
        }
    }
}
