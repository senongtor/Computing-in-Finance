package dbreader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;



public class Merger {
    protected String outputFilePathName;
	protected DataOutputStream outputFile;
	protected DatDBReader[] readerlist;
	protected LinkedList<File> files;
	int filenumber=0;
	/**
	 * Method to merge multiple files. You can merge a list of files, not just 2 or 3 files. 
	 * For the sake of this hw, we will just merge a list of 2 or 3 files, i.e. the linkedlist 
	 * parameter should only have size 2 or 3.
	 * @throws Exception
	 */
	public void mergeTrades(LinkedList<File> files) throws Exception {
		this.files=files;
		filenumber=files.size();
		readerlist=new DatDBReader[files.size()];
		//Instatiate DatDBReader objects to be put in the LinkedList<I_DBReader>
		//for each file. Put akk the DatDBReader in an array for accessing iteratively.
		for(int i=0;i<files.size();i++){
			String tradesPathName = files.get(i).getAbsolutePath();
			final DatDBReader readertemp = new DatDBReader( tradesPathName );
			readerlist[i]=readertemp;
		}
		//We want to get the version number and update it accordingly.
		String tradesPathName1 = files.getFirst().getAbsolutePath();
		Path p = Paths.get(tradesPathName1);
		String file = p.getFileName().toString();
		//get the version number (Eg: 2 in 123_2.dat)
		int version_before =Character.getNumericValue(file.charAt(file.length()-5));
		int version_after = version_before+1;
		// Put both quotes readers into a list
		LinkedList<I_DBReader> readers = new LinkedList<I_DBReader>();
        for(int i=0;i<files.size();i++){
        	readers.addLast(readerlist[i]);
        }
		

		// Define a .dat output stream for merged data
		// Name the new file with the version number increased by 1.
		// The name(which is some random number)can stay as it was because 
		// we only care about the version(E.g. 123_1.dat,11_1.dat=>11_2.dat)
		outputFilePathName = tradesPathName1.replace("_"+version_before, "_"+version_after);
		outputFile = new DataOutputStream(new FileOutputStream( outputFilePathName ));
			
		// Instantiate merge processor, telling it where to write merged output of two quotes files
		I_DBProcessor mergeProcessor = new I_DBProcessor() {
			
			/**
			 * This is where we do something with both readers
			 */
			@Override
			public boolean processReaders(
				long sequenceNumber,
				int  numReadersWithNewData,
				LinkedList<I_DBReader> readers
			) {
				// Note that, unlike the join example that follows
				// we do not need to have data for both readers. We
				// need data for one, the other, or both.
				
				for(int j=0;j<filenumber;j++){
					if( readerlist[j].getLastSequenceNumberRead() == sequenceNumber) {
						int nRecs = readerlist[j].getNRecsRead();
						try {
							for( int i = 0; i < nRecs; i++ ) {
								//outputFile.writeInt( id1 );
								outputFile.writeLong( readerlist[j].getSequenceNum( i ) );
								outputFile.writeShort( readerlist[j].getId(i));
								outputFile.writeInt( readerlist[j].getSize( i ) );
								outputFile.writeFloat( readerlist[j].getPrice( i ) );
							}
						} catch (IOException e) {
							return false;
						}
					}
				}
				return true;
				
			}
			
			/**
			 * Close output files
			 * @throws IOException 
			 */
			@Override
			public void stop() throws Exception {
				outputFile.flush();
				outputFile.close();
				
			}
			
		}; // End of new I_DBProcessor(...) {...}
		
		// Create a list of processors, which, in this case, will
		// contain only one processor, the one we created above
		LinkedList<I_DBProcessor> processors = new LinkedList<I_DBProcessor>();
		processors.add( mergeProcessor );
			
		// Create a merge clock
		MergeClock clock = new MergeClock( readers, processors );
		
		// Hand all of the readers, processors, and clock to the DBManager
		DBManager dbm = new DBManager( readers, processors, clock );
	
		// Launch the DBManager
		dbm.launch();
        Iterator<File> it=files.iterator();
        while (it.hasNext()){
        	Path path=Paths.get(it.next().getAbsolutePath());
			Files.delete(path);
        }
}
	//We want to track the files that get merged into newer version.
	public File getmergedfile(){
		File temp=new File(outputFilePathName);
    	return temp;
    }
	
	//test
	public static void main(String[] args){
		Merger m=new Merger();
		try {
			File _b=new File("/Users/senongtor/Downloads/sampleTAQ2/trades/20070621/104734_1.dat");
			File _c=new File("/Users/senongtor/Downloads/sampleTAQ2/trades/20070621/2969338_1.dat");
			
			LinkedList<File> integrated=new LinkedList<File>();
			integrated.add(_b);
			integrated.add(_c);
			m.mergeTrades(integrated);
			System.out.println(m.getmergedfile().getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
