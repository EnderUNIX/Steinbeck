/*
 * UAAudit.java
 *
 * Created on May 5, 2005, 3:46 PM
 */
package steinbeck;

import com.jcraft.jsch.*;
import java.io.*;

/**
 *
 * @author Bedirhan Urgun
 */
public class UAAudit implements Runnable{
    
    // remote machine auditor will audit
    private String IP;    
    private String port;
    private String username;
    private String passwd;
    
    // local path that auditor will use
    private String localPath;
    // remote path that auditor will use
    private String remotePath;
    // name and path of the auditor script
    private String scriptName;
    
    // auditor thread
    private Thread auditorThread;
        
    // 1-100 
    private int progress;
    
    // should check progress
    boolean checkProgress;
    
    // should get results
    boolean getResults;
    
    /** Creates a new instance of UAAudit */
    public UAAudit(String IP, String username, String port, String passwd, String remotePath, String localPath, String scriptName){
        this.IP = IP;
        this.username = username;
        this.port = port;
        this.passwd = passwd;
        
        // check whether remotePath has an ending / char
        if(!remotePath.endsWith("/"))
            remotePath = remotePath + "/";
        this.remotePath = remotePath;
        
        // check whether localPath has an ending / char
        if(!localPath.endsWith("/"))
            localPath = localPath + "/";
        this.localPath = localPath;
        
        this.scriptName = scriptName;
                
        progress = 0;
        checkProgress = true;
        getResults = true;
    }
    
    protected void DoNotCheckProgress(){
        checkProgress = false;
    }
    
    protected void CheckProgress(){
        checkProgress = true;
    }
    
    protected void GetResults(){
        getResults = true;
    }
    
    protected void DoNotGetResults(){
        getResults = false;
    }    
    
    public void start(){
        if(auditorThread == null){
            auditorThread = new Thread(this, IP);
            // auditorThread.setPriority(Thread.MIN_PRIORITY);
            auditorThread.start();
        }
    }
    
    public void stop(){
        auditorThread = null;
    }
    
    public boolean isAlive(){
        if(auditorThread == null)
            return false;
        return auditorThread.isAlive();
    }
    
    private void printInfo(){
        System.out.println(IP);
        System.out.println(port);
        System.out.println(username);
        System.out.println(passwd);
        System.out.println(localPath);
        System.out.println(remotePath);
    }
    
    public void run(){
        try{
            
            // printInfo();
            
            // get a passwd session
            Session session = UASSHSessionHandler.CreatePasswordBasedSession(username, IP, port, passwd);

            session.connect();
                                               
            // remove the directory if it has already existed 
            new UACommandHandler().ExecuteCommandWait(session, "rm -rf " + remotePath);
            // make a directory on the remote machine
            new UACommandHandler().ExecuteCommandWait(session, "mkdir " + remotePath);
            // copy the auditor
            new UACommandHandler().SecureCopyTo(session, scriptName, remotePath + scriptName);
            // run the auditor (run the script but do not wait for the answer)
            new UACommandHandler().ExecuteCommandNoWait(session, "cd " + remotePath + ";./" + scriptName + " > results.txt");
            

            // HERE THE AUDITOR HAS TO SAMPLE THE PROGRESS.TXT ON THE REMOTE MACHINE ONCE IN A WHILE
            boolean auditorDone = false;
            
            // thread stuff
            Thread thisThread = Thread.currentThread();
            
            while(!auditorDone && thisThread == auditorThread){
                
                try{
                    Thread.sleep(2000);
                }
                catch(InterruptedException ie){
                    if(UAGUIEntrance.gui != null)
                        UAGUIEntrance.gui.showErrorMessage("Error occurred during thread execution. Suggestion: Restart sessions");
                    else
                        System.out.println("Exception caught:(interrupt)" + ie.getMessage());
                }
                
                // should we get the progress 
                if(checkProgress){
                    try{
                        // get the progress file
                        new UACommandHandler().SecureCopyFrom(session, remotePath + "progress.txt", localPath + IP + "progress.txt");
                                                
                        // read the progress file
                        File inputFile = new File(localPath + IP + "progress.txt");
                        BufferedReader in = new BufferedReader(new FileReader(inputFile));
                    
                        String c;
                        while ((c = in.readLine()) != null){
                            progress = Integer.parseInt(c);
                            if(progress == 100) {
                                auditorDone = true;
                                break;
                            }
                        }            
                        in.close();
                    }
                    catch(Exception e){
                        // here again stopping responsibility falls into the hands of user
                        System.out.println("Can not securely copy progress.txt from IP " + IP + " with script " + scriptName + " and error message: " + e.getMessage());
                        System.out.println("\tIf this message keeps displaying, you are on your own to stop this scan and get the results");
                        System.out.println("\tSuggestion: make sure your script produces progress.txt!");
                    }
                }
            }
            
            // should we get the results back 
            if(getResults){
                try{
                    // copy the resuls into [IP]results.txt
                    new UACommandHandler().SecureCopyFrom(session, remotePath + "results.txt", localPath + IP + "results.txt");                
                }
                catch(Exception e){
                    // here we finished the script (audit), but could not securely copy results
                    System.out.println("Can not securely copy results.txt from IP " + IP + " and error message: " + e.getMessage());
                    System.out.println("\tThis means you are on your own to get results (if exists there) from the remote machine");
                    System.out.println("\tSuggestion: make sure your script produces results.txt!");                        
                }
            }
            
            // either somebody called stop this thread, or audit is finished, 
            // we have to disconnect, otherwise, program will not quit gracefully ;)
            session.disconnect();    
            
        }
        catch(Exception e){
            if(UAGUIEntrance.gui != null)
                UAGUIEntrance.gui.showErrorMessage("Error occurred during session execution with IP " + IP + " and error message: " + e.getMessage() + ".\n Suggestion: Restart session.");
            else
                System.out.println("Error occurred during session execution with IP " + IP + " and error message: " + e.getMessage() + ".\n Suggestion: Restart session ");
        }
        
    }
        
    public int getProgress(){
        return this.progress;
    }
    
    public String getIP(){
        return this.IP;
    }
    
}
