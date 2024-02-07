package net.davekirkwood.vumeter.graphics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SoundFrame extends JFrame {

	public static final double RED_ZONE = 0.1;
	public static final double YELLOW_ZONE = 0.5;
	
	public static final double PEAK = Math.log(128);
	
	List<SoundGraph> lSoundGraphs = new ArrayList<SoundGraph>();
	List<SoundGraph> rSoundGraphs = new ArrayList<SoundGraph>();
	
	public SoundFrame() {
		
		lSoundGraphs.add(new WaveGraph(1));
		lSoundGraphs.add(new WaveGraph(50));
		lSoundGraphs.add(new PeakMeter());
		
		rSoundGraphs.add(new WaveGraph(1));
		rSoundGraphs.add(new WaveGraph(50));
		rSoundGraphs.add(new PeakMeter());
		
		this.setLayout(new BorderLayout());
		
		JPanel wavePanel = new JPanel();
		wavePanel.setLayout(new GridLayout(2,2));
		wavePanel.add(lSoundGraphs.get(0));
		wavePanel.add(rSoundGraphs.get(0));
		wavePanel.add(lSoundGraphs.get(1));
		wavePanel.add(rSoundGraphs.get(1));
		
		this.add(wavePanel, BorderLayout.CENTER);
		
		JPanel peakPanel = new JPanel();
		peakPanel.setLayout(new GridLayout(1, 2));
		peakPanel.add(lSoundGraphs.get(2));
		peakPanel.add(rSoundGraphs.get(2));
		
		this.add(peakPanel, BorderLayout.SOUTH);

		this.setSize(1024, 800);
		this.setLocation(1980, 0);
		
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	public void addByte(byte left, byte right) {

		double l = getDecibels(left);
		double r = getDecibels(right);
		
		for(SoundGraph soundGraph : lSoundGraphs) {
			soundGraph.addDecibels(l);
		}
		
		for(SoundGraph soundGraph : rSoundGraphs) {
			soundGraph.addDecibels(r);
		}
	}
	
	private double getDecibels(byte b) {
		double decibel = b == 0 ? 0 : Math.log(Math.abs(b));
		if(b < 0) {
			decibel = -decibel;	
		}
		return decibel;
	}
	
}
