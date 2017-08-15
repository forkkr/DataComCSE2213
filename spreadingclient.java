import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class spreadingclient {
    static String bitstring(String s,int l)
    {
        String string="";
        for(int i=0;i<l;i++) {
            int bitfirst = s.charAt(i);
            Object checkedNumber = bitfirst;
            BigInteger val = new BigInteger(String.valueOf(checkedNumber));
            string+=val.toString(2);
        }
        return  string;
    }
    static String bittointxor(char a,char b,int spnum)
    {
        String s="";
        String tm="";
        tm+=a;
        s+=tm+tm+tm+tm;
        tm=""+b;
        s+=tm+tm+tm+tm;
        System.out.println(s+" "+a+" "+" "+b+" "+spnum);
        return xor(Integer.parseInt(s,2),spnum);
    }
    static String xor(int n,int spnum)
    {
        int xorvalue = 0;
        if(spnum==0)
        {
            xorvalue=85^n;
        }
        if(spnum==1)
        {
            xorvalue=51^n;
        }
        if(spnum==2)
        {
            xorvalue=0^n;
        }
        Object checkedNumber = xorvalue;
        BigInteger val = new BigInteger(String.valueOf(checkedNumber));
        String s=val.toString(2);
        String temp="";
        if(s.length()<8) {
            for (int i = 0; i < 8 - s.length(); i++) {
                temp += "+1";
            }
        }
        for(int i=0;i<s.length();i++)
        {
            if(s.charAt(i)=='0')
            {
                temp+="+1";
            }
            else
            {
                temp+="-1";
            }
        }
        System.out.println(temp+"  "+" user "+ spnum);
        return temp;
    }
    static String sum(String s1,String s2,String s3)
    {
        String sumstring = "",tempstring;
        for(int i=0;i<s1.length();i+=2)
        {
            int sm = 0;
            if(s1.charAt(i)=='-')
            {
                sm -= s1.charAt(i+1) - '0';
            }
            else
            {
                sm += s1.charAt(i+1) - '0';
            }
            if(s2.charAt(i)=='-')
            {
                sm -= s2.charAt(i+1) - '0';
            }
            else
            {
                sm += s2.charAt(i+1) - '0';
            }
            if(s3.charAt(i)=='-')
            {
                sm -= s3.charAt(i+1) - '0';
            }
            else
            {
                sm += s3.charAt(i+1) - '0';
            }

            if(sm>=0)
            {
                sumstring +="+";
            }
            sumstring +=sm;
        }
        System.out.println(sumstring+" "+" sumstring ");
        return sumstring;
    }
    public static void main(String[] args) throws IOException {
        char x,y,z,a,b;
        int i,j;
        Socket clientsocket=new Socket("192.168.3.104",5555);
        DataInputStream din=new DataInputStream(clientsocket.getInputStream());
        DataOutputStream dout=new DataOutputStream(clientsocket.getOutputStream());
        FileReader fin[]=new FileReader[3];
        Scanner fsc[]=new Scanner[3];
        for(i=0;i<3;i++)
        {
            fin[i]=new FileReader("input"+i+".txt");
            fsc[i]=new Scanner(fin[i]);
        }
        String in[]=new String[3];
        for(i=0;i<3;i++)
        {
            in[i]="";
            while(fsc[i].hasNext())
            {
                in[i]+=fsc[i].nextLine();
            }
        }
        int maxl=Math.min(in[0].length(),Math.min(in[1].length(),in[2].length()));
        String bit[]=new String[3];
        String bin[]=new String[3];
        String fullstring="";
            bit[0]="";
            bit[1]="";
            bit[2]="";
            for(i=0;i<3;i++)
            {
                bit[i]=bitstring(in[i],maxl);
                System.out.println("user " +i+bit[i]);
                System.out.println(bit[i].length() +" "+i);
            }
        String finals;
        for(i=0;i< bit[0].length();i+=2)
        {
            for(j=0;j<3;j++)
            {
                bin[j]=bittointxor(bit[j].charAt(i),bit[j].charAt(i+1),j);
            }
            finals=sum(bin[0],bin[1],bin[2]);
            System.out.println(finals);
            dout.writeUTF(finals);
            dout.flush();
        }
        dout.writeUTF("okay");
        dout.flush();
        clientsocket.close();
        dout.close();
    }

}
