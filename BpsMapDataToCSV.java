import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class BpsMapDataToCSV {
	public static void main(String[] args) throws IOException{
		
		if (args.length < 1) {
			System.out.println("usage:\njava BpsMapDataToCSV <input-bps-json-array>");
			return;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Object> mapData = (ArrayList<Object>)mapper.readValue(new File(args[0]), ArrayList.class);
		
		System.out.println("regionId,regionName,pop2010");
		
		for(Object data : mapData ) {
			LinkedHashMap<String, Object> hashMap = (LinkedHashMap) data;
			
			//ArrayList<String> childIdWilayah = (ArrayList<String>) hashMap.get("childIdWilayah"); //found dupes
			LinkedHashMap<String, String> childNamaWilayah = (LinkedHashMap<String, String>)hashMap.get("childNamaWilayah");
			LinkedHashMap<String, String> childData = (LinkedHashMap<String, String>)hashMap.get("data");
						
			for(String childId : childNamaWilayah.keySet()) {
				String id = childId;
				String nama = childNamaWilayah.get(childId);
				String pop = childData.get(childId);
				System.out.printf("%s,%s,%s%n",id,nama,pop.split("\\.")[0]);
			}
		}
	}
}
