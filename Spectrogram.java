package crappie;

import com.meapsoft.*;
import bluegill.*;

public class Spectrogram {

	private double[] re;
	private double[] im;
	private int size;

	private FFT fft;
	
	private Phasor zero;
	

	public Spectrogram ( Phasor p, int requestedSize ) {
		size = fftSize( requestedSize, 2 );
		List<Complex> window = Windows.window(size, Windows.Hann);
		fft = new FFT( size );
		re = new double[ size ];
		im = new double[ size ];
	}
	
	private static fftSize ( int minSize, int powerOf2 ) {
		if (powerOf2 > minSize) return powerOf2;
		else return fftSize( minSize, powerOf2*powerOf2 );
	}
	
	public List<Complex> fft () {
		for (int t=0; t<size(); t++) {
			
	}

}