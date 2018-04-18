package recsys;

//import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
//import sun.security.jca.GetInstance;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
//import java.util.regex.Pattern;

public class RecByJson {
    //String defaultret = "{\"VODFilmList\":[{\"VODFileName\":\"HD_欢乐好声音（国语版）\",\"VODFileID\":\"Z1201705021911.ts\"},{\"VODFileName\":\"圣殿骑士团的宝藏：火焰十字架\",\"VODFileID\":\"V0201612292674.mpg\"},{\"VODFileName\":\"HD_我爱灰太狼\",\"VODFileID\":\"Z9101209130064.ts\"},{\"VODFileName\":\"生化危机5\",\"VODFileID\":\"V0201508047775.mpg\"},{\"VODFileName\":\"神偷奶爸\",\"VODFileID\":\"CV6M1625410110005700.ts\"},{\"VODFileName\":\"果宝特攻之水果大逃亡\",\"VODFileID\":\"V0201603255206.mpg\"},{\"VODFileName\":\"HD_赛罗奥特曼外传：流星的誓言\",\"VODFileID\":\"Z1201608236210.ts\"},{\"VODFileName\":\"HD_芭比之蝴蝶仙子和精灵公主（国语版）\",\"VODFileID\":\"Z1201612292417.ts\"},{\"VODFileName\":\"HD_速度与激情8\",\"VODFileID\":\"Z1201707264869.ts\"},{\"VODFileName\":\"宝贝当家\",\"VODFileID\":\"V0201608267168.mpg\"}],\"VODDramaList\":[{\"VODDramaID\":\"V0201704071474.mpg\",\"VODDramaName\":\"小鱼儿与花无缺（40）\"},{\"VODDramaID\":\"Z1201607298209.ts\",\"VODDramaName\":\"HD_小猪佩奇第二季（52）星星\"},{\"VODDramaID\":\"V0201706054565.mpg\",\"VODDramaName\":\"李三枪回看版（43）\"},{\"VODDramaID\":\"V0201705063409.mpg\",\"VODDramaName\":\"名侦探柯南（559）\"},{\"VODDramaID\":\"ZJHM1709119320003360.ts\",\"VODDramaName\":\"鸡毛飞上天(54)\"},{\"VODDramaID\":\"GXVM1531815170002880.ts\",\"VODDramaName\":\"神机妙算刘伯温(40)\"},{\"VODDramaID\":\"V0201701211688.mpg\",\"VODDramaName\":\"摇摇和丢丢第一季[13]0\"},{\"VODDramaID\":\"Z1201708152554.ts\",\"VODDramaName\":\"HD_绝密543[36]\"},{\"VODDramaID\":\"NGHM1722720360002640.ts\",\"VODDramaName\":\"神勇武工队传奇(40)\"},{\"VODDramaID\":\"V0201609110771.mpg\",\"VODDramaName\":\"百变纸箱王NEW[18]0\"}],\"remark\":\"备注\",\"state\":1,\"UserType\":\"用户类型\",\"BroadcastList\":[{\"ChannelName\":\"xx卫视\",\"EndTime\":\"20171018133000\",\"ChannelNumber\":1,\"StartTime\":\"20171018130000\",\"ProgramName\":\"直播节目名字1\"},{\"ChannelName\":\"xx卫视\",\"EndTime\":\"20171018133000\",\"ChannelNumber\":4,\"StartTime\":\"20171018130000\",\"ProgramName\":\"直播节目名字2\"},{\"ChannelName\":\"xx卫视\",\"EndTime\":\"20171018133000\",\"ChannelNumber\":9,\"StartTime\":\"20171018130000\",\"ProgramName\":\"直播节目名字3\"},{\"ChannelName\":\"xx卫视\",\"EndTime\":\"20171018133000\",\"ChannelNumber\":16,\"StartTime\":\"20171018130000\",\"ProgramName\":\"直播节目名字4\"},{\"ChannelName\":\"xx卫视\",\"EndTime\":\"20171018133000\",\"ChannelNumber\":25,\"StartTime\":\"20171018130000\",\"ProgramName\":\"直播节目名字5\"}]}";
    Map<String, List<Integer>> movieRec = new HashMap<>();
    Map<String, List<Integer>> seriesRec = new HashMap<>();
    Map<String, List<Integer>> broadcastRec = new HashMap<>();
    Map<Integer, String> movieMap = new HashMap<>();
    Map<Integer, String> seriesMap = new HashMap<>();
    Map<Integer, String> broadcastMap = new HashMap<>();
    Map<String, Integer> userLike = new HashMap<>();//直播1，点播0
    String userLikeFile = "E:\\OCN\\result\\userLike.txt";
    String filePath = "E:\\OCN\\result\\";

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
            return "推荐已更新";
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
            mac = "00264C6733BD";
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
        InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath + type + "Output0801_0831.txt"), "UTF-8");
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
        InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath + type + "Map.txt"), "UTF-8");
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
        InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath + type + "Default.txt"), "UTF-8");
        BufferedReader r = new BufferedReader(isr);
        while ((line = r.readLine()) != null) {
            listDefault.add(line);
        }
        r.close();
        isr.close();
    }

    public void readResult() throws Exception {

        String modelUsers = "E:\\OCN\\broadcast\\modelUsers";
        String modelItems = "E:\\OCN\\broadcast\\modelItems";
        recSysBoardcast.loadModel(modelUsers, modelItems);
        broadcastMap = recSysBoardcast.raedItemRecListByTime("E:\\OCN\\broadcast\\RecListOfTime\\", 1);


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
        InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath + "active_users_10000.txt"), "UTF-8");
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
            jsonArrayMovies = new JSONArray("[{\"VODFileName\":\"HD_潜艇总动员5时光宝盒\",\"VODFileID\":\"4316134\"},{\"VODFileName\":\"林世荣\",\"VODFileID\":\"6209209\"},{\"VODFileName\":\"死亡占卜2\",\"VODFileID\":\"6091576\"},{\"VODFileName\":\"超级飞侠乐迪万圣节\",\"VODFileID\":\"5465036\"},{\"VODFileName\":\"HD_芭比之狗狗奇遇记（国语版）\",\"VODFileID\":\"6091533\"},{\"VODFileName\":\"丑小鸭历险记\",\"VODFileID\":\"6093552\"},{\"VODFileName\":\"神探夏洛克第四季\",\"VODFileID\":\"6095146\"},{\"VODFileName\":\"HD_芭比之蝴蝶仙子和精灵公主（国语版）\",\"VODFileID\":\"6052763\"},{\"VODFileName\":\"达芬奇密码-2\",\"VODFileID\":\"4106738\"},{\"VODFileName\":\"功夫熊猫之至尊传奇（3）新\",\"VODFileID\":\"5844841\"}]");
