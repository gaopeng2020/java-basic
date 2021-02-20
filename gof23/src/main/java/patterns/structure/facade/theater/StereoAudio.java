package patterns.structure.facade.theater;

//立体声音响
public class StereoAudio {

	private static StereoAudio instance = new StereoAudio();

	public static StereoAudio getInstance() {
		return instance;
	}

	public void on() {
		System.out.println(" Stereo on ");
	}

	public void off() {
		System.out.println(" Screen off ");
	}

	public void up() {
		System.out.println(" Screen up.. ");
	}

	// ...
}
