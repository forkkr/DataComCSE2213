package datatelecom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

class Server {

    static ServerSocket server;
    static Socket client;
    static PrintWriter dout;
    static BufferedReader in, stdIn;
    static DataInputStream is;

   public static void main(String[] args) throws IOException
   {
       
        server = new ServerSocket(5555);
        client = server.accept();
        System.out.println("client");
        dout = new PrintWriter(client.getOutputStream());
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        is = new DataInputStream(client.getInputStream());
        String msg;
        while (true) {
            msg = is.readUTF();
            //System.out.println(msg +" meg");
            if(msg=="stop")
            {
                break;
                
            }
            if (paritChecking(msg)) {
                System.out.print(getCharacter(msg));
            }
            else
            {
            System.out.println("Error Found !!!");
                
            }
        }
        
    }

   

    static boolean paritChecking(String msg) {

        int num = Integer.parseInt(msg);
        //System.out.println(num);
        int count = function(num);
        if (count % 2 == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static int function(int checkedNumber) {
        BigInteger val = new BigInteger(String.valueOf(checkedNumber));
        val = val.abs();
        int count = val.bitCount();
        String binaryString = val.toString(2);

        //System.out.println("count = " + count);
        //System.out.println("bin = " + binaryString);
        return count;
    }
    static String getCharacter(String msg)
    {
        int num = Integer.parseInt(msg);
        num = num >> 1;
        char ch = (char)num;
        String ret = Character.toString(ch);
        return ret;
    }
}
