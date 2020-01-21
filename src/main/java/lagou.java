import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName lagou
 * @Description TOOO
 * @Authot 胡小宇
 * @Date 2019/8/9 16:30
 * @Vrrsion 1.0
 **/
//方法缺陷变量不灵活 爬取页面单一
public class lagou {
    public static void main(String[] args) {
        //设置webdriver 路径  根据浏览器 版本 操作系统的不同选择对应驱动 https://npm.taobao.org/mirrors/chromedriver/
        System.setProperty("webdriver.chrome.driver", lagou.class.getClassLoader().getResource("chromedriver.exe").getPath());
        //创建webdriver
        WebDriver webDriver = new ChromeDriver();
        //跳转目标页面
        webDriver.get("https://www.lagou.com/jobs/list_%E6%B5%8B%E8%AF%95%E5%B7%A5%E7%A8%8B%E5%B8%88?px=default&city=%E5%85%A8%E5%9B%BD#filterBox");

        //获取节点
        setOption(webDriver);

        //解析页面元素
        new lagou().paing(webDriver);
    }

    //设置节点变量
    private static void setOption(WebDriver webDriver) {
        clickOption(webDriver, "工作经验", "3-5年");
        clickOption(webDriver, "学历要求", "大专");
        clickOption(webDriver, "融资阶段", "不限");
        clickOption(webDriver, "公司规模", "不限");
        clickOption(webDriver, "行业领域", "电商");
    }

    List<Map<String,Object>> jobList = new ArrayList<Map<String,Object>>();

    //获取页面数据 递归分页方法
    private void paing(WebDriver webDriver) {
        List<WebElement> jobElements = webDriver.findElements(By.className("con_list_item"));


        for (WebElement jobElement : jobElements) {
            //MapList companyMap =new MapList();
            Map<String, Object> companyMap = new HashMap<String, Object>();
            String moneyName = jobElement.findElement(By.className("position")).findElement(By.className("money")).getText();
            String companyName = jobElement.findElement(By.className("company_name")).getText();
           /* companyMap.setJobmoney(moneyName);
            companyMap.setJobname(companyName);*/
            companyMap.put("moneyName",moneyName);
            companyMap.put("companyName",companyName);
            jobList.add(companyMap);
        }


        WebElement pageNext = webDriver.findElement(By.className("pager_next"));
        //递归点击
        if (!pageNext.getAttribute("class").contains("pager_next pager_next_disabled")) {
            pageNext.click();
            //插件bug需要线程睡眠1秒
            try {
                Thread.sleep(1000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            paing(webDriver);
        } else {
            for(Map<String,Object> jobListItem:jobList){
                System.out.println(jobListItem);
            }
        }
    }

    //模拟人为操作点击节点
    private static void clickOption(WebDriver webDriver, String choseTitle, String optionTitle) {
        WebElement chosenElement = webDriver.findElement(By.xpath("//li[@class='multi-chosen']//span[contains(text(),'" + choseTitle + "')]"));
        WebElement optionElement = chosenElement.findElement(By.xpath("../a[contains(text(),'" + optionTitle + "')]"));
        optionElement.click();
    }




}
