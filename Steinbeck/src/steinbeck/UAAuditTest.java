/*
 * UAEntrance.java
 *
 * Created on 05 Mayýs 2005 Perþembe, 09:16
 */

package steinbeck;

import com.jcraft.jsch.*;
import java.util.*;

/**
 *
 * @author Bedirhan URGUN
 */
public class UAAuditTest {
     
    /** Creates a new instance of UAEntrance */
    public UAAuditTest() {
    }
    
    /** Command-line handler */
    /*
    public static void main(String argv[]) throws Exception{
                
        try{
            // SCRIPTPATH IS DEPRECATED
            // test
        
            // local path where auditor will use          
            //String localPath = "C:/temp"; // for windows
            String localPath = System.getProperty("user.dir");
            // String localPath = "/tmp/"; // for unix, I'm lazy enough to not to get it from std
            
            // remote path where auditor will use
            String remotePath = "/tmp/auditor";
            
            String rawScript = "C:/temp/a/a.txt";
            
            int indexOfLastSlash = rawScript.lastIndexOf("/");
        
            String scriptName = rawScript.substring(indexOfLastSlash+1);
            String scriptPath = rawScript.substring(0, indexOfLastSlash);
                    
            UAAuditHandler auditHandler = new UAAuditHandler(localPath);
            
            auditHandler.addAudit("192.168.56.asdfw", "root","22", "hehe", remotePath, scriptPath, scriptName, true, true);
            
            // auditHandler.addAudit("192.168.236.sdfs", "root","22", "hehe", remotePath, scriptName);
            
            //auditHandler.addAudit("192.168.4234.xvy", "root","22", "hehe", remotePath, scriptName);
            
            auditHandler.startAllAudits();
            
            Thread.sleep(100000);

            auditHandler.stopAllAudits();
            
            System.out.println("stopped all");           

        }
        catch(Exception e){
            System.out.println("Exception caught: " + e.getMessage());
        }
        System.out.println("exiting");
    }
    */
    
    
}
