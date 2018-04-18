package tool;


//import com.sun.javafx.collections.MappingChange;
//import com.sun.javaws.security.AppContextUtil;
//import sun.awt.datatransfer.DataTransferer;

import java.io.*;
import java.util.*;

public class tianchi {
    //MapID mapShop = new MapID();
    //MapID mapMall = new MapID();
    //MapID mapWifi = new MapID();

    Map<String,Integer> mapShop = new HashMap<>();
    Map<String,Integer> mapWifi = new HashMap<>();


    List<String> listShop = new ArrayList<>();
    List<String> listMall = new ArrayList<>();
    List<String> listWifi = new ArrayList<>();

    String mapShopTxt = "E:\\tianchi\\mapShop.txt";
    String mapMallTxt = "E:\\tianchi\\mapMall.txt";
    String mapWifiTxt = "E:\\tianchi\\mapWifi.txt";

    String shopInfo = "E:\\tianchi\\训练数据-ccf_first_round_shop_info.csv";
    String behaviorInfo  = "E:\\tianchi\\训练数据-ccf_first_round_user_shop_behavior.csv";

    public void mall_shop_wifi() throws Exception{
        String shop_mall="E:\\tianchi\\shop_mall.txt";
        String shop_wifi="E:\\tianchi\\shop_wifi.txt";
        String all="E:\\tianchi\\all.txt";

        Map<String,String> map = new HashMap<>();

        BufferedReader r1 = new BufferedReader(new FileReader(shop_mall));
        String line = null;
        while( (line=r1.readLine())!=null ){
            String words[] = line.split("\t");
            //System.out.println(words[0]+"----"+words[1]);
            map.put(words[0],words[1]);
        }

        BufferedWriter w = new BufferedWriter(new FileWriter(all));
        BufferedReader r2 = new BufferedReader(new FileReader(shop_wifi));
        while( (line=r2.readLine())!=null ){
            String words[] = line.split("\t");
            String mallName = map.get(words[0]);
            String wifis[] = words[1].split(";");
            for(int i=0;i<wifis.length;i++){
                String abc[] = wifis[i].split("\\|");
                w.write(mallName+" "+words[0]);
                for(int j=0;j<abc.length;j++){
                    w.write(" "+abc[j]);
                }
                w.write("\r\n");
            }
        }
        w.close();
    }
    public void sort() throws Exception{
        List<String> list = new ArrayList<>();
        String all = "E:\\tianchi\\mall_shop_behaveior.txt";
        BufferedReader r1 = new BufferedReader(new FileReader(all));
        String line = null;
        while( (line=r1.readLine())!=null ){
            list.add(line);
        }
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        BufferedWriter w = new BufferedWriter(new FileWriter("E:\\tianchi\\mall_shop_behaveior_sort.txt"));
        for(String s:list){
            w.write(s+"\r\n");
        }
        w.close();
    }

    public void mall_shop_behavior() throws Exception{
        String shop_mall="E:\\tianchi\\shop_mall.txt";
        String shop_wifi="E:\\tianchi\\b.csv";
        String all="E:\\tianchi\\mall_shop_behaveior.txt";

        Map<String,String> map = new HashMap<>();

        BufferedReader r1 = new BufferedReader(new FileReader(shop_mall));
        String line = null;
        while( (line=r1.readLine())!=null ){
            String words[] = line.split("\t");
            //System.out.println(words[0]+"----"+words[1]);
            map.put(words[0],words[1]);
        }

        BufferedWriter w = new BufferedWriter(new FileWriter(all));
        BufferedReader r2 = new BufferedReader(new FileReader(shop_wifi));
        while( (line=r2.readLine())!=null ){
            String words[] = line.split(",");
            String mallName = map.get(words[1]);
            w.write(mallName);
            for(String s :words){
                w.write(" "+s);
            }
            w.write("\r\n");
        }
        w.close();
    }


    public void mapForAll() throws Exception{
        BufferedReader r1 = new BufferedReader(new FileReader(shopInfo));
        String line = r1.readLine();
        while( (line=r1.readLine())!=null ){
            String[] words = line.split(",");
            if(!listShop.contains(words[0]))listShop.add(words[0]);
            if(!listMall.contains(words[5]))listMall.add(words[5]);
        }
        listShop.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        BufferedWriter wshop = new BufferedWriter(new FileWriter(mapShopTxt));
        for(int i=0;i<listShop.size();i++){
            wshop.write(i+","+listShop.get(i)+"\r\n");
        }
        System.out.println("shop完成");
        wshop.close();
        listMall.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        BufferedWriter wmall = new BufferedWriter(new FileWriter(mapMallTxt));
        for(int i=0;i<listMall.size();i++){
            wmall.write(i+","+listMall.get(i)+"\r\n");
        }
        System.out.println("mall完成");
        wmall.close();
/*
        BufferedReader r2 = new BufferedReader(new FileReader(behaviorInfo));
        line = r2.readLine();
        while( (line=r2.readLine())!=null ){
            String[] words = line.split(",");
            String[] wifis = words[5].split(";");
            for(String wifi : wifis){
                String[] name = wifi.split("\\|");
                if(!listWifi.contains(name[0])) listWifi.add(name[0]);
            }
        }
        System.out.println("wifi读取完成");
        listWifi.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println("wifi排序完成");
        BufferedWriter wwifi = new BufferedWriter(new FileWriter(mapWifiTxt));
        for(int i=0;i<listWifi.size();i++){
            wwifi.write(i+","+listWifi.get(i)+"\r\n");
        }
        System.out.println("wifi完成");
        */
    }

