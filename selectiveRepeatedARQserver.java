import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by kashob on 11/2/17.
 */
public class selectiveRepeatedARQserver {

    static ServerSocket server;
    static Socket client;
    static DataInputStream is;
    static DataOutputStream out;
    static FileWriter write;
    static boolean acknowledgement[] = new boolean[10000];
    static boolean sendFlag[] = new boolean[10000];
    static boolean timeOutFlag[] = new boolean[10000];
    static boolean negativeAcknowledgement[] = new boolean[10000];
    static boolean negativeAcknowledgementPrint[] = new boolean[10000];
    static boolean lostMsgPrint[] = new boolean[10000];
    static boolean receiveMsgPrint[] = new boolean[10000];
    static long timer[] = new long[10000];
    static String input;
    static int windowLeft;
    static int windowRight;
    static int frameCounter;
    static int windowSize = 8;
    static String frame[] = new String[100];
    static boolean communicationEnding;
    public static void main(String [] args) throws IOException {
        server = new ServerSocket(5555);
        client = server.accept();
        System.out.println("Client connected!");
        is = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());
        FileReader fin = new FileReader("input.txt");
        Scanner sc = new Scanner(fin);
        input=sc.nextLine();
        while(sc.hasNext())
        {
            input+="*"+sc.nextLine();
        }
        createFrame(input);
        System.out.println("Total frame: "+frameCounter);
        communicationEnding = false;
        for(int i = 0; i < 10000; i++)
        {
            acknowledgement[i] = false;
            sendFlag[i] = false;
        }
        new receive().start();
        windowLeft = 0;
        windowRight = 0;
        while(true)
        {
            long current = System.currentTimeMillis();
            while((windowRight-windowLeft+1) < windowSize && windowRight < frameCounter)
            {
                windowRight++;
                if(frame[windowRight]!=null)
                out.writeUTF(frame[windowRight]);
                out.flush();
                sendFlag[windowRight] = true;
                timer[windowRight] = current + 100;
                System.out.println("Start Time for "+ windowRight + ": "+current);
                timeOutFlag[windowRight] = false;
                negativeAcknowledgement[windowRight] = false;
            }
            while(acknowledgement[windowLeft])
            {
                windowLeft++;
            }
            for(int i = windowLeft; i<= windowRight; i++)
            {
                if(!acknowledgement[i] && timer[i] < current && !timeOutFlag[i])
                {
                    System.out.println("Time Out Occured. for "+ i +" At : "+timer[i]);
                    timeOutFlag[i] = true;
                    sendFlag[i] = false;
                }
            }
            for(int i = windowLeft; i <= windowRight; i++)
            {
                if(sendFlag[i]==false)
                {
                    if(frame[i]!=null)
                    out.writeUTF(frame[i]);
                    out.flush();
                   // timeOutFlag[i] = false;
                    negativeAcknowledgement[i] = false;
                }
            }
            for(int i = windowLeft; i <= windowRight; i++)
            {
                if(!negativeAcknowledgementPrint[i]&&negativeAcknowledgement[i]==true && !acknowledgement[i])
                {
                    System.out.println("Negative Acknowledgement from "+i);
                    negativeAcknowledgement[i] = false;
                    negativeAcknowledgementPrint[i] = true;
                }
            }
            if(windowLeft==frameCounter)
            {
                System.out.println(windowLeft+" equal "+frameCounter);
                out.writeUTF("end");
                out.flush();
                communicationEnding = true;
                break;
            }
        }
        while(true) {

        }

    }
    static void createFrame(String msg)
    {
        frameCounter = 0;
        while(msg.length()!=0)
        {
            if(msg.length() > 6)
            {
                frame[frameCounter] = frameCounter+"#"+msg.substring(0 , 6);
                msg = msg.substring(6);
                frameCounter++;
            }
            else
            {
                frame[frameCounter] = frameCounter+"#"+msg.substring(0);
                msg = "";
                frameCounter++;
            }
        }
    }
}
class receive extends Thread
{
    public receive() {

    }
    public void run() {
        while (true){
            if(selectiveRepeatedARQserver.communicationEnding)
                break;
            try {
                String msg = selectiveRepeatedARQserver.is.readUTF();
                //System.out.println("Received Msg: " + msg);
                String ar[] = msg.split("#");
                if (ar[0].equals("ac")) {
                    int tmp = Integer.parseInt(ar[1]);
                    synchronized (this) {
                        for (int i = selectiveRepeatedARQserver.windowLeft; i <= tmp; i++) {
                            selectiveRepeatedARQserver.acknowledgement[i] = true;
                            if(!selectiveRepeatedARQserver.receiveMsgPrint[i]) {
                                System.out.println("Received : " + i);
                                selectiveRepeatedARQserver.receiveMsgPrint[i] = true;
                            }
                        }
                    }
                } else {
                    synchronized (this) {
                        int tmp = Integer.parseInt(ar[1]);
                        selectiveRepeatedARQserver.sendFlag[tmp] = false;
                        selectiveRepeatedARQserver.negativeAcknowledgement[tmp] = true;
                        if(!selectiveRepeatedARQserver.lostMsgPrint[tmp]) {
                            System.out.println("Lost: " + tmp);
                            selectiveRepeatedARQserver.lostMsgPrint[tmp] = true;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    }
}

