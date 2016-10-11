package Conversion;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/***
 * Class to match a integer to a ticker. Scan one date directory to map all
 * the distinct tickers to some integers.
 * 
 * @author senongtor
 *
 */
public class Mapper {
	private Map<String, Short> pairmap;

	public Mapper(File dir) {
		pairmap = new HashMap<String, Short>();

		File[] directoryListing = dir.listFiles();
		short s = 1;
		if (directoryListing != null && dir.isDirectory()) {
			for (File child : directoryListing) {
				Path path = Paths.get(child.getAbsolutePath());
				// if the file is .ds_store then we ignore it.
				if (!child.getAbsolutePath().toLowerCase().endsWith(".binrt"))
					continue;

				pairmap.put(path.getFileName().toString(), s++);
			}
		}

	}

	public Map<String, Short> getMap() {

		return pairmap;

	}

	// Test
	public static void main(String[] args) {
		File s=new File("/Users/senongtor/Downloads/sampleTAQ2/trades/20070620");
		Mapper mapper = new Mapper(s);

		Map<String, Short> map = mapper.getMap();
		for (Map.Entry<String, Short> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " and " + entry.getValue());
		}
	}
}
