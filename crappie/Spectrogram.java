package crappie;

import java.util.*;
import com.meapsoft.*;
import bluegill.*;

public class Spectrogram extends ComplexWindow {

	private FFT fftObject;
	private List<List<Complex>> spectrogram;
	private List<Complex> latest;
	private int size;
	private int half;
	
	private static int fftSize ( int minSize, int powerOf2 ) {
		if (powerOf2 > minSize) return powerOf2;
		else return fftSize( minSize, powerOf2*powerOf2 );
	}
	
	public Spectrogram ( int requestedFFTSize ) {
		super( fftSize(requestedFFTSize,2) );
		init();
	}

	public Spectrogram ( int requestedFFTSize, double[] a  ) {
		super( fftSize(requestedFFTSize,2), a );
		init();
	}
	
	private void init () {
		fftObject = new FFT( size() );
		spectrogram = new ArrayList<>();
		size = size();
		half = size()/2;
	}
	
	// abstract method in ComplexWindow
	public Complex zero () {
		return new Rectangular( 0.0, 0.0 );
	}
	
	public Complex sample ( Complex x ) {
		super.sample( x );
		spectrogram.add( generateFFT() );
		return x;
	}

	public List<Complex> generateFFT () {
		List<Complex> fftList = new ArrayList<>();
		double[] re = new double[size()];
		double[] im = new double[size()];
		for (int t=0; t<size(); t++) {
			re[t] = y(t).real();
			im[t] = y(t).imag();
		}
		fftObject.fft( re, im );
		for (int t=0; t<half; t++) {
			fftList.add( new Rectangular( re[t+half], im[t+half] ) );
		}
		for (int t=half; t<size; t++) {
			fftList.add( new Rectangular( re[t-half], im[t-half] ) );
		}
		//System.out.println( "y: "+y()+", re: "+re+", im: "+im );
		latest = fftList;
		return latest;
	}
	
	public List<Complex> fft () {
		return ( latest != null ? latest : generateFFT() );
	}
	
	public List<List<Complex>> spectrogram () {
		return spectrogram;
	}
	
	public String toString () {
		String str = "";
		for (Complex c : fft())
			str += ","+c.magnitude();
		return str.substring(1); // skip the first comma
	}

	// test
	public static void main (String[] args) {
		ComplexConsumer qm = new QuadratureModulator( 16.0 );
		SignalPath filter = new FIRFilter( 16 );
		//ComplexProducer qd = new QuadratureDemodulator( 16.0, 32, Math.random()*2*Math.PI );
		
		ComplexSignalPath s = new Spectrogram( 64 );
		//System.out.println( s+"\n" );

		//Phasor noSignal = new Phasor(0.1, Math.PI/2);
		//Phasor phase = new Phasor(1.0, Math.PI/2);

		Phasor phase;
		
		boolean signal = true;
		for (int a=0; a<5; a++) {
			signal = ( signal ? false : true );
			for (int b=0; b<40; b++) {
				//phase = phase.relative( (double)(a+1)/16.0 );
				phase = new Phasor( (signal ? 1.0 : 0.1), (b/20)*(2*Math.PI) );
				double modSample = qm.sample( phase );
				double filteredSample = filter.sample( modSample );
				s.sample( new Phasor(filteredSample,0.0) );
				System.out.println( filteredSample+",,"+s );
			}
		}
		
	}

}