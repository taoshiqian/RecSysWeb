package recsys;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Tuple;
import tool.Rating;

import java.awt.image.RescaleOp;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecSysOfSeries extends RecSysOCN {

    private static final int USER_ID_INDEX = 0;
    private static final int ITEM_ID_INDEX = 1;
    private static final int USER_DURATION_INDEX = 2;
    private static final int ITEM_DURATION_INDEX = 3;

    @Override
    protected Triplet<Integer, Integer, Double> stringArraysToTuple(String[] words) { //电视剧评判喜好程度得好好想想
        int userId = mapUser.getId(words[USER_ID_INDEX]);
        int itemId = mapItem.getId(words[ITEM_ID_INDEX]);
        int userDuration = Integer.parseInt(words[USER_DURATION_INDEX]);
        int itemDuration = Integer.parseInt(words[ITEM_DURATION_INDEX]);
        if (itemDuration <= 0) return new Triplet<Integer, Integer, Double>(-1, -1, -1.0); //换成0再试试
        if (userDuration > itemDuration) userDuration = itemDuration;
        double score = 1 + userDuration * 1.0 / itemDuration;
        return new Triplet<Integer, Integer, Double>(userId, itemId, score);
    }

    @Override
    protected List<Pair<Integer, Double>> recForOneUser(int userId, int num) {
        //return als.limitedRecommendByUserId(userId,itemRecList,5,false);//不重复推荐
        return als.limitedRecommendByUserId(userId, itemRecList, num, true);//重复推荐
    }

    @Override
    protected int train(String FileName, double lambda) throws Exception {
        Rating rating = new Rating();
        Map<Pair<Integer, Integer>, Double> map = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(FileName));
        String line = null;
        int record = 0;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\\|");
            int timeday = Integer.parseInt(words[4].substring(0, 8));
            int timehm = Integer.parseInt(words[4].substring(8, 12));
            int timeQ = timeQuantum(timeday, timehm);
            int userId = mapUser.getId(words[USER_ID_INDEX] + "_" + timeQ);
            int itemId = mapItem.getId(words[ITEM_ID_INDEX]);
            int userDuration = Integer.parseInt(words[USER_DURATION_INDEX]);
            int itemDuration = Integer.parseInt(words[ITEM_DURATION_INDEX]);
            //if(time%100<20) continue;
            int day = timeday % 100;
            if (filterByday(day)) continue;
            if (itemDuration <= 0) continue; //换成0再试试
            if (userDuration > itemDuration) userDuration = itemDuration;
            double score = userDuration * 1.0 / itemDuration;   //去掉1试试
            int beforeDays = (31 - day); //目前是因为预测期是1号
            double weight = 1.0; //默认无区别
            if (beforeDays > 50) beforeDays = 50;  //不能低于0.5
            weight = (100 - beforeDays) * 1.0 / 100;  //有重要性区别，修改权重
            score = score * weight;//分数*权重
            Pair<Integer, Integer> useritem = new Pair<>(userId, itemId);
            if (map.containsKey(useritem)) map.put(useritem, map.get(useritem) + score);
            else map.put(useritem, score);
            record++;
        }
        for (Map.Entry<Pair<Integer, Integer>, Double> entry : map.entrySet()) {
            int userId = entry.getKey().getValue0();
            int itemId = entry.getKey().getValue1();
            double score = entry.getValue();
            try {
                rating.add(new Triplet<Integer, Integer, Double>(userId, itemId, score));
            } catch (Exception e) {
                System.out.println("错误：" + userId + "," + itemId + "," + score);
            }
        }
        System.out.println("训练数据读取完成，记录条数：" + record);
        reader.close();
        rating.setNumOfUsers(mapUser.getSize());
        rating.setNumOfItems(mapItem.getSize());
        System.out.println("rating制作完成");// + rating.size()
        int factorNum = rating.size() / 1500;
        if (factorNum < 30) factorNum = 30;
        if (factorNum > 80) factorNum = 80;
        factorNum=10;
        int iterNum = rating.size() / 1000;
        if (iterNum < 20) iterNum = 20;
        if (iterNum > 80) iterNum = 80;
        iterNum=10;
        System.out.println("模型迭代");
        als.train(rating, factorNum, iterNum, lambda);
        System.out.println("模型迭代完成");
        //System.out.println("模型训练完成,人" + rating.getNumOfUsers() + ",节目" + rating.getNumOfItems());
        //als.saveModel("E:\\model");
        //als.loadModel("E:\\model");
        return record;
    }
}
