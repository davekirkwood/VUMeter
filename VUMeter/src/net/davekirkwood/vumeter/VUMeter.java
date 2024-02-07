package net.davekirkwood.vumeter;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import net.davekirkwood.vumeter.graphics.SoundFrame;

public class VUMeter {

	static final long RECORD_TIME = 20000000;
	
	// format of audio file

	// the line from which audio data is captured
	TargetDataLine line;
	
	private SoundFrame frame;

	public VUMeter() {
		frame = new SoundFrame();
	}

	void start() {
		try {

			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			// checks if system supports the data line
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line not supported");
				System.exit(0);
			}
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start(); // start capturing

			System.out.println("Start capturing...");

			AudioInputStream ais = new AudioInputStream(line);

			System.out.println("Start recording...");

			byte[] buffer = new byte[2];
			while(true) {
				int b = ais.read(buffer);
//				for(byte bv : buffer) {
					frame.addByte(buffer[0], buffer[1]);
//				}
			}
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
   void finish() {
      line.stop();
      line.close();
      frame.dispose();
      System.out.println("Finished");
  }


	/**
	 * Defines an audio format
	 */
	AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 8;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		return format;
	}

	public static void main(String[] args) {
      final VUMeter recorder = new VUMeter();
      
      // creates a new thread that waits for a specified
      // of time before stopping
      Thread stopper = new Thread(new Runnable() {
          public void run() {
              try {
                  Thread.sleep(RECORD_TIME);
              } catch (InterruptedException ex) {
                  ex.printStackTrace();
              }
              recorder.finish();
          }
      });

      stopper.start();

      // start recording
      recorder.start();
	}

}
