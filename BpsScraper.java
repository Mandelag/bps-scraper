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
public class BpsScraper {
    
    public static void main( String[] args ) throws IOException {

        String SCRAPE_REGION_ID_URL = "https://sp2010.bps.go.id/index.php/navigation/wilayah";
        String GML_DIRECTORY_URL = "https://sp2010.bps.go.id/assets/50cfb262/gml";
        ArrayList<String[]> scrapes = new ArrayList<>();
        
        String htmlString = ScraperUtil.getString(new URL(SCRAPE_REGION_ID_URL)); // download a page that contain region ids.
        String regex = "id.([0-9]{10}).wilayah.(.*)..style";

        scrapes = ScraperUtil.scrape(regex, htmlString); // scrape the region ids, put them into a list.

        File dir = new File("kabupaten");
        dir.mkdir();
        dir = new File("provinsi");
        dir.mkdir();

        for(String scrape[] : scrapes) { // iterate over list of region ids.
            
            String regionId = scrape[0];
            
            String outputDir = "kabupaten";
			
            if("00".equals(regionId.substring(2,4))) {
                outputDir = "provinsi";
            }

            URL gmlFileURL = new URL(GML_DIRECTORY_URL+"/"+regionId+".gml");
            URLConnection gmlConn = gmlFileURL.openConnection();
            gmlConn.addRequestProperty("User-Agent", ScraperUtil.USER_AGENT);
            InputStream in = gmlConn.getInputStream();            

            File gmlOutput = new File(outputDir+"/"+regionId+".gml");
            OutputStream out = new FileOutputStream(gmlOutput);

            ScraperUtil.streamCopier(in, out); // download the data.

            in.close();
            out.close();
        }
    }
}
