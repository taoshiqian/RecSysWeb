package tool;


public class Test implements Runnable {

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(new Test());
        thread.start();
        System.out.println("begin");
        thread.join();
        System.out.println("end");

    }

    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.print(i+" ");
        }
    }
}
