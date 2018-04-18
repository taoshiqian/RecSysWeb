package recsys;
import java.io.File;
import java.io.PrintStream;

import fm.ALS;
import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class usersClustering {
    public static void main(String[] args) throws Exception{

        ALS als = new ALS();
        als.loadModel("E:\\OCN\\broadcast\\modelUsers","E:\\OCN\\broadcast\\modelItems");
        System.out.println( als.getUserVec(1));
        als.Kmeans();


        Instances ins = null;

        SimpleKMeans KM = null;
        DistanceFunction disFun = null;

        try {
            // 读入样本数据
            File file = new File("E:\\JavaWorkSpace\\RecSysWeb\\data1test.txt");
            ArffLoader loader = new ArffLoader();
            loader.setFile(file);
            ins = loader.getDataSet();

            // 初始化聚类器 （加载算法）
            KM = new SimpleKMeans();
            KM.setNumClusters(4);       //设置聚类要得到的类别数量
            KM.buildClusterer(ins);     //开始进行聚类
            //System.out.println(KM.preserveInstancesOrderTipText());
            // 打印聚类结果
            //System.out.println(KM.toString());

            System.out.println();

            Instance i = new Instance(2);
            i.setValue(0,0.68);
            i.setValue(1,0.68);
            //System.out.println( KM.clusterInstance(i) );
            //System.out.println();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}