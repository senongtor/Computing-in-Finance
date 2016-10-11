package Datreader;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
/**
 * Simple .datreader that will read any .dat files.
 * @author senongtor
 *
 */
public class Datreader {
	protected DataInputStream input;
	protected long[] _secondsFromEpoch;
	protected short[] _stockId;
	protected int[] _size;
	protected float[] _price;
	protected int _nRecs = 0;
	
	public long getSecondsFromEpoch(int index) {
		return _secondsFromEpoch[index];
	}

	public short getStockId (int index)   {
		return _stockId[ index ];
	}

	public int getSize(int index) {
		return _size[index];
	}

	public float getPrice(int index) {
		return _price[index];
	}
	public int getNRecs(){
		return _nRecs;
	}

	public Datreader(String path) throws IOException {
		
		try {
			input = new DataInputStream(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<Long> _secondsFromEpochtemp = new LinkedList<Long>();
		LinkedList<Short> _stockIdtemp = new LinkedList<Short>();
		LinkedList<Integer> _sizetemp = new LinkedList<Integer>();
		LinkedList<Float> _pricetemp = new LinkedList<Float>();

		boolean endOfFile = false;
		while (endOfFile == false) {

			try {
				long x = input.readLong();
				_secondsFromEpochtemp.addLast(x);
				short y = input.readShort();
				_stockIdtemp.addLast(y);
				int z = input.readInt();
				_sizetemp.addLast(z);
				float u = input.readFloat();
				_pricetemp.addLast(u);
				//System.out.println("Seconds " + x + " StockID " + y + " Size " + z + " Price " + u);
				_nRecs++;

			}

			catch (EOFException e) {
				endOfFile = true;
			}
		
		}
		_secondsFromEpoch = new long[_nRecs];
		_stockId = new short[_nRecs];
		_size = new int[_nRecs];
		_price = new float[_nRecs];
		for (int j = 0; j < _nRecs ; j++){
			_secondsFromEpoch[j] = _secondsFromEpochtemp.removeFirst();
		    _stockId[j] = _stockIdtemp.removeFirst();
		    _size[j] = _sizetemp.removeFirst();
		    _price[j] = _pricetemp.removeFirst();
		    }
		input.close();
		System.out.println(_nRecs + " lines in the file");
	}
//Some test
	public static void main(String[] args) {
		String f = "/Users/senongtor/Downloads/sampleTAQ2/trades/20070621/6987490_2.dat";
		try {
			Datreader reader = new Datreader(f);
			System.out.println(reader.getSize(1));
            //System.out.println(reader.getNRecs());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
