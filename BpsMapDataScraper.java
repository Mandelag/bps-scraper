import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;

import java.net.URL;
import java.net.URLConnection;

import java.util.ArrayList;


/**
 *
 * Scraping administrative region data from BPS census site (sp2010.bps.go.id).
 *
 * Steps:
 *  1) Get the region ids.
 *  2) Download the data by iterating over it.
 *
 * @author: Keenan Mandela Gebze
 * @version: 1.0.3
 *
 */
public class BpsMapDataScraper {
    
    public static void main( String[] args ) throws IOException {

        String SCRAPE_REGION_ID_URL = "https://sp2010.bps.go.id/index.php/navigation/wilayah";
        String htmlString = ScraperUtil.getString(new URL(SCRAPE_REGION_ID_URL)); // download a page that contain region ids.
        String regex = "id.([0-9]{10}).wilayah.(.*)..style";
        ArrayList<String[]> scrapes = new ArrayList<>();
        scrapes = ScraperUtil.scrape(regex, htmlString); // scrape the region ids, put them into a list.
        
        String SCRAPE_MAPDATA_URL = "https://sp2010.bps.go.id/index.php/site/mapdata?map=%d&wilayah=%s"; //
        
        /* all of these code are derived form loadMapData() function in province/regency page.*/
        int POPULATION_COUNT_CODE = 4; // or 19
        int SEX_RATIO_CODE = 74; // or 20
        int POPULATION_DENSITY_CODE = 10; // province only
        int POPULATION_GROWTH_VELOCITY_CODE = 11; // province only
        int POPULATION_DISTRIBUTION_CODE = 21; //regency only
        
        
        /* We will download ONLY the population count data for reason I'll explain in the blog! */
        
        File dir = new File("region-data-4");
        dir.mkdir();

        for(String[] scrape : scrapes) { // iterate over list of region ids.
            String regionId = scrape[0];
            String fileLoc = String.format("region-data-4/%s.json",regionId);
            System.out.print("Downloading "+fileLoc+"..");

            URL dataUrl = new URL(String.format(SCRAPE_MAPDATA_URL, POPULATION_COUNT_CODE, regionId));
            URLConnection dataConnection = dataUrl.openConnection();
            dataConnection.addRequestProperty("User-Agent", ScraperUtil.USER_AGENT);
            InputStream in = dataConnection.getInputStream();            

            File dataOutput = new File(fileLoc);
            OutputStream out = new FileOutputStream(dataOutput);


            ScraperUtil.streamCopier(in, out); // download the data.

            in.close();
            out.close();
            System.out.println(" Done");
        }
    }
}
