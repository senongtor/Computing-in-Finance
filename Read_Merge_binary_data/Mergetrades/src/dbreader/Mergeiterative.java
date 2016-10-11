package dbreader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * I make a linkedlist out of the file directory with all the files I want to
 * merge. Now I want to merge files two at a time as described until there is a
 * giant .dat file left. But it's possible that the number of files is odd. So
 * if the amount is odd, I just merge the first 3 files, and merge 2 files at a
 * time throughout the list. And at every iteration when I've merged 2 or 3
 * files, these 2 or 3 old files will be deleted from the directory as I added
 * an operation to delete the old version files once a new version of the
 * concatenation is created in the Merger class.(Eg, 2_1.dat,4_1.dat=>2_2.dat,
 * then 2_1.dat and 4_1.dat will be deleted automatically.). And at one time
 * there are n number of _1.dat files, I will iterate over them all, do the
 * rewriting, and remove the 2 or 3 files that are already merged from the list.
 * 
 * @author senongtor
 *
 */
public class Mergeiterative {
	protected File[] tomerge;
	public Mergeiterative(File parentfile) throws Exception {
		tomerge = parentfile.listFiles();
	}
	public void mergeandupdate() throws Exception{
		Merger merger = new Merger();
		LinkedList<File> filelist = new LinkedList<File>();
		
		for (int i = 0; i < tomerge.length; i++) {
			if (!tomerge[i].getAbsolutePath().toLowerCase().endsWith(".dat"))
	    		continue;
			filelist.addLast(tomerge[i]);
		}
		//Use a temp linkedlist of file to add two files at a time for the merger
		LinkedList<File> tempmerge = new LinkedList<File>();
		int count = filelist.size();
		while (count > 1) {
			//if current number of files is even, then just merge 2 files at a time iteratively
			if (count % 2 == 0) {
				for (int i = 0; i < count; i = i + 2) {
					File _f = filelist.removeFirst();
					File _s = filelist.removeFirst();
					tempmerge.addFirst(_f);
					tempmerge.addLast(_s);
					merger.mergeTrades(tempmerge);
					filelist.addLast(merger.getmergedfile());
					//empty the temp linkedlist for next 2 files
					tempmerge.clear();
				}
			}
			//if current number of files is odd, then just merge the first 3 files, 
            //and merge 2 files at a time iteratively
			if (count % 2 != 0) {
				File _f = filelist.removeFirst();
				File _s = filelist.removeFirst();
				File _t = filelist.removeFirst();
				tempmerge.addLast(_f);
				tempmerge.addLast(_s);
				tempmerge.addLast(_t);

				merger.mergeTrades(tempmerge);

				filelist.addLast(merger.getmergedfile());
				tempmerge.clear();
				//when we've already merged 3 files in the front,
				//we can iteratively merge 2 files at a time until count reaches 1.
				if (count > 3) {
					for (int i = 3; i < count; i = i + 2) {
						File t1 = filelist.removeFirst();
						File t2 = filelist.removeFirst();
						tempmerge.addFirst(t1);
						tempmerge.addLast(t2);

						merger.mergeTrades(tempmerge);

						filelist.addLast(merger.getmergedfile());
						//empty the temp linkedlist for next 2 files
						tempmerge.clear();
					}
				}
			}
			//Each time, we decrease our count by 1/2 because we merge 2 files a time(there might be 
			//odd count but integer division works fine).
			count = count / 2;
		}
	}
	public static void main(String[] args){
		 String s="/Users/senongtor/Downloads/sampleTAQ2/trades/20070620"; 
		 File parentfile=new File(s);
		 try {
			Mergeiterative m=new Mergeiterative(parentfile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
