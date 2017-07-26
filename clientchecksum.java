import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class clientchecksum {
    public static void main(String[] args) throws IOException {
        Socket echoSocket = new Socket("192.168.3.104", 5555);
        System.out.println("serverconnected");
        DataInputStream din=new DataInputStream(echoSocket.getInputStream());
        DataOutputStream dout=new DataOutputStream(echoSocket.getOutputStream());
        FileReader fr=new FileReader("input.txt");
        Scanner fsc=new Scanner(fr);
        int i,j,sum=0,mod;
        String check;
        Random ran=new Random();
        while(fsc.hasNext())
        {
            sum=0;
            String s=fsc.nextLine();
            System.out.println("string "+s);
            for(i=0;i<s.length();i++)
            {
                sum+=s.charAt(i);
            }
            System.out.println(s);
            do {
                mod= ran.nextInt(16);
                String temp=s+"#"+mod;
                System.out.println(temp);
                dout.writeUTF(temp);
                dout.flush();
                check=din.readUTF();
            }
            while(check.equals("error"));
        }
        dout.writeUTF("stop");
        fr.close();
        echoSocket.close();
        dout.close();
        din.close();
    }
}
