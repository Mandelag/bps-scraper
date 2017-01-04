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
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 *
 * General web scraping utility.
 *
 * @author: Keenan Mandela Gebze
 * @version: 1.0.0
 *
 */
public class ScraperUtil {

    public static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";

    public static ArrayList<String[]> scrape(String regex, String input) throws IOException {
        ArrayList<String[]> result = new ArrayList<>();        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        int groupCount = matcher.groupCount();
        while(matcher.find()){
            String[] match = new String[groupCount];
            for(int i=0;i<groupCount;i++) {
                match[i] = matcher.group(i+1); //matcher group start at index 1, unlike array.
            }
            result.add(match);
        }
        return result;
    }

    public static String getString(URL url) throws IOException {
        URLConnection page = url.openConnection();
        page.addRequestProperty("User-Agent", USER_AGENT);
        InputStream is = page.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        
        String result = "";
        String line = "";
        while( (line = br.readLine()) != null) {
            result += line+"\r\n";
        }
        is.close();
        return result;
    }

    public static void streamCopier(InputStream is, OutputStream os) throws IOException {
        int b = 0;
        while((b = is.read()) >= 0 ) {
            os.write(b);
        }
    }
}
