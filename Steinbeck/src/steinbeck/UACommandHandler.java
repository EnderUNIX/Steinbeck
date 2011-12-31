/*
 * UASSHCommandHandler.java
 *
 * Created on 05 Mayýs 2005 Perþembe, 12:06
 */

package steinbeck;

import com.jcraft.jsch.*;

import java.io.*;
/**
 *
 * @author Bedirhan Urgun
 */
public class UACommandHandler {
    
    /** Securely copies local source file to remote destination file */
    public void SecureCopyTo(Session session, String sourceFile, String destinationFile) 
                                                        throws Exception{
      // exec 'scp -t destinationFile' remotely
      String command="scp -p -t " + destinationFile;
      Channel channel=session.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);
                                                                                                                                                             
      // get I/O streams for remote scp
      OutputStream out=channel.getOutputStream();
      InputStream in=channel.getInputStream();
                                                                                                                                                             
      channel.connect();
                                                                                                                                                             
      byte[] tmp=new byte[1];
                                                                                                                                                             
      if(checkAck(in)!=0){
        System.exit(0);
      }
                                                                                                                                                             
      // send "C0500 filesize filename", where filename should not include '/'
      int filesize=(int)(new File(sourceFile)).length();
      // 500 -> r-x------
      command="C0500 " + filesize + " ";
      if(sourceFile.lastIndexOf('/') > 0){
        command += sourceFile.substring(sourceFile.lastIndexOf('/') + 1);
      }
      else{
        command += sourceFile;
      }
      command += "\n";
      out.write(command.getBytes()); out.flush();

      if(checkAck(in)!=0){
        System.exit(0);
      }
                                                                                                                                                             
      // send the content of the sourceFile
      FileInputStream fis = new FileInputStream(sourceFile);
      byte[] buf=new byte[1024];
      while(true){
        int len = fis.read(buf, 0, buf.length);
        if(len <= 0) break;
        out.write(buf, 0, len); out.flush();
      }
                                                                                                                                                             
      // send '\0'
      buf[0]=0; out.write(buf, 0, 1); out.flush();
                                                                                                                                                             
      if(checkAck(in)!=0){
        System.exit(0);
      }    
    }

    /** Securely copies remote source file to local destination file */
    public void SecureCopyFrom(Session session, String sourceFile, String destinationFile) 
                                                        throws Exception{
      // exec 'scp -f rfile' remotely
      String command="scp -f " + sourceFile;
      Channel channel=session.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);
 
      // get I/O streams for remote scp
      OutputStream out=channel.getOutputStream();
      InputStream in=channel.getInputStream();
                                                                                                                                                             
      channel.connect();
                                                                                                                                                             
      byte[] buf=new byte[1024];
                                                                                                                                                             
      // send '\0'
      buf[0]=0; out.write(buf, 0, 1); out.flush();
      while(true){
        int c=checkAck(in);
        if(c != 'C'){
          break;
        }
                                                                                                                                                             
        // read '0644 '
        in.read(buf, 0, 5);
                                                                                                                                                             
        int filesize=0;
        while(true){
          in.read(buf, 0, 1);
          if(buf[0] == ' ') break;
          filesize=filesize*10 + (buf[0]-'0');
        }
                                                                                                                                                             
        String file=null;
        for(int i=0;;i++){
          in.read(buf, i, 1);
          if(buf[i]==(byte)0x0a){
            file=new String(buf, 0, i);
            break;
          }
        }
                                                                                                                                                             
        // send '\0'
        buf[0]=0; out.write(buf, 0, 1); out.flush();
                                                                                                                                                             
        // write the content of destinationFile
        FileOutputStream fos=new FileOutputStream(destinationFile);

        int foo;
        while(true){
          if(buf.length<filesize) foo=buf.length;
          else foo=filesize;
          in.read(buf, 0, foo);
          fos.write(buf, 0, foo);
          filesize-=foo;
          if(filesize==0) break;
        }
        fos.close();
        byte[] tmp=new byte[1];
                                                                                                                                                             
        if(checkAck(in)!=0){
          System.exit(0);
        }
                                                                                                                                                             
        // send '\0'
        buf[0]=0; out.write(buf, 0, 1); out.flush();
      }    
    }
    
    
    /** accepted by heart, as it is */
    private int checkAck(InputStream in) throws Exception{
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if(b == 0) return b;
        if(b == -1) return b;
                                                                                                                                                             
        if(b == 1 || b == 2){
            StringBuffer sb=new StringBuffer();
            int c;
            do {
                c=in.read();
                sb.append((char)c);
            }
            while(c!='\n');
            if(b == 1){ // error
                throw new Exception(sb.toString());
            }
            if(b == 2){ // fatal error
                throw new Exception(sb.toString());
            }
        }
        return b;
    }
    
    
    /** Executes a given command on the ssh session, mkdir, ls,  
     *  WAITS FOR THE ANSWER */
    public String ExecuteCommandWait(Session session, String command) 
                                                        throws Exception{

        // open an 'exec' channel on the session and set the command
        Channel channel=session.openChannel("exec");
        // "\\" in front of the command discards the alias defined on the 
        // remote box
        ((ChannelExec)channel).setCommand("\\"+command);
        InputStream in = channel.getInputStream();
        channel.connect();
        String result = ReadChannel(channel, in);
        channel.disconnect();
        
        return result;
    }
    
    /** Executes a given command on the ssh session, mkdir, ls,  
     *  DOES NOT WAIT FOR THE ANSWER */
    public void ExecuteCommandNoWait(Session session, String command)
                                                        throws Exception{
        // open an 'exec' channel on the session and set the command
        Channel channel=session.openChannel("exec");
        // "\\" in front of the command discards the alias defined on the 
        // remote box
        ((ChannelExec)channel).setCommand("\\"+command);
        InputStream in = channel.getInputStream();
        channel.connect();
        channel.disconnect();
    }        
    
    private String ReadChannel(Channel channel, InputStream in) 
                                                        throws IOException{
        StringBuffer sb = new StringBuffer();
        byte[] tmp=new byte[1024];
        int count = 0;
        while(true){
            if(channel.isEOF() && in.available()<=0) break;
            while(in.available() > 0){
                int i = in.read(tmp, 0, 1024);
                if(i < 0) break;
                sb.append(new String(tmp, 0, i));
            }
            count++;
        }
        return sb.toString();
    }
       
}
