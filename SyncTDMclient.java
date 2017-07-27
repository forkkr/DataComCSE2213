
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Student
 */
public class SyncTDMclient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
       Socket echoSocket = new Socket("172.16.13.197", 5555);
        System.out.println("serverconnected");
        DataInputStream din = new DataInputStream(echoSocket.getInputStream());
        DataOutputStream dout = new DataOutputStream(echoSocket.getOutputStream());
        FileReader fr[]=new FileReader[5];
        Scanner fsc[]=new Scanner[5];
        String s[]=new String[5];
        int i,j,k;
        for(i=0;i<5;i++)
        {
            fr[i]=new FileReader("input"+i+".txt");
            fsc[i]=new Scanner(fr[i]);
        }
        for(i=0;i<5;i++)
        {
            s[i]=fsc[i].nextLine();
            while(fsc[i].hasNext())
            {
             s[i]+="*"+fsc[i].nextLine();
            }
        }
        String temp;
        String co;
        int t=0;
        for(k=0;k<5;k++)
        {
            temp="start";
            for(i=0;i<5;i++)
            {
                temp+="#"+i+"#"+(10+i)+"#";
                temp+=s[i].substring(t, t+10);
            }
            t+=10;
            temp+="#end";
            System.out.println(temp);
            dout.writeUTF(temp);
            dout.flush();
        }
        dout.writeUTF("stop");
        dout.close();
        for(i=0;i<5;i++)
        {
            fr[i].close();
        }
        echoSocket.close();
    }

}
