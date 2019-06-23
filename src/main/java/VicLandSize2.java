import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

/**
 * Created by jingfeiyang on 17/1/23.
 */


public class VicLandSize2 {
    public static void main(String[] args) throws Exception{
//        String readfile = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/one.csv";
//        String writefile = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/oneresult.csv";

        String readfile2 = args[0]; //"/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/two.csv";
        String writefile2 = args[1];    //"/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/tworesult.csv";

//        String readfile3 = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/three.csv";
//        String writefile3 = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/threeresult.csv";
//
//        String readfile4 = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/test4.csv";
//        String writefile4 = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/testwrite4.csv";
//
//        String readfile5 = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/test5.csv";
//        String writefile5 = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/mingzhaofullvic/testwrite5.csv";
//
        String driver = args[2];

//        Thread thread1 = new VicThread2(readfile,writefile);

        Thread thread2 = new VicThread2(readfile2,writefile2,driver);

        //Thread thread3 = new VicThread2(readfile3,writefile3,driver);
//
//        Thread thread4 = new VicThread2(readfile4,writefile5);
//
//        Thread thread5 = new VicThread2(readfile5,writefile5);


//        thread1.start();
//
//        Thread.sleep(10000);

        thread2.start();

//        Thread.sleep(10000);

//        thread3.start();
//
//        Thread.sleep(10000);
//
//        thread4.start();

//        Thread.sleep(10000);
//
//        thread5.start();

    }
}

class VicThread2 extends Thread
{
    public String readfrom;
    public String writeto;


    public String driverPath;


    public VicThread2(String readfrom,String writeto, String driverPath)
    {
        this.readfrom = readfrom;
        this.writeto = writeto;

        this.driverPath = driverPath;
    }

    public void run()
    {
        String readDirection = readfrom;
        String writeDirection = writeto;


        ///////////////read part/////////////////////

        try
        {



            WebDriver driver ;

            System.setProperty("webdriver.chrome.driver", driverPath /*"/usr/local/bin/chromedriver"*/);

            //System.setProperty("webdriver.gecko.driver", driverPath /*"/usr/local/bin/chromedriver"*/);


            driver = new ChromeDriver();

            //driver = new FirefoxDriver();

            driver.get("http://maps.land.vic.gov.au/lassi/");

            Thread.sleep(5000);






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



                //driver = new HtmlUnitDriver();



//                System.setProperty("phantomjs.binary.path","/usr/local/bin/phantomjs");
//
//                driver = new PhantomJSDriver();



                //System.out.println(driver.getTitle());



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

                    Thread.sleep(3500);

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

//                Thread.sleep(1000);
//
//                List<WebElement> searchButtonIs0 = driver.findElements(By.className("tabTitle"));
//
//                searchButtonIs0.get(0).click();
//
//                Thread.sleep(1000);
//
//
//                List<WebElement> clearButtonIs1 = driver.findElements(By.className("buttonTitle"));
//
//                clearButtonIs1.get(1).click();
//
//                Thread.sleep(500);
//
//
//                Thread.sleep(1000);
//
//                WebElement menu2 = driver.findElement(By.className("menuButton"));
//
//
//
//                menu2.click();
//
//                Thread.sleep(1000);
//
//                WebElement geography2 = driver.findElement(By.xpath("//*[contains(text(), 'GDA94 Geographicals')]"));
//
//                geography2.click();
//
//                Thread.sleep(1000);

                driver.navigate().refresh();

                Thread.sleep(2000);

                //driver.quit();

            }

        }
        catch (Exception e1)
        {
            e1.printStackTrace();
            System.out.println("something wrong with the read part");
        }
    }
}
