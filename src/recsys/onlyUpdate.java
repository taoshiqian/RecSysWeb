package recsys;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;

public class onlyUpdate {

    static String broadcastInput = "E:\\OCN\\NGBLab\\date\\broadcastInput.txt";
    static String broadcastRecList = "E:\\OCN\\NGBLab\\date\\broadcastRecList.txt";
    static String broadcastOutput = "E:\\OCN\\NGBLab\\date\\broadcastOutput.txt";
    static String broadcastFileUsers = "E:\\OCN\\NGBLab\\date\\broadcastModelUsers";
    static String broadcastFileItems = "E:\\OCN\\NGBLab\\date\\broadcastModelItems";

    static String movieInput = "E:\\OCN\\NGBLab\\date\\movieInput.txt";
    static String moiveRecList = "E:\\OCN\\NGBLab\\date\\movieRecList.txt";
    static String movieOutput = "E:\\OCN\\NGBLab\\date\\movieOutput.txt";
    static String movieFileUsers = "E:\\OCN\\NGBLab\\date\\movieModelUsers";
    static String movieFileItems = "E:\\OCN\\NGBLab\\date\\movieModelItems";

    static String seriesInput = "E:\\OCN\\NGBLab\\date\\seriesInput.txt";
    static String seriesRecList = "E:\\OCN\\NGBLab\\date\\seriesRecList.txt";
    static String seriesOutput = "E:\\OCN\\NGBLab\\date\\seriesOutput.txt";
    static String seriesFileUsers = "E:\\OCN\\NGBLab\\date\\seriesModelUsers";
    static String seriesFileItems = "E:\\OCN\\NGBLab\\date\\seriesModelItems";

    public static void main(String[] args) throws Exception{
        RecSysOCN recSys;

        System.out.println("电影数据处理");
        recSys = new RecSysOfMovies();
        recSys.loadModel(movieFileUsers,movieFileItems);
        //recSys.train(movieInput,  0.01);
        recSys.raedItemRecList(moiveRecList);
        recSys.recAllUsersToFile(movieOutput, 10);
        System.out.println("电影推荐完成");

        System.out.println("电视剧数据处理");
        recSys = new RecSysOfSeries();
        recSys.loadModel(seriesFileUsers,seriesFileItems);
        //recSys.train(seriesInput,  0.01);
        recSys.raedItemRecList(seriesRecList);
        recSys.recAllUsersToFile(seriesOutput, 10);
        System.out.println("电视剧推荐完成");

        recSys = new RecSysOfBroadcast();
        System.out.println("直播数据处理");
        recSys.loadModel(broadcastFileUsers,broadcastFileItems);
        //recSys.train(broadcastInput,  0.01);
        recSys.raedItemRecList(broadcastRecList);
        recSys.recAllUsersToFile(broadcastOutput, 10);
        System.out.println("直播模型完成");

        String reponseString = callRec("update");//请求服务
        System.out.println(reponseString);
        System.out.println("推荐接口已更新");
    }
    private static final String SERVICE_ENDPOINT = "http://139.224.80.170:8080/webservices/services/HelloWorld?wsdl";//服务器的wsdl地址
    //private static final String SERVICE_ENDPOINT = "http://localhost:8080/webservices/services/HelloWorld?wsdl";//服务器的wsdl地址
    private static final String REC_FUNCTION = "recommendRequest"; //服务器推荐算法的方法名

    public static String callRec(String requestJsonString) {
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(SERVICE_ENDPOINT));
            call.setOperationName(new QName("http://", REC_FUNCTION));//"http://"可以随便填,REC_FUNCTION就是方法名
            call.addParameter("requestJsonString", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            //call.addParameter("from2", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);//form和from2  参数 也可以改为其他英文字母
            call.setReturnType(org.apache.axis.Constants.XSD_STRING);
            try {
                //远程调用发布的方法
                String ret = (String) call.invoke(new Object[]{requestJsonString});  //requestJsonString 为传递给服务器的消息
                //String ret = (String) call.invoke(new Object[]{new String("123")});  //requestJsonString 为传递给服务器的消息
                return ret;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "错误";
    }
}