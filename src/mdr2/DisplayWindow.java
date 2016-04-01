/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Administrator
 */
class DisplayWindow extends JPanel 
{
    JTextArea screen;
    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem, fileitem;

    public DisplayWindow(JTextArea screen) 
    {
        super(new BorderLayout());
        this.screen = screen;
        JPanel menuPanel = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new GridLayout(0,1));
        
        screen.setEditable(false);
        screen.setWrapStyleWord(true);
        screen.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(screen);
        
        menuBar = new JMenuBar();
        
        menu = new JMenu("File");
        fileitem = new JMenuItem("Exit Program",
                                 KeyEvent.VK_T);
        
        menu.add(fileitem);
        menuBar.add(menu);
        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);
        
        menuItem = new JMenuItem("Manual Load",
                                 KeyEvent.VK_T);
        menu.add(menuItem);
        
        
        
        menuPanel.add(menuBar);
        panel2.add(scroll);
        
        add(menuPanel, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        fileitem.addActionListener(new exitProgram());
    }
    
    public class exitProgram implements ActionListener 
    {
            @Override
            public void actionPerformed (ActionEvent e) 
            {
                /*
               Email email = new Email("Market Data Reader 1 Application has "
                       + "been Intentionally closed!!!!","WARNING Market Data "
                       + "Reader Has been Closed");
               email.sendMail();
                        */
               System.exit(0);
            }
    }
    
}
