import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.print.attribute.standard.Severity;

/**
 *
 * @author Student
 */
public class statTDMServer {

    static FileWriter[] output ;
    static DataInputStream is;
    static DataOutputStream os;
    public static void main(String[] args) throws IOException {
        
        ServerSocket server = new ServerSocket(5555);
        Socket client = server.accept();
        System.out.println("client connected");
        os = new DataOutputStream(client.getOutputStream());
        is = new DataInputStream(client.getInputStream());
        output = new FileWriter[5];
        for(int i = 0; i < 5; i++)
        {
            String name = "ouptut"+(i+10)+".txt";
            output[i] = new FileWriter(name , true);
        }
        while(true)
        {
            String msg = is.readUTF();
            if(msg.equals("stop"))
            {
                break;
            }
            System.out.print("Recieved Frame: ");
            System.out.println(msg +" " +msg.length());
            fileWriting(msg);
        }
        for(int i = 0; i <5 ; i++)
        {
            output[i].close();
        }
    }
    public static void fileWriting(String msg) throws IOException
    {
        String ar[] = msg.split("#");
        int i =  1;
        while(i < ar.length - 1)
        {
            int cn = Integer.parseInt(ar[i]);
            i++;
            int num = Integer.parseInt(ar[i]);
            i++;
            num -=10;
            String data = ar[i];
            String tmp[] = ar[i].split("\\*");
            if(tmp.length > 1)
            {
                for(int j = 0; j < tmp.length - 1 ; j++)
                {
                     output[num].write(tmp[j]);
                     output[num].write(System.getProperty("line.separator"));
                }
                 output[num].write(tmp[tmp.length - 1]);
            }
            else
            {
                 output[num].write(data);
            }
            i++;
            
        }
    }
    
}