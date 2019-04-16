package baidu.Ecai;

import baidu.M0401_JianK;
import baidu.M0424_Shan_Tiao;
import baidu.M04_Liang_Dui;
import baidu.bean.TicketInfo;
import baidu.utils.Out;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.push.PushClient;
import jiguang.jiguangtest.JpushTest;
import matchore.MatchCore;
import matchore.WaitTimeCall;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static jiguang.jiguangtest.JpushTest.appKey;
import static jiguang.jiguangtest.JpushTest.masterSecret;

/**
 * V1.0  -- 实现自动登录并开启 VR投注
 * V1.1  -- 增加多线程 多账号同时登录 投注 并对配置传递简化 优化下注与弹窗冲突
 * V1.2  -- 增加启动等待 开盘才开启监控
 */

public class Main {
    
    private RemoteWebDriver firefoxDriver;
    public static PushClient jpushClient = new PushClient(masterSecret, appKey);

    public static void pushAllMessage(String str) {
        try {
            jpushClient.sendPush(JpushTest.getPushPayload(str));
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

    public Main() {

    }


    public static void main(String[] args) {
        Main main = new Main();

        M0424_Shan_Tiao.main(new String[0]);
        try {
            Thread.sleep(2 * 30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        M0401_JianK.main(new String[0]);
        try {
            Thread.sleep(2 * 30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        M04_Liang_Dui.main(new String[0]);
    }

    public void start(List<Info> infos) {
        for (int i = 0; i < infos.size(); i++) {
            start(infos.get(i));
            try {
                Thread.sleep(2 * 30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start(TicketInfo info) {

        MatchCore.waitMarketOpen(info, new WaitTimeCall() {
            @Override
            public void onTimeOpen(long timeInMillis) {
                Out.e(" 已开盘  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeInMillis)));

                new Thread(new MainRunn(info, Main.this)).start();
            }

            @Override
            public void onNoTime(long timeInMillis) {
                Out.e(" 等待开盘 开盘时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeInMillis)));
            }
        });
       
    }


    private void win() {
        String lastissue = firefoxDriver.findElement(By.id("lastissue")).getText();
        WebElement element = firefoxDriver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[1]/div[4]/div"));
        WebElement lastdigit1 = element.findElement(By.id("lastdigit1"));
        WebElement lastdigit2 = element.findElement(By.id("lastdigit2"));
        WebElement lastdigit3 = element.findElement(By.id("lastdigit3"));
        WebElement lastdigit4 = element.findElement(By.id("lastdigit4"));
        WebElement lastdigit5 = element.findElement(By.id("lastdigit5"));
        Out.e("win", "第 " + lastissue + " 期 开奖号码为 " + (lastdigit1.getText() + lastdigit2.getText() + lastdigit3.getText() + lastdigit4.getText() + lastdigit5.getText()));
    }


    public static class Info extends TicketInfo {
    }
}
