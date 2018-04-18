package tool;

import java.io.File;

public class jar2classpath {
    public static void main(String[] args){
        String filepath = "E:\\JavaWorkSpace\\RecSysWeb\\jar";
        File file = new File(filepath);
        String[] fileList = file.list();
        //System.out.println(fileList);
        String ret = "";
        for(String s :fileList){
            //System.out.println(s);
            if(s.charAt(s.length()-1)!='r') continue;
            System.out.println(s);
            s = "%JAR_HOME%\\"+s+";";
            System.out.println(s);
            ret+=s;
        }
        System.out.println(ret);
    }
}
