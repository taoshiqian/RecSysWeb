package recsys;


import org.javatuples.Pair;
import tool.WatchDataManager;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.*;

public class testRecSys {

    static String path = "E:\\OCN\\vod\\";
    //static String path = "E:\\OCN\\_vod_test\\";

    //    static String movieInput = path+"movie\\movieInput201708.txt";
//    static String movieA = path+"movie\\movieA.txt";
//    static String movieB = path+"movie\\movieB.txt";
//    static String movieRecList = path+"movie\\movieRecList201708.txt";
//    static String movieOutput = path+"movie\\movieOutput201708.txt";
//    static String movieTimes = path+"movie\\movieTimes.txt";
//    static String movieTest = path+"movie\\movieInput1201_1207.txt";
//
//    static String seriesInput = path+"series\\seriesInput1101_1130.txt";
//    static String seriesA = path+"series\\seriesA.txt";
//    static String seriesB = path+"series\\seriesB.txt";
//    static String seriesRecList = path+"series\\seriesRecList1101_1130.txt";
//    static String seriesOutput = path+"series\\seriesOutput1107_1130.txt";
//    static String seriesTimes = path+"series\\seriesTimes.txt";
//    static String seriesTest = path+"series\\seriesInput1201_1207.txt";
//
    static String broadcastInput = "E:\\OCN\\broadcast\\watchdata_201708.txt";
    static String broadcastRecList = "E:\\OCN\\broadcast\\broadcastMap.txt";
    static String broadcastOutput = "E:\\OCN\\broadcast\\broadcastOutput201708.txt";
    static String broadcastTimes = "E:\\OCN\\broadcast\\broadcastOutput201708.txt";

    public static void recTest() throws Exception {
//        RecSysOCN recSysOfMovies = new RecSysOfMovies();
//        recSysOfMovies.train(movieInput,30,100,0.01);
//        recSysOfMovies.raedItemRecList(movieRecList);
//        recSysOfMovies.recAllUsersToFile(movieOutput);

//        RecSysOCN recSysOfSeries = new RecSysOfSeries();
//        recSysOfSeries.train(seriesInput,30,100,0.01);
//        recSysOfSeries.raedItemRecList(seriesRecList);
//        recSysOfSeries.recAllUsersToFile(seriesOutput);

//        String path08 = "E:\\OCN\\result201708\\";
//        RecSysOCN recSysOfSeries = new RecSysOfSeries();
//        recSysOfSeries.train(path08+"Input201708.txt",30,100,0.01);
//        recSysOfSeries.raedItemRecList(path08+"seriesRecList.txt");
//        recSysOfSeries.recAllUsersToFile(path08+"seriesResult.txt");
//
//        RecSysOCN recSysOfMovies = new RecSysOfMovies();
//        recSysOfMovies.train(path08+"Input201708.txt",30,100,0.01);
//        recSysOfMovies.raedItemRecList(path08+"movieRecList.txt");
//        recSysOfMovies.recAllUsersToFile(path08+"movieResult.txt");

        RecSysOCN recSys = new RecSysOfSeries();
        recSys.train(broadcastInput,  0.01);
        String modelUsers = "E:\\OCN\\broadcast\\modelUsers";
        String modelItems = "E:\\OCN\\broadcast\\modelItems";
        //recSys.saveModel(modelUsers, modelItems);
        //recSys.loadModel(modelUsers, modelItems);
        //System.out.println(recSys.getUserVec("001968439E65"));
        //System.out.println(recSys.getUsersClass("001968439E65"));
        //recSys.saveClassVector("E:\\OCN\\broadcast\\mac_class_vector.txt");
        recSys.raedItemRecList(broadcastRecList);
        recSys.recAllUsersToFile(broadcastOutput,5);

        System.out.println("总内存：" + Runtime.getRuntime().totalMemory()
                / 1024 / 1024 + "M");
        System.out.println("空闲内存：" + Runtime.getRuntime().freeMemory()
                / 1024 / 1024 + "M");
        System.out.println("最大内存：" + Runtime.getRuntime().maxMemory()
                / 1024 / 1024 + "M");
        System.out.println("已使用内存："
                + (Runtime.getRuntime().totalMemory() - Runtime
                .getRuntime().freeMemory()) / 1024 / 1024 + "M");
    }

