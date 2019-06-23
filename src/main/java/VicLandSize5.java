/**
 * Created by jingfeiyang on 17/2/28.
 */
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;


public class VicLandSize5 {
    public static void main(String[] args) throws Exception{

        String readfile2 = args[0];
        String writefile2 = args[1];

        String driver = args[2];

        Thread thread2 = new VicThread5(readfile2,writefile2,driver);

        thread2.start();


    }
}

class VicThread5 extends Thread
{
    public String readfrom;
    public String writeto;


    public String driverPath;


    public VicThread5(String readfrom,String writeto, String driverPath)
    {
        this.readfrom = readfrom;
        this.writeto = writeto;

        this.driverPath = driverPath;
    }

    public void run()
    {
        String readDirection = readfrom;
        String writeDirection = writeto;

///////////get the last line of the result file////////////////////////////
        int counter = 0;

        try
        {
            String nextLine0[];
            CSVReader reader = new CSVReader(new FileReader(writeDirection));
            while ((nextLine0 = reader.readNext()) != null) {
                counter = Integer.parseInt(nextLine0[11]);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("no result file");
            counter = 0;
        }



        ///////////////read part/////////////////////

        try
        {



            WebDriver driver ;

            System.setProperty("webdriver.chrome.driver", driverPath /*"/usr/local/bin/chromedriver"*/);

            //System.setProperty("webdriver.gecko.driver", driverPath /*"/usr/local/bin/chromedriver"*/);


            driver = new ChromeDriver();

            driver.manage().window().maximize();
            //driver = new FirefoxDriver();

            driver.get("http://maps.land.vic.gov.au/lassi/");

            Thread.sleep(10000);






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
                String year = nextLine[10];

                String newSize = "null";

                int counter1 = Integer.parseInt(nextLine[11]);


                sizeUnit = "m2";
                System.out.println(address);

                /////////////crawl information part/////////////////////


                if(counter1 == (counter + 1))
                {
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

                        mainpicture.click();

                        Thread.sleep(3500);

                        String source = driver.getPageSource();

                        try {
                            String theLandSize = source.substring(source.indexOf("Area:</td><td class=\"formCell\" align=\"left\"><div class=\"staticTextItem\" style=\"white-space:normal\">")+99,source.indexOf("<sup>2</sup>")-1);

                            System.out.println(theLandSize);

                            newSize = theLandSize;

                            ////////////////write part///////////////////////////////////

                            File CSVFile = new File(writeDirection);
                            Writer fileWriter = new FileWriter(CSVFile, true);
                            CSVWriter writer = new CSVWriter(fileWriter, ',');

                            String entry[] = {proID, proType, address, area, state, postcode, Lati, Lng, landSize, sizeUnit, year, counter1+"", newSize};

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

                            String entry[] = {proID, proType, address, area, state, postcode, Lati, Lng, landSize, sizeUnit, year, counter1+"", newSize};

                            writer.writeNext(entry);
                            writer.close();

                        }

                    }
                    catch (Exception e2)
                    {
                        e2.printStackTrace();
                        System.out.println("crawl informtion part wrong");
                        File CSVFile = new File(writeDirection);
                        Writer fileWriter = new FileWriter(CSVFile, true);
                        CSVWriter writer = new CSVWriter(fileWriter, ',');

                        String entry[] = {proID, proType, address, area, state, postcode, Lati, Lng, landSize, sizeUnit, year, counter1+"", newSize};

                        writer.writeNext(entry);
                        writer.close();
                    }


                    counter = counter + 1;

                    driver.navigate().refresh();

                    Thread.sleep(2000);
                }



            }

        }
        catch (Exception e1)
        {
            e1.printStackTrace();
            System.out.println("something wrong with the read part");
        }
    }
}

