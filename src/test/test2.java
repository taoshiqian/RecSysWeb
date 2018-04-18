package test;


import tool.WatchDataManager;
import tool.tianchi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test2 {
    private static String input = "E:\\OCNdata\\VOD\\test\\inputTest.txt";
    private static String output = "E:\\OCNdata\\VOD\\test\\recResult.txt";
    private static String itemRecListFilePath ="E:\\OCNdata\\VOD\\test\\itemRecList.txt";
    private static int factorNum = 10;
    private static int iterNum = 20;
    private static double lambda = 0.01;
    // 创建目录
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
    public static void main(String[] args) throws Exception {
        tianchi t= new tianchi();
        t.fileter(1,-80);
        t.ABtestIntensity();
        t.ABtestConnection();
        t.mergeABtest(20,80);
        t.lookABtest();
        //复制mall2改名为mall3，运行
        //t.filter(3);
        //createDir("E:\\tianchi\\mall\\1");
        //createDir("E:\\tianchi\\mall\\1");
        //tianchi t = new tianchi();
        //t.mall_shop_sort();
        //t.mallCopy();
       //t.findAllShopName("E:\\tianchi\\result0.txt","E:\\tianchi\\result.csv");
        //t.ABtest();
        // t.isLegal();
        //t.pretreatment();
//        Tester tester = new Tester4();
//        tester.trainFromTxt(input,factorNum,iterNum,lambda);
//        tester.recToTxt(output,itemRecListFilePath);
//        System.out.println("推荐完成，结果见"+output);
        //System.out.println(findNum("是"));
    }
    public static int findNum(String str){
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
        for(int i=strArr.length-1;i>=0;i--) {
            try {
                ans = Integer.parseInt(strArr[i]);//最后数字
                break;
            } catch (Exception e) {
            }
        }
        return ans;
    }
}
