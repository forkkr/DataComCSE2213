/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author student
 */
public class Datacom {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Socket echoSocket = new Socket("172.16.13.206", 5555);
//          PrintWriter out =new PrintWriter(echoSocket.getOutputStream(), true);
//          BufferedReader in =new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
//          BufferedReader stdIn =new BufferedReader(new InputStreamReader(System.in));
        DataInputStream din = new DataInputStream(echoSocket.getInputStream());
        DataOutputStream dout = new DataOutputStream(echoSocket.getOutputStream());
        FileReader fr = new FileReader("C:\\Users\\student\\Desktop\\1lab.txt");
        int i, a, f, d;
        char c;
        Scanner sc = new Scanner(fr);


        Scanner in = new Scanner(System.in);
        String s;
        s = sc.nextLine();
        for (i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            System.out.println(c);
            d = c;
            Object checkedNumber = d;
            BigInteger val = new BigInteger(String.valueOf(checkedNumber));
            val = val.abs();
            int count = val.bitCount();
            String binaryString = val.toString(2);
            System.out.println("count = " + count);
            System.out.println("bin = " + binaryString);
            a = d << 1;
            if (count % 2 == 1) {
                f = a | 0;
            } else {
                f = a | 1;
            }
            String str = Integer.toString(f);
            System.out.println(str);
            dout.writeUTF(str);
            dout.flush();
        }
        //dout.writeUTF(s);

        while (true) {

        }

        //System.out.println("echo: " + in.readLine());       
//        fr.close();    
    }
}
