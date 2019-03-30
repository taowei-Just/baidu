package baidu.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Elementutil {
    public static String tag = "";
    RemoteWebDriver chromeDriver;

    public Elementutil(RemoteWebDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
    }

    public void clickId(String id) {
        try {
            chromeDriver.findElement(By.id(id)).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickText(String text) {
        chromeDriver.findElement(By.linkText(text)).click();
    }

    public static void wait_(int i) {
        try {
            Out.e(tag, "wait:" + i + "s");
            for (int j = 0; j < i; j++) {
                Thread.sleep(1 * 1000);
            }
        } catch (InterruptedException e) {
            Out.e(tag, "click error:" + e.toString());

        }

    }

    public void clickClass(String game) {
        WebElement element;
        try {
            element = chromeDriver.findElement(By.className(game));
            System.out.println(element.getAttribute("class"));
            element.click();
        } catch (Exception e) {
            Out.e(tag, "click error:" + e.toString());

        }
    }

    public void sendTextByClass(String className, String text) {
        try {
            chromeDriver.findElement(By.className(className)).sendKeys(text);
        } catch (Exception e) {
            e.printStackTrace();
            Out.e(tag, "click error:" + e.toString());
        }
    }

    public void clickPath(String s) {
        try {
            chromeDriver.findElement(By.xpath(s)).click();
        } catch (Exception e) {
            Out.e(tag, "click error:" + e.toString());

        }
    }

    public void sendTextByid(String s, String s1) {
        try {
            chromeDriver.findElement(By.id(s)).sendKeys(s1);
        } catch (Exception e) {
            Out.e(tag, "click error:" + e.toString());

        }
    }

    public void clickCssS(String s) {
        try {
            chromeDriver.findElement(By.cssSelector(s)).click();
        } catch (Exception e) {
            Out.e(tag, "click error:" + e.toString());
        }
    }

    public String readTextById(String walletAmount) {
        return chromeDriver.findElement(By.id(walletAmount)).getText();
    }

    public String readTextByPath(String path) {
        return chromeDriver.findElement(By.id(path)).getText();
    }

    public String readTextByClass(String clas) {
        return chromeDriver.findElement(By.className(clas)).getText();
    }

    public void waitDialog(RemoteWebDriver chromeDriver) {
        while (true) {
            try {

                Out.e("等待弹窗消失");
                WebElement time = chromeDriver.findElement(By.className("time"));
                String hour = time.findElement(By.id("hour")).getText();
                String minute = time.findElement(By.id("minute")).getText();
                String second = time.findElement(By.id("second")).getText();

                Out.e("截止下注还剩：" + hour + ":" + minute + ":" + second);
                if (minute.equals("01") && Integer.valueOf(second) > 25) {
                    for (int i = 0; i < 10; i++) {
                        WebElement element = chromeDriver.findElement(By.xpath("/html/body/div[5]/div/div/div[3]/button"));
                        if (element.isDisplayed()) {
                            element.click();
                        }
                        Elementutil.wait_(1);
                        if (!chromeDriver.findElement(By.xpath("/html/body/div[5]/div/div/div[3]/button")).isDisplayed())
                            return;
                    }

                }

                if (minute.equals("00") && Integer.valueOf(second) < 5) {
                    for (int i = 0; i < Integer.valueOf(second) + 6; i++) {

                        WebElement element = chromeDriver.findElement(By.xpath("/html/body/div[5]/div/div/div[3]/button"));
                        if (element.isDisplayed()) {
                            element.click();
                        }
                        Elementutil.wait_(1);
                        if (!chromeDriver.findElement(By.xpath("/html/body/div[5]/div/div/div[3]/button")).isDisplayed())
                            return;
                    }
                }
            } catch (Exception e) {
                
            }
            try {
                Thread.sleep(1*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
