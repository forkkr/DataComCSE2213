package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class crc_client {
    final static  String Divisor = "10001000000100001";
    final static  String ZERO="00000000000000000";
    public static  char makeerror(char c)
    {
        if(c=='1')
            return '0';
        else
            return '1';
    }
    public static String singleerror(String dividend)
    {
        char ch=dividend.charAt(2);
        return dividend.substring(0,2)+makeerror(ch)+dividend.substring(3);
    }
    public static String bursterror(String dividend)
    {
        char ch=dividend.charAt(1);
        char ch2=dividend.charAt(16);
        return dividend.substring(0,1)+makeerror(ch)+dividend.substring(2,16)+makeerror(ch2)+dividend.substring(17);
    }
    public static String XOR(String reminder, String Divisor) {
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
    public static String divide(String temp,String reminder)
    {
        while (true) {
            if (reminder.charAt(0) == '1')
                reminder = XOR(reminder, Divisor);
            else
                reminder = XOR(reminder,ZERO);
            reminder = reminder.substring(1);
            if (temp.length() == 0) {
                break;
            }
            reminder += temp.charAt(0);
            temp = temp.substring(1);
        }
        return  reminder;
    }
    public static String process(String Dividend, String Divisor) {
        String temp = Dividend, reminder = "";
        for (int i = 1; i < Divisor.length(); i++) {
            temp += "0";
        }
        reminder = temp.substring(0, Divisor.length());
        temp = temp.substring(Divisor.length());
        reminder=divide(temp,reminder);
        if(Dividend.length()<26)
            temp=Dividend+reminder;
        else
        temp = Dividend.substring(0,26) + reminder;
        System.out.println(" Remainder: " + reminder + " Code : " + temp);
        return temp;
    }
    public static void main(String[] args) throws IOException {
        Socket s=new Socket("192.168.11.196",5555);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        FileReader fin = new FileReader("input.txt");
        Scanner sc = new Scanner(fin);
        String Dividend = "";
        String inputTxt = "";
        String str=sc.nextLine();
        while(sc.hasNext())
        {
            str+="*"+sc.nextLine();
        }
        for(int i=0;i<str.length();i++) {
            int ch=str.charAt(i);
            String binaryString = Integer.toBinaryString(ch);
            while (binaryString.length() < 8) {
                binaryString = "0" + binaryString;
            }
            inputTxt += binaryString;
        }
        while(inputTxt.length()>0)
        {
            if(inputTxt.length()>26) {
                Dividend = inputTxt.substring(0, 26);
                inputTxt = inputTxt.substring(26);
                System.out.print("Data: " + Dividend );
                Dividend = process(Dividend,Divisor);
            }
            else
            {
                Dividend = inputTxt;
                inputTxt = "";
                Dividend = process(Dividend,Divisor);
            }
            do {
                String temp=Dividend;
                Random ran = new Random();
                int makeer = ran.nextInt(3);
                if (makeer == 1) {
                    temp= singleerror(Dividend);
                    System.out.println("Single error occurred ");
                }
                else if(makeer==2)
                {
                    temp=bursterror(Dividend);
                    System.out.println("burst error occurred");
                }
                else
                    System.out.println("correct message send");
                dout.writeUTF(temp);
                dout.flush();
            }while (din.readUTF().equals("error"));
        }
        dout.writeUTF("end");
        dout.flush();
        dout.close();
        s.close();
    }
}