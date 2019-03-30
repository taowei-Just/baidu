package baidu;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Baidu {
    public static void main(String[] args) {
        String url = "http://6668532.com";
        System.setProperty("webdriver.gecko.driver", "D:\\Program Files (x86)\\Mozilla Firefox\\geckodriver.exe");
        System.setProperty("webdriver.firefox.bin", "D:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
        FirefoxDriver chromeDriver = new FirefoxDriver();
        chromeDriver.get(url);
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
            chromeDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取当前浏览器的信息
        System.out.println("Title:" + chromeDriver.getTitle());
        System.out.println("currentUrl:" + chromeDriver.getCurrentUrl());
        System.out.println(" find click ui-dialog-close");
        WebElement element = null;
        clickClass(chromeDriver, "ui-dialog-close");

        System.out.println(" find input login-mane");
        element = chromeDriver.findElement(By.className("login-mane"));
        element = element.findElement(By.className("input_tip"));
        System.out.println(element.getAttribute("class"));
        element.sendKeys("wtw9604");

        System.out.println(" find input login-password");
        element = chromeDriver.findElement(By.className("login-password"));
        element = element.findElement(By.className("input_tip"));
        System.out.println(element.getAttribute("class"));
        element.sendKeys("953934cp");
        try {
            element = chromeDriver.findElement(By.id("validateCodeWrapJM2B"));
            element.click();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.err.println("请操作！");
        wait_(10);
        System.out.println("点击登录！");
        clickClass(chromeDriver, "btn-login");
        wait_(5);
        clickClass(chromeDriver, "ui-dialog-close");
//        clickClass(chromeDriver, "lottery");
//        clickClass(chromeDriver, "game_109");
//        Actions actions = new Actions(chromeDriver);
//        actions.moveToElement(chromeDriver.findElement(By.className("lottery")));
//        actions.perform();
        clickClass(chromeDriver, "btn_user");

        chromeDriver.manage().window().maximize();

        Actions actions = new Actions(chromeDriver);
        actions.moveToElement(chromeDriver.findElement(By.id("gameListBtn")));
        actions.perform();

//        clickClass(chromeDriver, "game_26");
        wait_(15);
        chromeDriver.findElement(By.linkText("VR金星1.5分彩")).click();

        clickId(chromeDriver, "regularBetType");


        chromeDriver.findElement(By.linkText("牛牛")).click();
        clickId(chromeDriver, "NiuNiuStud");
//
//        //执行js脚本
//        String jString = "alert('122')";
//        ((JavascriptExecutor) chromeDriver).executeScript(jString);
//
//        //获取单个元素
//        WebElement element = chromeDriver.findElement(By.id("wrapper"));
//        System.out.println(element.getAttribute("class"));
//
//        //获取多个元素
//        List<WebElement> elList = chromeDriver.findElements(By.tagName("input"));
//        for (WebElement e : elList) {
//            System.out.println("获取多个元素:"+e.getAttribute("name"));
//        }
//
//        //定位层级元素
//        WebElement e = chromeDriver.findElement(By.cssSelector("#qrcode-item qrcode-item-1"));
//        List<WebElement> list = e.findElements(By.tagName("div"));
//        for (WebElement e1 : list) {
//            System.out.println("定位层级元素:"+e1.getAttribute("class"));
//        }
//
//
//
//        //获取当前的窗口
//        String currentWindow = chromeDriver.getWindowHandle();
//
//        //获取所有的窗口
//        Set<String> handles = chromeDriver.getWindowHandles();
//
//        //遍历窗口
//        Iterator<String> iterator = handles.iterator();
//        while (iterator.hasNext()) {
//            if (currentWindow == iterator.next())
//                continue;
//            //跳转到弹出的窗口
//            WebDriver chromeDriver2 = chromeDriver.switchTo().window(iterator.next());
//            chromeDriver2.getTitle();
//
//        }
//
//
//        //处理弹出框
//        Alert alert = chromeDriver.switchTo().alert();
//        alert.getText();
//        alert.dismiss();//相当于点击取消
//
//
//        Alert confirm = chromeDriver.switchTo().alert();
//        confirm.getText();
//        confirm.accept();//相当于点击确认
//
//
//        Alert prompt = chromeDriver.switchTo().alert();
//        prompt.getText();
//        prompt.accept();
//        prompt.sendKeys("测试1");//输入值
//
//        //处理下拉列表
//        Select select = new Select(chromeDriver.findElement(By.id("select")));
//        select.selectByIndex(1);
//        select.selectByValue("西安");
//        select.selectByVisibleText("咸阳");
//        //获取下拉框的全部选项
//        List<WebElement> list2 = select.getOptions();
//        for (WebElement webElement : list2) {
//            webElement.click();//点击下拉框
//        }
//
//        //处理cookie
//        //添加一个cookie
//        Cookie cookie = new Cookie("COOKIE", "NAME","D://COOKIES");
//        chromeDriver.manage().addCookie(cookie);
//
//        //获取cookies
//        Set<Cookie> set = chromeDriver.manage().getCookies();
//        Iterator<Cookie> iterator2 = set.iterator();
//        while (iterator2.hasNext()) {
//            Cookie c = iterator2.next();
//            c.getName();
//            c.getValue();
//            c.getPath();
//
//        }
//
//        chromeDriver.manage().deleteAllCookies();
//        chromeDriver.manage().deleteCookie(cookie);
//        chromeDriver.manage().deleteCookieNamed("COOKIE");
//
//
//        //等待加载完页面
//        try {
//            chromeDriver.manage().timeouts().wait(1);
//            chromeDriver.manage().timeouts().implicitlyWait(1,TimeUnit.SECONDS);//等待界面加载完
//        } catch (InterruptedException e2) {
//
//            e2.printStackTrace();
//        }
//
//        //模拟鼠标和键盘操作
//        Actions action = new Actions(chromeDriver);
//        action.click();
//        action.dragAndDrop(element, e);
//        action.sendKeys(element,"12222").perform();
//        action.click(element);
//        action.keyDown(currentWindow);
//

        // chromeDriver.quit();// 退出浏览器

        /**
         * dr.quit()和dr.close()都可以退出浏览器,简单的说一下两者的区别：第一个close，
         * 如果打开了多个页面是关不干净的，它只关闭当前的一个页面。第二个quit，
         * 是退出了所有Webdriver所有的窗口，退的非常干净，所以推荐使用quit最为一个case退出的方法。
         */


    }

    private static void clickId(FirefoxDriver chromeDriver, String id) {
        chromeDriver.findElement(By.id(id)).click();
    }

    private static void wait_(int i) {

        try {
            for (int j = 0; j < i; j++) {
                System.out.println("wait:" + j + "s");
                Thread.sleep(1 * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void clickClass(FirefoxDriver chromeDriver, String game) {
        WebElement element;
        try {
            element = chromeDriver.findElement(By.className(game));
            System.out.println(element.getAttribute("class"));
            element.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
