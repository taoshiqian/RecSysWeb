package recsys;

import Jama.Matrix;
import fm.ALS;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Tuple;
import tool.MapID;
import tool.Rating;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class RecSysOCN {

    protected static MapID mapUser = new MapID();
    protected static MapID mapItem = new MapID();
    protected ALS als = new ALS();
    protected List<Integer> itemRecList = new ArrayList<>();
    protected Map<Integer, String> mapItemRecList = new HashMap<>();
    protected Map<String, List<Pair<Integer, Double>>> recResult = new HashMap<>();

    protected abstract Triplet<Integer, Integer, Double> stringArraysToTuple(String[] words);

    protected abstract List<Pair<Integer, Double>> recForOneUser(int userId, int num);

    protected abstract int train(String FileName, double lambda) throws Exception;

    protected boolean filterByday(int day) {
        if (day < 15) return true;  //15号之前的过滤掉
        else return false;
    }

    protected List<Integer> recForOneUserByMac(String mac, int num) {
        List<Pair<Integer, Double>> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.addAll(recForOneUser(mapUser.getId(mac + "_" + i), num));
        }
        list.sort(new Comparator<Pair<Integer, Double>>() {
            @Override
            public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
                return Double.compare(o2.getValue1(), o1.getValue1());
            }
        });
        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < Math.min(list.size(), num); i++) {
            ret.add(list.get(i).getValue0());
        }
        return ret;
    }

    protected int timeQuantum(int day, int hm) {
        int y = day / 10000;
        int m = day / 100 % 100;
        int d = day % 100;
        int week;
        //System.out.println(y + " " + m + " " + d );
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
        //System.out.println(" " + (week+1));
        week += 1;  //1-7
        if (week == 6 || week == 7) return 0;
        else if (hm >= 900 && hm <= 1930) return 1;
        else return 2;
    }


    protected void raedItemRecList(String fileName) throws Exception {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
        BufferedReader reader = new BufferedReader(isr);
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\\|");
            String itemName = words[0];
//            String itemInfo = words[1];
            int itemId = mapItem.getId(itemName);
            if (mapItemRecList.containsKey(itemId)) continue;
            itemRecList.add(itemId);
//            mapItemRecList.put(itemId,itemInfo);
        }
        System.out.println("待推荐列表读完成"); //+ itemRecList.size());
        reader.close();
    }

    protected void recAllUsersToFile(String fileName, int recNum) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (int i = 0; i < mapUser.getSize(); i++) {
            List<Pair<Integer, Double>> recList = recForOneUser(i, recNum);// als.limitedRecommendByUserId(userId,itemRecList,5,false);
            String name = mapUser.getName(i).split("_")[0];
            if (!recResult.containsKey(name)) recResult.put(name, new ArrayList<>());
            recResult.get(name).addAll(recList);
//            String recString = "";
//            for (int j = 0; j < recList.size(); j++) {
//                int itemId = recList.get(j);
//                if (mapItem.getName(itemId).equals("无")) {
//                }//加热门影片
//                recString += "|" + mapItem.getName(itemId);//;+mapItem.getName(itemId);
//            }
//            writer.write(mapUser.getName(i) + recString + "\r\n");
        }
        for (Map.Entry<String, List<Pair<Integer, Double>>> entry : recResult.entrySet()) {
            String recString = "";
            List<Pair<Integer, Double>> recList = entry.getValue();
            recList.sort(new Comparator<Pair<Integer, Double>>() {
                @Override
                public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
                    return Double.compare(o2.getValue1(), o1.getValue1());
                }
            });
            recList = recList.subList(0,Math.min(recNum,recList.size()));
            for (int j = 0; j < recList.size(); j++) {
                int itemId = recList.get(j).getValue0();
                if (mapItem.getName(itemId).equals("无")) {
                }//加热门影片
                recString += "|" + mapItem.getName(itemId);//;+mapItem.getName(itemId);
            }
            writer.write(entry.getKey() + recString + "\r\n");
        }
        writer.close();
        System.out.println("推荐文件成功");
    }

    protected Map<Integer, String> raedItemRecListByTime(String recListPath, int houres) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String nowTime = df.format(new Date());
        System.out.println("时间:" + nowTime);// new Date()为获取当前系统时间
        long startTime = Long.parseLong(nowTime.substring(0, 10));
        long endTime = startTime + houres;
        for (Long time = startTime + 1; time <= endTime; time++) {
            //long fileTime = (time/100)*100+time%24;
            //long fileTime = 2017083100 + time % 100 % 24;
            long fileTime = time % 100 % 24;
            raedItemRecList(recListPath + fileTime + "RecList.txt");
        }
        return mapItemRecList;
    }


    public void saveModel(String fileUsers, String fileItems) throws Exception {
        als.saveModel(fileUsers, fileItems);
    }

    public void loadModel(String fileUsers, String fileItems) throws Exception {
        als.loadModel(fileUsers, fileItems);
    }

    public int getUsersClass(String userMac) {
        if (!mapUser.contains(userMac)) return -1;
        int userId = mapUser.getId(userMac);
        return als.getUserClass(userId);
    }

    public List<Double> getUserVec(String userMac) {
        if (!mapUser.contains(userMac)) return new ArrayList<>();
        int userId = mapUser.getId(userMac);
        List<Double> list = als.getUserVec(userId);
        return list;
    }

    public void saveClassVector(String filename) throws Exception {
        BufferedWriter w = new BufferedWriter(new FileWriter(filename));
        try {
            for (int i = 0; i < 10000; i++) {
                String name = mapUser.getName(i);
                int clas = getUsersClass(name);
                List<Double> vec = getUserVec(name);
                w.write(name + "," + clas + "," + vec + "\r\n");
                System.out.println(i + "," + name);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            w.close();
        }
    }
}
