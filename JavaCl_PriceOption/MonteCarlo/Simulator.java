package MonteCarlo;

import static org.bridj.Pointer.allocateFloats;

import java.util.Arrays;
import java.util.Random;

import org.bridj.Pointer;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLDevice;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem;
import com.nativelibs4java.opencl.CLPlatform;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;

public class Simulator {

	private final static double PROB = 0.96;
	private final static double ERROR = 0.1;
	private static double Y;
	static String optionName = "IBM";
	static int optionPeriod = 252;
	static double initialPrice = 152.35;
	static double interestRate = 0.0001;
	static double volatility = 0.01;
	static double strikePrice = 165;
	static String optionType = "Call";
	private double value;
	final int NUM = 1048576;
	private StatsCollector stat;

	public Simulator(Option option) {
		Y = InverseNormal.getY(PROB);
		boolean converge = false;
		stat = new StatsCollector();
		// Creating the platform which is out computer.
		CLPlatform clPlatform = JavaCL.listPlatforms()[0];
		// Getting the GPU device
		CLDevice device = clPlatform.getBestDevice();
		// CLDevice device = clPlatform.listGPUDevices(true)[0];
		// Verifing that we have the GPU device
		System.out.println("*** New device *** ");
		System.out.println("Vendor: " + device.getVendor());
		System.out.println("Name: " + device.getName());
		System.out.println("Type: " + device.getType());
		// Let's make a context

		CLContext context = JavaCL.createContext(null, device);
		// Lets make a default FIFO queue.
		CLQueue queue = context.createDefaultQueue();

		// Read the program sources and compile them
		// this kernel program takes two independent list of uniformly
		// distributed numbers a,b and generates two independent normally
		// distributed number lists out1,out2
		String src = "__kernel void normal_random_var(__global const float* a, __global const float* b, __global float* out1,__global float* out2, int n) \n"
				+ "{\n" + " #ifndef M_PI\n" + "#define M_PI 3.14159265358979323846\n" + "#endif\n"
				+ "    int i = get_global_id(0);\n" + "    if (i >= n)\n" + "        return;\n" + "\n"
				+ "    out1[i] = sqrt(-2*log(a[i]))*cos(2*M_PI*b[i]);\n"
				+ "    out2[i] = sqrt(-2*log(b[i]))*cos(2*M_PI*a[i]);\n" + "}";
		CLProgram program = context.createProgram(src);
		program.build();
		int count = 0;
		// We will try to run the kernel program once and get two batches of
		// random numbers. From there, we will calculate payouts and update
		// standard deviation, number of simulations and mean value accordingly
		// until stopping criteria is met.
		while (converge == false) {
			CLKernel kernel = program.createKernel("normal_random_var");

			final Pointer<Float> aPtr = allocateFloats(NUM), bPtr = allocateFloats(NUM);

			Random rand1 = new Random();
			Random rand2 = new Random();
			// Two independent list of uniformly distributed random variables
			for (int i = 0; i < NUM; i++) {
				aPtr.set(i, rand1.nextFloat());
				bPtr.set(i, rand2.nextFloat());
			}

			// Create OpenCL input buffers (using the native memory pointers
			// aPtr and bPtr) :
			CLBuffer<Float> a = context.createFloatBuffer(CLMem.Usage.Input, aPtr),
					b = context.createFloatBuffer(CLMem.Usage.Input, bPtr);

			// Create an OpenCL output buffer :
			CLBuffer<Float> out1 = context.createFloatBuffer(CLMem.Usage.Output, NUM);
			CLBuffer<Float> out2 = context.createFloatBuffer(CLMem.Usage.Output, NUM);
			kernel.setArgs(a, b, out1, out2, NUM);
			CLEvent event = kernel.enqueueNDRange(queue, new int[] { NUM });
			final Pointer<Float> cPtr1 = out1.read(queue, event);
            //Get the prices for the last day using the normal random variables
			//we get from GPU.
			StockGenerator generator = new StockGenerator(option);
			Call europeancall = new Call(option.strikePrice);

			for (int i = 0; i < NUM; ++i) {

				double temp = europeancall.getPayout(generator.getPrices(cPtr1.get(i)));
				stat.add(temp);
				if (Y * stat.getStdDv() / Math.sqrt(i) < ERROR && i > 1000) {
					System.out.println(i + " simulations to converge");
					converge = true;
					break;
				}

			}
			// If we've reached converge from the first batch, we will jump
			// out of the while loop.
			if (converge == true) {
				break;
			}
			// else, calculate results from the second batch and check
			// for the stop criteria.
			count += NUM;
			final Pointer<Float> cPtr2 = out2.read(queue, event);
			for (int i = 0; i < NUM; ++i) {

				double temp = europeancall.getPayout(generator.getPrices(cPtr2.get(i)));
				stat.add(temp);

				if (Y * stat.getStdDv() / Math.sqrt(i) < ERROR) {
					System.out.println(count + " simulations to converge");
					converge = true;
					break;

				}
			}
			count += NUM;
		}
		// The final desired value
		value = stat.getmean();

	}

	public double getprice(Option option) {

		return value * Math.exp(-option.interestRate * option.period);
	}

	public static void main(String[] args) {
		Option option = new Option.OptionBuilder().setInitialPrice(initialPrice).setInterest(interestRate)
				.setStrikePrice(strikePrice).setName(optionName).setVolatility(volatility).setDuration(optionPeriod)
				.setOptionType(optionType).build();
		Simulator sim = new Simulator(option);
		double finalprice = sim.getprice(option);
		System.out.format("%s option price for %s which will expire in %d days is: %s \n", option.optionType,
				option.name, option.period, finalprice);
	}

}
