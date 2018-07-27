package webCam;

import org.bytedeco.javacv.*;

import static org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;


public class WebCamViewer implements Runnable {
    final int INTERVAL = 100;///you may use interval
    CanvasFrame canvas = new CanvasFrame("Live Mode");
    IplImage img;
    String savePath;

    public WebCamViewer(String savePath) {
    	this.savePath = savePath;
    	 KeyListener listener = new KeyListener() {
             
 			@Override
 			public void keyTyped(KeyEvent e) {
 				// TODO Auto-generated method stub
 				switch (e.getKeyCode()) {
			       
 		        case KeyEvent.VK_ENTER:
 		        	/*String artNum = JOptionPane.showInputDialog("Article Number");
 		        	saveImage(artNum);*/
 		            break;
 		        case KeyEvent.VK_BACK_SPACE:
 		        
 		            break;
 		        default:
 		           
 		    }
 			}

 			@Override
 			public void keyPressed(KeyEvent e) {
 				// TODO Auto-generated method stub
 				switch (e.getKeyCode()) {
 			       
 		        case KeyEvent.VK_ENTER:
 		        	String artNum = JOptionPane.showInputDialog("Article Number");
 		        	saveImage(artNum);
 		            break;
 		        case KeyEvent.VK_BACK_SPACE:
 		        
 		            break;
 		        default:
 		           
 		    }
 			}

 			@Override
 			public void keyReleased(KeyEvent e) {
 				// TODO Auto-generated method stub
 				switch (e.getKeyCode()) {
 			       
 		        case KeyEvent.VK_ENTER:
 		        	/*String artNum = JOptionPane.showInputDialog("Article Number");
 		        	saveImage(artNum);*/
 		            break;
 		        case KeyEvent.VK_BACK_SPACE:
 		        
 		            break;
 		        default:
 		           
 		    }
 			}

         };
     	canvas.addKeyListener(listener);
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    	
    }
    
    public void saveImage(String articleNumber) {
    	cvSaveImage(this.savePath +"/"+articleNumber+ ".jpg", this.img);
    
    }
    
    public void run() {

     //  FrameGrabber grabber = new VideoInputFrameGrabber(0);
      FrameGrabber grabber = new OpenCVFrameGrabber(0);
        
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        
        try {
            grabber.start();
            while (true) {
                Frame frame = grabber.grab();

                this.img = converter.convert(frame);

                cvFlip(this.img, this.img, 1);
                canvas.showImage(converter.convert(this.img));
                
                 
               // Thread.sleep(INTERVAL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
				grabber.stop();
			} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

}
