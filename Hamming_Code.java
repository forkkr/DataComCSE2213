package datacom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author student
 */

public class Hamming_Code {
    /**
     * 
     * @param args the command line arguments
     */
    static String setparity(int n,String s,int a[])
    {       
           int i,sum=0;
           for(i=0;i<a.length;i++)
           {
               sum+=Integer.parseInt(""+s.charAt(a[i]-1));
           }
           if(sum%2==1)
           {
                
                char[] myNameChars = s.toCharArray();
                //if(n!=1)
                myNameChars[n] = '1';
                //else
                  //  myNameChars[n] = '0';
                s = String.valueOf(myNameChars);
           }
           return s;
    }
    static String makeeightbit(String s)
    {
        int i,n;
        n=8-s.length();
        String a="";
        for(i=0;i<n;i++)
        {
            a+="0";
        }
        s=a+s;
       // System.out.println("sub s 8 "+s);
        return s;
    }
    static String maketwelvebit(String s)
    {
        //System.out.println("s"+s);
        int i,n;
        int a[]={0,1,3,7};
        String temp="00"+s.charAt(0);
        //System.out.println("sub s"+temp);
        temp=temp+"0"+s.substring(1,4);
        //System.out.println("sub s"+temp);
        temp=temp+"0"+s.substring(4,8);
        //System.out.println("sub s"+temp);
        return temp;
        
    }
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        int firstparity[]={3,5,7,9,11};
        int secondparity[]={3,6,7,10,11};
        int thirdparity[]={5,6,7,12};
        int forthparity[]={8,9,10,11,12};
        Socket echoSocket = new Socket("172.16.13.206", 5555);
//          PrintWriter out =new PrintWriter(echoSocket.getOutputStream(), true);
//          BufferedReader in =new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
//          BufferedReader stdIn =new BufferedReader(new InputStreamReader(System.in));
        DataInputStream din = new DataInputStream(echoSocket.getInputStream());
        DataOutputStream dout = new DataOutputStream(echoSocket.getOutputStream());
        FileReader fr = new FileReader("input.txt");
        int i, a, f, d;
        char c;
        Scanner sc = new Scanner(fr);
        Scanner in = new Scanner(System.in);
        String s="";
        while(sc.hasNext())
        {
            String temp = sc.nextLine();
            s+=temp+"*";
        }
        for (i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            System.out.println(c);
            d = c;
            System.out.println("ascii " +d);
            Object checkedNumber = d;
            BigInteger val = new BigInteger(String.valueOf(checkedNumber));
            val = val.abs();
            int count = val.bitCount();
            String binaryString = val.toString(2);
            
        //    System.out.println("count = " + count);
            //System.out.println("bin = " + binaryString);
            binaryString=makeeightbit(binaryString);
            binaryString=new StringBuffer(binaryString).reverse().toString();
            System.out.println("without parity "+ binaryString);
            binaryString=maketwelvebit(binaryString);
            //System.out.println(binaryString);
            binaryString=setparity(0,binaryString,firstparity);
            binaryString=setparity(1,binaryString,secondparity);
            binaryString=setparity(3,binaryString,thirdparity);
            binaryString=setparity(7,binaryString,forthparity);
            Random ran=new Random();
            int value;
            value = ran.nextInt(10);
            
            char[] myNameChars = binaryString.toCharArray();
           
            System.out.println("position "+value+" "+myNameChars[value]);
                myNameChars[value] = '1';
                binaryString = String.valueOf(myNameChars);
            String temp=new StringBuffer(binaryString).reverse().toString();
            System.out.println("parity "+ temp);
            
//            a = d << 1;
//            if (count % 2 == 1) {
//                f = a | 0;
//            } else {
//                f = a | 1;
//            }
            
            //String str = Integer.toString(f);
            //System.out.println(str);
            dout.writeUTF(binaryString);
            //dout.flush();
        }
        dout.writeUTF("stop");
        
        while (true) {

        }

        //System.out.println("echo: " + in.readLine());       
//        fr.close();    
    }
}