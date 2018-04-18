package test;

import org.javatuples.Pair;
import org.javatuples.Tuple;
import tool.WatchDataManager;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class test {

    private static String input = "E:\\OCNdata\\VOD\\active_users_watch_data.txt";
    private static String output = "E:\\OCNdata\\VOD\\recResult.txt";
    private static String itemRecListFilePath = "E:\\OCNdata\\VOD\\rec_20161101_20161115_data.txt";
    private static int factorNum = 20;
    private static int iterNum = 50;
    private static double lambda = 0.01;

    public static void main(String[] args) throws Exception {
        WatchDataManager w = new WatchDataManager();
        // w.userLike("E:\\JavaWorkSpace\\RecSysWeb\\userLikeT.txt","E:\\JavaWorkSpace\\RecSysWeb\\userLike.txt");
//        Tester tester = new Tester4();
//        tester.trainFromTxt(input,factorNum,iterNum,lambda);
//        tester.recToTxt(output,itemRecListFilePath);
//        System.out.println("推荐完成，结果见"+output);

        for (int i = 20171212; i <= 20171230; i++) {
            timeQuantum(i);
            //System.out.println(i + " " + timeQuantum(i));
        }
        for (int i = 20180101; i <= 20180107; i++) {
            timeQuantum(i);
            //System.out.println(i + " " + timeQuantum(i));
        }
    }

    public static void timeQuantum(int day) {
        int y = day / 10000;
        int m = day / 100 % 100;
        int d = day % 100;
        int week;
        //int century = y / 100 + 1;
        //week = y + y / 4 + century / 4 - 2 * century + (26 * (m + 1)) / 10 + day -1;
        //week %= 7;
        //return week%7;
        System.out.println(y + " " + m + " " + d );
        // 星期几
        if (m == 1 || m == 2) {
            m += 12;
            y--;
        }
        if ((y < 1752) || (y == 1752 && m < 9) || (y == 1752 && m == 9 && d < 3)) {
            week = (d + 2 * m + 3 * (m + 1) / 5 + y + y / 4 + 5) % 7;
        } else {
            week = (d + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400) % 7;
        }//得到了星期几
//        return week;
        System.out.println(" " + (week+1));
    }


//    private static boolean findFromList(List<Tuple> list, int targetID) {
//        int left = 0, right = list.size() - 1;
//        while (left <= right) {
//            int mid = (left + right) / 2;
//            int middleID = Integer.parseInt(list.get(mid).getValue(0).toString());
//            if (middleID == targetID) return true;
//            else if (middleID < targetID) left = mid + 1;
//            else right = mid - 1;
//        }
//        return false;
//    }


    //测试用，请无视
    public static void main2(String[] args) throws Exception {

        //Tester tester = new Tester8();
        //tester.precision2("E:\\OCNdata\\ActiveUser2.txt","E:\\OCNdata\\201601-04\\201601-04.txt");


// get name representing the running Java virtual machine.
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);
// get pid
        String pid = name.split("@")[0];
        System.out.println("Pid is:" + pid);

        String trainDirectory = "E:\\data\\OCN\\train";
        //String testDirectory  = "E:\\data\\OCN\\test";

        //Tester tester = new Tester4();
        //tester.testFromDirectory(trainDirectory,false, 10, 20, 0.01);
        //tester.testFromFile("E:\\data\\OCN\\split\\out1.txt", false, 10, 20, 0.01);

        //Tester tester = new Tester8();
        //tester.testFromFile("E:\\data\\OCN\\train8out\\out.txt", false, 10, 20, 0.01);
        //tester.testFromFile("E:\\data\\OCN\\userWatchTable8.txt", false, 10, 20, 0.01);

        //tester.testRMSE("E:\\data\\OCN\\test\\20160504.txt");

        System.out.println("总内存：" + Runtime.getRuntime().totalMemory()
                / 1024 / 1024 + "M");
        System.out.println("空闲内存：" + Runtime.getRuntime().freeMemory()
                / 1024 / 1024 + "M");
        System.out.println("最大内存：" + Runtime.getRuntime().maxMemory()
                / 1024 / 1024 + "M");
        System.out.println("已使用内存："
                + (Runtime.getRuntime().totalMemory() - Runtime
                .getRuntime().freeMemory()) / 1024 / 1024 + "M");

        while (true) ;

    }
}