//            System.out.println(e.getMessage());
//            for (int i = 0; i < movieDefault.size(); i++) {
//                String[] itemInfo = movieDefault.get(i).split(",");
//                JSONObject jsonMovie = new JSONObject();
//                jsonMovie.put("VODFileID", itemInfo[0]);
//                jsonMovie.put("VODFileName", itemInfo[1]);
//                jsonArrayMovies.put(jsonMovie);
//            }
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
            jsonArray = new JSONArray("[{\"VODDramaID\":\"6198728\",\"VODDramaName\":\"HD_养母的花样年华回看版（57）\"},{\"VODDramaID\":\"5708329\",\"VODDramaName\":\"我和我的宠物[9]0\"},{\"VODDramaID\":\"6301863\",\"VODDramaName\":\"HD_锦绣未央回看版（48）\"},{\"VODDramaID\":\"5939104\",\"VODDramaName\":\"HD_擒狼回看版（36）\"},{\"VODDramaID\":\"6154575\",\"VODDramaName\":\"HD_骑士的手套回看版（46）\"},{\"VODDramaID\":\"5914741\",\"VODDramaName\":\"HD_八九不离十回看版（40）\"},{\"VODDramaID\":\"6267226\",\"VODDramaName\":\"芭比之梦想豪宅第七季[13]0\"},{\"VODDramaID\":\"4129812\",\"VODDramaName\":\"下一站婚姻(34)\"},{\"VODDramaID\":\"5703754\",\"VODDramaName\":\"于成龙(40)\"},{\"VODDramaID\":\"6250997\",\"VODDramaName\":\"我的前半生回看版（44）\"}]");
