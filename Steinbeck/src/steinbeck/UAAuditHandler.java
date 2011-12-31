/*
 * UAAuditHandler.java
 *
 * Created on May 6, 2005, 11:41 AM
 */

package steinbeck;

import java.io.*;
import java.util.*;
import com.jcraft.jsch.*;

/**
 *
 * @author Bedirhan Urgun
 */
public class UAAuditHandler {
    
    private Hashtable auditTable;
    private Vector IPList;
   
   
    /** Creates a new instance of UAAuditHandler*/
    public UAAuditHandler(){
        auditTable = new Hashtable();
        IPList = new Vector();
    }
    
    /** Creates a new instance of UAAuditHandler with a file having IPs at 
     * every line, it also gets a working local directory */
    public UAAuditHandler(String IPListFile) throws Exception {
        
        auditTable = new Hashtable();
        IPList = new Vector();
        
        // read the progress file
        File inputFile = new File(IPListFile);
        BufferedReader in = new BufferedReader(new FileReader(inputFile));
                
        String anIP;
        while ((anIP = in.readLine()) != null){
            // read each IPs at every line
            if(!IPList.contains(anIP))
                IPList.addElement(anIP);            
        }
        in.close();                
    }
    
    /** Adds an IP to the list of audits */
    public void addIP(String anIP){
        if(!IPList.contains(anIP))
            IPList.addElement(anIP);        
    }
    
    /** Adds an audit to the audit table */
    public void addAudit(String anIP, String username, String port, String passwd, String remotePath, String localPath, String scriptName, boolean checkProgress, boolean getResults) throws Exception{
        if(auditTable.containsKey(anIP+username+port+remotePath+localPath))
           return; 
        
        // create and add the related audit
        UAAudit anAudit = new UAAudit(anIP, username, port, passwd, remotePath, localPath, scriptName);
        
        if(!checkProgress)
            anAudit.DoNotCheckProgress();
        
        if(!getResults)
            anAudit.DoNotGetResults();
        
        auditTable.put(anIP+username+port+remotePath+localPath, anAudit);
    }
    
   
    /** Removes an audit from the audit table */
    public void removeAudit(String key){
        auditTable.remove(key);
    }
    
    /** Removes all audits from the table */
    public void removeAllAudits(){
        auditTable.clear();
    }
    
    /** Starts an audit */
    public void startAudit(String key){
        if(!auditTable.containsKey(key))
            return;
        
        // start the related audit if it has not been run yet
        if(!((UAAudit)auditTable.get(key)).isAlive())
            ((UAAudit)auditTable.get(key)).start();
    }
    
    /** Stops an audit */
    public void stopAudit(String key){
        if(!auditTable.containsKey(key))
            return;
        
        // stop the related audit if it has been run and has not been finished
        if(((UAAudit)auditTable.get(key)).isAlive())
            ((UAAudit)auditTable.get(key)).stop();   
    }
    
    /** Starts all the audits */
    public void startAllAudits(){      
        Iterator audits = auditTable.values().iterator();
        while(audits.hasNext()){
            UAAudit anAudit = (UAAudit)audits.next();
            if(!anAudit.isAlive()){
                anAudit.start();
            }
        }        
    }
    
    /** Stops all the audits */
    public void stopAllAudits(){                      
        Iterator audits = auditTable.values().iterator();
        while(audits.hasNext()){
            UAAudit anAudit = (UAAudit)audits.next();
            if(anAudit.isAlive())
                anAudit.stop();
        } 
     
    }
    
    /** Get audit's progress */
    public int getProgress(String key){
        if(!auditTable.containsKey(key))
            return 0;
        return ((UAAudit)auditTable.get(key)).getProgress();
    }
    
    /** Returns the number of audits */
    public int getNumberOfAudits(){
        return auditTable.size();
    }
    
    /** Returns all the audits */
    public Iterator getAllAudits(){
        return auditTable.values().iterator();
    }
    
}