    public static void main(String[] args) throws Exception {

        recTest();

        //WatchDataManager w = new WatchDataManager();
        //w.mapMultiTORecListTime("E:\\OCN\\broadcast\\itemIdMapMulti.txt","E:\\OCN\\broadcast\\RecListOfTime\\");

        //w.allToMonthTest("E:\\OCN\\AllVod\\serial_all.txt","series");
        //w.recListHot("E:\\OCN\\broadcast\\itemIdMapOne.txt","E:\\OCN\\broadcast\\broadcastTimes.txt","E:\\OCN\\broadcast\\broadcastMap.txt");
        //w.itemTimes("E:\\OCN\\broadcast\\watchdata_201708.txt","E:\\OCN\\broadcast\\broadcastTimes.txt");
        //w.multi2oneBroadcast("E:\\OCN\\broadcast\\itemIdMapMulti2.txt","E:\\OCN\\broadcast\\itemIdMapOne.txt","20170831");
        //w.getRecList(20170831,broadcastInput,broadcastRecList);
        //w.getOfferingId();
        //w.getRecList1234();
        //w.multi2one("E:\\OCN\\result\\vodmetadata_film.txt","E:\\OCN\\result\\movieMap2.txt");

        //w.itemTimes(movieInput,movieTimes);
        //w.itemTimes(seriesInput,seriesTimes);
        //w.getRecList(20170832,movieInput,movieRecList);
        //w.getRecList(20170832,seriesInput,seriesRecList);

//        System.out.println("电影：");
//        w.testByTwoFile(path+"movie\\movieOutput1101_1130.txt",movieTest);

//        System.out.println("\r\n\n\n电视剧：");
//        w.testByTwoFile(path+"series\\seriesOutput1101_1130.txt",seriesTest);

        //w.has(path+"series\\seriesRecList1201_1207.txt",path+"series\\seriesRecList1101_1115.txt");

        //w.splitInput(seriesInput,seriesA,seriesB);

        //w.multi2one("E:\\OCN\\result\\seriesMap.txt","E:\\OCN\\result\\seriesMap0.txt");



// get name representing the running Java virtual machine.
//        String name = ManagementFactory.getRuntimeMXBean().getName();
//        System.out.println(name);
//// get pid
//        String pid = name.split("@")[0];
//        System.out.println("Pid is:"+pid);
//
//
//        System.out.println("总内存：" + Runtime.getRuntime().totalMemory()
//                / 1024 / 1024 + "M");
//        System.out.println("空闲内存：" + Runtime.getRuntime().freeMemory()
//                / 1024 / 1024 + "M");
//        System.out.println("最大内存："+Runtime.getRuntime().maxMemory()
//                /1024/1024+"M");
//        System.out.println("已使用内存："
//                + (Runtime.getRuntime().totalMemory() - Runtime
//                .getRuntime().freeMemory()) / 1024 / 1024 + "M");
//        BufferedWriter wAcc = new BufferedWriter(new FileWriter("E:\\OCN\\AllVod\\准确率详细.txt"));
//        double allAcc = 0.0;
//        for (int i = 201601; i <= 201612; i++) {
//            allAcc += trainAndTest(i, wAcc);
//            System.out.println("总内存：" + Runtime.getRuntime().totalMemory()
//                    / 1024 / 1024 + "M");
//            System.out.println("空闲内存：" + Runtime.getRuntime().freeMemory()
//                    / 1024 / 1024 + "M");
//            System.out.println("最大内存：" + Runtime.getRuntime().maxMemory()
//                    / 1024 / 1024 + "M");
//            System.out.println("已使用内存："
//                    + (Runtime.getRuntime().totalMemory() - Runtime
//                    .getRuntime().freeMemory()) / 1024 / 1024 + "M");
//            System.out.println("\r\n\r\n\r\n");
//       }
//        for (int i = 201701; i <= 201707; i++) {
//            allAcc += trainAndTest(i, wAcc);
//            System.out.println("总内存：" + Runtime.getRuntime().totalMemory()
//                    / 1024 / 1024 + "M");
//            System.out.println("空闲内存：" + Runtime.getRuntime().freeMemory()
//                    / 1024 / 1024 + "M");
//            System.out.println("最大内存：" + Runtime.getRuntime().maxMemory()
//                    / 1024 / 1024 + "M");
//            System.out.println("已使用内存："
//                    + (Runtime.getRuntime().totalMemory() - Runtime
//                    .getRuntime().freeMemory()) / 1024 / 1024 + "M");
//            System.out.println("\r\n\r\n\r\n");
//        }
//        allAcc /= 19;
//        System.out.println(allAcc);
//        wAcc.close();

        //recTest();

        //while (true);
        //trainAndTest(201611);
        //shoulian();
        //userSim();

        //收敛
//        BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\OCN\\AllVod\\收敛.txt"));
//        BufferedWriter wp = new BufferedWriter(new FileWriter("E:\\OCN\\AllVod\\收敛人数.txt"));
//        double[] dayAcc = new double[20];
//        for(int month=201601;month<=201612;month++) {
//            System.out.println("月份："+month);
//            writer.write("\r\n\r\n月份："+month+"\r\n");
//            shoulianFile(month);
//            shoulian(writer,dayAcc);
//        }
//        writer.write("\r\n\r\n平均：");
//        for(int i=1;i<=7;i++){
//            writer.write( dayAcc[i]/12+"   ");
//        }
//        writer.close();
        //coverageAll();
        //WatchDataManager w= new WatchDataManager();
        //w.statistics("E:\\OCNdata\\电影电视剧尚未分开\\201601-04.txt","E:\\OCNdata\\电影电视剧尚未分开\\直播2\\20160101_07.txt");
        //w.look("E:\\OCNdata\\电影电视剧尚未分开\\直播2\\20160101_07.txt");
        //trainOneTime();
    }

