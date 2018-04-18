package recsys;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class testWeb {



    public static void main(String[] args) throws Exception {//15

    }
    public static void main44(String[] args) throws Exception {
        String requestJsonString=" {\"mac\":\"00264C6733BD1\",\"responseType\":1} ";
        RecByJson3 recByJson = new RecByJson3();
        String reponse = recByJson.rec(requestJsonString);
        System.out.println(reponse);
    }



    public static void main2(String[] args) throws Exception {
        String requestJsonString = "",line="";
        BufferedReader r = new BufferedReader(new FileReader("C:\\Users\\TaoShiqian\\Desktop\\联调JSON\\example0\\request.json"));
        while ( (line=r.readLine())!=null){
            requestJsonString += line;
        }
        //请求服务
        //  tester.callSayHello(requestJsonString);
        RecByJson recByJson = new RecByJson();
        String reponse = recByJson.rec(requestJsonString);
        BufferedWriter w = new BufferedWriter(new FileWriter("C:\\Users\\TaoShiqian\\Desktop\\联调JSON\\example0\\repo nseString.json"));
        w.write(reponse);
        w.close();
    }
}
