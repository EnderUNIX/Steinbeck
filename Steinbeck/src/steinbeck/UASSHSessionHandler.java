/*
 * UASSHSessionHandler.java
 *
 * Created on 05 Mayýs 2005 Perþembe, 12:04
 */

package steinbeck;

import com.jcraft.jsch.*;

/**
 *
 * @author Bedirhan Urgun
 */
public class UASSHSessionHandler {
    
    /** Creates a new SSH session based on password */
    public static Session CreatePasswordBasedSession(String username, String IP, String port, String passwd) throws Exception{
        JSch jsch=new JSch();
        
        Session session = jsch.getSession(username, IP, Integer.parseInt(port));
        session.setPassword(passwd);
        
        // no checking on known_host keys
        java.util.Hashtable config=new java.util.Hashtable();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        return session;
    }
    
    /** Creates a new SSH session based on RSA keys */
    public static Session CreateRSABasedSession(String username, String IP, String port, String path) throws Exception{
        JSch jsch=new JSch();
        jsch.addIdentity(path + "/UAKey");

        Session session = jsch.getSession(username, IP, Integer.parseInt(port));
        // no checking on known_host keys
        java.util.Hashtable config=new java.util.Hashtable();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        return session;
    }
    
}
