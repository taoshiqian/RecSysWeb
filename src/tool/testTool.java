package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class testTool {

    public static void main(String[] args) throws Exception {

        reclist2two();
    }

    static void broadcastRename()throws Exception{
        WatchDataManager w = new WatchDataManager();
        w.broadcastRename("E:\\OCN\\NGBLab\\201607\\RecList02\\broadcastRecList\\","","20170830");
    }

    static void reclist2two()throws Exception{ //推荐列表一分为2
        WatchDataManager w = new WatchDataManager();
        w.reclist2two("E:\\OCN\\NGBLab\\201605\\mapHot201605.txt",
                "E:\\OCN\\NGBLab\\201605\\RecList01\\broadcastRecList.txt",
                "E:\\OCN\\NGBLab\\201605\\RecList02\\broadcastRecList.txt");
    }

    static void broadcastReclistHot()throws Exception{//直播列表获取
                WatchDataManager w = new WatchDataManager();
       // w.eight2six("E:\\OCN\\live\\live_201607.txt","E:\\OCN\\live\\live_b_201607.txt");
        //movieNanme("E:\\OCN\\NGBLab\\201608\\movie201608RecListMapOne.txt","E:\\OCN\\NGBLab\\201608\\movie201608RecListMapOne2.txt");
        w.itemTimes("E:\\OCN\\live\\live_b_201606.txt","E:\\OCN\\live\\itemTime201606.txt");
        w.recListHot("E:\\OCN\\broadcast\\itemIdMap_201605-08_One.txt","E:\\OCN\\live\\itemTime201606.txt","E:\\OCN\\live\\mapHot201606.txt");
    }

    static void movieNanme(String input,String output)throws Exception{
        BufferedReader r = new BufferedReader(new FileReader(input));
        BufferedWriter w = new BufferedWriter(new FileWriter(output));
        String line = "";
        while ((line = r.readLine()) != null) {
            if(line.contains("NBA")||line.contains("赛")||line.contains("vs")||line.contains("VS")||line.contains("nba")) continue;
            w.write(line+"\r\n");
        }
        r.close();
        w.close();
    }


    static String mapOneFile = "E:\\OCN\\AllVod\\vodmetdataMapOne.txt";
    static Map<String, String> mapOne = new HashMap<>();

    public static void main2(String[] args) throws Exception {
        BufferedReader r = new BufferedReader(new FileReader(mapOneFile));
        String line = "";
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            mapOne.put(words[0], words[1]);
        }
        r.close();

        WatchDataManager w = new WatchDataManager();

        int month = 201601;
        for (; month <= 201612; month++) {
            recList2recListMap(month,"movie");
            recList2recListMap(month,"series");
        }
        for (month = 201701; month <= 201707; month++) {
            recList2recListMap(month,"movie");
            recList2recListMap(month,"series");
        }
    }


    static void recList2recListMap(int month,String type) throws Exception {
        String input = "E:\\OCN\\AllVod\\"+month+"\\"+type+""+month+"RecList.txt";
        String output = "E:\\OCN\\AllVod\\"+month+"\\"+type+""+month+"RecListMapOne.txt";
        BufferedReader r = new BufferedReader(new FileReader(input));
        BufferedWriter w = new BufferedWriter(new FileWriter(output));
        String line = "";
        while ((line = r.readLine()) != null) {
            String num = line;
            w.write(num+"|"+mapOne.get(num)+"\r\n");
        }
        r.close();
        w.close();
    }

}
