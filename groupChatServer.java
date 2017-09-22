/**
 * Created by kashob on 9/22/17.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;


public class groupChatServer {

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static final int maxClientsCount = 10;
    private static final clientThread[] threads = new clientThread[maxClientsCount];

    public static void main(String args[]) {

        // The default port number.
        int portNumber = 5555;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("*** Conversation ***");
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {

                        (threads[i] = new clientThread(clientSocket, threads)).start();
                        break;
                    }
                }
                if (i == maxClientsCount) {
                    DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
                    os.writeUTF("Server too busy. Try later.");
                    os.flush();
                    os.close();
                    clientSocket.close();
                }
                int cnt = 0;
                for(i =0; i < maxClientsCount; i++)
                {
                    if(threads[i]==null)
                        cnt++;
                }
                if(cnt==maxClientsCount)
                    break;
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

class clientThread extends Thread {

    private DataInputStream is = null;
    private DataOutputStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;

    public clientThread(Socket clientSocket, clientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;
        try {

            is = new DataInputStream(clientSocket.getInputStream());
            os = new DataOutputStream(clientSocket.getOutputStream());
            os.writeUTF("Enter your name.");
            String name = is.readUTF().trim();
            os.writeUTF("Hello " + name
                    + " to our chat room.\nTo leave enter /quit in a new line");
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.writeUTF("*** A new user " + name
                            + " entered the chat room !!! ***");
                }
            }

            while (true) {
                String line = is.readUTF();
                System.out.println("< " + name +" >: "+ line);
                if (line.startsWith("BYE") || line.startsWith("bye") || line.startsWith("Bye")) {
                    break;
                }
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null) {
                        threads[i].os.writeUTF("<" + name + ">: " + line);
                    }
                }
            }
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.writeUTF("*** The user " + name
                            + " is leaving the chat room !!! ***");
                }
            }
            os.writeUTF("*** Bye " + name + " ***");
            os.flush();
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }

            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}