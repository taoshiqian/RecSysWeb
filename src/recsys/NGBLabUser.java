package recsys;

import org.javatuples.Pair;
import recsys.RecSysOCN;
import recsys.RecSysOfSeries;
import tool.WatchDataManager;

public class NGBLabUser {

    static String broadcastInput = "E:\\OCN\\NGBLabCheck\\date\\broadcastInput.txt";
    static String broadcastRecList = "E:\\OCN\\NGBLabCheck\\date\\broadcastRecList.txt";
    static String broadcastOutput = "E:\\OCN\\NGBLabCheck\\date\\broadcastOutput.txt";

    static String movieInput = "E:\\OCN\\NGBLabCheck\\date\\movieInput.txt";
    static String moiveRecList = "E:\\OCN\\NGBLabCheck\\date\\movieRecList.txt";
    static String movieOutput = "E:\\OCN\\NGBLabCheck\\date\\movieOutput.txt";

    static String seriesInput = "E:\\OCN\\NGBLabCheck\\date\\seriesInput.txt";
    static String seriesRecList = "E:\\OCN\\NGBLabCheck\\date\\seriesRecList.txt";
    static String seriesOutput = "E:\\OCN\\NGBLabCheck\\date\\seriesOutput.txt";

    public static void main(String[] args) throws Exception {
        System.out.println("电影数据处理");
        RecSysOCN recSys = new RecSysOfMovies();
        recSys.train(movieInput,  0.01);
        recSys.raedItemRecList(moiveRecList);
        recSys.recAllUsersToFile(movieOutput, 10);
        System.out.println("电影推荐完成");
        System.out.println("电视剧数据处理");
        recSys = new RecSysOfSeries();
        recSys.train(seriesInput,  0.01);
        recSys.raedItemRecList(seriesRecList);
        recSys.recAllUsersToFile(seriesOutput, 10);
        System.out.println("电视剧推荐完成");
//        System.out.println("直播数据处理");
//        recSys.train(broadcastInput,0.01);
//        recSys.raedItemRecList(broadcastRecList);
//        recSys.recAllUsersToFile(broadcastOutput,10);
//        System.out.println("直播推荐完成");
    }
}
