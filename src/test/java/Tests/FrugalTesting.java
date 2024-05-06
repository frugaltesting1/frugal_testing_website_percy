package Tests;

import io.percy.selenium.Percy;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

public class FrugalTesting {

    public WebDriver driver;
    public WebDriverWait wait;

    @BeforeTest
    public void Setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.frugaltesting.com/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='cl-dialog-close-icon']")));
        driver.findElement(
                By.xpath("//div[@class='cl-dialog-close-icon']")).click();
    }

    @Test
    public void PercyTest() {

        try {
            FileInputStream file = new FileInputStream("resources/Frugal_Testing.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet("Sheet1");
            Percy percy = new Percy(driver);

            for (Row row : sheet) {
                System.out.println(row.getPhysicalNumberOfCells());
                if (row.getPhysicalNumberOfCells()==0)
                    break;

                if (row.getRowNum() != 0) {
                    String urlName = row.getCell(0).getStringCellValue();
                    String url = row.getCell(1).getStringCellValue();
                    System.out.println(url);
                    driver.navigate().to(url);
                    percy.snapshot(urlName);
                }
            }
        } catch (IOException e) {
            System.out.println("Exception found for IOE or Warning Levels" + e);
        }
    }

    @AfterMethod
    public void TearDown() {
        driver.quit();
    }

}