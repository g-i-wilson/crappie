package crappie;

import com.meapsoft.*;
import bluegill.*;

public class Spectrogram implements SignalPath {

	private FFT fft;

	public Spectrogram ( int fftSize ) {
		fft = new FFT( fftSize );
		// TODO: Complex class
	}

}