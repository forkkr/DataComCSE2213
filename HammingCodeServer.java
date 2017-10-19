package hammingcodeserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class HammingCodeServer {

    static ServerSocket server;
    static Socket client;
    static PrintWriter dout;
    static BufferedReader in, stdIn;
    static DataInputStream is;

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(5555);
        client = server.accept();
        System.out.println("client");
        FileWriter write = new FileWriter("output.txt");
        dout = new PrintWriter(client.getOutputStream());
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        is = new DataInputStream(client.getInputStream());
          String msg;
          int cnt =0;
        while (true) {
            msg = is.readUTF();
            String outmsg =  new StringBuffer(msg).reverse().toString();
            System.out.println("Received Massage: "+msg);
            if (msg.equals( "stop")) {
                 System.out.println("Received Massage: close");
                write.close();
                break;
            }
               msg = paritChecking(msg);
               String s=getCharacter(msg);
               if(s.equals("*"))
                 write.write(System.getProperty("line.separator"));
                else
               write.write(s);
               
            } 
        }
    
    static String paritChecking(String msg) {
        int parityPosition[] ={1 ,2 ,4,8};
       ArrayList<Integer> list = new ArrayList<Integer>(); 
        boolean flag = false;
        for(int i = 0; i < 4;i++)
        {
             int count = function(parityPosition[i],msg);
             if(count != msg.charAt(parityPosition[i]-1)-'0')
             {
                list.add(parityPosition[i]);
                 flag |= true;
             }
        }
        int sum =0;
        if(flag)
        {
            System.out.println("Error: " + msg);
            for(int i =0; i <list.size();i++)
            {
            sum +=list.get(i);
            System.out.print(list.get(i)+" ");
            }
            System.out.println("Error Occurred at: " + (sum));
           char[] ar  = msg.toCharArray();
           ar[sum-1]= '0';
           msg = String.valueOf(ar);
           System.out.println("Corrected: " + msg);
            
        }
        return msg;
    }

    public static int function(int num,String msg) {
        int count;
        if(num == 1)
        {
           count = (msg.charAt(3-1)-'0')^(msg.charAt(5-1)-'0') ^(msg.charAt(7-1)-'0') ^(msg.charAt(9-1)-'0') ^(msg.charAt(11-1)-'0');
        }
        else if(num == 2)
        {
            count = (msg.charAt(3-1)-'0')^(msg.charAt(6-1)-'0') ^(msg.charAt(7-1)-'0') ^(msg.charAt(10-1)-'0') ^(msg.charAt(11-1)-'0');
        }
        else if(num==4)
        {
            count = (msg.charAt(5-1)-'0')^(msg.charAt(6-1)-'0') ^(msg.charAt(7-1)-'0') ^(msg.charAt(12-1)-'0');
        }
        else
        {
            count = (msg.charAt(9-1)-'0') ^(msg.charAt(10-1)-'0') ^(msg.charAt(11-1)-'0') ^(msg.charAt(12-1)-'0');
        }
        return count;
    }

    static String getCharacter(String msg) {
       int num = 0;
       String tmp ="";
       for(int i =0;i<12 ;i++)
       {
           if(i==0||  i == 1|| i==3 ||i==7)
           {
               continue;
           }
          tmp +=msg.charAt(i);
       }
       
     System.out.println(tmp);
     int p = 1;
     for(int i = 0; i < tmp.length(); i++)
     {
         num += p*(tmp.charAt(i)-'0');
         p = p*2;
     }
      
        char ch = (char) num;
        String ret = Character.toString(ch);
        System.out.println(ret+" Character");
        return ret;
    }

}


