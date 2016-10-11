package Conversion;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;

/** This class reads the gzipped file in the one-file-per-day-per-stock format 
 * that was described in class and can also be read in the R statistical
 * programming language. After that, we reformat the files to keep the format
 * of all files uniform. So we create a new [0-9]*_1.dat file based on each old
 * gzipped raw binary file.
 *
 */
public class Reformatgzip {
	
	// Header fields

		protected int _secsFromEpoch;
		protected int _nRecs;

	// Record fields
		
		protected int   [] _millisecondsFromMidnight;
		protected int   [] _size;
		protected float [] _price;

	// Get-ter methods for header fields

		public int getSecsFromEpoch () { return _secsFromEpoch; }
		public int getNRecs         () { return _nRecs;         }

	// Get-ter methods for record fields

		public int   getMillisecondsFromMidnight ( int index ) { return _millisecondsFromMidnight[ index ]; }
		public int   getSize                     ( int index ) { return _size[ index ];                     }
		public float getPrice                    ( int index ) { return _price[ index ];                    }
	
	/**
	 * Constructor - Opens a gzipped TAQ trades file and reads entire contents into memory.
	 * 
	 * @param filePathName Name of gzipped TAQ trades file to read
	 * @throws IOException 
	 */
	public Reformatgzip( String filePathName ) throws IOException {
		
		// Open file and get data input stream
		
			FileInputStream fileInputStream = new FileInputStream( filePathName );
			InputStream in = new GZIPInputStream( fileInputStream );
			DataInputStream dataInputStream = new DataInputStream( in );
			
		// Read and save header info

			_secsFromEpoch = dataInputStream.readInt();
			_nRecs = dataInputStream.readInt();
		
		// Allocate space for data
		
			_millisecondsFromMidnight = new int   [ _nRecs ];
			_size                     = new int   [ _nRecs ];
			_price                    = new float [ _nRecs ];
			
		// Read all records into memory
			
			for( int i = 0; i < _nRecs; i++ )
				_millisecondsFromMidnight[ i ] = dataInputStream.readInt();

			for( int i = 0; i < _nRecs; i++ )
				_size[ i ] = dataInputStream.readInt();

			for( int i = 0; i < _nRecs; i++ )
				_price[ i ] = dataInputStream.readFloat();

		// Finished reading - close the stream

			dataInputStream.close();

	}
	
	/**
	 * The method to rewrite the original gzip files to .dat files named using the format
	 * number_1.dat
	 * @param {@code File}
	 */
	public static void reformat(File onedate) {
		Mapper mapping=new Mapper(onedate);
        Map<String,Short> map=mapping.getMap();
		File[] directoryListing = onedate.listFiles();
		if (directoryListing != null && onedate.isDirectory()) {
		    for (File child : directoryListing ) {
		    	
		    	if (!child.getAbsolutePath().toLowerCase().endsWith(".binrt"))
		    		continue;
			    	
		    	try {
					
					// Read entire TAQ trades file into memory
                        
		    		Reformatgzip taqTrades = new Reformatgzip( child.getAbsolutePath() );
						
					// Iterate over all records, writing the contents of each to the console
						
						int nRecs = taqTrades.getNRecs();
						
						Random rand=new Random();
						int filename=rand.nextInt(10000000);
						//we want to check if the random number we generated is already used 
						//in some existing file. If this number is not found, then we can 
						//use it as our file name.
						File file=new File(onedate+"/"+filename+"_1.dat");
						while (file.exists()){
							
							filename=rand.nextInt();
							file=new File(onedate+"/"+filename+"_1.dat");
						}
						
						//Rewrite the old gzip files to .dat files named <some number>_1.dat for a singel day.
						DataOutputStream dataOut = new DataOutputStream(new FileOutputStream(onedate+"/"+filename+"_1.dat"));
						Path path=Paths.get(child.getAbsolutePath());
						//System.out.println(path.getFileName().toString());
						 System.out.println("Number of lines"+nRecs);
						for( int i = 0; i < nRecs; i++ ) {
//							System.out.println(
//								map.get(path.getFileName().toString())
//								+ ","	
//							    + taqTrades.getMillisecondsFromMidnight( i )
//								+ ","
//								+ taqTrades.getSize( i )
//								+ ","
//								+ taqTrades.getPrice( i )
//								+ ","
//								+ taqTrades.getSecsFromEpoch()
//							);
						
							dataOut.writeLong(taqTrades.getSecsFromEpoch());
							dataOut.writeShort(map.get(path.getFileName().toString()));
							dataOut.writeInt(taqTrades.getSize(i));
							dataOut.writeFloat(taqTrades.getPrice(i));
							
						}
						dataOut.flush();
						dataOut.close();
						 
//We can delete our gzip files once they are converted to .dat files
				//Files.delete(path);
						
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		    	
		    }
		    
		  } 
     
	}
	
	//Test for the reformatting.
	public static void main( String[] args ) {
		File dir = new File("/Users/senongtor/Downloads/sampleTAQ2/trades/");
		  File[] directoryListing = dir.listFiles();
		  
		  if (directoryListing != null && dir.isDirectory()) {
		    for (File child : directoryListing) {
		    	//We need to ignore ds_store files.
//		    	if (child.getAbsolutePath().toLowerCase().endsWith(".ds_store"));
//		    		{continue;}
		    	if(!child.isDirectory()){
		    		continue;
		    	}
		    	
		    		System.out.println(child.getAbsolutePath());
		    		reformat(child); 	
		    }
		    System.out.println("Written to memory and Reformation is finished!");
		  } 
		
	}
	
	
}

