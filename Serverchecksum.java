import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by kashob on 7/26/17.
 */
public class Server {
    public static ServerSocket server;
    public static Socket client;
    public static DataInputStream input;
    public static DataOutputStream output;
    public static FileWriter write;
    public static void main(String [] args) throws IOException {
        server = new ServerSocket(5555);
        client = server.accept();
        input = new DataInputStream(client.getInputStream());
        output = new DataOutputStream(client.getOutputStream());
        write = new FileWriter("output.txt",true);
        while(true)
        {
            String msg = input.readUTF();
            System.out.println(msg +" just input");
            if(msg.compareTo("stop")==0) {
                write.flush();
                write.close();
                break;
            }
            if(isOkay(msg))
            {
                System.out.println(msg + "after checking");
                output.writeUTF("Received");
                output.flush();
                System.out.println(correctOuput(msg) +"correct mag");
                write.write(correctOuput(msg)+"\n");

            }
            else
            {
                output.writeUTF("error");
                output.flush();
            }
        }
    }
    static boolean isOkay(String msg)
    {
        String ar[] = msg.split("#");
        int sum = 0;
        for(int j =0; j < ar[0].length(); j++)
        {
            sum += ar[0].charAt(j);
        }
        for(int i = 1; i < ar.length - 1; i++ )
        {
            sum +='#';
            for(int j =0; j < ar[i].length(); j++)
            {
                sum += ar[i].charAt(j);
            }
        }
        int checksum = Integer.parseInt(ar[ar.length-1]);
        System.out.println(sum +" sum&checksum "+ checksum);
        if( (sum % 16) == checksum)
        {
            return true;
        }
        else
            return false;
    }
    static String correctOuput(String msg) {
    String ar[] = msg.split("#");
    String ret =ar[0];
    for(int i = 1; i < ar.length - 1; i++)
    {
        ret+="#";
        ret+=ar[i];
    }
    return ret;
    }
}
