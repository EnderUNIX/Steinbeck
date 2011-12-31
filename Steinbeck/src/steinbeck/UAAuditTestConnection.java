/*
 * UAAuditTestConnection.java
 *
 * Created on May 23, 2005, 10:10 AM
 */

package steinbeck;

import com.jcraft.jsch.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public class UAAuditTestConnection {

    // remote machine auditor will audit
    private String IP;    
    private String port;
    private String username;
    private String passwd;
    
    // local path that auditor will use
    private String localPath;
    // remote path that auditor will use
    private String remotePath;
    // name of the auditor script
    private String scriptName;    
    
    /** Creates a new instance of UAAuditTestConnection */
    public UAAuditTestConnection(String IP, String username, String port, String passwd, String remotePath, String localPath, String scriptName){
        
        this.IP = IP;
        this.username = username;
        this.port = port;
        this.passwd = passwd;
        
        // check whether remotePath has an ending / char
        if(!remotePath.endsWith("/"))
            remotePath = remotePath + "/";
        this.remotePath = remotePath;

        // check if there is an ending / in localPath
        if(!localPath.endsWith("/"))
            localPath = localPath + "/";

        this.localPath = localPath;
        this.scriptName = scriptName;
        
    }
    
    public boolean testConnection(){
        Session session = null;
        try{
            // get a passwd session
            session = UASSHSessionHandler.CreatePasswordBasedSession(username, IP, port, passwd);

            session.connect();
                       
            // remove the directory if it has already existed 
            new UACommandHandler().ExecuteCommandWait(session, "rm -rf " + remotePath);
            // make a directory on the remote machine
            new UACommandHandler().ExecuteCommandWait(session, "mkdir " + remotePath);
            // copy the auditor
            new UACommandHandler().SecureCopyTo(session, scriptName, remotePath + scriptName);
        }
        // I had to use finally stmt.
        catch(Exception e){

            if(UAGUIEntrance.gui != null)
                UAGUIEntrance.gui.showErrorMessage("Error occurred during session connection test with IP " + IP + " and error message: " + e.getMessage() + ".\n Suggestion: Edit your configuration and press Done again.");
            else
                System.out.println("Error occurred during session connection test with IP " + IP + " and error message: " + e.getMessage() + ".\n Suggestion: Edit your configuration and press Done again.");

            session.disconnect();        
            return false;
        }

        session.disconnect();
        
        return true;
    }
        
}
