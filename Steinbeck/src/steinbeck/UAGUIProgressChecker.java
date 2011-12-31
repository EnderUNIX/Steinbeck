/*
 * UAGUIProgressChecker.java
 *
 * Created on May 18, 2005, 11:39 AM
 */

package steinbeck;

import javax.swing.*;
import java.util.*;

/**
 *
 * @author Administrator
 */
public class UAGUIProgressChecker implements Runnable{
    
    // progress checker thread
    private Thread progressCheckerThread;
    
    // parent progress frame
    private UAGUIProgress parentProgressFrame;
 
    public void start(){
        if(progressCheckerThread == null){
            progressCheckerThread = new Thread(this, "progress checker");
            progressCheckerThread.start();
        }
    }
    
    public void stop(){
        progressCheckerThread = null;
    }
    
    public boolean isAlive(){
        if(progressCheckerThread == null)
            return false;
        return progressCheckerThread.isAlive();
    }
    
    /** Creates a new instance of UAGUIProgressChecker */
    public UAGUIProgressChecker(UAGUIProgress parentProgressFrame) {
        this.parentProgressFrame = parentProgressFrame;
    }
       
    public void run(){
        
        // all the progresses are not done yet (we barely started dude ;))
        boolean allDone = false;
        
        // thread stuff
        Thread thisThread = Thread.currentThread();
            
        int progressesThatAreDone = 0;
        // as long as;
        // "there is one progress is going on" and "our thread is not stopped"
        while(!allDone && thisThread == progressCheckerThread){
            // sleep some
            try{
                Thread.sleep(2000);
            }
            catch(InterruptedException ie){
                System.out.println("Exception caught:(interrupt)" + ie.getMessage()); 
            }
            
            // assume all are done
            allDone = true;
            
            // get vector of progress bars to update
            Vector progressBars = parentProgressFrame.getProgressBars();
            // get vector of keys
            Vector keys = parentProgressFrame.getKeys();
            
            // check the progresses and update the related progress bar
            for(int i = 0; i < progressBars.size(); i++){
                // get the next progress bar
                JProgressBar aProgressBar = (JProgressBar)(progressBars.elementAt(i));
                // get its key
                String key = (String)(keys.elementAt(i));
                // set the new value of progress on the bar
                int currentProgress = parentProgressFrame.getProgress(key);
                aProgressBar.setValue(currentProgress);
                
                // if even one of them is not equal to 100, that means we are not done yet
                if(currentProgress != 100)
                    allDone = false;
                
            }            
        }
        
        // this means all the progresses are completed, not the thread is stopped
        if(allDone){
            // closes parent progress frame and shows main entrance            
            parentProgressFrame.showDone();
            UAGUIEntrance.gui.setVisible(true);
            parentProgressFrame.dispose();
        }
        
    }
    
}
