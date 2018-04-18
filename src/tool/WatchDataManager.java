package tool;


//import com.sun.java.swing.plaf.motif.MotifMenuUI;
//import com.sun.javafx.collections.MappingChange;
import org.javatuples.Pair;
//import org.omg.PortableInterceptor.INACTIVE;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WatchDataManager {

    public void sortByUser(String input, String output) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\\|");
            list.add(new Pair<String, String>(words[0], words[1] + "|" + words[2] + "|" + words[3]));
        }
        list.sort(new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                return o1.compareTo(o2);
            }
        });
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        for (Pair<String, String> pair : list) {
            writer.write(pair.getValue0() + "|" + pair.getValue1() + "\r\n");
        }
        writer.close();
    }

    //20171020202755    8+6=14位数字
    public void getRecList(int timesThreshold, String input, String ret) throws Exception {
        //public void getRecList(String input , String ret) throws  Exception{
        BufferedReader r = new BufferedReader(new FileReader(input));
        String line = "";
        Map<String, Integer> itemTimes = new HashMap<>();
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            String itemName = words[1];
            int time = Integer.parseInt(words[4].substring(0, 8));
            //if(time<timesThreshold) { //time<timesThreshold
            //if(time%100<=15) continue;
            if (!itemTimes.containsKey(itemName)) itemTimes.put(itemName, 0);
            itemTimes.put(itemName, itemTimes.get(itemName) + 1);
            //}
        }
        r.close();
        List<Pair<String, Integer>> recList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : itemTimes.entrySet()) {
            recList.add(new Pair<String, Integer>(entry.getKey(), entry.getValue()));
        }
        recList.sort(new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                return o2.getValue1().compareTo(o1.getValue1());
            }
        });
        BufferedWriter w = new BufferedWriter(new FileWriter(ret));
        for (Pair<String, Integer> pair : recList) {
            w.write(pair.getValue0() + "\r\n");
        }
        w.close();
    }

    public void getRecListTestCoverage(String input, String ret, int num, double percentageItem, double percentagePeople) throws Exception {
        //public void getRecList(String input , String ret) throws  Exception{
        BufferedReader r = new BufferedReader(new FileReader(input));
        String line = "";
        Map<String, Integer> itemTimes = new HashMap<>();
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            String itemName = words[1];
            int time = Integer.parseInt(words[4].substring(0, 8));
            if (!itemTimes.containsKey(itemName)) itemTimes.put(itemName, 0);
            itemTimes.put(itemName, itemTimes.get(itemName) + 1);
            //}
        }
        r.close();
        List<Pair<String, Integer>> recList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : itemTimes.entrySet()) {
            recList.add(new Pair<String, Integer>(entry.getKey(), entry.getValue()));
        }
        recList.sort(new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                return o2.getValue1().compareTo(o1.getValue1());
            }
        });
        System.out.println("所有节目个数" + recList.size());
        BufferedWriter w = new BufferedWriter(new FileWriter(ret));
        int count = 0;
        // 电影待推荐数=min（原推荐列表*0.3，受推荐人数*0.02）
        num = (int) Math.sqrt(num * percentagePeople * recList.size() * percentageItem);
        for (Pair<String, Integer> pair : recList) {
            if ((++count) > num) continue;
            w.write(pair.getValue0() + "\r\n");
        }
        w.close();
    }

    public void splitInput(String Input, String out1, String out2) throws Exception {
        BufferedReader r = new BufferedReader(new FileReader(Input));
        BufferedWriter wa = new BufferedWriter(new FileWriter(out1));
        BufferedWriter wb = new BufferedWriter(new FileWriter(out2));
        String line = "";
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            int time = Integer.parseInt(words[4].substring(0, 8));
            if (time >= 20161124) wb.write(line + "\r\n");
            else wa.write(line + "\r\n");
        }
        r.close();
        wa.close();
        wb.close();
    }

    private int findNum(String str) {
        String regEx = "[^0-9]+";//匹配指定范围内的数字
        //Pattern是一个正则表达式经编译后的表现模式
        Pattern p = Pattern.compile(regEx);
        // 一个Matcher对象是一个状态机器，它依据Pattern对象做为匹配模式对字符串展开匹配检查。
        Matcher m = p.matcher(str);
        //将输入的字符串中非数字部分用空格取代并存入一个字符串
        String string = m.replaceAll(" ").trim();
        //以空格为分割符在讲数字存入一个字符串数组中
        String[] strArr = string.split(" ");
        //遍历数组转换数据类型输出
        int ans = 0;
        for (int i = strArr.length - 1; i >= 0; i--) {
            try {
                ans = Integer.parseInt(strArr[i]);//最后数字
                break;
            } catch (Exception e) {
            }
        }
        return ans;
    }

    public void multi2one(String input, String output) throws Exception {
        BufferedReader r = new BufferedReader(new FileReader(input));
        BufferedWriter w = new BufferedWriter(new FileWriter(output));
        String line = "";
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            if (words.length == 2) {
                w.write(line + "\r\n");
            } else if (words.length > 2) {
                int index = 1, max = findNum(words[1]);
//                for (int i = 2; i < words.length; i++) {
//                    int num = findNum(words[i]);
//                    if (num > max) {
//                        max = num;
//                        index = i;
//                    }
//                }
                w.write(words[0] + "|" + words[index] + "\r\n");
            }
        }
        r.close();
        w.close();
    }

    public void multi2oneBroadcast(String input, String output, String threshold) throws Exception {
        BufferedReader r = new BufferedReader(new FileReader(input));
        BufferedWriter w = new BufferedWriter(new FileWriter(output));
        String line = "";
        while ((line = r.readLine()) != null) {
            System.out.println(line);
            String[] words = line.split("\\|");
            if (words.length == 2) {
                w.write(line + "\r\n");
            } else if (words.length > 2) {
                for (int i = 1; i < words.length; i++) {
                    if (words[i].contains(threshold)) {
                        w.write(words[0] + "|" + words[i] + "\r\n");
                        break;
                    }
                }
            }
        }
        r.close();
        w.close();
    }

    public Pair<Integer, Integer> testByTwoFile(String output, String inB) throws Exception {
        Map<String, HashSet<String>> predictItems = new HashMap<>();
        Map<String, HashSet<String>> predictUsers = new HashMap<>();
        Map<String, HashSet<String>> realItems = new HashMap<>();
        Map<String, HashSet<String>> realUsers = new HashMap<>();

        BufferedReader ro = new BufferedReader(new FileReader(output)); //predict
        String line = "";
        while ((line = ro.readLine()) != null) {
            String[] words = line.split("\\|");
            String userName = words[0];
            for (int i = 1; i < words.length; i++) {
                String itemName = words[i];
                if (!predictUsers.containsKey(userName)) predictUsers.put(userName, new HashSet<>());
                predictUsers.get(userName).add(itemName);
                if (!predictItems.containsKey(itemName)) predictItems.put(itemName, new HashSet<>());
                predictItems.get(itemName).add(userName);
            }
        }

        BufferedReader rb = new BufferedReader(new FileReader(inB));
        Set<String> userOfHasWatched = new HashSet<>();
        while ((line = rb.readLine()) != null) {
            String[] words = line.split("\\|");
            String userName = words[0];
            String itemName = words[1];
            userOfHasWatched.add(userName);
            if (!realUsers.containsKey(userName)) realUsers.put(userName, new HashSet<>());
            realUsers.get(userName).add(itemName);
            if (!realItems.containsKey(itemName)) realItems.put(itemName, new HashSet<>());
            realItems.get(itemName).add(userName);
        }

        //System.out.println("多少用户接受了我们的推荐：");
        int sum = 0, yes = 0;
        for (Map.Entry<String, HashSet<String>> entry : realUsers.entrySet()) {
            String userName = entry.getKey();
            if (predictUsers.get(userName) == null) continue;
            Set<String> userRealItems = entry.getValue();
            Set<String> result = new HashSet<>();
            result.addAll(userRealItems);
            result.retainAll(predictUsers.get(userName));
            if (result.size() > 0) yes++;
            sum++;
        }
        //System.out.println("看了点播的用户"+sum+",看了点播而且看了推荐的用户"+yes);
        //System.out.println("用户比例："+yes*1.0/sum);

        //System.out.println("多少item被正确推荐");//accuracy
        int itemSum = 0;
        sum = 0;
        yes = 0;
        for (Map.Entry<String, HashSet<String>> entry : predictItems.entrySet()) { //被推荐物品集合
            itemSum++;
            String itemName = entry.getKey();
            Set<String> itemPredictUsers = entry.getValue();//该物品被推荐的人
            itemPredictUsers.retainAll(userOfHasWatched); //被推荐而且看了点播的人
            Set<String> result = new HashSet<>(itemPredictUsers);
            Set<String> itemRealUsers = realItems.get(itemName);
            if (itemRealUsers == null) result.clear();
            else result.retainAll(itemRealUsers);   //真正看的人，交集
            int yesI = result.size();
            int sumI = itemPredictUsers.size();
            yes += yesI;
            sum += sumI;
        }
        //System.out.println("平均准确率"+yes*1.0/sum);
        //System.out.println("推荐出去的item列表长度"+itemSum);
        //System.out.println("所有被推荐item的yes总数"+yes);
        //System.out.println("所有被推荐item的sum总数"+sum);
        return new Pair<Integer, Integer>(yes, sum);
    }

    public void has(String rec1, String rec2) throws Exception {
        Set<String> set1 = new HashSet<>();
        BufferedReader r1 = new BufferedReader(new FileReader(rec1));
        String line = "";
        while ((line = r1.readLine()) != null) {
            set1.add(line);
        }

        Set<String> set2 = new HashSet<>();
        BufferedReader r2 = new BufferedReader(new FileReader(rec2));
        while ((line = r2.readLine()) != null) {
            set2.add(line);
        }

        Set<String> result = new HashSet<>(set1);
        result.retainAll(set2);
        System.out.println(set1.size());
        System.out.println(set2.size());
        System.out.println(result.size());
        System.out.println("\r\n");
    }

    //输入历史行为，得到item的观看人数的排序，后接recListHot
    public void itemTimes(String input, String itemTimes) throws Exception {
        BufferedReader r = new BufferedReader(new FileReader(input));
        String line = "";
        Map<String, List<String>> itemsTimes = new HashMap<>();
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            String itemName = words[1];
            String time = words[4];
            if (!itemsTimes.containsKey(itemName)) {
                itemsTimes.put(itemName, new ArrayList<String>());
            }
            itemsTimes.get(itemName).add(time.substring(4, 8));
        }
        r.close();

        List<String> itemlist = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : itemsTimes.entrySet()) {
            itemlist.add(entry.getKey());
        }
        itemlist.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return itemsTimes.get(o2).size() - itemsTimes.get(o1).size();
            }
        });


        BufferedWriter w = new BufferedWriter(new FileWriter(itemTimes));
        //for(Map.Entry<String,List<String>> entry : itemsTimes.entrySet() ){
        for (String itemName : itemlist) {
            // String itemName = entry.getKey();
            List<String> list = itemsTimes.get(itemName);
            list.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            w.write(itemName);
            for (String s : list) {
                // w.write("|"+s);
            }
            w.write("\r\n");
        }
        w.close();
    }

    public void getRecList1234() throws Exception {
        String seriesRecListHot = "E:\\OCN\\result201708\\seriesRecListHot.txt";
        String film123 = "E:\\OCN\\result201708\\film_2020.txt";
        String seriesRecListFile = "E:\\OCN\\result201708\\seriesRecList.txt";
        String moiveRecListFile = "E:\\OCN\\result201708\\movieRecList.txt";
        BufferedWriter ws = new BufferedWriter(new FileWriter(seriesRecListFile));
        BufferedWriter wm = new BufferedWriter(new FileWriter(moiveRecListFile));
        BufferedReader r1 = new BufferedReader(new FileReader(seriesRecListHot));
        String line = "";
        while ((line = r1.readLine()) != null) {
            ws.write(line + "\r\n");
        }
        r1.close();
        BufferedReader r2 = new BufferedReader(new FileReader(film123));
        while ((line = r2.readLine()) != null) {
            String[] words = line.split("\\|");
            if (words[0].equals("1")) {
                ws.write(words[1] + "\r\n");
            } else if (words[0].equals("2")) {
                wm.write(words[1] + "\r\n");
            }
        }
        r2.close();
        ws.close();
        wm.close();
    }

    public void getOfferingId() throws Exception {
        String input = "E:\\OCN\\result\\seriesMap2.txt";
        String output = "E:\\OCN\\result\\seriesMap.txt";
        String mapFile = "E:\\OCN\\result\\offeringidBycontentname.txt";
        BufferedReader rm = new BufferedReader(new FileReader(mapFile));
        String line = "";
        Map<String, String> mapOfferingId = new HashMap<>();
        while ((line = rm.readLine()) != null) {
            String[] words = line.split("\\|");
            mapOfferingId.put(words[0], words[1]);
        }
        rm.close();
        BufferedReader r = new BufferedReader(new FileReader(input));
        BufferedWriter w = new BufferedWriter(new FileWriter(output));
        while ((line = r.readLine()) != null) {
            String ts = line.split("\\|")[1].split(",")[0];
            line = line.replace(ts, mapOfferingId.get(ts));
            w.write(line + "\r\n");
        }
        r.close();
        w.close();
    }

    //前接itemTimes，按时间排成hot序
    public void recListHot(String mapOne, String Times, String itemMap) throws Exception {
        BufferedReader ro = new BufferedReader(new FileReader(mapOne));
        BufferedReader rt = new BufferedReader(new FileReader(Times));
        BufferedWriter w = new BufferedWriter(new FileWriter(itemMap));
        String line = "";
        Map<String, String> map = new HashMap<>();
        while ((line = ro.readLine()) != null) {
            String[] words = line.split("\\|");
            map.put(words[0], words[1]);
        }
        ro.close();
        while ((line = rt.readLine()) != null) {
            if (!map.containsKey(line)) continue;
            w.write(line + "|" + map.get(line) + "\r\n");
        }
        rt.close();
        w.close();
    }

    public void allToMonth(String input, String type) throws Exception {
        Map<String, BufferedWriter> mapWriter = new HashMap<>();
        for (int i = 201601; i <= 201612; i++) {
            mapWriter.put(i + "", new BufferedWriter(new FileWriter("E:\\OCN\\AllVod\\" + i + "\\" + type + "" + i + "Input.txt")));
        }
        for (int i = 201701; i <= 201708; i++) {
            mapWriter.put(i + "", new BufferedWriter(new FileWriter("E:\\OCN\\AllVod\\" + i + "\\" + type + "" + i + "Input.txt")));
        }
        BufferedReader rall = new BufferedReader(new FileReader(input));
        String line = "";
        int count = 0;
        while ((line = rall.readLine()) != null) {
            String[] words = line.split("\\|");
            String time = words[4].substring(0, 6);
            mapWriter.get(time).write(line + "\r\n");
            count++;
        }
        System.out.println(count);
        rall.close();
        for (Map.Entry<String, BufferedWriter> map : mapWriter.entrySet()) {
            map.getValue().close();
        }
    }

    public void allToMonthTest(String input, String type) throws Exception {
        Map<String, BufferedWriter> mapWriter = new HashMap<>();
        for (int i = 201601; i <= 201611; i++) {
            mapWriter.put(i + "", new BufferedWriter(new FileWriter("E:\\OCN\\AllVod\\" + i + "\\" + type + "" + (i + 1) + "0107Test.txt")));
        }
        mapWriter.put(201612 + "", new BufferedWriter(new FileWriter("E:\\OCN\\AllVod\\" + 201612 + "\\" + type + "" + 201701 + "0107Test.txt")));
        for (int i = 201701; i <= 201707; i++) {
            mapWriter.put(i + "", new BufferedWriter(new FileWriter("E:\\OCN\\AllVod\\" + i + "\\" + type + "" + (i + 1) + "0107Test.txt")));
        }
        BufferedReader rall = new BufferedReader(new FileReader(input));
        String line = "";
        int count = 0;
        while ((line = rall.readLine()) != null) {
            String[] words = line.split("\\|");
            String time = words[4].substring(0, 6);
            if (time.equals("201601")) continue;
            else if (time.equals("201701")) time = "201612";
            else time = (Integer.parseInt(time) - 1) + "";
            if (Integer.parseInt(words[4].substring(6, 8)) > 7) continue;
            mapWriter.get(time).write(line + "\r\n");
            count++;
        }
        System.out.println(count);
        rall.close();
        for (Map.Entry<String, BufferedWriter> map : mapWriter.entrySet()) {
            map.getValue().close();
        }
    }

    public void allToWeek(String allfile, String trainfile, int start, int end) throws Exception {
        BufferedReader r = new BufferedReader(new FileReader(allfile));
        BufferedWriter w = new BufferedWriter(new FileWriter(trainfile));
        String line = "";
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            String timeString = words[4].substring(0, 8);
            int time = Integer.parseInt(timeString);
            if (time >= start && time <= end) w.write(line + "\r\n");
        }
        r.close();
        w.close();
    }

    public void mapMultiTORecListTime(String input, String outputPath) throws Exception {
        BufferedReader ri = new BufferedReader(new FileReader(input));
        Map<Long, BufferedWriter> mapTimeWriter = new HashMap<>();
        String line = "";
        while ((line = ri.readLine()) != null) {
            String[] programs = line.split("\\|");
            for (int i = 1; i < programs.length; i++) {
                String program = programs[i];
                String[] words = program.split(",");
                long endTime = Long.parseLong(words[words.length - 1].substring(0, 10));
                int minute = Integer.parseInt(words[words.length - 1].substring(0, 10));
                if (minute == 0) endTime -= 1;
                if (!mapTimeWriter.containsKey(endTime))
                    mapTimeWriter.put(endTime, new BufferedWriter(new FileWriter(outputPath + endTime + "RecList.txt")));
                mapTimeWriter.get(endTime).write(programs[0] + "|" + programs[i] + "\r\n");
                //会重复写ID，所以读取的时候去重操作（用 Map 等）
            }
        }
        ri.close();
        for (Map.Entry<Long, BufferedWriter> entry : mapTimeWriter.entrySet()) {
            entry.getValue().close();
        }
    }


    public Pair<Integer, Integer> coverageRatio(String output, String RecList) throws Exception {
        BufferedReader ro = new BufferedReader(new FileReader(output));
        Set<String> recSet = new HashSet<>();
        String line = "";
        int people = 0;
        while ((line = ro.readLine()) != null) {
            people++;
            String[] words = line.split("\\|");
            for (int i = 1; i < words.length; i++) {
                String itemId = words[i];
                recSet.add(itemId);
            }
        }
        BufferedReader rl = new BufferedReader(new FileReader(RecList));
        List<String> list = new ArrayList<>();
        while ((line = rl.readLine()) != null) {
            list.add(line);
        }
        int max = 0;
        for (int i = 0; i < list.size(); i++) {
            if (recSet.contains(list.get(i))) max = i;
        }
        System.out.println("人数" + people + "。。。已推荐size" + recSet.size() + "。。。max:" + max + "。。。待推荐size:" + list.size());
        return new Pair<Integer, Integer>(recSet.size(), list.size());
        //return max*1.0/list.size();
    }

    public void statistics(String input, String output) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式 2017-11-09 17:35:20
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        BufferedReader r = new BufferedReader(new FileReader(input));
        BufferedWriter w = new BufferedWriter(new FileWriter(output));
        String line = "";
        Set<String> users = new HashSet<>();
        Set<String> items = new HashSet<>();
        int c = 0;
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            String userName = words[0];
            String itemName = words[5];
            Long time = Long.parseLong(words[3].substring(0, 8));
            if (time >= 20160201) break;
            int duration = Integer.parseInt(words[4]);
            if (duration <= 180) continue;
            w.write(userName + "|" + itemName + "|" + words[4] + "|" + words[7] + "|" + words[3] + "\r\n");
            users.add(userName);
            items.add(itemName);
            c++;
        }
        r.close();
        w.close();
        System.out.println(users.size() + "," + items.size());
        System.out.println(c);
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
    }

    public void look(String file) throws Exception {
        BufferedReader r = new BufferedReader(new FileReader(file));
        String line = "";
        int i = 30;
        while ((line = r.readLine()) != null) {
            if (i < 0) break;
            i--;
            System.out.println(line);
        }
    }

    public void userLike(String input, String output) throws Exception { //直播1，点播0
        BufferedReader r = new BufferedReader(new FileReader(input));
        BufferedWriter w = new BufferedWriter(new FileWriter(output));
        String line = "";
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\t");
            int like = words[1].equals("直播") ? 1 : 0;
            w.write(words[0] + " " + like + "\r\n");
        }
        r.close();
        w.close();
    }
    public void eight2six(String input,String output)throws Exception{
        BufferedReader r = new BufferedReader(new FileReader(input));
        BufferedWriter w = new BufferedWriter(new FileWriter(output));
        String line = "";
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            line = words[0]+"|"+words[5]+"|"+words[4]+"|"+words[7]+
                    "|"+words[3]+"|"+words[6];
            w.write(line + "\r\n");
        }
        r.close();
        w.close();
    }

    //分为两部分
    public void reclist2two(String input,String output1,String output2)throws Exception{
        BufferedReader r = new BufferedReader(new FileReader(input));
        BufferedWriter w1 = new BufferedWriter(new FileWriter(output1));
        BufferedWriter w2 = new BufferedWriter(new FileWriter(output2));
        String line = "";
        int count = 0;
        while ((line = r.readLine()) != null) {
            count++;
            if(count%2==1) w1.write(line+"\r\n");
            else w2.write(line+"\r\n");
        }
        r.close();
        w1.close();
        w2.close();
    }

    //
    public void broadcastRename(String input,String output,String day)throws Exception{
        for(int i=0;i<=23;i++){
            renameFile(input+day+(i>=10?"":"0")+i+"RecList.txt",
                    input+(i>=10?"":"0")+i+"RecList.txt");
        }
    }

    //文件重命名
    public static boolean renameFile(String file, String toFile) {
        File toBeRenamed = new File(file);
        // 检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            System.out.println("文件不存在: " + file);
            return false;
        }
        File newFile = new File(toFile);
        // 修改文件名
        if (toBeRenamed.renameTo(newFile)) {
            System.out.println("重命名成功.");
            return true;
        } else {
            System.out.println("重命名失败");
            return false;
        }
    }

    public  boolean createDir(String destDirName) {
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
}
