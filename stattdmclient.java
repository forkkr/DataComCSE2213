import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Student
 */
public class stattdmclient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
       Socket echoSocket = new Socket("172.16.13.207", 5555);
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
        int l[]=new int[5];
        for(i=0;i<5;i++)
        {
            l[i]=0;
        }
        String temp;
        String co;
       
        int max=l[0];
        for(i=1;i<5;i++)
        {
            max=Math.max(s[i].length(), max);
        }
        while(l[0]<s[0].length()||l[1]<s[1].length()||l[2]<s[2].length()||l[3]<s[3].length()||l[4]<s[4].length())
        {
            Random rd=new Random();
            int inputn=rd.nextInt(6);
            temp="start";
            for(i=0;i<5;i++)
            {
                if(i==inputn)
                    continue;
      
                temp+="#"+i+"#"+(10+i)+"#";
                if(l[i]+10<s[i].length())
                temp+=s[i].substring(l[i], l[i]+10);
                else if(l[i]<s[i].length())
                temp+=s[i].substring(l[i],s[i].length());
                l[i]+=10;
            }
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