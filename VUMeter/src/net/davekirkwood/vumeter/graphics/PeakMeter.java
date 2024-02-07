package net.davekirkwood.vumeter.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class PeakMeter extends SoundGraph {

	private Color BG_COLOUR = Color.BLACK;
	private Color FG_COLOUR_1 = Color.GREEN;
	private Color FG_COLOUR_2 = Color.YELLOW;
	private Color FG_COLOUR_3 = Color.RED;
	
	double val = 0;
	
	public PeakMeter() {
		setPreferredSize(new Dimension(100,100));
	}
	
	@Override
	public synchronized void addDecibels(double db) {
		
		db = Math.abs(db);
		
		if(val > 0) {
			val-=0.001;
		}
		if(db > val) {
			val = db;
		}
		repaint();
	}
	
	
	public void paint(Graphics g) {
		sPaint(g);
	}
	
	private synchronized void sPaint(Graphics g) {
		
		BufferedImage i = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics ig = i.getGraphics();
		
		ig.setColor(BG_COLOUR);
		ig.fillRect(0, 0, getWidth(), getHeight());
	
		double tickWidth = getWidth() / SoundFrame.PEAK;
		
		ig.setColor(FG_COLOUR_3);
		int width = (int)(tickWidth * val);
		if(width > getWidth() * (1 - SoundFrame.RED_ZONE)) {
			ig.fillRect(0, 0, width, getHeight());
		}
		ig.setColor(FG_COLOUR_2);
		if(width > getWidth() * (1 - SoundFrame.YELLOW_ZONE)) {
			ig.fillRect(0, 0, (int)Math.min(getWidth() * (1 - SoundFrame.RED_ZONE), width), getHeight());
		}
		ig.setColor(FG_COLOUR_1);
		ig.fillRect(0, 0, (int)Math.min(getWidth() * (1 - SoundFrame.YELLOW_ZONE), width), getHeight());
		
		g.drawImage(i, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight(), null);
		
	}

}