    public void pretreatment2() throws Exception{
        BufferedReader r1 = new BufferedReader(new FileReader(mapShopTxt));
        String line="";
        while((line=r1.readLine())!=null){
            String[] words = line.split(",");
            mapShop.put(words[1],Integer.parseInt(words[0]));
        }
        BufferedReader r2 = new BufferedReader(new FileReader(mapMallTxt));
        while((line=r2.readLine())!=null){
            String[] words = line.split(",");
            //mapMall.put(words[1],Integer.parseInt(words[0]));
        }
        BufferedReader r3 = new BufferedReader(new FileReader(mapWifiTxt));
        while ((line=r3.readLine())!=null){
            String[] words = line.split(",");
            mapWifi.put(words[1],Integer.parseInt(words[0]));
        }



        int shopL = 0;
        int mallL = 0;
        int wifiL = 0;
        BufferedReader r = new BufferedReader(new FileReader(behaviorInfo));
        line = r.readLine();
        int count = 0;
        while( (line=r.readLine())!=null ){
            count++;
            String[] ziduan = line.split(",");
            String shopName = ziduan[1];
            int shopId = mapShop.get(shopName);
            String[] wifis = ziduan[5].split(";");

            for(String wifi : wifis){
                String[] words = wifi.split("\\|");

            }
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

    public void xie (Map<String,Integer> map,List<String> list,BufferedWriter w,String mallName){
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        for(int i=0;i<list.size();i++){
           // w.write(list.get(i)+","+i+"\r\n");
        }
    }

    public void pretreatment3() throws Exception {
        String mallPath="E:\\tianchi\\mall\\";
        String file = "E:\\tianchi\\训练数据-ccf_first_round_user_shop_behavior.csv";
        BufferedReader r = new BufferedReader(new FileReader(file));
        String line = "";
        String oldMall = "m_1085";
        String nowPath = mallPath+oldMall;
        BufferedWriter wshop = new BufferedWriter(new FileWriter(nowPath+"\\_mapShop.txt"));
        BufferedWriter wwifi = new BufferedWriter(new FileWriter(nowPath+"\\_mapWifi.txt"));
        while((line=r.readLine())!=null){
            String[] words = line.split(" ");
            String mallName = words[0];
            //createDir(mallPath+mallName);
            if(!oldMall.equals(mallName)){//新商场
                //createDir(mallPath+mallName);
                listShop.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
                for(int i=0;i<listShop.size();i++){
                    wshop.write(listShop.get(i)+","+i+"\r\n");
                }
                wshop.close();
                listWifi.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
                for(int i=0;i<listWifi.size();i++){
                    wwifi.write(listWifi.get(i)+","+i+"\r\n");
                }
                wwifi.close();
                nowPath = mallPath+mallName;
                wshop = new BufferedWriter(new FileWriter(nowPath+"\\_mapShop.txt"));
                wwifi = new BufferedWriter(new FileWriter(nowPath+"\\_mapWifi.txt"));
                listShop.clear();
                listWifi.clear();
            }
            oldMall=mallName;
            String shopName = words[1];
            String WifiName = words[2];
            if(!listShop.contains(shopName)) listShop.add(shopName);
            if(!listWifi.contains(WifiName)) listWifi.add(WifiName);
        }
    }

    public void mall_shop_sort() throws Exception{
        String output = "E:\\tianchi\\mall_shop_sort.txt";
        BufferedWriter w = new BufferedWriter(new FileWriter(output));
        List<String> list = new ArrayList<>();

        String input = "E:\\tianchi\\训练数据-ccf_first_round_shop_info.csv";
        BufferedReader r = new BufferedReader(new FileReader(input));
        String line = r.readLine();
        while((line=r.readLine())!=null){
            String[] words = line.split(",");
            String mallName = words[5];
            String shopName = words[0];
            String s= mallName+","+shopName;
           // w.write(s+"\r\n");
            list.add(s);
        }
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        for(String s:list){
            w.write(s+"\r\n");
        }

        w.close();
        r.close();
    }

    public void pretreatment4shop() throws Exception {
        String mallPath="E:\\tianchi\\mall\\";
        String file = "E:\\tianchi\\mall_shop_sort.txt";
        BufferedReader r = new BufferedReader(new FileReader(file));
        String line = r.readLine();
        String oldMall = "m_1021";
        String nowPath = mallPath+oldMall;
        BufferedWriter wshop = new BufferedWriter(new FileWriter(nowPath+"\\_mapShop.txt"));
        while((line=r.readLine())!=null){
            String[] words = line.split(",");
            String mallName = words[0];
            //createDir(mallPath+mallName);
            if(!oldMall.equals(mallName)){//新商场
                //createDir(mallPath+mallName);
                listShop.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
                for(int i=0;i<listShop.size();i++){
                    wshop.write(listShop.get(i)+","+i+"\r\n");
                }
                wshop.close();
                nowPath = mallPath+mallName;
                wshop = new BufferedWriter(new FileWriter(nowPath+"\\_mapShop.txt"));
                listShop.clear();
            }
            oldMall=mallName;
            String shopName = words[1];
            if(!listShop.contains(shopName)) listShop.add(shopName);
        }
    }


    public void mall_wifi_sort() throws Exception{
        BufferedReader r1 = new BufferedReader(new FileReader("E:\\tianchi\\shop_mall.txt"));
        Map<String,String> shop_mall = new HashMap<>();
        String line="";
        while((line=r1.readLine())!=null){
            String[] words = line.split("\t");
            String shopName = words[0];
            String mallName = words[1];
            shop_mall.put(shopName,mallName);
        }r1.close();


        String output = "E:\\tianchi\\mall_wifi_sort.txt";
        BufferedWriter w = new BufferedWriter(new FileWriter(output));
        List<String> list = new ArrayList<>();

        String input = "E:\\tianchi\\训练数据-ccf_first_round_user_shop_behavior.csv";
        BufferedReader r = new BufferedReader(new FileReader(input));
        line = r.readLine();
        while((line=r.readLine())!=null){
            String[] words = line.split(",");
            String mallName = words[5];
            String shopName = words[0];
            String s= mallName+","+shopName;
            // w.write(s+"\r\n");
            list.add(s);
        }
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        for(String s:list){
            w.write(s+"\r\n");
        }

        w.close();
        r.close();

    }

    public void forMapMall() throws Exception{
        String input = "E:\\tianchi\\mall_shop_sort.txt";
        BufferedReader r = new BufferedReader(new FileReader(input));
        String line="";
        String oldmall="";
        BufferedWriter w = new BufferedWriter(new FileWriter("E:\\tianchi\\mapMall.txt"));
        while((line=r.readLine())!=null){
            String[] words = line.split(",");
            String mall = words[0];
            if(!mall.equals(oldmall)) w.write(mall+"\r\n");
            oldmall=mall;
        }
        r.close();w.close();
    }

    public void pretreatment5wifi() throws Exception {
        BufferedReader r1 = new BufferedReader(new FileReader("E:\\tianchi\\shop_mall.txt"));
        Map<String,String> shop_mall = new HashMap<>();
        String line="";
        while((line=r1.readLine())!=null){
            String[] words = line.split("\t");
            String shopName = words[0];
            String mallName = words[1];
            shop_mall.put(shopName,mallName);
        }r1.close();


        Set<String> set = new HashSet<>();
        List<String> list = new ArrayList<>();
        BufferedWriter w = new BufferedWriter(new FileWriter("E:\\tianchi\\all2sort.txt"));
        String mallPath="E:\\tianchi\\mall\\";
        String file = "E:\\tianchi\\训练数据-ccf_first_round_user_shop_behavior.csv";
        BufferedReader r = new BufferedReader(new FileReader(file));
        line = r.readLine();
        int c=0;
        while((line=r.readLine())!=null){
            String[] ziduan = line.split(",");
            String mallname = shop_mall.get(ziduan[1]);
            String[] wifis = ziduan[5].split(";");
            for(String wifi : wifis){
                String[] name = wifi.split("\\|");
                String wifiname = name[0];
                String s= mallname+","+wifiname;
                //if(!list.contains(s))list.add(s);
                set.add(s);
                //System.out.println(c++);
            }
        }
        for(String ss : set){
            list.add(ss);
        }
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        for(String s:list){
            w.write(s+"\r\n");
        }w.close();
        r.close();r1.close();
    }

    public void pretreatment6wifi() throws Exception{
        String mallpath = "E:\\tianchi\\mall\\";
        BufferedReader r = new BufferedReader(new FileReader("E:\\tianchi\\all2sort.txt"));
        String line = "";
        String oldmall="m_1085";
        BufferedWriter w= new BufferedWriter(new FileWriter(mallpath+oldmall+"\\_mapWifi.txt"));
        while( (line=r.readLine())!=null ){
            String[] words = line.split(",");
            String mall = words[0];
            if(!mall.equals(oldmall)){
                listWifi.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
                for(int i=0;i<listWifi.size();i++){
                    w.write(listWifi.get(i)+","+i+"\r\n");
                }
                System.out.println(mall+"完成");
                listWifi.clear();
                oldmall=mall;
                w.close();
                w=new BufferedWriter(new FileWriter(mallpath+mall+"\\_mapWifi.txt"));
            }
            String wifi = words[1];
            listWifi.add(wifi);
        }
    }

    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    public void mallCopy()throws Exception{
        BufferedReader r0 = new BufferedReader(new FileReader(mapMallTxt));
        List<String> listMallName = new ArrayList<>();
        String line="";
        while((line=r0.readLine())!=null){
            String mallName = line;
            listMallName.add(mallName);
        }r0.close();
        for(String mallName :listMallName){
            //copyFile("E:\\tianchi\\mall\\"+mallName+"\\_mapShop.txt","E:\\tianchi\\mall2\\"+mallName+"\\_mapShop.txt");
            copyFile("E:\\tianchi\\mall\\"+mallName+"\\_mapWifi.txt","E:\\tianchi\\mall2\\"+mallName+"\\_mapWifi.txt");
        }
    }
    public void pretreatment() throws Exception {

        Map<String,BufferedWriter> mallCWriter = new HashMap<>();
        Map<String,BufferedWriter> mallIWriter = new HashMap<>();
        Map<String,BufferedWriter> mallSWriter = new HashMap<>();

        BufferedReader r0 = new BufferedReader(new FileReader(mapMallTxt));
        List<String> listMallName = new ArrayList<>();
        String line="";
        while((line=r0.readLine())!=null){
            //String[] words = line.split(",");
            String mallName = line;
            listMallName.add(mallName);
            createDir("E:\\tianchi\\mall2\\"+mallName);
            mallCWriter.put(mallName,new BufferedWriter(new FileWriter("E:\\tianchi\\mall2\\"+mallName+"\\connection.txt")));
            mallIWriter.put(mallName,new BufferedWriter(new FileWriter("E:\\tianchi\\mall2\\"+mallName+"\\intensity.txt")));
            mallSWriter.put(mallName,new BufferedWriter(new FileWriter("E:\\tianchi\\mall2\\"+mallName+"\\shopvec.txt")));
        }r0.close();

        BufferedReader r1 = new BufferedReader(new FileReader("E:\\tianchi\\shop_mall.txt"));
        Map<String,String> shop_mall = new HashMap<>();
        line="";
        while((line=r1.readLine())!=null){
            String[] words = line.split("\t");
            String shopName = words[0];
            String mallName = words[1];
            shop_mall.put(shopName,mallName);
        }r1.close();




        Map<String,Map> mapMallShop = new HashMap<>();
        Map<String,Map> mapMallWifi = new HashMap<>();
        String mallPath = "E:\\tianchi\\mall2\\";
        for(String mallName:listMallName){
            String _mapShop = mallPath+mallName+"\\_mapShop.txt";//E:\tianchi\mall2\\_mapShop.txt
            Map<String,Integer> maps = new HashMap<>();
            BufferedReader rshop = new BufferedReader(new FileReader(_mapShop));
            while ( (line=rshop.readLine())!=null ){
                String[] words = line.split(",");
                maps.put(words[0],Integer.parseInt(words[1]));
            } rshop.close();
            mapMallShop.put(mallName,maps);

            String _mapWifi = mallPath+mallName+"\\_mapWifi.txt";
            Map<String,Integer> mapw = new HashMap<>();
            BufferedReader rwifi = new BufferedReader(new FileReader(_mapWifi));
            while( (line=rwifi.readLine())!=null ){
                String[] words = line.split(",");
                mapw.put(words[0],Integer.parseInt(words[1]));
            } rwifi.close();
            mapMallWifi.put(mallName,mapw);
        }
        System.out.println(mapMallShop.get("m_909").get("s_1332")+"成功");
        System.out.println(mapMallWifi.get("m_615").get("b_925165")+"成功");


        BufferedReader r = new BufferedReader(new FileReader(behaviorInfo));
        line = r.readLine();
        int count = 0,errornum=0;
        while( (line=r.readLine())!=null ){
            count++;
            System.out.println("总完成数："+count);
            String[] ziduan = line.split(",");
            String shopName = ziduan[1];
            String mallName = shop_mall.get(shopName);
            int shopLen = mapMallShop.get(mallName).size();
            if(shopLen==0){System.out.println(errornum++);   continue;}
            int wifiLen = mapMallWifi.get(mallName).size();
            if(wifiLen==0){System.out.println(errornum++);   continue;}
            int shopId = 1;
            boolean errorshop = false;
            try {
                shopId = (int)mapMallShop.get(mallName).get(shopName);
            }catch (Exception e){
                System.out.println(errornum++);
                continue;
            }
            //System.out.println(mallName+" "+shopName+" "+shopId);
            //if(count>=1) break;
            String[] wifis = ziduan[5].split(";");
            int[] connection = new int[wifiLen];
            int[] intensity = new int[wifiLen];//intensity[wifiId] = Integer.parseInt(wifiInten);
            int[] shopvec = new int[shopLen];
            shopvec[shopId]=1;
            boolean errorwifi = false;
            for(String wifi : wifis){
                String[] words = wifi.split("\\|");
                String wifiName = words[0];
                String wifiInten = words[1];
                String wifiCon = words[2];
                int wifiId = 1;
                try {
                    wifiId = (int) mapMallWifi.get(mallName).get(wifiName);
                }catch (Exception e){
                    //System.out.println(e.getMessage());
                    //System.out.println(errornum);
                    errorwifi=true;
                    continue;
                }
                connection[wifiId] = wifiCon=="true"?1:0;
                intensity[wifiId] = Integer.parseInt(wifiInten);
            }
            if(errorwifi==true) {System.out.println(errornum++);continue;}
            errorwifi = false;
            //String cfile = mallPath+mallName+"\\connection.txt";//String cfile = mallPath+mallName+"\\connection_"+count+".txt";
            //BufferedWriter wc = new BufferedWriter(new FileWriter(cfile));
            BufferedWriter wc = mallCWriter.get(mallName);
            wc.write(count+"");
            for(int i=0;i<wifiLen;i++){
                wc.write(","+connection[i]);
            } wc.write("\r\n");
            //wc.close();

            //String ifile = mallPath+mallName+"\\intensity.txt";//String ifile = mallPath+mallName+"\\intensity_"+count+".txt";
            //BufferedWriter wi = new BufferedWriter(new FileWriter(ifile));
            BufferedWriter wi = mallIWriter.get(mallName);
            wi.write(count+"");
            for(int i=0;i<wifiLen;i++){
                wi.write(","+intensity[i]);
            } wi.write("\r\n");
            //wi.close();

            //String sfile = mallPath+mallName+"\\shop_vec.txt";//String sfile = mallPath+mallName+"\\shop_vec_"+count+".txt";
            //BufferedWriter ws = new BufferedWriter(new FileWriter(sfile));
            BufferedWriter ws = mallSWriter.get(mallName);
            ws.write(count+"");
            for(int i=0;i<shopLen;i++){
                ws.write(","+shopvec[i]);
            }ws.write("\r\n");
            //ws.close();

        }r.close();
        for(Map.Entry<String,BufferedWriter> entry : mallCWriter.entrySet()){
            entry.getValue().close();
        }

        for(Map.Entry<String,BufferedWriter> entry : mallIWriter.entrySet()){
            entry.getValue().close();
        }

        for(Map.Entry<String,BufferedWriter> entry : mallSWriter.entrySet()){
            entry.getValue().close();
        }
    }

    public void isLegal()throws Exception{

        BufferedReader r0 = new BufferedReader(new FileReader(mapMallTxt));
        List<String> listMallName = new ArrayList<>();
        String line="";
        while((line=r0.readLine())!=null){
            //String[] words = line.split(",");
            String mallName = line;
            listMallName.add(mallName);
        }r0.close();

        for(String mallName:listMallName){
            LineNumberReader rc = new LineNumberReader(new FileReader("E:\\tianchi\\mall2\\"+mallName+"\\connection.txt"));
            int lc = rc.getLineNumber();
            LineNumberReader ri = new LineNumberReader(new FileReader("E:\\tianchi\\mall2\\"+mallName+"\\intensity.txt"));
            int li = ri.getLineNumber();
            LineNumberReader rs = new LineNumberReader(new FileReader("E:\\tianchi\\mall2\\"+mallName+"\\shopvec.txt"));
            int ls = rs.getLineNumber();
            //System.out.println(lc+" "+li+" "+ls);
            if( !(lc==li&&li==ls) ) System.out.println("NO");
        }
    }


    public void findAllShopName(String input,String output) throws Exception {

        BufferedReader r0 = new BufferedReader(new FileReader(mapMallTxt));
        List<String> listMallName = new ArrayList<>();
        String line="";
        while((line=r0.readLine())!=null){
            //String[] words = line.split(",");
            String mallName = line;
            listMallName.add(mallName);
        }r0.close();
        Map<String,List<String>> mallShopName = new HashMap<>();
        String mallPath = "E:\\tianchi\\mall2\\";
        for(String mallName:listMallName) {
            List<String> shopNames = new ArrayList<>();
            String _mapShop = mallPath + mallName + "\\_mapShop.txt";//E:\tianchi\mall2\\_mapShop.txt
            BufferedReader rshop = new BufferedReader(new FileReader(_mapShop));
            while((line=rshop.readLine())!=null){
                shopNames.add(line.split(",")[0]);
            }rshop.close();
            mallShopName.put(mallName,shopNames);
        }


        BufferedReader rAB = new BufferedReader(new FileReader("E:\\tianchi\\AB榜测试集-evaluation_public.csv"));
        line = rAB.readLine();
        Map<String,String> rowMall = new HashMap<>();
        while((line=rAB.readLine())!=null){
            String[] words = line.split(",");
            String no = words[0];
            String mallName = words[2];
            rowMall.put(no,mallName);
        }
        rAB.close();



        BufferedReader ri = new BufferedReader(new FileReader(input));
        //BufferedWriter wo = new BufferedWriter(new FileWriter(output));
        List<String> dataList=new ArrayList<String>();
        dataList.add("row_id,shop_id");
        while ((line=ri.readLine())!=null){
            String[] words = line.split(",");
            String rowId = words[0];
            String mallName = rowMall.get(rowId);
            int shopId = Integer.parseInt( words[1] );
            String shopName = mallShopName.get(mallName).get(shopId);
            dataList.add(rowId+","+shopName);
        }ri.close();
        //dataList.add("1,张三,男");
        boolean isSuccess=CSVUtils.exportCsv(new File(output), dataList);
        System.out.println(isSuccess);
    }

    public void Bnum()throws Exception{
        BufferedReader rsw = new BufferedReader(new FileReader("E:\\tianchi\\wifi22.txt"));
        BufferedWriter w3 = new BufferedWriter(new FileWriter("E:\\tianchi\\wifi33.txt"));
        BufferedWriter w4 = new BufferedWriter(new FileWriter("E:\\tianchi\\wifi44.txt"));
        BufferedWriter w5 = new BufferedWriter(new FileWriter("E:\\tianchi\\wifi55.txt"));
        String line = "";
        int count = 0;
        Map<String,Integer> map = new HashMap<>();
        while((line=rsw.readLine())!=null){
            if( line.length()<1 ) continue;
            String[] words = line.split("\\|");
            if(!map.containsKey(words[0])) map.put(words[0],0);
            map.put(words[0],map.get(words[0])+1);
            w3.write(words[0]+"\r\n");
            //System.out.println(count++);
        }rsw.close();w3.close();
        List<String> list = new ArrayList<>();
        for(Map.Entry<String,Integer> entry:map.entrySet()){
            w4.write(entry.getKey()+" "+entry.getValue().toString()+"\r\n");
            list.add(entry.getKey()+" "+entry.getValue().toString());
        }w4.close();
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o2.split(" ")[1])-Integer.parseInt(o1.split(" ")[1]);
            }
        });
        for(String s:list){
            w5.write(s+"\r\n");
        }w5.close();
    }

    public void numCount()throws Exception{
        BufferedReader r5 = new BufferedReader(new FileReader("E:\\tianchi\\wifi55.txt"));
        BufferedWriter w6 = new BufferedWriter(new FileWriter("E:\\tianchi\\wifi66.txt"));
        String line = "";
        int[] count = new int[4100];
        while((line=r5.readLine())!=null){
            if( line.length()<1 ) continue;
            String[] words = line.split(" ");
            int c = Integer.parseInt(words[1]);
            count[c]++;
        }r5.close();
        for(int i=1;i<=4033;i++){
            w6.write(i+","+count[i]+"\r\n");
        }w6.close();
    }

