package fm;

import Jama.Matrix;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Tuple;
import tool.Rating;
import tool.RatingDao;
import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ALS {
    private int factorNum = 5;       //分解因子数（或者叫 特征值数量），即分解后小矩阵的宽度。
    private int iterNum = 20;         //迭代次数
    private double lambda = 0.01;     //正则化参数
    private int numOfUsers = 2000;   //用户数量
    private int numOfItems = 2000;   //物品数量（电影……）
    private List<Tuple>[] itemsOfUserRated; //该用户评价过的物品集合,下标是用户i
    private List<Tuple>[] usersOfRatedItem; //评价过该物品的用户集合,下标是物品j
    //private Matrix R;         //评分矩阵,太稀疏，升级版肯定不能用
    private Matrix users;     //分解的用户矩阵
    private Matrix items;     //分解的物品矩阵
    private List<Integer> usersClass = new ArrayList<>();


    private void sortByID(List<Tuple>[] list) {
        for (int i = 0; i < list.length; i++) {
            Collections.sort(list[i], new Comparator<Tuple>() {
                @Override
                public int compare(Tuple o1, Tuple o2) { //小到大
                    return Double.compare(new Double(o1.getValue(0).toString()), new Double(o2.getValue(0).toString()));
                }
            });
        }
    }

    private void init(Rating rating) {
        itemsOfUserRated = new ArrayList[numOfUsers];
        usersOfRatedItem = new ArrayList[numOfItems];
        for (int i = 0; i < numOfUsers; i++)
            itemsOfUserRated[i] = new ArrayList<Tuple>();
        for (int i = 0; i < numOfItems; i++)
            usersOfRatedItem[i] = new ArrayList<Tuple>();
        for (int i = 0; i < rating.size(); i++) {
            int userId = rating.getUserId(i);
            int itemId = rating.getItemId(i);
            double rateScore = rating.getRateScore(i);
            //if (userId > numOfUsers) numOfUsers = userId;//更新用户编号最大值
            //if (itemId > numOfItems) numOfItems = itemId;//更新物品编号最大值
            itemsOfUserRated[userId].add(new Pair<Integer, Double>(itemId, rateScore));
            usersOfRatedItem[itemId].add(new Pair<Integer, Double>(userId, rateScore));
        }
        sortByID(itemsOfUserRated);
        sortByID(usersOfRatedItem);
        users = Matrix.random(factorNum, numOfUsers);
        items = Matrix.random(factorNum, numOfItems);
        for (int itemId = 0; itemId < numOfItems; itemId++) {
            double sum = 0.0;   //将item矩阵的第1行置为平均分
            for (int j = 0; j < usersOfRatedItem[itemId].size(); j++) {
                sum += (Double) usersOfRatedItem[itemId].get(j).getValue(1);
            }
            if (usersOfRatedItem[itemId].size() == 0) sum = 0.0;
            else sum /= usersOfRatedItem[itemId].size();
            items.set(0, itemId, sum);
        }
    }

    public double predict(int userId, int itemId) {
        if(userId>numOfUsers||itemId>numOfItems) return 0;
        double ret = 0;
        for (int f = 0; f < factorNum; f++) {
            ret += users.get(f, userId) * items.get(f, itemId);
        }
        return ret;
    }

    private void updateUser(int userId) throws Exception {
        if (itemsOfUserRated[userId].size() == 0) return;
        Matrix subMatOfItems = new Matrix(factorNum, itemsOfUserRated[userId].size());
        for (int f = 0; f < factorNum; f++) {
            for (int itemId = 0; itemId < itemsOfUserRated[userId].size(); itemId++) {
                subMatOfItems.set(f, itemId, items.get(f, (Integer) itemsOfUserRated[userId].get(itemId).getValue(0)));
            }
        }
        Matrix XtX = subMatOfItems.times(subMatOfItems.transpose());
        for (int f = 0; f < factorNum; f++) {
            XtX.set(f, f, XtX.get(f, f) + lambda * itemsOfUserRated[userId].size());  //LAMBDA*M
        }
        Matrix subMatOfRate = new Matrix(1, itemsOfUserRated[userId].size());
        for (int itemId = 0; itemId < itemsOfUserRated[userId].size(); itemId++) {
            subMatOfRate.set(0, itemId, (Double) itemsOfUserRated[userId].get(itemId).getValue(1));
        }
        Matrix XtY = subMatOfItems.times(subMatOfRate.transpose());
        users.setMatrix(0, factorNum - 1, userId, userId, XtX.solve(XtY));
    }

    private void updateItem(int itemId) throws Exception {
        if (usersOfRatedItem[itemId].size() == 0) return;
        Matrix subMatOfUsers = new Matrix(factorNum, usersOfRatedItem[itemId].size());
        for (int f = 0; f < factorNum; f++) {
            for (int userId = 0; userId < usersOfRatedItem[itemId].size(); userId++) {
                subMatOfUsers.set(f, userId, users.get(f, (Integer) usersOfRatedItem[itemId].get(userId).getValue(0)));
            }
        }
        Matrix XtX = subMatOfUsers.times(subMatOfUsers.transpose());
        for (int f = 0; f < factorNum; f++) {
            XtX.set(f, f, XtX.get(f, f) + lambda * usersOfRatedItem[itemId].size());  //LAMBDA*M
        }
        Matrix subMatOfRate = new Matrix(usersOfRatedItem[itemId].size(), 1);
        for (int userId = 0; userId < usersOfRatedItem[itemId].size(); userId++) {
            subMatOfRate.set(userId, 0, (Double) usersOfRatedItem[itemId].get(userId).getValue(1));
        }
        Matrix XtY = subMatOfUsers.times(subMatOfRate);
        items.setMatrix(0, factorNum - 1, itemId, itemId, XtX.solve(XtY));
    }

    private void learnALS() throws Exception {
        for (int iter = 0; iter < iterNum; iter++) {
            for (int userId = 0; userId < numOfUsers; userId++)
                updateUser(userId);
            for (int itemId = 0; itemId < numOfItems; itemId++)
                updateItem(itemId);
            //rmseOfTestFile();
        }
    }

    public void rmseOfTestFile(String testFilePath) throws Exception { //测试用，请无视
        Rating rating = RatingDao.readFromFileWithNum(testFilePath);
        //if (rating.size() == 0) { System.out.println("没有测试数据");return;}
        double sum = 0.0;
        for (int i = 0; i < rating.size(); i++) {
            int userId = rating.getUserId(i);
            int itemId = rating.getItemId(i);
            double RateScore = rating.getRateScore(i);
            double predictScore = predict(userId, itemId);
            double error = RateScore - predictScore;
            sum += Math.pow(error, 2);
            //sum += Math.abs(error);
            //System.out.println(sum+"-----"+error+"---"+i);
            //System.out.println(userId + "," + itemId + "," + RateScore + "," + predictScore);
        }
        double rmse = Math.sqrt(sum / rating.size());
        //double mse = sum / rating.size();
        System.out.println(rmse);
    }

    private boolean findFromList(List<Tuple> list, int targetID) {
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int middleID = Integer.parseInt(list.get(mid).getValue(0).toString());
            if (middleID == targetID) return true;
            else if (middleID < targetID) left = mid + 1;
            else right = mid - 1;
        }
        return false;
    }

    public List<Pair<Integer, Double>> limitedRecommendByUserId(int userId, List<Integer> itemRecList, int num, boolean repeat) {//用户ID，item的可推荐列表，推荐多少个，是否推荐重复的
        ArrayList<Pair<Integer, Double>> recommendList = new ArrayList<Pair<Integer, Double>>();
        for (int i = 0; i < itemRecList.size(); i++) {
            int itemId = itemRecList.get(i);
            if (repeat == false && findFromList(itemsOfUserRated[userId], itemId)) continue;
            recommendList.add(new Pair<Integer, Double>(itemId, itemId >= numOfItems ? 0 : predict(userId, itemId)));//未出现过，喜爱度置0
        }
        Collections.sort(recommendList, new Comparator<Pair<Integer, Double>>() {
            @Override
            public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
                return Double.compare(o2.getValue1(), o1.getValue1());
            }
        });
        List<Pair<Integer, Double>> ret = new ArrayList<>();
        for (int i = 0; i < Math.min(recommendList.size(), num); i++) {
            ret.add(recommendList.get(i));
        }
        return ret;
    }

    public List<Pair<Integer, Double>> recommendByUserId(int userId, int num, boolean repeat) {//用户ID，推荐多少个，是否推荐重复的
        ArrayList<Pair<Integer, Double>> recommendList = new ArrayList<Pair<Integer, Double>>();
        for (int itemId = 0; itemId < numOfItems; itemId++) {
            if (repeat == false && findFromList(itemsOfUserRated[userId], itemId)) continue;
            recommendList.add(new Pair<Integer, Double>(itemId, predict(userId, itemId)));
        }
        Collections.sort(recommendList, new Comparator<Pair<Integer, Double>>() {
            @Override
            public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
                return Double.compare(o2.getValue1(), o1.getValue1());
            }
        });
        return recommendList.subList(0, Math.min(num, recommendList.size()));
    }

    public Rating recommendOneForAllUsers(Rating rating, boolean repeat, int factorNum, int iterNum, double lambda) throws Exception {
        train(rating, factorNum, iterNum, lambda);
        Rating ratingOfRecommend = new Rating();
        for (int userId = 0; userId < numOfUsers; userId++) {
            List<Pair<Integer, Double>> list = recommendByUserId(userId, 1, repeat);
            if (list.size() == 0) ratingOfRecommend.add(new Triplet<Integer, Integer, Double>(userId, -1, 0.0));
            else
                ratingOfRecommend.add(new Triplet<Integer, Integer, Double>(userId, list.get(0).getValue0(), list.get(0).getValue1()));
        }
        return ratingOfRecommend;
    }

    public void Kmeans() throws Exception {
        //InputStreamReader isr = new InputStreamReader(new FileInputStream("Map.txt"), "UTF-8");
        //BufferedReader r = new BufferedReader(isr);

        //File directory = new File("");
        //System.out.println(directory.getAbsolutePath()+"\\usersVectorWeka");
        //File file = new File(directory.getAbsolutePath()+"usersVectorWeka");
        File file = new File("usersVectorWeka");
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        BufferedWriter w = new BufferedWriter(osw);
        w.write("@RELATION OCN"+"\r\n");
        for (int f = 0; f < factorNum; f++) {//@ATTRIBUTE one REAL
            w.write("@ATTRIBUTE OCN" + f + " REAL"+"\r\n");
        }
        w.write("@DATA"+"\r\n");
        for (int userId = 0; userId < users.getColumnDimension(); userId++) {
            for (int f = 0; f < factorNum; f++) {
                w.write(users.get(f, userId) + " ");
            }
            w.write("\r\n");
        }
        w.close();

        //以下weka的Kmeans聚类
        Instances ins = null;
        SimpleKMeans KM = null;
        DistanceFunction disFun = null;
        // 读入样本数据
        //File file = new File("usersVectorWeka");
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);
        ins = loader.getDataSet();
        // 初始化聚类器 （加载算法）
        KM = new SimpleKMeans();
        KM.setNumClusters(factorNum);       //设置聚类要得到的类别数量
        KM.buildClusterer(ins);     //开始进行聚类
        //System.out.println(KM.preserveInstancesOrderTipText());
        // 打印聚类结果
        //System.out.println(KM.toString());
        for (int userId = 0; userId < users.getColumnDimension(); userId++) {
            Instance i = new Instance(factorNum);
            for (int f = 0; f < factorNum; f++) {
                i.setValue(f, users.get(f, userId));
            }
            usersClass.add(userId, KM.clusterInstance(i));
            //System.out.println(KM.clusterInstance(i));
        }
        //System.out.println(users.getColumnDimension());
    }

    public void train(Rating rating, int factorNum, int iterNum, double lambda//, int numOfUsers, int numOfItems
    ) throws Exception {
        this.factorNum = factorNum;
        this.iterNum = iterNum;
        this.lambda = lambda;
        this.numOfUsers = rating.getNumOfUsers();
        this.numOfItems = rating.getNumOfItems();
        init(rating);
        learnALS();
        Kmeans();
        saveModel("modelUsers","modelItems");
        //rmseOfTestFile();
    }

    public List<Double> getUserVec(int uersId) {
        List<Double> list = new ArrayList<>();
        for (int f = 0; f < factorNum; f++) {
            list.add(users.get(f, uersId));
        }
        return list;
    }

    public void saveModel(String fileUsers, String fileItems) throws Exception {
        ObjectOutputStream oosUsers = new ObjectOutputStream(new FileOutputStream(fileUsers));
        oosUsers.writeObject(users);
        oosUsers.close();
        ObjectOutputStream oosItems = new ObjectOutputStream(new FileOutputStream(fileItems));
        oosItems.writeObject(items);
        oosItems.close();
        System.out.println("模型保存成功");
    }

    public void loadModel(String fileUsers, String fileItems) throws Exception {
        ObjectInputStream oisUsers = new ObjectInputStream(new FileInputStream(fileUsers));
        users = (Matrix) oisUsers.readObject();
        oisUsers.close();
        ObjectInputStream oisItems = new ObjectInputStream(new FileInputStream(fileItems));
        items = (Matrix) oisItems.readObject();
        oisItems.close();
        System.out.println("模型读取成功");
    }
    public int getUserClass(int userId){
        return usersClass.get(userId);
    }
}
