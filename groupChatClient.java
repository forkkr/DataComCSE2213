/**
 * Created by kashob on 9/22/17.
 */

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class groupChatClient implements Runnable {

    private static Socket clientSocket = null;
    private static DataOutputStream os = null;
    private static DataInputStream is = null;

    private static BufferedReader inputLine = null;
    private static boolean closed = false;

    public static void main(String[] args) {
        int portNumber = 5555;
        String host = "localhost";
        try {
            clientSocket = new Socket(host, portNumber);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new DataOutputStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to the host "
                    + host);
        }

        if (clientSocket != null && os != null && is != null) {
            try {
                new Thread(new groupChatClient()).start();
                while (!closed) {
                    os.writeUTF(inputLine.readLine().trim());
                }
                os.close();
                is.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }
    public void run() {
        String responseLine;
        try {
            while ((responseLine = is.readUTF()) != null) {
                System.out.println(responseLine );
                if (responseLine.startsWith("*** Bye"))
                    break;
            }
            closed = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}
class checkSum
{

}