    public static void trainOneTime() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式 2017-11-09 17:35:20
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        String broadcastInput = "E:\\OCNdata\\电影电视剧尚未分开\\直播\\20160101_07.txt";
        String broadcastRecList = "E:\\OCNdata\\电影电视剧尚未分开\\直播\\20160101_07RecList.txt";
        String broadcastOutput = "E:\\OCNdata\\电影电视剧尚未分开\\直播\\20160101_07Output.txt";
        RecSysOCN recSysSeries = new RecSysOfSeries();
        recSysSeries.train(broadcastInput, 0.01);
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        WatchDataManager w = new WatchDataManager();
        w.getRecList(20171330, broadcastInput, broadcastRecList);
        recSysSeries.raedItemRecList(broadcastRecList);
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        recSysSeries.recAllUsersToFile(broadcastOutput, 10);
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
    }

    public static void coverageAll() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\OCN\\AllVod\\覆盖率.txt"));
        double allCoverageRatio = 0.0;
        for (int i = 201601; i <= 201612; i++) {
            allCoverageRatio += coverageMonth(i, writer);
        }
        for (int i = 201701; i <= 201707; i++) {
            allCoverageRatio += coverageMonth(i, writer);
        }
        allCoverageRatio /= 19;
        System.out.println(allCoverageRatio);
        writer.write("平均\t" + allCoverageRatio + "\r\n");
        writer.close();
    }

    public static double coverageMonth(int month, BufferedWriter writer) throws Exception {
        String movieInput = "E:\\OCN\\AllVod\\" + month + "\\movie" + month + "Input.txt";
        String seriesInput = "E:\\OCN\\AllVod\\" + month + "\\series" + month + "Input.txt";
        String movieRecList = "E:\\OCN\\AllVod\\" + month + "\\movie" + month + "RecList.txt";
        String seriesRecList = "E:\\OCN\\AllVod\\" + month + "\\series" + month + "RecList.txt";
        String movieOutput = "E:\\OCN\\AllVod\\" + month + "\\movie" + month + "Output.txt";
        String seriesOutput = "E:\\OCN\\AllVod\\" + month + "\\series" + month + "Output.txt";
        int mj = month + 1;
        if (mj == 201613) mj = 201701;
        String movieTest = "E:\\OCN\\AllVod\\" + month + "\\movie" + mj + "0107Test.txt";
        String seriesTest = "E:\\OCN\\AllVod\\" + month + "\\series" + mj + "0107Test.txt";

        WatchDataManager w = new WatchDataManager();
        System.out.println("\r\n\r\n\r\n" + month + "电影");
        RecSysOCN recSysMovie = new RecSysOfMovies();
        int movieRecord = recSysMovie.train(movieInput,  0.01);
        w.getRecListTestCoverage(movieInput, movieRecList, 10000, 0.3, 0.02);
        recSysMovie.raedItemRecList(movieRecList);
        recSysMovie.recAllUsersToFile(movieOutput, 10);
        Pair<Integer, Integer> movieAcc = w.testByTwoFile(movieOutput, movieTest);
        Pair<Integer, Integer> movieRatio = w.coverageRatio(movieOutput, movieRecList);
        System.out.println(movieRatio);

        System.out.println("" + month + "电视剧");
        RecSysOCN recSysSeries = new RecSysOfSeries();
        int seriesRecord = recSysSeries.train(seriesInput,  0.01);
        w.getRecListTestCoverage(seriesInput, seriesRecList, 10000, 0.6, 0.02);
        recSysSeries.raedItemRecList(seriesRecList);
        recSysSeries.recAllUsersToFile(seriesOutput, 10);
        Pair<Integer, Integer> seriesAcc = w.testByTwoFile(seriesOutput, seriesTest);
        Pair<Integer, Integer> seriesRatio = w.coverageRatio(seriesOutput, seriesRecList);
        System.out.println(seriesRatio);

        double ans = (movieAcc.getValue0() + seriesAcc.getValue0()) * 1.0 / (movieAcc.getValue1() + seriesAcc.getValue1());
        System.out.println("" + month + "整体准确率：" + ans);
        double cove = (movieRatio.getValue0() + seriesRatio.getValue0()) * 1.0 / (movieRatio.getValue1() + seriesRatio.getValue1());
        System.out.println("" + month + "整体覆盖率：" + cove);
        String sss = month + "\t" + cove + "\t" + (movieRatio.getValue0() + seriesRatio.getValue0()) + "\t" + (movieRatio.getValue1() + seriesRatio.getValue1()) +
                "\t" + movieRatio.getValue0() + "\t" + movieRatio.getValue1() +
                "\t" + seriesRatio.getValue0() + "\t" + seriesRatio.getValue1() +
                "\r\n";
        System.out.println(sss);
        writer.write(sss);
        return cove;
    }

    public static void shoulianFile(int month) throws Exception {
        String allmovie = "E:\\OCN\\AllVod\\film_all.txt";
        String allseries = "E:\\OCN\\AllVod\\serial_all.txt";
        String path = "E:\\OCN\\AllVod\\收敛天\\";
        WatchDataManager w = new WatchDataManager();
        for (int i = 1; i <= 15; i++) {
            createDir(path + i);
            w.allToWeek(allmovie, path + i + "\\movieInput.txt", month * 100 + 01, month * 100 + 01 - 1 + i);
            w.allToWeek(allmovie, path + i + "\\movieTest.txt", month * 100 + 01 + i, month * 100 + 01 + i + 6);
            w.allToWeek(allseries, path + i + "\\seriesInput.txt", month * 100 + 01, month * 100 + 01 - 1 + i);
            w.allToWeek(allseries, path + i + "\\seriesTest.txt", month * 100 + 01 + i, month * 100 + 01 + i + 6);
        }
    }

    public static void shoulian(BufferedWriter writer, double[] dayAcc) throws Exception {
        String path = "E:\\OCN\\AllVod\\收敛天\\";
        for (int i = 1; i <= 15; i++) {
            String movieInput = path + i + "\\movieInput.txt";
            String seriesInput = path + i + "\\seriesInput.txt";
            String movieRecList = path + i + "\\movieRecList.txt";
            String seriesRecList = path + i + "\\seriesRecList.txt";
            String movieOutput = path + i + "\\movieOutput.txt";
            String seriesOutput = path + i + "\\seriesOutput.txt";
            String movieTest = path + i + "\\movieTest.txt";
            String seriesTest = path + i + "\\seriesTest.txt";

            WatchDataManager w = new WatchDataManager();
            System.out.println("\r\n\r\n\r\n" + i + "电影");
            RecSysOCN recSysMovie = new RecSysOfMovies();
            recSysMovie.train(movieInput,  0.01);
            w.getRecList(20171230, movieInput, movieRecList);
            recSysMovie.raedItemRecList(movieRecList);
            recSysMovie.recAllUsersToFile(movieOutput, 10);
            Pair<Integer, Integer> movieAcc = w.testByTwoFile(movieOutput, movieTest);

            System.out.println("" + i + "电视剧");
            RecSysOCN recSysSeries = new RecSysOfSeries();
            recSysSeries.train(seriesInput, 0.01);
            w.getRecList(20171330, seriesInput, seriesRecList);
            recSysSeries.raedItemRecList(seriesRecList);
            recSysSeries.recAllUsersToFile(seriesOutput, 10);
            Pair<Integer, Integer> seriesAcc = w.testByTwoFile(seriesOutput, seriesTest);

            double ans = (movieAcc.getValue0() + seriesAcc.getValue0()) * 1.0 / (movieAcc.getValue1() + seriesAcc.getValue1());
            System.out.println("" + i + "整体准确率：" + ans);
            writer.write(i + "天的准确率" + ans + "\r\n");
            dayAcc[i] += ans;
        }
    }

    public static double trainAndTest(int month, BufferedWriter wAcc) throws Exception {

        long begintime = System.currentTimeMillis();

        String movieInput = "E:\\OCN\\AllVod\\" + month + "\\movie" + month + "Input.txt";
        String seriesInput = "E:\\OCN\\AllVod\\" + month + "\\series" + month + "Input.txt";
        String movieRecList = "E:\\OCN\\AllVod\\" + month + "\\movie" + month + "RecList.txt";
        String seriesRecList = "E:\\OCN\\AllVod\\" + month + "\\series" + month + "RecList.txt";
        String movieOutput = "E:\\OCN\\AllVod\\" + month + "\\movie" + month + "Output.txt";
        String seriesOutput = "E:\\OCN\\AllVod\\" + month + "\\series" + month + "Output.txt";
        int mj = month + 1;
        if (mj == 201613) mj = 201701;
        String movieTest = "E:\\OCN\\AllVod\\" + month + "\\movie" + mj + "0107Test.txt";
        String seriesTest = "E:\\OCN\\AllVod\\" + month + "\\series" + mj + "0107Test.txt";

        WatchDataManager w = new WatchDataManager();
        System.out.println("\r\n\r\n\r\n" + month + "电影");
        RecSysOCN recSysMovie = new RecSysOfMovies();
        int movieRecord = recSysMovie.train(movieInput,  0.01);
        w.getRecList(20171230, movieInput, movieRecList);
        recSysMovie.raedItemRecList(movieRecList);
        recSysMovie.recAllUsersToFile(movieOutput, 5);
        Pair<Integer, Integer> movieAcc = w.testByTwoFile(movieOutput, movieTest);
        System.out.println(movieAcc.getValue0());


        System.out.println("" + month + "电视剧");
        RecSysOCN recSysSeries = new RecSysOfSeries();
        int seriesRecord = recSysSeries.train(seriesInput,  0.1);
        w.getRecList(20171330, seriesInput, seriesRecList);
        recSysSeries.raedItemRecList(seriesRecList);
        recSysSeries.recAllUsersToFile(seriesOutput, 5);
        Pair<Integer, Integer> seriesAcc = w.testByTwoFile(seriesOutput, seriesTest);
        System.out.println(seriesAcc.getValue0());

        double ans = (movieAcc.getValue0() + seriesAcc.getValue0()) * 1.0 / (movieAcc.getValue1() + seriesAcc.getValue1());
        System.out.println("" + month + "整体准确率：" + ans);

        long endtime = System.currentTimeMillis();
        long costTime = (endtime - begintime);
        System.out.println("数据量" + (movieRecord + seriesRecord));
        System.out.println("耗时毫秒" + costTime);
        //wHaoshi.write(month + "\t" + (movieRecord + seriesRecord) + "\t" + costTime + "\r\n");
        wAcc.write(month + "\t" +
                movieAcc.getValue0() + "\t" + movieAcc.getValue1() + "\t" + (movieAcc.getValue0() * 1.0 / movieAcc.getValue1()) + "\t" +
                seriesAcc.getValue0() + "\t" + seriesAcc.getValue1() + "\t" + (seriesAcc.getValue0() * 1.0 / seriesAcc.getValue1()) + "\t" +
                (movieAcc.getValue0() + seriesAcc.getValue0()) + "\t" + (movieAcc.getValue1() + seriesAcc.getValue1()) + "\t" + ans +"\t"+
                movieAcc.getValue0()*1.0 / (movieAcc.getValue0() + seriesAcc.getValue0()) + "\t" + seriesAcc.getValue0()*1.0 / (movieAcc.getValue0() + seriesAcc.getValue0()) +
                "\r\n");
        return ans;
    }

    //for(int i=201701;i<=201708;i++){
    //    createDir("E:\\OCN\\AllVod\\"+i);
    //}
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {// 判断目录是否存在
            System.out.println("创建目录失败，目标目录已存在！");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
            destDirName = destDirName + File.separator;
        }
        if (dir.mkdirs()) {// 创建目标目录
            System.out.println("创建目录成功！" + destDirName);
            return true;
        } else {
            System.out.println("创建目录失败！");
            return false;
        }
    }

    public static void userSim() throws Exception {
        String userMac = "DC2A14053546";
        String path = "E:\\OCN\\AllVod\\收敛天\\";
        String movieInput = path + 20 + "\\movieInput.txt";
        String seriesInput = path + 20 + "\\seriesInput.txt";
        String movieRecList = path + 20 + "\\movieRecList.txt";
        String seriesRecList = path + 20 + "\\seriesRecList.txt";
        String movieOutput = path + 20 + "\\movieOutput.txt";
        String seriesOutput = path + 20 + "\\seriesOutput.txt";
        String movieTest = path + 20 + "\\movieTest.txt";
        String seriesTest = path + 20 + "\\seriesTest.txt";

        WatchDataManager w = new WatchDataManager();
        RecSysOCN recSysMovie = new RecSysOfMovies();
        recSysMovie.train(movieInput, 0.01);
        List<Double> list1 = recSysMovie.getUserVec(userMac);

        BufferedReader r = new BufferedReader(new FileReader(movieInput));
        String line = "";
        Set<String> userItems = new HashSet<>();
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            if (words[0].equals(userMac)) userItems.add(words[1]);
        }
        r = new BufferedReader(new FileReader(movieInput));

        Map<String, Integer> simUser = new HashMap<>();
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            if (!userItems.contains(words[1])) continue;
            if (!simUser.containsKey(words[0])) simUser.put(words[0], 0);
            simUser.put(words[0], simUser.get(words[0]) + 1);
        }
        List<Pair<String, Integer>> simList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : simUser.entrySet()) {
            simList.add(new Pair<String, Integer>(entry.getKey(), entry.getValue()));
        }
        simList.sort(new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                return o2.getValue1().compareTo(o1.getValue1());
            }
        });
        for (int i = 0; i < simList.size(); i++) {
            Pair<String, Integer> pair = simList.get(i);
            String userName = pair.getValue0();
            int sim = pair.getValue1();
            List<Double> list2 = recSysMovie.getUserVec(userName);
            double ans = 0;
            for (int j = 0; j < list1.size(); j++) {
                ans += Math.pow(list1.get(j) - list2.get(j), 2.0);
            }
            ans /= list1.size();
            System.out.println(userName + " " + sim + " " + ans);
        }
    }
}
/*

20171021184800
1.推荐列表为 11.01-11.30 记录里面出现过的所有item
2.评分= 求和：1+x/y
 */