//    public void filter(int threshold) throws Exception{
//        BufferedReader r5 = new BufferedReader(new FileReader("E:\\tianchi\\wifi55.txt"));
//        BufferedWriter w6 = new BufferedWriter(new FileWriter("E:\\tianchi\\wifiThreshold.txt"));
//        String line = "";
//        while((line=r5.readLine())!=null){
//            String[] words = line.split(" ");
//            int c = Integer.parseInt(words[1]);
//            if(c<=threshold) w6.write(words[0]+"\r\n");
//        }r5.close();w6.close();
//    }

    public void fileter(int thresholdCount,int thresholdIntensity) throws Exception{
        BufferedReader r0 = new BufferedReader(new FileReader(mapMallTxt));
        List<String> listMallName = new ArrayList<>();
        String line="";
        while((line=r0.readLine())!=null){
            //String[] words = line.split(",");
            String mallName = line;
            listMallName.add(mallName);
        }r0.close();

        Set<String> wifiFilter = new HashSet<>();
        BufferedReader rf = new BufferedReader(new FileReader("E:\\tianchi\\wifi55.txt"));
        while ((line=rf.readLine())!=null){
            String[] words = line.split(" ");
            String wifiName = words[0];
            int count = Integer.parseInt(words[1]);
            if(count<=thresholdCount) wifiFilter.add(wifiName);
        }

        int yes = 1;
        for(String mallName:listMallName){
            System.out.println(yes++);
        //String mallName = "m_615";
            BufferedReader rMapWifi = new BufferedReader(new FileReader("E:\\tianchi\\mall2\\"+mallName+"\\_mapWifi.txt"));
            BufferedWriter wMapWifi = new BufferedWriter(new FileWriter("E:\\tianchi\\mall3\\"+mallName+"\\_mapWifi.txt"));
            BufferedReader rc = new BufferedReader(new FileReader("E:\\tianchi\\mall2\\"+mallName+"\\connection.txt"));
            BufferedWriter wc = new BufferedWriter(new FileWriter("E:\\tianchi\\mall3\\"+mallName+"\\connection.txt"));
            BufferedReader ri = new BufferedReader(new FileReader("E:\\tianchi\\mall2\\"+mallName+"\\intensity.txt"));
            BufferedWriter wi = new BufferedWriter(new FileWriter("E:\\tianchi\\mall3\\"+mallName+"\\intensity.txt"));

            List<String> wifiAll = new ArrayList<>();
            List<String> wifiNeed = new ArrayList<>();
            while ( (line=rMapWifi.readLine())!=null ){
                String[] words = line.split(",");
                wifiAll.add(words[0]);
                if(!wifiFilter.contains(words[0])) wifiNeed.add(words[0]);
            }

            for(int i=0;i<wifiNeed.size();i++) {
                wMapWifi.write(wifiNeed.get(i)+","+i+"\r\n");
            }

            while((line=rc.readLine())!=null){
                Map<String,Integer> mapC = new HashMap<>();
                String[] words = line.split(",");
                for(int i=1;i<words.length;i++) {
                    mapC.put(wifiAll.get(i-1),Integer.parseInt(words[i]));
                }
                wc.write(words[0]);
                for (int i=0;i<wifiNeed.size();i++) {
                    wc.write(","+mapC.get(wifiNeed.get(i)));
                }
                wc.write("\r\n");
            }

            while((line=ri.readLine())!=null){
                Map<String,Integer> mapI = new HashMap<>();
                String[] words = line.split(",");
                for(int i=1;i<words.length;i++){
                    int intensity = Integer.parseInt(words[i]);
                    if(intensity<=thresholdIntensity) intensity=0;
                    mapI.put(wifiAll.get(i-1),intensity);
                }
                wi.write(words[0]);
                for(int i=0;i<wifiNeed.size();i++){
                    wi.write(","+mapI.get(wifiNeed.get(i)));
                }
                wi.write("\r\n");
            }

            rMapWifi.close();wMapWifi.close();
            rc.close();wc.close();
            ri.close();wi.close();
        }
    }
    public void ABtestIntensity() throws Exception{

        BufferedReader r0 = new BufferedReader(new FileReader(mapMallTxt));
        List<String> listMallName = new ArrayList<>();
        String line="";

        while((line=r0.readLine())!=null){
            //String[] words = line.split(",");
            String mallName = line;
            listMallName.add(mallName);
        }r0.close();


        Map<String,Map> mapMallWifi = new HashMap<>();
        String mallPath = "E:\\tianchi\\mall3\\";
        for(String mallName:listMallName){
            String _mapWifi = mallPath+mallName+"\\_mapWifi.txt";
            Map<String,Integer> mapw = new HashMap<>();
            BufferedReader rwifi = new BufferedReader(new FileReader(_mapWifi));
            while( (line=rwifi.readLine())!=null ){
                String[] words = line.split(",");
                mapw.put(words[0],Integer.parseInt(words[1]));
            } rwifi.close();
            mapMallWifi.put(mallName,mapw);
        }
        System.out.println(mapMallWifi.get("m_615").get("b_1034403")+"成功");


        BufferedWriter wAB = new BufferedWriter(new FileWriter("E:\\tianchi\\ABtest3.txt"));
        BufferedReader rAB = new BufferedReader(new FileReader("E:\\tianchi\\AB榜测试集-evaluation_public.csv"));
        line = rAB.readLine();
        while((line=rAB.readLine())!=null){
            String[] words = line.split(",");
            String no = words[0];
            String userName = words[1];
            String mallName = words[2];
            String wifiziduan = words[6];
            String[] wifis = wifiziduan.split(";");

            int wifiLen = mapMallWifi.get(mallName).size();
            if(wifiLen==0) {wAB.write(no+","+userName+",null\r\n"); continue;}
            int[] intensity = new int[wifiLen];
            for(String wifi:wifis){
                String wifiname = wifi.split("\\|")[0];
                String wifiInten = wifi.split("\\|")[1];
                int in = Integer.parseInt(wifiInten);
                if(in<=-80) in=0;
                wifiInten = in+"";
                if(mapMallWifi.get(mallName).get(wifiname)==null)continue;
                int wifiId = Integer.parseInt( mapMallWifi.get(mallName).get(wifiname).toString() );
                intensity[wifiId] = Integer.parseInt(wifiInten);
            }
            wAB.write(no+","+userName+","+mallName);
            for(int i=0;i<wifiLen;i++) wAB.write(","+intensity[i]);
            wAB.write("\r\n");
        }
        rAB.close();wAB.close();
    }
    public void ABtestConnection() throws Exception{

        BufferedReader r0 = new BufferedReader(new FileReader(mapMallTxt));
        List<String> listMallName = new ArrayList<>();
        String line="";

        while((line=r0.readLine())!=null){
            //String[] words = line.split(",");
            String mallName = line;
            listMallName.add(mallName);
        }r0.close();


        Map<String,Map> mapMallWifi = new HashMap<>();
        String mallPath = "E:\\tianchi\\mall3\\";
        for(String mallName:listMallName){
            String _mapWifi = mallPath+mallName+"\\_mapWifi.txt";
            Map<String,Integer> mapw = new HashMap<>();
            BufferedReader rwifi = new BufferedReader(new FileReader(_mapWifi));
            while( (line=rwifi.readLine())!=null ){
                String[] words = line.split(",");
                mapw.put(words[0],Integer.parseInt(words[1]));
            } rwifi.close();
            mapMallWifi.put(mallName,mapw);
        }
        System.out.println(mapMallWifi.get("m_615").get("b_1034403")+"成功");

        BufferedWriter wAB = new BufferedWriter(new FileWriter("E:\\tianchi\\ABtestConnection.txt"));
        BufferedReader rAB = new BufferedReader(new FileReader("E:\\tianchi\\AB榜测试集-evaluation_public.csv"));
        line = rAB.readLine();
        while((line=rAB.readLine())!=null){
            String[] words = line.split(",");
            String no = words[0];
            String userName = words[1];
            String mallName = words[2];
            String wifiziduan = words[6];
            String[] wifis = wifiziduan.split(";");

            int wifiLen = mapMallWifi.get(mallName).size();
            if(wifiLen==0) {wAB.write(no+","+userName+",null\r\n"); continue;}
            //int[] intensity = new int[wifiLen];
            int[] connection = new int[wifiLen];
            for(String wifi:wifis){
                String wifiname = wifi.split("\\|")[0];
                String wifiInten = wifi.split("\\|")[1];
                String wifiConString = wifi.split("\\|")[2];
                int wifiCon = wifiConString.equals("false")?0:1;
                int in = Integer.parseInt(wifiInten);
                if(in<=-80) in=0;
                wifiInten = in+"";
                if(mapMallWifi.get(mallName).get(wifiname)==null)continue;
                int wifiId = Integer.parseInt( mapMallWifi.get(mallName).get(wifiname).toString() );
                //intensity[wifiId] = Integer.parseInt(wifiInten);
                connection[wifiId] = wifiCon;
            }
            wAB.write(no+","+userName+","+mallName);
            //for(int i=0;i<wifiLen;i++) wAB.write(","+intensity[i]);
            for(int i=0;i<wifiLen;i++) wAB.write(","+connection[i]);
            wAB.write("\r\n");
        }
        rAB.close();wAB.close();
    }
    public void mergeABtest(int coefficient,int denominator) throws Exception{
        BufferedReader ri = new BufferedReader(new FileReader("E:\\tianchi\\ABtest3.txt"));
        BufferedReader rc = new BufferedReader(new FileReader("E:\\tianchi\\ABtestConnection.txt"));
        BufferedWriter w = new BufferedWriter(new FileWriter("E:\\tianchi\\ABtest_80_20.txt"));
        String linei = "", linec = "";
        while ((linei=ri.readLine())!=null){
            linec=rc.readLine();
            String[] wordsi = linei.split(",");
            String[] wordsc = linec.split(",");
            w.write(wordsi[0]+","+wordsi[1]+","+wordsi[2]);
            for(int i=3;i<wordsi.length;i++){
                int intensity = Integer.parseInt(wordsi[i]);
                int connection = Integer.parseInt(wordsc[i]);
                if(intensity==0) intensity=-80;
                intensity+=80;
                double feature = (intensity*1.0+coefficient*connection)/denominator;  //(x+20*z)/80
                w.write(","+feature);
            }
            w.write("\r\n");
        }
        ri.close();rc.close();w.close();
    }
    public void lookABtest() throws Exception{
        BufferedReader r = new BufferedReader(new FileReader("E:\\tianchi\\ABtest_80_20.txt"));
        String line = "";
        int i=30;
        while((line=r.readLine())!=null){
            if(i<0) break;
            i--;

            System.out.println(line);
        }
    }
}
