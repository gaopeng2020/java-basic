package multhread;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author ：xxx
 * @date ：Created in 2021/2/17 17:57
 * @description：用Thread实现多线程下载图片
 * @modified By：
 * @version: 1.0$
 */
public class Threadtest extends Thread {
/*  自定义线程类继承Thread类
	重写run（）方法，编写线程执行体
	创建线程对象，调用start（）方法启动线程
	*/

    private String url;
    private String filePath;

    public Threadtest(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        webPicDownload(this.url, this.filePath);
        System.out.println(currentThread().getName() + "图片下载完成，图片名称为" + filePath);
    }

    public void webPicDownload(String url, String filePath) {
        try {
            FileUtils.copyURLToFile(new URL(url), new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String url1 = "http://img.desktx.com/d/file/wallpaper/scenery/20161209/b7899a7cb2d5f56872dc637ac0c38062.jpg";
        String url2 = "http://img17.3lian.com/d/file/201702/20/be942fe70a55bbc5ac24050ecca60414.jpg";
        String url3 = "https://img.zcool.cn/community/016ece5a28a9c6a801216e8dbb3a9a.jpg@1280w_1l_2o_100sh.jpg";
        String url4 = "https://img.zcool.cn/community/01936a58be7362a801219c77fe8358.jpg@1280w_1l_2o_100sh.jpg";
        String url5 = "https://www.benbenla.cn//images/20160903/benbenla-01b.jpg";

        new Threadtest(url1, "basic/src/main/resources/1.jpg").start();
        new Threadtest(url2, "basic/src/main/resources/2.jpg").start();
        new Threadtest(url3, "basic/src/main/resources/3.jpg").start();
        new Threadtest(url4, "basic/src/main/resources/4.jpg").start();
        new Threadtest(url5, "basic/src/main/resources/5.jpg").start();
    }

}
