package dbreader;

import java.io.IOException;

import Conversion.Reformatgzip;
import Datreader.Datreader;



/**
 * This class implements an I_DBReader interface for use in the DBManager class.
 * 
 * <P>
 * 1) A ReadGZippedTAQTradesFile class is used to read an entire day of data for
 * one stock into memory.
 * </P>
 *  
 * <P>
 * 2) This class allows a calling object to step through that data one chunk at
 * a time. A chunk is defined as a piece of data in which all of the records have
 * the same sequence number.
 * </P>
 * 
 * <P>
 * 3) After a chunk is read, this class allows a calling object to retrieve the
 * number of records read for that chunk, and the fields in each record.
 * </P>
 * 
 * @author Lee
 *
 */
public class DatDBReader implements I_DBReader {

	protected Datreader                _dat;
	protected int                      _recCount;     // Index into record array of underlying ReadGZippedTAQQuotesFile object
	protected int                      _nRecsRead;    // Number of records read in last read
	protected boolean                  _isFinished;
	protected long                     _lastSequenceNumberRead;
	
	public boolean isFinished() { return _isFinished; }
	
	public DatDBReader( String filePathName ) throws IOException {
		_dat=new Datreader(filePathName);
		_recCount = 0;
		_nRecsRead = 0;
		_isFinished = false;
		_lastSequenceNumberRead = 0;
	}
	
	@Override
	public int readChunk( long targetSequenceNum ) {
		
		if( _isFinished )
			return 0;
		
		// Iterate over records until we hit last record in data or we hit
		// a sequence number that is higher than the target sequence number
		
			int maxRecs = _dat.getNRecs();
			int i = _recCount;
			while( true ) {
				if( _recCount == maxRecs ) {
					_isFinished = true;
					break;
				}
				if( getSequenceNumber() > targetSequenceNum )
					break;
				_recCount++;
			}
			
		// Save the sequence number that was just read
			
			_lastSequenceNumberRead = targetSequenceNum;
			
		// Save number of records red and return it
			
			_nRecsRead = _recCount - i;
			return _nRecsRead;
			
	}

	// These methods give an external object access to the data that
	// was read in the last readChunk call
	
		public int getNRecsRead() { return _nRecsRead; }
		
		public long getSequenceNum( int i ) {
			return  _dat.getSecondsFromEpoch( _recCount - _nRecsRead + i );
		}
	
		public int getSize( int i ) {
			return _dat.getSize( _recCount - _nRecsRead + i );
		}
		
		public float getPrice( int i ) {
			return _dat.getPrice( _recCount - _nRecsRead + i );
		}

		public short getId(int i){
			return _dat.getStockId( _recCount - _nRecsRead + i );
		}
	/**
	 * Return the sequence number of the record that this reader is currently
	 * pointing to and will step over when it is asked to read all of the 
	 * records it has for this sequence number.
	 * 
	 * return Current sequence number of this reader
	 */
	@Override
	public long getSequenceNumber() {
		//long millisFromEpoch = _dat.getSecsFromEpoch() * 1000L;
//		long millisFromMidnight = _dat.getSecondsFromEpoch( _recCount );
//		long sequenceNumber = millisFromEpoch + millisFromMidnight;
//		return sequenceNumber;
		return _dat.getSecondsFromEpoch( _recCount );
	}

	/**
	 * Stop reading and close all files. In this case, there's nothing to do
	 * because everything has already been read into memory and the file has
	 * been closed.
	 */
	@Override
	public void stop() {
		// Nothing to do here because file was closed in _taq
	}

	/**
	 * Return the last sequence number for which records were successfully
	 * read by this reader.
	 */
	@Override
	public long getLastSequenceNumberRead() {
		return _lastSequenceNumberRead;
	}
	
	
	public static void main(String[] args){
		String f = "/Users/senongtor/Downloads/sampleTAQ2/trades/20070620/150333_1.dat";
		try {
			DatDBReader reader=new DatDBReader(f);
			for(int i=0;i<1000;i++){
			System.out.println(reader.getPrice(i++));
			if(reader.isFinished()){
				break;
			}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
