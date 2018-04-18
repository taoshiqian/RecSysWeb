package test;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import tool.MapID;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FindActiveUserItem {

    //用户：编号，时段，总时间
    private List<Triplet<String, Integer, Integer>> user = new ArrayList<>();

    //节目：编号，总时间
    private List<Pair<Integer, Long>> item = new ArrayList<>();

    private final int USER_NUM = 400 * 10000 * 24;
    private final int ITEM_NUM = 300 * 10000;
    private final int ACTIVE_USER_NUM = 10000;  //活跃用户数量
    private final int ACTIVE_ITEM_NUM = 10000;  //活跃节目数量
    private final int USER_THRESHOLD = 300;//用户受奖励的阈值，单位秒
    private final int USER_REWARD = 60;//用户换台的奖励，单位秒
    private final int ITEM_THRESHOLD = 300;//节目受奖励的阈值，单位秒
    private final int ITEM_REWARD = 60;//节目被更多用户观看的奖励，单位秒

    private MapID mapUser = new MapID();//用户编号映射
    private int[] sumtimeOfUser = new int[USER_NUM];
    private long[] sumtimeOfItem = new long[ITEM_NUM];

    private int timeToPeriod(String time) {
        int hour = Integer.parseInt(time.substring(8, 10));
        int half = Integer.parseInt(time.substring(10, 12)) >= 30 ? 1 : 0;
        return hour * 2 + half;
    }

    public void findFromFile(String source, String activeUserFile, String activeItemFile) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(source));
        String line = null;
        int maxItemId = 0;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\\|");
            int userId = mapUser.getId(words[0] + "|" + timeToPeriod(words[3]));
            int itemId = Integer.parseInt(words[5]);
            if (itemId > maxItemId) maxItemId = itemId;
            int duration = Integer.parseInt(words[4]);
            sumtimeOfUser[userId] += duration + ((duration >= USER_THRESHOLD) ? USER_REWARD : 0); //观看超过阈值，"+数字"作为用户换台的活跃奖励
            sumtimeOfItem[itemId] += duration + ((duration >= ITEM_THRESHOLD) ? ITEM_REWARD : 0); //观看超过阈值，"+数字"作为节目有更多用户的活跃奖励
        }
        for (int i = 0; i < USER_NUM; i++) {
            if (sumtimeOfUser[i] == 0) break;
            String[] words = mapUser.getName(i).split("\\|");
            String uID = words[0];
            int period = Integer.parseInt(words[1]);
            int sumtime = sumtimeOfUser[i];
            user.add(new Triplet<String, Integer, Integer>(uID, period, sumtime));
        }
        for (int i = 0; i < maxItemId; i++) {
            item.add(new Pair<Integer,Long>(i,sumtimeOfItem[i]));
        }
        user.sort(new Comparator<Triplet<String, Integer, Integer>>() {
            @Override
            public int compare(Triplet<String, Integer, Integer> o1, Triplet<String, Integer, Integer> o2) {
                return o2.getValue2()-o1.getValue2();
            }
        });
        item.sort(new Comparator<Pair<Integer, Long>>() {
            @Override
            public int compare(Pair<Integer, Long> o1, Pair<Integer, Long> o2) {
                long long1=o1.getValue1();
                long long2=o2.getValue1();
                if(long2-long1>0) return 1;
                else if(long2-long1==0) return 0;
                else return -1;
            }
        });
        BufferedWriter writerUser = new BufferedWriter(new FileWriter(activeUserFile));
        BufferedWriter writerItem = new BufferedWriter(new FileWriter(activeItemFile));
        for(int i=0;i<Math.min(ACTIVE_USER_NUM,user.size());i++) {
            Triplet<String, Integer, Integer> t = user.get(i);
            //System.out.println(t.getValue0()+","+t.getValue1()+","+t.getValue2());
            String writeLine = t.getValue0()+"|"+t.getValue1()+"|"+t.getValue2()+"\r\n";
            writerUser.write(writeLine);
        }
        for(int i=0;i<Math.min(ACTIVE_ITEM_NUM,item.size());i++) {
            Pair<Integer, Long> t = item.get(i);
            String writeLine = t.getValue0()+"|"+t.getValue1()+"\r\n";
            writerItem.write(writeLine);
        }
    }
}
