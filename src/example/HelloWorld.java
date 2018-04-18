package example;

import recsys.RecByJson;

public class HelloWorld {
    public String recommendRequest(String requestJsonString) throws Exception {
        System.out.println("请求到达了web");
        RecByJson recByJson = new RecByJson();
        String reponseJsonString = recByJson.rec(requestJsonString);
        System.out.println("推荐完成，现在返回JSON字符串");
        return reponseJsonString;
    }
}
