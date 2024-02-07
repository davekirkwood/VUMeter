package net.davekirkwood.vumeter.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class WaveGraph extends SoundGraph {

//	private Color BG_COLOUR = Color.BLACK;
	private Color FG_COLOUR = Color.WHITE;
	
	private Color BG_COLOUR_1 = Color.GREEN.darker().darker().darker();
	private Color BG_COLOUR_2 = Color.YELLOW.darker().darker().darker();
	private Color BG_COLOUR_3 = Color.RED.darker().darker().darker();
	
	private int freq;
	private int skip = 1;
	
	public WaveGraph(int freq) {
		this.freq = freq;
	}
	
	private List<Double> dbs = new ArrayList<Double>();
	
	public synchronized void addDecibels(double db) {

		if(skip-- == 0) {
			
			if(dbs.size() > getWidth()) {
				dbs.remove(0);
			}
			dbs.add(db);
			repaint();
			
			skip = freq;
		} else {
			if(dbs.size() > 0) {
				if(Math.abs(dbs.get(dbs.size()-1)) < Math.abs(db)) {
					dbs.remove(dbs.size()-1);
					dbs.add(db);
					repaint();
				}
			}
		}
		
	}
	
	public void paint(Graphics g) {
		sPaint(g);
	}
	
	private synchronized void sPaint(Graphics g) {
		
		BufferedImage i = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics ig = i.getGraphics();
		
		double redZoneHeight = getHeight() / 2 * SoundFrame.RED_ZONE;
		double yellowZoneHeight = (getHeight() / 2 * SoundFrame.YELLOW_ZONE) - redZoneHeight;

		ig.setColor(BG_COLOUR_1);
		ig.fillRect(0, 0, getWidth(), getHeight());

		ig.setColor(BG_COLOUR_2);
		ig.fillRect(0, (int)redZoneHeight, getWidth(), (int)yellowZoneHeight);
		ig.fillRect(0, (int)(getHeight() - redZoneHeight - yellowZoneHeight), getWidth(), (int)yellowZoneHeight);
		
		ig.setColor(BG_COLOUR_3);
		ig.fillRect(0, (int)(getHeight() - redZoneHeight), getWidth(), (int)redZoneHeight);
		ig.fillRect(0, 0, getWidth(), (int)redZoneHeight);


		int yCentre = getHeight() / 2;
		double tick = getHeight() / (2*SoundFrame.PEAK);
		
		int lastX = 0;
		int lastY = yCentre;
		
		ig.setColor(FG_COLOUR);
		
		for(int x=0; x<dbs.size(); x++) {
			double b = dbs.get(x);
			int y = yCentre + (int)(tick * b);
			ig.drawLine(lastX, lastY, x, y);
			lastX = x;
			lastY = y;
		}
		g.drawImage(i, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight(), null);
		
		
	}
	
}
