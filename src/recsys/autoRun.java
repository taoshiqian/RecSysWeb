package recsys;

import javax.servlet.ServletContextEvent;//这个是自启动要用到的类，服务器后台事件
import javax.servlet.ServletContextListener;//这个是自启动要用到的类，服务器后台监听
//声明一个autoRun类，使用服务器后台监听接口。固定用法，死记硬背
public class autoRun  implements ServletContextListener {
    //当后台被初始化，即发生了tomcat启动了事件，固定用法
    public void contextInitialized(ServletContextEvent arg0){
        //读取文件到内存
        System.out.println("tomcat启动时自动从文件读取推荐");
    }
    //当后台被销毁，即发生了tomcat关闭了事件，固定用法
    public void contextDestroyed(ServletContextEvent arg0){
        //关闭时需要执行的内容写在这里
        System.out.println("tomcat关闭");
    }
}
