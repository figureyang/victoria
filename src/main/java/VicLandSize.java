import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.lift.find.TableCellFinder;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

/**
 * Created by jingfeiyang on 17/1/21.
 */
public class VicLandSize {
    public static void main(String[] args) throws Exception{
        String readfile1 = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/2017_1_25/1_10000.csv";
        String writefile1 = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/2017_1_25/1_10000result.csv";
        String readfile2 = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/2017_1_25/10001_20000.csv";
        String writefile2 = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/2017_1_25/10001_20000result.csv";

        Thread thread1 = new VicThread(readfile1,writefile1);
        Thread thread2 = new VicThread(readfile2,writefile2);

        thread1.start();
        Thread.sleep(3000);
        thread2.start();

    }
}

class VicThread extends Thread
{
    public String readfrom;
    public String writeto;


    public VicThread(String readfrom,String writeto)
    {
        this.readfrom = readfrom;
        this.writeto = writeto;
    }

    public void run()
    {
        String readDirection = readfrom;
        String writeDirection = writeto;


        ///////////////read part/////////////////////

        try
        {
            String nextLine[];
            CSVReader reader = new CSVReader(new FileReader(readDirection));
            while((nextLine=reader.readNext())!=null)
            {
                String proID = nextLine[0];
                String proType = nextLine[1];
                String address = nextLine[2];
                String area = nextLine[3];
                String state = nextLine[4];
                String postcode = nextLine[5];
                String Lati = nextLine[6];
                String Lng = nextLine[7];
                String landSize = nextLine[8];
                String sizeUnit = nextLine[9];


                sizeUnit = "m2";
                System.out.println(address);

                /////////////crawl information part/////////////////////

                WebDriver driver ;

                //driver = new HtmlUnitDriver();

                System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

                driver = new ChromeDriver();

//                System.setProperty("phantomjs.binary.path","/usr/local/bin/phantomjs");
//
//                driver = new PhantomJSDriver();

                driver.get("http://maps.land.vic.gov.au/lassi/");

                //System.out.println(driver.getTitle());

                Thread.sleep(5000);

                try
                {

                    WebElement coordinate = driver.findElement(By.id("isc_93"));
                    coordinate.click();
                    //System.out.println("already click the coordinate");

                    Thread.sleep(1000);

                    WebElement menu = driver.findElement(By.className("menuButton"));

                    menu.click();

                    Thread.sleep(1000);

                    WebElement geography = driver.findElement(By.xpath("//*[contains(text(), 'GDA94 Geographicals')]"));

                    geography.click();

                    Thread.sleep(1000);

                    WebElement latitude = driver.findElement(By.id("isc_BN"));

                    latitude.sendKeys(Lati);

                    WebElement longitude = driver.findElement(By.id("isc_BR"));

                    longitude.sendKeys(Lng);

                    WebElement searchButton = driver.findElement(By.id("isc_CB"));

                    searchButton.click();

                    Thread.sleep(1000);

                    WebElement identify_Property = driver.findElement(By.name("isc_3Uicon"));

                    identify_Property.click();

                    Thread.sleep(1000);

                    WebElement mainpicture = driver.findElement(By.id("OpenLayers_Layer_Vector_16_svgRoot"));

//                    Actions actions = new Actions(driver);
//
//                    actions.moveToElement(mainpicture).click();

                    mainpicture.click();

                    Thread.sleep(5000);

                    String source = driver.getPageSource();

                    try {
                        String theLandSize = source.substring(source.indexOf("Area:</td><td class=\"formCell\" align=\"left\"><div class=\"staticTextItem\" style=\"white-space:normal\">")+99,source.indexOf("<sup>2</sup>")-1);

                        System.out.println(theLandSize);

                        landSize = theLandSize;

                        //System.out.println(source);

                        //List<WebElement> tbodys = driver.findElements(By.className("staticTextItem"));

                        //List<WebElement> tbodys = driver.findElements(By.tagName("div"));

//                    List<WebElement> tbodys = driver.findElements(By.className("lassiPropertyFrom"));
//
//                    for(WebElement tbody : tbodys)
//                    {
//
//                        System.out.println(tbody.findElements(By.tagName("tr")).get(5).getText());
//                    }

                        //System.out.println();



                        ////////////////write part///////////////////////////////////

                        File CSVFile = new File(writeDirection);
                        Writer fileWriter = new FileWriter(CSVFile, true);
                        CSVWriter writer = new CSVWriter(fileWriter, ',');

                        String entry[] = {proID,proType,address,area, state, postcode,Lati,Lng, landSize,sizeUnit};

                        writer.writeNext(entry);
                        writer.close();

                    }

                    catch (Exception e3)
                    {
                        e3.printStackTrace();
                        System.out.println("get information part wrong");

                        File CSVFile = new File(writeDirection);
                        Writer fileWriter = new FileWriter(CSVFile, true);
                        CSVWriter writer = new CSVWriter(fileWriter, ',');

                        String entry[] = {proID,proType,address,area, state, postcode,Lati,Lng, landSize,sizeUnit};

                        writer.writeNext(entry);
                        writer.close();

                    }







                }
                catch (Exception e2)
                {
                    e2.printStackTrace();
                    System.out.println("crawl informtion part wrong");
                }


                //System.setProperty()

                driver.quit();

            }

        }
        catch (Exception e1)
        {
            e1.printStackTrace();
            System.out.println("something wrong with the read part");
        }
    }
}
