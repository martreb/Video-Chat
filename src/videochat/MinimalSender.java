package videochat;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/*
 * @author Bert
 */
public class MinimalSender 
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        Webcam webcam = null;
        DataOutputStream sender = null;
        
        try
        {
            sender = new DataOutputStream(new BufferedOutputStream(new Socket("127.0.0.1", 8080).getOutputStream()));   //change ip to laptop ip   
        }
        catch(IOException e)
        {
            
        }
        
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(320,240));
        webcam.open();
        
        while (true) 
        {
            BufferedImage frame = webcam.getImage(); 

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
                            
            Thread.sleep(30);    
        }

    }
}
