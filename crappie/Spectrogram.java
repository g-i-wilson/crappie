package crappie;

import java.util.*;
import com.meapsoft.*;
import bluegill.*;

public class Spectrogram extends ComplexWindow {

	private FFT fftObject;
	private List<List<Complex>> spectrogram;
	
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
	}
	
	// abstract method in ComplexWindow
	public Complex zero () {
		return new Phasor( 0.0, 0.0 );
	}
	
	public Complex sample ( Complex x ) {
		super.sample( x );
		spectrogram.add( fft() );
		return x;
	}

	public List<Complex> fft () {
		List<Complex> fftList = new ArrayList<>();
		double[] re = new double[size()];
		double[] im = new double[size()];
		for (int t=0; t<size(); t++) {
			re[t] = y(t).real();
			im[t] = y(t).imag();
		}
		fftObject.fft( re, im );
		for (int t=0; t<size(); t++) {
			fftList.add( new Rectangular( re[t], im[t] ) );
		}
		return fftList;		
	}
	
	public List<List<Complex>> spectrogram () {
		return spectrogram;
	}

}