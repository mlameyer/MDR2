/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author Administrator
 */
public class MDR2 extends JFrame{

    private static FuturesFeed fut;
    private static OptionsFeed opt;
    private static final JTextArea screen = new JTextArea();
    private static final Properties prop = new Properties();
    
    /**
     * @param args the command line arguments
     */
    private static void mainWindow()
    {
        MDR2 mainwindow = new MDR2();
        mainwindow.setMinimumSize(new Dimension(1200, 600)); 
        mainwindow.setResizable(false);
        mainwindow.setTitle("Market Data Reader 2"); 
        mainwindow.add(new DisplayWindow(screen));
        mainwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainwindow.pack();
        mainwindow.setLocationRelativeTo(null);
        mainwindow.setVisible(true);
    }
    
    public static void main(String[] args) throws IOException {  
	String propFileName = "C:\\ProgramData\\MDR\\configMDR2.properties";

	InputStream inputStream = new FileInputStream(propFileName);
        prop.load(inputStream);
        
        mainWindow();
        
        new Thread(new FuturesFeed(prop, screen)).start();
        new Thread(new OptionsFeed(prop, screen)).start();
         
    }
    
}
