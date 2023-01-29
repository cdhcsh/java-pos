package others;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class drawChart extends JPanel{
	public static final int CNT = 9;
	Vector<Integer> datav = null;
	Vector<Integer> yv = null;
	public void paint(Graphics g) {
		super.paint(g);
		
		g.setColor(Color.LIGHT_GRAY);
		for(int i = 0 ; i<CNT ; i++) {
			g.drawLine(10,(getHeight()-20)/(CNT+1)*(i+1) + 10, getWidth()-10, (getHeight()-20)/(CNT+1)*(i+1) + 10);
			g.drawLine((getWidth()-20)/(CNT+1)*(i+1) + 10,10,(getWidth()-20)/(CNT+1)*(i+1) + 10,getHeight()-10);
		}
		g.setColor(Color.black);
		
		g.drawLine(10, getHeight()-10, getWidth()-10, getHeight()-10);
		g.drawLine(10, 10, 10, getHeight()-10);
		g.drawLine(getWidth()-10, 10, getWidth()-10, getHeight()-10); 
		g.drawLine(10, 10, getWidth()-10, 10);	
		
		
		
		yv = minMax(datav, getHeight() - 10 ,10);
		
		g.setColor(Color.blue);
		if(yv.size() >= 1) {
			int width = (getWidth()-20)/yv.size();
			for(int i = 0 ; i < datav.size() ;i++) {
				g.fillRect((i*width)+10, getHeight() - yv.get(i), width, yv.get(i)-10);
			}
		}
		
		
	}
	public static Vector<Integer> minMax(Vector<Integer> v, double limitmax,double limitmin) {
		int max,min;
		double q;
		max = Integer.MIN_VALUE;
		min = Integer.MAX_VALUE;
		for(Integer i :v) {
			if(i > max) max = i;
			if(i < min) min = i;
		}
		q = (limitmax-limitmin) / (max);
		Vector<Integer> r = new Vector<Integer>();
		for(Integer i : v) {
			double j = (i)* q + limitmin;	
			r.add((int)Math.round(j));
		}
		return r;
		
	}
	public drawChart(Rectangle s,Vector<Integer> datav,Vector<String> labelv) {
		this.datav = datav;
		setBounds(s);
		setBackground(Color.white);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		setLayout(null);
		setVisible(true);
		repaint();
	}
	
}
