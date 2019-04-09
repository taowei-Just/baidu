package baidu.Ecai;

import baidu.bean.TicketInfo;
import baidu.utils.Elementutil;
import baidu.utils.Out;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainRunn implements Runnable {

    private RemoteWebDriver firefoxDriver;
    private TicketInfo info;
    List<String> windowIds = new ArrayList<>();
    Main main;
    private Elementutil elementutil;

    public MainRunn(TicketInfo info, Main main) {

        this.info = info;
        this.main = main;
    }

    @Override
    public void run() {
        waitIngOpen(info.openMarket, info.closeMarket);
        String url = "http://6618222.com";
        System.setProperty("webdriver.gecko.driver", "D:\\Program Files (x86)\\Mozilla Firefox\\geckodriver.exe");
        System.setProperty("webdriver.firefox.bin", "D:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");

        ChromeOptions options = new ChromeOptions();
// 设置禁止加载项
        Map<String, Object> prefs = new HashMap<String, Object>();
// 禁止加载js
        prefs.put("profile.default_content_settings.javascript", 2); // 2就是代表禁止加载的意思
// 禁止加载css
        prefs.put("profile.default_content_settings.images", 2); // 2就是代表禁止加载的意思

        options.addArguments("headless");
        options.addArguments("disable-infobars");
        options.setExperimentalOption("prefs", prefs);

        firefoxDriver = new FirefoxDriver();
        firefoxDriver.get(url);
        elementutil = new Elementutil(firefoxDriver);
        //driver.navigate().to(url); // 打开指定的网站        
        /*
         *
         * driver.findElement(By.id("kw")).sendKeys(new String[] { "hello" });//
         * 找到kw元素的id，然后输入hello driver.findElement(By.id("su")).click(); // 点击按扭
         */
        try {
            /**
             * WebDriver自带了一个智能等待的方法。 dr.manage().timeouts().implicitlyWait(arg0, arg1）；
             * Arg0：等待的时间长度，int 类型 ； Arg1：等待时间的单位 TimeUnit.SECONDS 一般用秒作为单位。
             */
            firefoxDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取当前浏览器的信息
        Out.e(info.tag, "Title:" + firefoxDriver.getTitle());
        Out.e(info.tag, "currentUrl:" + firefoxDriver.getCurrentUrl());
        System.err.println(firefoxDriver.getWindowHandle());
        windowIds.add(firefoxDriver.getWindowHandle());
        elementutil.wait_(1);
        elementutil.clickClass("ivu-modal-close");
        elementutil.sendTextByClass("username", info.account);
        elementutil.sendTextByClass("password", info.password);
        elementutil.clickClass("btn_log");
        Elementutil.wait_(1);
        elementutil.clickPath("/html/body/div/div/div/div[2]/div[2]/input[2]");
        switch (info.ticketKind) {
            case 1:
                WebElement element = firefoxDriver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div[1]/div[2]/div/ul/li[3]/a/span"));
                System.err.println(element.getText());
                if (element.getText().contains("VR彩票"))
                    element.click();
                firefoxDriver.close();

                Elementutil.wait_(1);
                Set<String> windowHandles = firefoxDriver.getWindowHandles();
                Iterator<String> iterator = windowHandles.iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    if (!windowIds.contains("next"))
                        windowIds.add(next);
                }

                firefoxDriver.switchTo().window(windowIds.get(windowIds.size() - 1));

                System.err.println("当前界面id：" + firefoxDriver.getWindowHandle());
                Elementutil.wait_(1);
                elementutil.waitDialog(firefoxDriver);
                elementutil.clickPath("/html/body/div[2]/div/div[1]/div[2]/div/div[1]/div/div/div[1]/a[5]/i");
                elementutil.clickId("regularBetType");
                elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[2]/ul/li[14]");
                elementutil.clickPath("//*[@id=\"NiuNiuStud\"]");

                DoPour doPour = new DoPour(firefoxDriver, info);
                doPour.start(4);
                break;
            case 2:
                elementutil.clickPath("/html/body/div[1]/div/div[1]/div/div[1]/div[2]/div/ul/li[2]/a/span");
                firefoxDriver.close();
                windowHandles = firefoxDriver.getWindowHandles();
                iterator = windowHandles.iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    System.err.println("" + next);
                    if (!windowIds.contains("next"))
                        windowIds.add(next);
                }
                firefoxDriver.switchTo().window(windowIds.get(windowIds.size() - 1));
                elementutil.wait_(1);
                elementutil.clickPath("/html/body/div[1]/div/div[1]/div/div/div/div[1]/div/ul/li[1]");

                break;
                case -1:
                      element = firefoxDriver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div[1]/div[2]/div/ul/li[3]/a/span"));
                    System.err.println(element.getText());
                    if (element.getText().contains("VR彩票"))
                        element.click();
                    firefoxDriver.close();

                    Elementutil.wait_(1);
                     windowHandles = firefoxDriver.getWindowHandles();
                     iterator = windowHandles.iterator();
                    while (iterator.hasNext()) {
                        String next = iterator.next();
                        if (!windowIds.contains("next"))
                            windowIds.add(next);
                    }

                    firefoxDriver.switchTo().window(windowIds.get(windowIds.size() - 1));

                    System.err.println("当前界面id：" + firefoxDriver.getWindowHandle());
                    Elementutil.wait_(1);
                    elementutil.waitDialog(firefoxDriver);
                    elementutil.clickPath("/html/body/div[2]/div/div[1]/div[2]/div/div[1]/div/div/div[1]/a[5]/i");
                    elementutil.clickId("regularBetType");
                    elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[2]/ul/li[14]");
                    elementutil.clickPath("//*[@id=\"NiuNiuStud\"]");

                  new DoPour2(firefoxDriver, info).start(4);
                    break; 
                    
                    case 3:
                      element = firefoxDriver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div[1]/div[2]/div/ul/li[3]/a/span"));
                    System.err.println(element.getText());
                    if (element.getText().contains("VR彩票"))
                        element.click();
                    firefoxDriver.close();

                    Elementutil.wait_(1);
                     windowHandles = firefoxDriver.getWindowHandles();
                     iterator = windowHandles.iterator();
                    while (iterator.hasNext()) {
                        String next = iterator.next();
                        if (!windowIds.contains("next"))
                            windowIds.add(next);
                    }

                    firefoxDriver.switchTo().window(windowIds.get(windowIds.size() - 1));
                    
                    System.err.println("当前界面id：" + firefoxDriver.getWindowHandle());
                    Elementutil.wait_(1);
                    elementutil.waitDialog(firefoxDriver);

                        Actions actions = new Actions(firefoxDriver);
                        actions.moveToElement(firefoxDriver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div[2]/div/div[1]/div/div/div[1]/a[21]")));
                        elementutil.clickPath("/html/body/div[2]/div/div[1]/div[2]/div/div[1]/div/div/div[1]/a[21]");
 
                    elementutil.clickId("regularBetType");
                    
                    
                    elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[1]/ul/li[14]/a");
                    elementutil.clickPath("//*[@id=\"NiuNiuStud\"]");

                  new DoPour_1_1(firefoxDriver, info).start(4);
                    break;
        }
    }

    private void waitIngOpen(String str, String end) {

        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date(System.currentTimeMillis()));
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.HOUR_OF_DAY, Integer.valueOf(str));
        long startTime = instance.getTimeInMillis();
        instance.set(Calendar.DAY_OF_YEAR, instance.get(Calendar.DAY_OF_YEAR) + 1);
        instance.set(Calendar.HOUR_OF_DAY, Integer.valueOf(end));
        long endTime = instance.getTimeInMillis();

        while (true) {
            if (System.currentTimeMillis() > startTime ||System.currentTimeMillis() < endTime) {
                Out.d("开盘时间到 开始启动。。");

                return;
            }
            Out.e(info.tag, "等待开盘：" + new Date(startTime) + "当前时间：" + new Date(System.currentTimeMillis()));

            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
