/**
 * 
 */
package zz.maven.test;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * @author zhangle
 *
 */
public class SeleniumTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        WebDriver driver = new ChromeDriver(chromeOptions);
        try {
            // Navigate to Url
            driver.get("https://etax.zhejiang.chinatax.gov.cn/zjgfdacx/sscx/nsrztcx/nsrztcx.html");
        } finally {
            driver.quit();
        }
    }

}
