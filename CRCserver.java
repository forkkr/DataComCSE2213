import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by kashob on 11/2/17.
 */
public class CRCserver {
    static ServerSocket server;
    static Socket client;
    static DataInputStream is;
    static DataOutputStream out;
    static FileWriter write;
    public static void main(String[] args) throws IOException {
        server = new ServerSocket(5555);
        client = server.accept();
        System.out.println("Client Connected!!");
        out = new DataOutputStream(client.getOutputStream());
        is = new DataInputStream(client.getInputStream());
        write = new FileWriter("output.txt");
        String divisor =  "10001000000100001";
        String outputBits="";
        while(true)
        {
            String msg =  is.readUTF();
            System.out.println("Input Data: "+msg);
            if(msg.equals("end"))
            {
                System.out.println("THE END");
                break;
            }
            String reminder = doingDivision(msg , divisor);
            System.out.println("Reminder: "+reminder);
            boolean errorFlag = checkingError(reminder);
            if(errorFlag)
            {
                outputBits+=gettingData(msg);
                out.writeUTF("correct");
                out.flush();
            }
            else
            {
                System.out.println("Error Occurred. Sending request to resent the same frame.");
                out.writeUTF("error");
                out.flush();
            }

        }
        writingIntoFile(outputBits);
        out.close();

    }
    public static String doingXOR(String reminder, String Divisor) {
        String temp = "";
        for (int i = 0; i < reminder.length(); i++) {
            if (reminder.charAt(i) == Divisor.charAt(i)) {
                temp += '0';
            } else {
                temp += '1';
            }
        }
        return temp;
    }

    public static String doingDivision(String Dividend, String Divisor) {
        String temp = Dividend, reminder = "",ZERO="0";
        for (int i = 1; i < Divisor.length(); i++) {
            ZERO += "0";
        }
        System.out.println("dataLength : "+Dividend.length());
        reminder = temp.substring(0, Divisor.length());
        temp = temp.substring(Divisor.length());
        while (true) {
            if (reminder.charAt(0) == '1')
                reminder = doingXOR(reminder, Divisor);
            else
                reminder = doingXOR(reminder,ZERO);

            reminder = reminder.substring(1);
            if (temp.length() == 0) {
                break;
            }
            reminder += temp.charAt(0);
            temp = temp.substring(1);
        }
        temp = Dividend.substring(0,26) + reminder;
        System.out.println("Remainder: " + reminder + " Code : " + temp);
        return reminder;
    }
    public static boolean checkingError(String reminder)
    {
        for(int i = 0; i < reminder.length(); i++)
        {
            if(reminder.charAt(i)!='0') {
                return false;
            }
        }
        return true;
    }
    public static String gettingData(String msg)
    {
        String tmp;
        if(msg.length() < 42)
           tmp = msg.substring(0 , msg.length()-16);
        else
            tmp= msg.substring(0 , 26);
        return tmp;
    }
    public static void writingIntoFile(String dataBits) throws IOException {
        String outputText = gettingDataText(dataBits);
        String tmp ="";
        for(int i = 0; i < outputText.length(); i++)
        {
            if(outputText.charAt(i)=='*')
            {
                write.write(tmp);
                tmp="";
                write.write(System.getProperty("line.separator"));
            }
            else
            {
                tmp +=outputText.charAt(i);
            }
        }
        write.write(tmp);
        write.flush();
        write.close();
    }
    public static String gettingDataText(String dataBits)
    {
        String dataText ="";
        while(dataBits.length() > 0)
        {
            String temp = dataBits.substring(0,8);
            dataBits = dataBits.substring(8);
            char ch = (char)Integer.parseInt(temp,2);
            dataText +=ch;
        }
        System.out.println("Output Text: "+dataText);
        return dataText;
    }
}
