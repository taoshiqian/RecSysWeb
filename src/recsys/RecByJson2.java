package recsys;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class RecByJson2 {
    //String defaultret = "{\"VODFilmList\":[{\"VODFileName\":\"HD_欢乐好声音（国语版）\",\"VODFileID\":\"Z1201705021911.ts\"},{\"VODFileName\":\"圣殿骑士团的宝藏：火焰十字架\",\"VODFileID\":\"V0201612292674.mpg\"},{\"VODFileName\":\"HD_我爱灰太狼\",\"VODFileID\":\"Z9101209130064.ts\"},{\"VODFileName\":\"生化危机5\",\"VODFileID\":\"V0201508047775.mpg\"},{\"VODFileName\":\"神偷奶爸\",\"VODFileID\":\"CV6M1625410110005700.ts\"},{\"VODFileName\":\"果宝特攻之水果大逃亡\",\"VODFileID\":\"V0201603255206.mpg\"},{\"VODFileName\":\"HD_赛罗奥特曼外传：流星的誓言\",\"VODFileID\":\"Z1201608236210.ts\"},{\"VODFileName\":\"HD_芭比之蝴蝶仙子和精灵公主（国语版）\",\"VODFileID\":\"Z1201612292417.ts\"},{\"VODFileName\":\"HD_速度与激情8\",\"VODFileID\":\"Z1201707264869.ts\"},{\"VODFileName\":\"宝贝当家\",\"VODFileID\":\"V0201608267168.mpg\"}],\"VODDramaList\":[{\"VODDramaID\":\"V0201704071474.mpg\",\"VODDramaName\":\"小鱼儿与花无缺（40）\"},{\"VODDramaID\":\"Z1201607298209.ts\",\"VODDramaName\":\"HD_小猪佩奇第二季（52）星星\"},{\"VODDramaID\":\"V0201706054565.mpg\",\"VODDramaName\":\"李三枪回看版（43）\"},{\"VODDramaID\":\"V0201705063409.mpg\",\"VODDramaName\":\"名侦探柯南（559）\"},{\"VODDramaID\":\"ZJHM1709119320003360.ts\",\"VODDramaName\":\"鸡毛飞上天(54)\"},{\"VODDramaID\":\"GXVM1531815170002880.ts\",\"VODDramaName\":\"神机妙算刘伯温(40)\"},{\"VODDramaID\":\"V0201701211688.mpg\",\"VODDramaName\":\"摇摇和丢丢第一季[13]0\"},{\"VODDramaID\":\"Z1201708152554.ts\",\"VODDramaName\":\"HD_绝密543[36]\"},{\"VODDramaID\":\"NGHM1722720360002640.ts\",\"VODDramaName\":\"神勇武工队传奇(40)\"},{\"VODDramaID\":\"V0201609110771.mpg\",\"VODDramaName\":\"百变纸箱王NEW[18]0\"}],\"remark\":\"备注\",\"state\":1,\"UserType\":\"用户类型\",\"BroadcastList\":[{\"ChannelName\":\"xx卫视\",\"EndTime\":\"20171018133000\",\"ChannelNumber\":1,\"StartTime\":\"20171018130000\",\"ProgramName\":\"直播节目名字1\"},{\"ChannelName\":\"xx卫视\",\"EndTime\":\"20171018133000\",\"ChannelNumber\":4,\"StartTime\":\"20171018130000\",\"ProgramName\":\"直播节目名字2\"},{\"ChannelName\":\"xx卫视\",\"EndTime\":\"20171018133000\",\"ChannelNumber\":9,\"StartTime\":\"20171018130000\",\"ProgramName\":\"直播节目名字3\"},{\"ChannelName\":\"xx卫视\",\"EndTime\":\"20171018133000\",\"ChannelNumber\":16,\"StartTime\":\"20171018130000\",\"ProgramName\":\"直播节目名字4\"},{\"ChannelName\":\"xx卫视\",\"EndTime\":\"20171018133000\",\"ChannelNumber\":25,\"StartTime\":\"20171018130000\",\"ProgramName\":\"直播节目名字5\"}]}";

    String defaultMac = "98BC570E7C6F";

    Map<String, List<Integer>> movieRec = new HashMap<>();
    Map<String, List<Integer>> seriesRec = new HashMap<>();
    Map<String, List<Integer>> broadcastRec = new HashMap<>();
    Map<Integer, String> movieMap = new HashMap<>();
    Map<Integer, String> seriesMap = new HashMap<>();
    Map<Integer, String> broadcastMap = new HashMap<>();
    Map<String, Integer> userLike = new HashMap<>();//直播1，点播0
    String userLikeFile = "E:\\OCN\\NGBLab\\conf\\userLike.txt";
    String dataPath = "E:\\OCN\\NGBLab\\date\\";
    String confPath = "E:\\OCN\\NGBLab\\conf\\";

    String modelUsers = "E:\\OCN\\broadcast\\modelUsers";
    String modelItems = "E:\\OCN\\broadcast\\modelItems";

    static String movieOutput = "E:\\OCN\\NGBLab\\date\\movieOutput.txt";
    static String seriesOutput = "E:\\OCN\\NGBLab\\date\\seriesOutput.txt";
    static String movieMapFile = "E:\\OCN\\NGBLab\\date\\movieRecList.txt";
    static String seriesMapFile = "E:\\OCN\\NGBLab\\date\\seriesRecList.txt";

    RecSysOCN recSysBoardcast = new RecSysOfSeries();

    List<String> movieDefault = new ArrayList<>();
    List<String> seriesDefault = new ArrayList<>();
    List<String> broadcastDefault = new ArrayList<>();
    Set<String> activeUser = new HashSet<>();

    public String isUnL() throws Exception {
        JSONObject reponseJson = new JSONObject();
        System.out.println("JSON格式不正确");
        reponseJson.put("state", -1);
        return reponseJson.toString();
    }

    public String rec(String requestJsonString) throws Exception {
        if (requestJsonString.equals("update")) {
            readResult();
            return "推荐接口已更新";
        }
        if (activeUser.isEmpty()) readResult();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式 2017-11-09 17:35:20
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        System.out.println("请求达到了recsys");
        System.out.println("请求的JSON：" + requestJsonString);
        //解析东方有线的请求JSON
        JSONObject requestJson = null;
        try {
            requestJson = new JSONObject(requestJsonString);//System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("JSON格式不正确");
            return isUnL();
        }
        System.out.println("解析完成");
        //推荐算法开始调用
        //0：训练？？
        //1:全部（含2、3、4所有内容） 2:直播节目 3:点播节目 4:用户分类信息
        int type = 0;
        String mac = "";
        try {
            type = requestJson.getInt("responseType");
            mac = requestJson.getString("mac");
            //requestJson = new JSONObject(requestJsonString);//System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("JSON格式不正确");
            return isUnL();
        }

        JSONObject reponseJson = new JSONObject();
        if (!activeUser.contains(mac)) {
            mac = "98BC570E7C6F";
            reponseJson.put("state", 2);
        } else reponseJson.put("state", 1);
        switch (type) {
            //case 0 : break;
            case 1:
                reponseJson = rec1(mac);
                break;
            case 2:
                reponseJson = rec2(mac);
                break;
            case 3:
                reponseJson = rec3(mac);
                break;
            case 4:
                reponseJson = rec4(mac);
                break;
            default:
                reponseJson.put("state", -1);
                break;
        }

        //推荐算法结束调用
        System.out.println("推荐完成，准备构造字符串");
        //reponseJson.put("name","随机推荐");
        String reponseString = reponseJson.toString();

        System.out.println("推荐json字符串构造完成：" + reponseString);
        return reponseString;
    }

    public void readRecFile(Map<String, List<Integer>> map, String type) throws Exception {
        String line = "";
        InputStreamReader isr = new InputStreamReader(new FileInputStream(dataPath + type + "Output.txt"), "UTF-8");
        BufferedReader r = new BufferedReader(isr);
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            String userName = words[0];
            List<Integer> recList = new ArrayList<>();
            for (int i = 1; i < words.length; i++) recList.add(Integer.parseInt(words[i]));
            map.put(userName, recList);
        }
        r.close();
        isr.close();
    }

    public void readMapFile(Map<Integer, String> map, String type) throws Exception {
        String line = "";
        InputStreamReader isr = new InputStreamReader(new FileInputStream(dataPath + type + "RecList.txt"), "UTF-8");
        BufferedReader r = new BufferedReader(isr);
        while ((line = r.readLine()) != null) {
            String[] words = line.split("\\|");
            int itemId = Integer.parseInt(words[0]);
            String name = words[1];
            map.put(itemId, name);
        }
        r.close();
        isr.close();
    }

    public void readUserLike(String userLikeFile) throws Exception { //userLikeFile
        String line = "";
        InputStreamReader isr = new InputStreamReader(new FileInputStream(userLikeFile), "UTF-8");
        BufferedReader r = new BufferedReader(isr);
        while ((line = r.readLine()) != null) {
            String[] words = line.split(" ");
            if (words.length >= 2) {
                userLike.put(words[0], Integer.parseInt(words[1]));
            }
        }
        r.close();
        isr.close();
    }

    public void readDefault(List<String> listDefault, String type) throws Exception {
        String line = "";
        InputStreamReader isr = new InputStreamReader(new FileInputStream(dataPath + type + "Default.txt"), "UTF-8");
        BufferedReader r = new BufferedReader(isr);
        while ((line = r.readLine()) != null) {
            listDefault.add(line);
        }
        r.close();
        isr.close();
    }

    public void readResult() throws Exception {

        recSysBoardcast.loadModel(modelUsers, modelItems);
        System.out.println("读文件");
        broadcastMap = recSysBoardcast.raedItemRecListByTime("E:\\OCN\\NGBLab\\date\\broadcastRecList\\", 1);

        readUserLike(userLikeFile);
        readRecFile(movieRec, "movie");
        readRecFile(seriesRec, "series");
        //readRecFile(broadcastRec,"broadcast");
        readMapFile(movieMap, "movie");
        readMapFile(seriesMap, "series");
        //readMapFile(broadcastMap,"broadcast");
//        readDefault(movieDefault,"movie");
//        readDefault(seriesDefault,"series");
//        readDefault(broadcastDefault,"broadcast");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(confPath + "active_users_10000.txt"), "UTF-8");
        BufferedReader r = new BufferedReader(isr);
        String line = "";
        while ((line = r.readLine()) != null) {
            activeUser.add(line.split(" ")[0]);
        }
        r.close();
        isr.close();
    }

    public JSONObject rec1(String mac) throws Exception {
        JSONObject json = new JSONObject();
        json.put("remark", "备注");
        json.put("userLike", userLike.get(mac));
        json.put("VODFilmList", recMovie(mac));
        json.put("VODDramaList", recSeries(mac));
        json.put("BroadcastList", recProgram(mac));
        json.put("UserType", "用户类型");
        return json;
    }

    public JSONObject rec2(String mac) throws Exception {
        JSONObject json = new JSONObject();
        json.put("state", 1);
        json.put("remark", "备注");
        json.put("userLike", userLike.get(mac));
        //json.put("VODFilmList",recMovie(mac));
        //json.put("VODDramaList",recSeries(mac));
        json.put("BroadcastList", recProgram(mac));
        //json.put("UserType","用户类型");
        return json;
    }

    public JSONObject rec3(String mac) throws Exception {
        JSONObject json = new JSONObject();
        json.put("state", 1);
        json.put("remark", "备注");
        json.put("userLike", userLike.get(mac));
        json.put("VODFilmList", recMovie(mac));
        json.put("VODDramaList", recSeries(mac));
        //json.put("BroadcastList",recProgram(mac));
        //json.put("UserType","用户类型");
        return json;
    }

    public JSONObject rec4(String mac) throws Exception {
        JSONObject json = new JSONObject();
        json.put("state", 1);
        json.put("remark", "备注");
        json.put("userLike", userLike.get(mac));
        //json.put("VODFilmList",recMovie(mac));
        //json.put("VODDramaList",recSeries(mac));
        //json.put("BroadcastList",recProgram(mac));
        json.put("UserType", "用户类型");
        return json;
    }

    public JSONArray recMovie(String mac) throws Exception {
        JSONArray jsonArrayMovies = new JSONArray();
        try {
            List<Integer> recList = movieRec.get(mac);
            for (int itemId : recList) {
                String[] itemInfo = movieMap.get(itemId).split(",");
                JSONObject jsonMovie = new JSONObject();
                jsonMovie.put("VODFileID", itemInfo[0]);
                jsonMovie.put("VODFileName", itemInfo[1]);
                jsonArrayMovies.put(jsonMovie);
            }
        } catch (Exception e) {
            List<Integer> recList = movieRec.get(defaultMac);
            for (int itemId : recList) {
                String[] itemInfo = movieMap.get(itemId).split(",");
                JSONObject jsonMovie = new JSONObject();
                jsonMovie.put("VODFileID", itemInfo[0]);
                jsonMovie.put("VODFileName", itemInfo[1]);
                jsonArrayMovies.put(jsonMovie);
            }
        }
        return jsonArrayMovies;
    }

    public JSONArray recSeries(String mac) throws Exception {
        JSONArray jsonArray = new JSONArray();
        try {
            List<Integer> recList = seriesRec.get(mac);
            for (int itemId : recList) {
                String[] itemInfo = seriesMap.get(itemId).split(",");
                JSONObject json = new JSONObject();
                json.put("VODDramaID", itemInfo[0]);
                json.put("VODDramaName", itemInfo[1]);
                jsonArray.put(json);
            }
        } catch (Exception e) {
            List<Integer> recList = seriesRec.get(defaultMac);
            for (int itemId : recList) {
                String[] itemInfo = seriesMap.get(itemId).split(",");
                JSONObject json = new JSONObject();
                json.put("VODDramaID", itemInfo[0]);
                json.put("VODDramaName", itemInfo[1]);
                jsonArray.put(json);
            }
        }
        return jsonArray;
    }

    public JSONArray recProgram(String mac) throws Exception {
        JSONArray jsonArray = new JSONArray();
        try {
            //List<Integer> recList = broadcastRec.get(mac);
            List<Integer> recList = recSysBoardcast.recForOneUserByMac(mac, 10);
            for (int itemId : recList) {
                String[] itemInfo = broadcastMap.get(itemId).split(",");
                JSONObject json = new JSONObject();
                json.put("ChannelNumber", Integer.parseInt(itemInfo[0]));
                json.put("ChannelName", itemInfo[1]);
                String ProgramName = itemInfo[2];
                for (int i = 3; i <= itemInfo.length - 3; i++) ProgramName += ("," + itemInfo[i]);
                json.put("ProgramName", ProgramName);
                json.put("StartTime", itemInfo[itemInfo.length - 2]);
                json.put("EndTime", itemInfo[itemInfo.length - 1]);
                jsonArray.put(json);
            }
        } catch (Exception e) {
            List<Integer> recList = recSysBoardcast.recForOneUserByMac(defaultMac, 10);
            for (int itemId : recList) {
                String[] itemInfo = broadcastMap.get(itemId).split(",");
                JSONObject json = new JSONObject();
                json.put("ChannelNumber", Integer.parseInt(itemInfo[0]));
                json.put("ChannelName", itemInfo[1]);
                String ProgramName = itemInfo[2];
                for (int i = 3; i <= itemInfo.length - 3; i++) ProgramName += ("," + itemInfo[i]);
                json.put("ProgramName", ProgramName);
                json.put("StartTime", itemInfo[itemInfo.length - 2]);
                json.put("EndTime", itemInfo[itemInfo.length - 1]);
                jsonArray.put(json);
            }
        }
        return jsonArray;
    }
}
