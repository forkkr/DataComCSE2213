import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by kashob on 11/2/17.
 */
public class selectiveRepeatedARQclient {
    static Socket server;
    static DataInputStream is;
    static DataOutputStream out;
    static FileWriter write;
    static int frameReceivedCounter;
    static boolean receivedFlag[] = new boolean[10000];
    static String receivedFrame[] = new String[10000];
    static boolean acknowledgementSendFlag[] = new boolean[10000];
    static int receiveWindowLeft;
    static int receiveWindowRight;
    static int receiveWindowSize = 8;
    static int totalReceivedFrameCounter = 0;
    static boolean ending;
    public static void main(String [] args) throws IOException {
        server = new Socket("localhost" ,5555);
        System.out.println("Server Connected!");
        is = new DataInputStream(server.getInputStream());
        out = new DataOutputStream(server.getOutputStream());
        write = new FileWriter("output.txt");
        new receiveFromServer().start();
        receiveWindowLeft = 0;
        receiveWindowRight = 0;
        ending = false;
        for(int i= 0; i < 10000; i++)
        {
            acknowledgementSendFlag[i] = false;
            receivedFlag[i] = false;
        }
        while(true)
        {
            if(ending) {

                break;
            }


            while(receivedFlag[receiveWindowLeft] && !ending)
            {
                if(ending)
                    break;
                receiveWindowLeft++;
            }
            if(!ending && receiveWindowLeft > 0&&!acknowledgementSendFlag[receiveWindowLeft-1])
            {
                if(ending)
                    break;
                out.writeUTF("ac"+"#"+(receiveWindowLeft-1));
                out.flush();
            }
            if(!ending){
                if(ending) {
                    break;
                }
                System.out.println("Lost: "+receiveWindowLeft);
            out.writeUTF("wa"+"#"+receiveWindowLeft);
            out.flush();
            }
            while((receiveWindowRight-receiveWindowLeft+1) < receiveWindowSize && !ending)
            {
                if(ending)
                    break;
                receiveWindowRight++;
            }

        }
        System.out.println("ending !!!");
        System.out.println("Total: "+totalReceivedFrameCounter);
        fileWriting();
    }
    static void fileWriting() throws IOException {
        for(int i  = 0; i < totalReceivedFrameCounter; i++)
        {
            if(receivedFrame[i].length() > 0)
            fileWritingHelper(receivedFrame[i]);
        }
        write.close();
    }
    static void fileWritingHelper(String msg) throws IOException {
        String tmp ="";
        for(int i = 0; i < msg.length(); i++)
        {
            if(msg.charAt(i)=='*')
            {
                write.write(tmp);
                write.flush();
                write.write(System.getProperty("line.separator"));
                tmp = "";
            }
            else
            {
                tmp +=msg.charAt(i);
            }
        }
        write.write(tmp);
        write.flush();
    }

}
class receiveFromServer extends Thread
{
    receiveFromServer()
    {

    }

    public void run() {
        while(true) {
            try {
                String msg = selectiveRepeatedARQclient.is.readUTF();
                //System.out.println("Received msg: "+msg);
                if(msg.equals("end"))
                {
                    System.out.println(msg);
                    selectiveRepeatedARQclient.ending = true;
                    break;
                }
                if (selectiveRepeatedARQclient.frameReceivedCounter < 4) {
                    frameWriting(msg);
                } else {
                    selectiveRepeatedARQclient.frameReceivedCounter = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    void frameWriting(String msg)
    {
        String ar[] = msg.split("#");
        int tmp = Integer.parseInt(ar[0]);
        if(!selectiveRepeatedARQclient.receivedFlag[tmp]) {
            selectiveRepeatedARQclient.totalReceivedFrameCounter++;
            System.out.println("Recieved: " + ar[1]);
        }
        selectiveRepeatedARQclient.receivedFlag[tmp] = true;
        selectiveRepeatedARQclient.receivedFrame[tmp] = ar[1];
    }
}
