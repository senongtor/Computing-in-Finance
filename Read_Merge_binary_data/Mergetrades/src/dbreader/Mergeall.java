package dbreader;

import java.io.File;
import Conversion.Reformatgzip;
/**
 * Final procedure of merging. For each data directory, we will first call the reformatgzip class 
 * and the reformat method to reformat our gzip files to .dat files with desired fields. 
 * Then we will do iterative merge where we merge all the _1.dat files to _2.dat files 2 or 3 at a time 
 * until we have no more files to merge.
 * @author senongtor
 *
 */
public class Mergeall {
	static final String ADDRESS="/Users/senongtor/Downloads/sampleTAQ2/trades/";
	//CHANGE THIS ADDRESS ACCORDINGLY
 public static void main(String[] args){
	 File _dir = new File(ADDRESS);
	  File[] directoryListing = _dir.listFiles();
	  
	  if (directoryListing != null && _dir.isDirectory()) {
	    for (File child : directoryListing) {
	    	//We need to ignore ds_store files.
//	    	if (child.getAbsolutePath().toLowerCase().endsWith(".ds_store"));
//	    		{continue;}
	    	if(!child.isDirectory()){
	    		continue;
	    	}
	    	
	    		System.out.println(child.getAbsolutePath());
	    		Reformatgzip.reformat(child); 	
	    }
	    System.out.println("Written to memory and Reformation is finished!");
	  
	 for(File child : directoryListing){
		 try {
			Mergeiterative merge=new Mergeiterative(child);
			merge.mergeandupdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 System.out.println("Merging finished! Check your directory!");
 }
}
}