//            System.out.println(e.getMessage());
//            for (int i = 0; i < seriesDefault.size(); i++) {
//                String[] itemInfo = seriesDefault.get(i).split(",");
//                JSONObject json = new JSONObject();
//                json.put("VODDramaID", itemInfo[0]);
//                json.put("VODDramaName", itemInfo[1]);
//                jsonArray.put(json);
//            }
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
            jsonArray = new JSONArray("[{\"ChannelName\":\"安徽卫视\",\"EndTime\":\"20170831063000\",\"ChannelNumber\":707,\"StartTime\":\"20170831053000\",\"ProgramName\":\"超级新闻场\"},{\"ChannelName\":\"央视新闻\",\"EndTime\":\"20170831170000\",\"ChannelNumber\":601,\"StartTime\":\"20170831160000\",\"ProgramName\":\"新闻直播间\"},{\"ChannelName\":\"新闻综合\",\"EndTime\":\"20170831090100\",\"ChannelNumber\":1704,\"StartTime\":\"20170831080000\",\"ProgramName\":\"上海早晨_2\"},{\"ChannelName\":\"体育频道\",\"EndTime\":\"20170831193000\",\"ChannelNumber\":205,\"StartTime\":\"20170831190000\",\"ProgramName\":\"体育新闻\"},{\"ChannelName\":\"动漫秀场\",\"EndTime\":\"20170831164100\",\"ChannelNumber\":1802,\"StartTime\":\"20170831163900\",\"ProgramName\":\"兔小贝中文儿歌:劳动最光荣\"},{\"ChannelName\":\"新闻综合\",\"EndTime\":\"20170831203800\",\"ChannelNumber\":1704,\"StartTime\":\"20170831194500\",\"ProgramName\":\"战地枪王(31)\"},{\"ChannelName\":\"重庆卫视\",\"EndTime\":\"20170831014700\",\"ChannelNumber\":904,\"StartTime\":\"20170831010600\",\"ProgramName\":\"我要当八路(7)\"},{\"ChannelName\":\"书画\",\"EndTime\":\"20170831093000\",\"ChannelNumber\":4304,\"StartTime\":\"20170831090000\",\"ProgramName\":\"美术新闻\"},{\"ChannelName\":\"央体赛事\",\"EndTime\":\"20170831173000\",\"ChannelNumber\":1301,\"StartTime\":\"20170831163000\",\"ProgramName\":\"第十三届全国运动会\"},{\"ChannelName\":\"金鹰卡通\",\"EndTime\":\"20170831090000\",\"ChannelNumber\":1008,\"StartTime\":\"20170831080000\",\"ProgramName\":\"动画连连看_1\"}]");
//            for (int i = 0; i < broadcastDefault.size(); i++) {
//                String[] itemInfo = broadcastDefault.get(i).split(",");
//                JSONObject json = new JSONObject();
//                json.put("ChannelNumber", Integer.parseInt(itemInfo[0]) );
//                json.put("ChannelName", itemInfo[1]);
//                String ProgramName = itemInfo[2];
//                for(int j=3;j<=itemInfo.length-3;j++) ProgramName+=(","+itemInfo[j]);
//                json.put("StartTime",itemInfo[itemInfo.length-2]);
//                json.put("EndTime",itemInfo[itemInfo.length-1]);
//                jsonArray.put(json);
//            }
        }
        return jsonArray;
    }
}
