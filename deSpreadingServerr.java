import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by kashob on 8/16/17.
 */
public class deSpreadingServerr {
    public static DataInputStream in;
    public static DataOutputStream out;
    public static ServerSocket server;
    public static Socket client;
    public static String bitString[] = new String[3];
    public static String spreadCode[] = new String[3];
    public static FileWriter writer[] = new FileWriter[3];
    public static void main(String[] agrs) throws IOException {
        ServerSocket serverSocket = server = new ServerSocket(5555);
        client = server.accept();
        System.out.println("Client connected !");
        for(int i = 0; i < 3; i++)
        {
            writer[i] = new FileWriter("User"+i+".txt");
        }
        in = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());
        spreadCode[0] = "0101";
        spreadCode[1] = "0011";
        spreadCode[2] = "0000";
        for(int i =0 ; i < 3; i++)
            bitString[i] ="";
        while(true)
        {
            String msg = in.readUTF();
            System.out.println(msg);
            if(msg.equals("okay"))
                break;
            writeBitString(msg);
        }
        client.close();
        fileWriter();
    }
    static void writeBitString(String msg)
    {
        String tmp="";
        int sum = 0;
        for(int j = 0; j < 3; j++) {
            tmp="";
            sum = 0;
            for (int i = 0; i < 8 ; i+=2) {
                if(spreadCode[j].charAt(i/2)=='0')
                {
                   if(msg.charAt(i)=='+')
                   {
                       sum += msg.charAt(i+1) - '0';
                   }
                   else
                   {
                       sum -= msg.charAt(i+1) - '0';
                   }
                }
                else
                {
                    if(msg.charAt(i)=='+')
                    {
                        sum -= msg.charAt(i+1) - '0';
                    }
                    else
                    {
                        sum += msg.charAt(i+1) - '0';
                    }
                }
            }
            System.out.println(sum +" sum");
            sum /=4;
            if(sum==1)
            {
                tmp +="0";
            }
            else
            {
                tmp+="1";
            }
            sum = 0;

            for (int i = 8; i < 16 ; i+=2) {
                if(spreadCode[j].charAt((i-8)/2)=='0')
                {
                    if(msg.charAt(i)=='+')
                    {
                        sum += msg.charAt(i+1) - '0';
                    }
                    else
                    {
                        sum -= msg.charAt(i+1) - '0';
                    }
                }
                else
                {
                    if(msg.charAt(i)=='+')
                    {
                        sum -= msg.charAt(i+1) - '0';
                    }
                    else
                    {
                        sum += msg.charAt(i+1) - '0';
                    }
                }
            }
            sum /= 4;
            if(sum==1)
            {
                tmp +="0";
            }
            else
            {
                tmp +="1";
            }
            bitString[j]+=tmp;
            System.out.println(tmp +" user "+ j);
        }
    }
    static void fileWriter() throws IOException {
        String str = "";
        for(int i = 0;i < 3; i++)
        {
            System.out.println(bitString[i] +" user bits "+i);
            str ="";
            for(int j = 0; j < bitString[i].length(); j+=7)
            {
                String tmp = bitString[i].substring(j , j+7);
                System.out.println(tmp +" 8bits");
                int ascii = Integer.parseInt(tmp , 2);
                System.out.println(ascii +" ascii");
                char ch = (char) ascii;
                System.out.println(ch);
                str += ch;
            }
            System.out.println(str);
            writer[i].write(str);
            writer[i].flush();
            writer[i].close();
        }
    }
}
