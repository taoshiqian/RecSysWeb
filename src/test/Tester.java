//package test;
//
//import fm.ALS;
//import org.javatuples.Pair;
//import org.javatuples.Triplet;
//import org.javatuples.Tuple;
//import tool.MapID;
//import tool.Rating;
//
//import java.io.*;
//import java.util.*;
//
//public abstract class Tester {
//
//    protected static MapID mapUser = new MapID();
//    protected static MapID mapItem = new MapID();
//    protected static int itemMaxNum = 1;
//    protected ALS als = new ALS();
//
//    protected DataConvert dataConvert = null;
//
//    public Tester() {
//    }
//
//    protected Tester(DataConvert dataConvert) {
//        this.dataConvert = dataConvert;
//    }
//
//    protected abstract boolean isLimitedData(String[] words);
//
//    protected boolean isLegalUser(String s) {
//        if (s.length() != 12) return false;
//        for (int i = 0; i < s.length(); i++) {
//            int ch = s.charAt(i);
//            if (!((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F'))) return false;
//        }
//        return true;
//    }
//
//    protected List<String> getLines(File file) throws Exception {
//        BufferedReader reader = new BufferedReader(new FileReader(file));
//        List<String> lines = new ArrayList<>();
//        String line = null;
//        while ((line = reader.readLine()) != null) {
//            lines.add(line);
//        }
//        return lines;
//    }
//
//    protected void ratingToResult(Rating rating) {
//        for (int i = 0; i < rating.size(); i++) {
//            Tuple tuple = rating.getTuple(i);
//            String result = dataConvert.tupleToString(tuple);
//            System.out.println(result);
//        }
//    }
//
//    protected void linesToRating(Rating rating, List<String> lines) throws Exception{
//        for (String line : lines) {
//            String[] words = line.split("\\|");
//            //if (words.length != 4) continue;//if (!isLegalUser(words[0]) || tuple.getValue() <= 0 || itemDuration <= 0)continue;   //System.out.println(line + " 不合法");// Tuple tuple = wordsToTuple(words);
//            Tuple tuple = dataConvert.stringsToTuple(words);
//            rating.add(tuple);
//        }
//        rating.setNumOfUsers(mapUser.getSize());
//        rating.setNumOfItems(mapItem.getSize());
//    }
//
//
//    public void testFromFile(String filePath, boolean repeat, int factorNum, int numOfIter, double lambda) throws Exception {
//        Rating ratingTrain = new Rating();
//        List<String> lines = getLines(new File(filePath));
//        linesToRating(ratingTrain,lines);
//        Rating ratingRecommend = als.recommendOneForAllUsers(ratingTrain,repeat, factorNum, numOfIter, lambda);
//        ratingToResult(ratingRecommend);
//        System.out.println(mapUser.getSize()+"   "+mapItem.getSize());
//    }
//
//    public void testFromDirectory(String directory, boolean repeat, int factorNum, int numOfIter, double lambda) throws Exception {
//        File fileDirectory = new File(directory);
//        if(fileDirectory.isDirectory()==false){System.out.println("目录错误");return ;}
//        File[] files = fileDirectory.listFiles();
//        Rating ratingTrain = new Rating();
//        for(File file:files) if(file.isFile()) {
//             List<String> lines = getLines(file);
//             linesToRating(ratingTrain,lines);
//        }
//        Rating ratingRecommend = als.recommendOneForAllUsers(ratingTrain, repeat, factorNum, numOfIter, lambda);
//        ratingToResult(ratingRecommend);
//        System.out.println(mapUser.getSize()+"   "+mapItem.getSize());
//    }
//
//    private int timeToPeriod(String time) {
//        int hour = Integer.parseInt(time.substring(8, 10));
//        int half = Integer.parseInt(time.substring(10, 12)) >= 30 ? 1 : 0;
//        return hour * 2 + half;
//    }
//
//    public void trainFromTxt (String input,int factorNum,int iterNum,double lambda) throws Exception{
//        Rating rating = new Rating();
//        BufferedReader reader = new BufferedReader(new FileReader(input));
//        String line = null;
//        while((line=reader.readLine())!=null){
//            String[] words = line.split("\\|");
//            Tuple tuple = dataConvert.stringsToTuple(words);
//            rating.add(tuple);
//        }
//        rating.setNumOfUsers(mapUser.getSize());
//        rating.setNumOfItems(mapItem.getSize());
//        //rating.setNumOfItems(itemMaxNum+1);
//        als.train(rating,factorNum,iterNum,lambda);
//    }
//
//    public void recToTxt (String output,String itemRecListFilePath) throws Exception{
//        BufferedReader reader = new BufferedReader(new FileReader(itemRecListFilePath));
//        List<Integer> itemRecList = new ArrayList<>();
//        String line = null;
//        while((line=reader.readLine())!=null){
//            int itemId = Integer.parseInt(line);
//            if(itemRecList.contains(itemId)) continue;
//            itemRecList.add(itemId);
//        }
//        System.out.println("把待推荐列表读完毕");
//
//        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
//        for(int i=0;i<mapUser.getSize();i++) {
//            List<Integer> recList = als.limitedRecommendByUserId(i,itemRecList,5,false);
//            String recString = "";
//            for(int j=0;j<recList.size();j++){
//                int itemId = recList.get(j);
//                recString += "|"+mapItem.getName(itemId);//;+mapItem.getName(itemId);
//            }
//            System.out.println(mapUser.getName(i)+ recString);
//            writer.write(mapUser.getName(i)+ recString +"\r\n");
//        }
//        writer.close();
//    }
//
//
//
//
//
//
//
//
//    //以下是中间测试代码，请无视
//    protected void testRMSE(String filePath) throws Exception { //测试用
//        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
//        List<String> lines = new ArrayList<>();
//        String line = null;
//        double sum = 0.0;
//        int count = 0;
//        while ((line = reader.readLine()) != null) {
//            String[] words = line.split("\\|");
//            if (isLimitedData(words) == false) continue; //sum+=1;
//            Tuple tuple = dataConvert.stringsToTuple(words);
//            int userId = (int) tuple.getValue(0);
//            int itemId = (int) tuple.getValue(1);
//            // if(userId>=mapUser.getSize()||itemId>=mapItem.getSize()) continue;
//            double score = (double) tuple.getValue(2);
//            sum += Math.pow(score - als.predict(userId, itemId), 2.0);
//            //System.out.println(sum);
//            count++;
//        }
//        double rmse = Math.sqrt(sum / count);
//        System.out.println(rmse);
//    }
//
//
//
//    protected void precision(String activeUserFile,String filePath) throws Exception{
//        BufferedReader readerOfUser = new BufferedReader(new FileReader(activeUserFile));
//        String line = null;
//        Set<String> activeUser = new HashSet();
//        while((line=readerOfUser.readLine())!=null){
//            String[] words = line.split("\\|");
//            activeUser.add(words[0]+"|"+words[1]);
//        }
//        System.out.println("activeUser大小:"+activeUser.size());
//
//        BufferedReader readerOfData = new BufferedReader(new FileReader(filePath));
//        Rating rating = new Rating();
//        int itemNum=0;
//        while ((line=readerOfData.readLine())!=null){
//            String[] words = line.split("\\|");
//            String date = words[3].substring(0,8);
//            if(date.equals("20160109")) break;
//            String uID = words[0]+"|"+timeToPeriod(words[3]);
//            if(activeUser.contains(uID)){
//                int userId = mapUser.getId(uID);
//                int itemId = Integer.parseInt(words[5]);
//                if(itemId>itemNum) itemNum=itemId;
//                int userDuration = Integer.parseInt(words[4]);
//                int itemDuration = Integer.parseInt(words[7]);
//                double score = userDuration * 1.0 / itemDuration;
//                rating.add(new Triplet<Integer,Integer,Double>(userId,itemId,score));
//            }
//        }
//        System.out.println(mapUser.getSize()+","+itemNum);
//        rating.setNumOfUsers(mapUser.getSize()+1);
//        rating.setNumOfItems(itemNum+1);
//        System.out.println("读取完成");
//        ALS als = new ALS();
//        als.train(rating,10,20,0.01);
//        System.out.println("训练完成");
//
//        //测试并计算
//        int all=0,yes=0;
//        Map<String,Boolean> hasRec = new HashMap();
//        while ((line=readerOfData.readLine())!=null){
//            String[] words = line.split("\\|");
//            String date = words[3].substring(0,8);
//            if(date.equals("20160110")) break;
//            String uID = words[0]+"|"+timeToPeriod(words[3]);
//            if(activeUser.contains(uID)){
//                if(hasRec.containsKey(uID)&&hasRec.get(uID)==true) continue; //已经推荐成功
//                if(!hasRec.containsKey(uID)) {hasRec.put(uID,false);all++;} //活跃用户来看电视了，计数
//                int userId = mapUser.getId(uID);
//                int itemId = Integer.parseInt(words[5]);
//                List<Pair<Integer, Double>> listR = als.recommendByUserId(userId,5,true);
//                for(int i=0;i<5;i++){
//                    int rId = listR.get(i).getValue0();
//                    if(rId==itemId) {
//                        yes++;
//                        hasRec.put(uID,true);
//                        System.out.println(uID+","+itemId);
//                        break;
//                    }
//                }
//            }
//        }
//        System.out.println("yes:"+yes);
//        System.out.println("all:"+all);
//        System.out.println("准确率:"+yes*1.0/all);
//    }
//
//
//    protected void precision2(String activeUserFile,String filePath) throws Exception{
//        BufferedReader readerOfUser = new BufferedReader(new FileReader(activeUserFile));
//        String line = null;
//        Set<String> activeUser = new HashSet();
//        while((line=readerOfUser.readLine())!=null){
//            String[] words = line.split("\\|");
//            activeUser.add(words[0]);
//        }
//        System.out.println("activeUser大小:"+activeUser.size());
//
//        BufferedReader readerOfData = new BufferedReader(new FileReader(filePath));
//        Rating rating = new Rating();
//        int itemNum=0;
//        while ((line=readerOfData.readLine())!=null){
//            String[] words = line.split("\\|");
//            String date = words[3].substring(0,8);
//            if(date.equals("20160103")) break;
//            String uID = words[0];//+"|"+timeToPeriod(words[3]);
//            if(activeUser.contains(uID)){
//                int userId = mapUser.getId(uID);
//                int itemId = Integer.parseInt(words[5]);
//                if(itemId>itemNum) itemNum=itemId;
//                int userDuration = Integer.parseInt(words[4]);
//                int itemDuration = Integer.parseInt(words[7]);
//                double score = userDuration * 1.0 / itemDuration;
//                rating.add(new Triplet<Integer,Integer,Double>(userId,itemId,score));
//            }
//        }
//        System.out.println(activeUser.size()+","+itemNum);
//        rating.setNumOfUsers(activeUser.size()+1);
//        rating.setNumOfItems(itemNum+1);
//        System.out.println("读取完成");
//        ALS als = new ALS();
//        als.train(rating,10,20,0.01);
//        System.out.println("训练完成");
//
//
//        long begintime = System.currentTimeMillis();
//        {
//            for(int i=0;i<100;i++){
//                List<Pair<Integer, Double>> listR = als.recommendByUserId(i,5,true);
//            }
//        }
//        long endtime=System.currentTimeMillis();
//        long costTime = (endtime - begintime);
//        System.out.println("100用户*"+rating.getNumOfItems()+"节目,耗时："+costTime);
//
//        //测试并计算
//        List<Pair<Integer, Double>> listR = new ArrayList<>();listR.add(new Pair<Integer,Double>(57,1.0));listR.add(new Pair<Integer,Double>(114,1.0));listR.add(new Pair<Integer,Double>(117,1.0));listR.add(new Pair<Integer,Double>(272,1.0));listR.add(new Pair<Integer,Double>(53,1.0));
//        int all=0,yes=0;
//        Map<String,Boolean> hasRec = new HashMap();
//        while ((line=readerOfData.readLine())!=null){
//            String[] words = line.split("\\|");
//            String date = words[3].substring(0,8);
//            if(date.equals("20160110")) break;
//            String uID = words[0];//+"|"+timeToPeriod(words[3]);
//            if(activeUser.contains(uID)){
//                if(hasRec.containsKey(uID)&&hasRec.get(uID)==true) continue; //已经推荐成功
//                if(!hasRec.containsKey(uID)) {hasRec.put(uID,false);all++;} //活跃用户来看电视了，计数
//
//                int userId = mapUser.getId(uID);
//                int itemId = Integer.parseInt(words[5]);
//                if(itemId>=itemNum) continue;
//                //List<Pair<Integer, Double>> listR = als.recommendByUserId(userId,5,true);
//                //List<Pair<Integer, Double>> listR = new ArrayList<>();listR.add(new Pair<Integer,Double>(57,1.0));listR.add(new Pair<Integer,Double>(114,1.0));listR.add(new Pair<Integer,Double>(117,1.0));listR.add(new Pair<Integer,Double>(272,1.0));listR.add(new Pair<Integer,Double>(53,1.0));
//                for(int i=0;i<5;i++){
//                    int rId = listR.get(i).getValue0();
//                    if(rId==itemId) {
//                        yes++;
//                        hasRec.put(uID,true);
//                        System.out.println(uID+","+itemId);
//                        break;
//                    }
//                }
//            }
//        }
//        System.out.println("yes:"+yes);
//        System.out.println("all:"+all);
//        System.out.println("准确率:"+yes*1.0/all);
//    }
//}
