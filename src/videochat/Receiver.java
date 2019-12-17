/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.ImageIcon;

/**
 *
 * @author Bert
 */
public class Receiver extends javax.swing.JFrame {

    /**
     * Creates new form Receiver
     */
    public Receiver() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        receiverImageHolder = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(receiverImageHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(receiverImageHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                new Receiver().setVisible(true);
            }
        });
        
        
        ServerSocket server = new ServerSocket(8080);
        Socket socket = server.accept();
        
        try (DataInputStream rcv = new DataInputStream(new BufferedInputStream(socket.getInputStream())))
        {
            while (true)
            {
                int frameWidth = rcv.readInt();
                int frameHeight = rcv.readInt();

                int[] pixelData = new int[frameWidth * frameHeight];

                for (int i = 0; i < pixelData.length; i++)
                {
                    pixelData[i] = rcv.readInt();
                }

                BufferedImage frame = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_RGB);
                frame.setRGB(0, 0, frameWidth, frameHeight, pixelData, 0, frameWidth);
                
                receiverImageHolder.setIcon(new ImageIcon(frame));   
            }
        }
        catch(Exception e)
        {
            System.out.println("Chat ended on sending side.");
            server.close();
            socket.close();
            System.exit(0);
        }
                
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel receiverImageHolder;
    // End of variables declaration//GEN-END:variables
}
