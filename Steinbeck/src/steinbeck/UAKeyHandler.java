/*
 * UASSHKeyHandler.java
 *
 * Created on 05 Mayýs 2005 Perþembe, 10:40
 */

package steinbeck;

import com.jcraft.jsch.*;

/**
 *
 * @author Bedirhan Urgun
 */
public class UAKeyHandler {
    
    public static String publicKey;  
    
    /** Generates a RSA key pair, writes them to local files */
    // utilizes a singleton pattern 
    public static String GenerateKeys(String path) throws Exception{
        if(publicKey != null)
            return publicKey;
        
        JSch jsch=new JSch();
        KeyPair keypair = KeyPair.genKeyPair(jsch, KeyPair.RSA);
        // no passphrase
        keypair.setPassphrase("");
        keypair.writePrivateKey(path + "/UAKey");
        // no comment
        keypair.writePublicKey(path + "/UAKey"+".pub", "");
        
        byte[] pubblob = keypair.getPublicKeyBlob();
        
        keypair.dispose();
        
        byte[] pub = ToBase64(pubblob, 0, pubblob.length);          
        publicKey = new String(pub);
        
        return publicKey;
    }
        
    /** Converts public key to base64 form */
    private static byte[] ToBase64(byte[] buf, int start, int length){
                                                                                                                                                             
        byte[] b64 ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".getBytes();

        byte[] tmp=new byte[length*2];
        int i,j,k;
                                                                                                                                                             
        int foo=(length/3)*3+start;
        i=0;
        for(j=start; j<foo; j+=3){
            k=(buf[j]>>>2)&0x3f;
            tmp[i++]=b64[k];
            k=(buf[j]&0x03)<<4|(buf[j+1]>>>4)&0x0f;
            tmp[i++]=b64[k];
            k=(buf[j+1]&0x0f)<<2|(buf[j+2]>>>6)&0x03;
            tmp[i++]=b64[k];
            k=buf[j+2]&0x3f;
            tmp[i++]=b64[k];
        }
                                                                                                                                                             
        foo=(start+length)-foo;
        if(foo==1){
            k=(buf[j]>>>2)&0x3f;
            tmp[i++]=b64[k];
            k=((buf[j]&0x03)<<4)&0x3f;
            tmp[i++]=b64[k];
            tmp[i++]=(byte)'=';
            tmp[i++]=(byte)'=';
        }
        else if(foo==2){
            k=(buf[j]>>>2)&0x3f;
            tmp[i++]=b64[k];
            k=(buf[j]&0x03)<<4|(buf[j+1]>>>4)&0x0f;
            tmp[i++]=b64[k];
            k=((buf[j+1]&0x0f)<<2)&0x3f;
            tmp[i++]=b64[k];
            tmp[i++]=(byte)'=';
        }
        
        byte[] bar=new byte[i];
        System.arraycopy(tmp, 0, bar, 0, i);
        return bar;
  }

}
