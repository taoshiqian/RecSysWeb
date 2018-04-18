package test;

import org.javatuples.Pair;
import tool.MapID;
import tool.WatchDataManager;

import java.io.*;
import java.util.*;

public class HelpUtil {

    public static void main(String[] args) throws Exception {
        String input = "E:\\OCNdata\\VOD\\active_users_watch_data.txt";
        String output = "E:\\OCNdata\\VOD\\watch_data_sorted_user.txt";
        WatchDataManager m = new WatchDataManager();
        m.sortByUser(input,output);
    }

    public static void main5(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("E:\\OCNdata\\VOD\\rec_20161101_20161115_data.txt"));
        List<Integer> itemRecList = new ArrayList<>();
        String line = null;
        while((line=reader.readLine())!=null){
            int itemId = Integer.parseInt(line);
            if(itemRecList.contains(itemId)) continue;
            itemRecList.add(itemId);
        }
        System.out.println("把待推荐列表读完毕,总数："+itemRecList.size());

        BufferedReader r = new BufferedReader(new FileReader("E:\\OCNdata\\VOD\\recResult.txt"));
        MapID mapItem = new MapID();
        int[] num = new int[10000];
        while((line=r.readLine())!=null){
            String[] words = line.split("\\|");
            for(int i=1;i<=5;i++){
                int itemId = mapItem.getId(words[i]);
                num[itemId]++;
            }
        }
        Arrays.sort(num);
        System.out.println("已推荐节目总数："+mapItem.getSize());
        int sum=0,count=0;
        for(int i=9999;i>=0;i--){
            if(num[i]==0) break;
            sum+=num[i];  count++;
            System.out.println(count+","+num[i]);
        }
        System.out.println("每个节目平均被推荐："+sum*1.0/count);
        r.close();
    }

    public static void main3(String[] args) throws Exception {
        BufferedReader r = new BufferedReader(new FileReader("E:\\OCNdata\\201601-04\\201601-04.txt"));
        BufferedWriter w2 = new BufferedWriter(new FileWriter("E:\\OCNdata\\201601-04\\20160401.txt"));
        String line = null;
        while((line=r.readLine())!=null){

        }
    }


    public static void main4(String[] args) throws Exception {
        //String source = "E:\\OCNdata\\userWatchTable8.txt";
        String source = "E:\\OCNdata\\201601-04\\201601-04.txt";
        String userFile="E:\\OCNdata\\ActiveUser.txt";
        String itemFile="E:\\OCNdata\\ActiveItem.txt";
        FindActiveUserItem2 f = new FindActiveUserItem2();
        f.findFromFile(source,userFile,itemFile);
    }


    static int MAXN=10000000;//8
    static  MapID mapUser = new MapID();
    static long[] sumTime = new long[MAXN];
    static List<Pair<Integer,Long> > people = new ArrayList<>();
    //10^6数据，1.5s
    //10^7数据，7.71s
    public static void main2(String[] args) throws Exception{
        long startTime = System.currentTimeMillis(); // 获取开始时间
        BufferedReader r = new BufferedReader(new FileReader("E:\\OCNdata\\201601-04\\201601-04.txt"));
        int num=MAXN;//8
        String line = null;
        while((line=r.readLine())!=null){
            //System.out.println(line);
            String[] words = line.split("\\|");
            int userId = mapUser.getId(words[0]);
            long duration = Long.parseLong(words[4]);
            sumTime[userId]+=duration;
            num--;
            if(num<=0) break;
        }
        for(int i=0;i<MAXN;i++){
            if(sumTime[i]>0)  people.add(new Pair<Integer,Long>(i,sumTime[i]));
        }
        people.sort(new Comparator<Pair<Integer,Long>>() {
            @Override
            public int compare(Pair p1, Pair p2) {
                Long t1=Long.parseLong(p1.getValue1().toString());
                Long t2=Long.parseLong(p2.getValue1().toString());
                if ( t1>t2 ) return -1;
                else if(t1==t2) return 0;
                else return 1;
            }
        });
        Long endTime = System.currentTimeMillis(); //结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
        for(int i=0;i<people.size();i++){
            //System.out.println(people.get(i).getValue0()+","+people.get(i).getValue1());
        }
    }

    //4字段变8字段,读多个文件+写一个文件
    public static void main1(String[] args) throws Exception {
        //BufferedWriter w1 = new BufferedWriter(new FileWriter("E:\\data\\split\\out1.txt"));
        BufferedWriter w = new BufferedWriter(new FileWriter("E:\\201601-04.txt",true));
        File fileDirectory = new File("E:\\data\\OCN\\train8");
        if (fileDirectory.isDirectory()) {
            File[] files = fileDirectory.listFiles();
            for (File file : files) if (file.isFile()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line = null;
                    while ((line=reader.readLine())!=null){
                        String[] words = line.split("\\|");
                        String out = words[0]+"|0|东方卫视|20170101095225|"+words[2]+"|"+words[1]+"|20170101094500|"+words[3]+"\r\n";
                        w.write(out);
                    }
            }
        }
    }
}
