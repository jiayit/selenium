package us.codecraft.webmagic.samples;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestProcessor {
//    private static String[] strings = {"apple","恶心","脓","疮","尸体","血腥","密集恐惧","生吞","寄生虫","面包虫","蛇","色情","低俗.","性感","丝袜","情趣","诱惑","裸照","性挑逗","床照","胸","摸胸","乳房","臀","强奸","性侵","迷奸"};
    private static String[] strings = {""};
    private static  int index = 0;
    private static int i = 0;
    public static void main(String[] args) {
        while(index < strings.length) {
            System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");//chromedriver服务地址
            WebDriver driver =new ChromeDriver(); //新建一个WebDriver 的对象
            driver.get("https://www.google.com/imghp?hl=zh-cn");//打开指定的网站
            driver.findElement(By.id("lst-ib")).sendKeys(strings[index]);//找到kw元素的id，然后输入需要搜索的值
            driver.findElement(By.id("mKlEF")).click(); //点击按扭
            try {
                /**
                 * WebDriver自带了一个智能等待的方法。
                 */
                driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
                String  windowId = driver.getWindowHandle();
                driver.switchTo().window(windowId);
                int page = 0;
                while (page++ < 15) {


//                ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(false)", webElements.get(webElements.size() - 1));

                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
                    WebElement button = driver.findElement(By.id("smb"));
                    if (button.isEnabled() && button.isDisplayed()) {
                        button.click();
                    }
                    driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                    Thread.sleep(1000);
                }
                WebElement element = driver.findElement(By.xpath("//*[@id=\"rg_s\"]"));
                List<WebElement> webElements =  element.findElements(By.xpath("//div[@jscontroller =\"Q7Rsec\"]/a/img"));
                for (WebElement webElement : webElements) {
                    String imgUrl = webElement.getAttribute("data-src");
                    if (StringUtils.isNotBlank(imgUrl)) {
                        downLoadFromUrl(imgUrl);
                    }
                    else {
                        imgUrl = webElement.getAttribute("src");
                        if (StringUtils.isNotBlank(imgUrl) && imgUrl.startsWith("https://")) {
                            downLoadFromUrl(imgUrl);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /**
             * dr.quit()和dr.close()都可以退出浏览器,简单的说一下两者的区别：第一个close，
             * 如果打开了多个页面是关不干净的，它只关闭当前的一个页面。第二个quit，
             * 是退出了所有Webdriver所有的窗口，退的非常干净，所以推荐使用quit最为一个case退出的方法。
             */
            driver.quit();//退出浏览器
            index++;
        }
    }
        public static void downLoadFromUrl (String urlStr){
            String filePath = "D:\\工作\\googleimage\\"+ strings[index] +"\\";
            String fileName = i++ + ".jpg";

            try {
                URL url = new URL(urlStr);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                DataInputStream in = new DataInputStream(httpURLConnection.getInputStream());

                File file = new File(filePath);
                if (!file.exists()) file.mkdir();
                DataOutputStream out = new DataOutputStream(new FileOutputStream(filePath + fileName));

                byte[] buffer = new byte[4096];
                int count = 0;
                while ((count = in.read(buffer)) > 0) {
                    out.write(buffer, 0, count);
                }
                out.close();
                in.close();
                httpURLConnection.disconnect();
            }
            catch (IOException e) {
                e.printStackTrace();
            }


        }

}
