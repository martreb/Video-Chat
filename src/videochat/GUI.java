package videochat;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

 // @author Bert
public class GUI extends javax.swing.JFrame 
{   
    //public static Webcam webcam;
    
    /**
     * Creates new form GUI
     */
    public GUI() 
    {
        initComponents(); 
        setLocationRelativeTo(null);   //centers the box
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imageHolder = new javax.swing.JLabel();
        captureButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        captureButton.setText("Capture");
        captureButton.setToolTipText("");
        captureButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        captureButton.setName("imageHolder"); // NOI18N
        captureButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                captureButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imageHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(captureButton)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(imageHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(captureButton))
        );

        imageHolder.getAccessibleContext().setAccessibleName("imageHolder");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void captureButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_captureButtonActionPerformed
        Thread thread1 = new Thread(new VideoFeed());   //when the button "jbutton1" is clicked it will make a new thread
        thread1.start();   //the new thread runs the run method in the VideoFeed class
    }//GEN-LAST:event_captureButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() { 
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton captureButton;
    private javax.swing.JLabel imageHolder;
    // End of variables declaration//GEN-END:variables
    
    public class VideoFeed implements Runnable
    {
        @Override
        public void run()
        {
            try 
            {
                LiveFeed();
            } 
            catch (IOException | InterruptedException ex) 
            {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void LiveFeed() throws IOException, InterruptedException
    {        
        Webcam webcam = null;
        
        DataOutputStream sender = null;
                
	String ipAddress = JOptionPane.showInputDialog("Enter your ip address");	

        try
        {
            //DataOutputStream sender is made into a BufferedOutputStream using a new socket on localhost on port 8080
            sender = new DataOutputStream(new BufferedOutputStream(new Socket(ipAddress, 8080).getOutputStream()));   //change back to localhost
        }
        catch(IOException e)
        {
            
        }
        
        try
        {
            webcam = Webcam.getDefault();
            webcam.setViewSize(new Dimension(320,240));
            webcam.open();
        }
        catch(WebcamException e)
        {
            System.out.println("No webcam to use");
            System.exit(0);
        }
            
        while (true)   //this boolean needs to change
        {
            BufferedImage frame = webcam.getImage(); //get frame from webcam

            int frameWidth = frame.getWidth();
            int frameHeight = frame.getHeight();

            sender.writeInt(frameWidth);
            sender.writeInt(frameHeight);

            int[] pixelData = new int[frameWidth * frameHeight];
            frame.getRGB(0, 0, frameWidth, frameHeight, pixelData, 0, frameWidth);

            for (int i = 0; i < pixelData.length; i++)
            {
                sender.writeInt(pixelData[i]);
            }
            
            imageHolder.setIcon(new ImageIcon(frame));
                
            Thread.sleep(17);   //sleep for 17 milliseconds, so that then a new picture will be taken after that 
        }
    } 
} 

    


