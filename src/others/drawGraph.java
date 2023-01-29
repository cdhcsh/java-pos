package others;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.nio.charset.Charset;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import javafx.scene.shape.Line;

public class drawGraph extends JPanel{
	public static final int CNT = 9;
	Vector<Integer> datav = null;
	Vector<String> labelv = null;
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



		yv = minMax(datav, getHeight() - 40 ,40);
		g.setFont(new Font("",Font.PLAIN,16));

		g.drawLine(10, getHeight() - getMax(yv), 15, getHeight() - getMax(yv));
		g.drawString(Integer.toString(getMax(datav)), 20, getHeight() - getMax(yv) + 5);
		g.drawLine(10, getHeight() - getMin(yv), 15, getHeight() - getMin(yv));
		g.drawString(Integer.toString(getMin(datav)), 20, getHeight() - getMin(yv)  + 5);

		if(yv.size() > 1) {
			int width = (getWidth()-40)/(yv.size());
			Point p1 = null,p2 = null;
			for(int i = 0 ; i < yv.size() - 1 ;i++) {
				g.setColor(Color.blue);
				p1 = new Point(i*width + width/2 + 20 ,getHeight()-  yv.get(i));
				p2 = new Point((i+1)*width + width/2 + 20,getHeight()- yv.get(i+1));
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
				g.fillOval(p1.x-3, p1.y-3, 7, 7);
				g.setColor(Color.black);
				g.drawString(Integer.toString(datav.get(i)), p1.x - 20, p1.y - 5);
				g.drawString(labelv.get(i), p1.x - 10, getHeight() - 20);
			}
			g.setColor(Color.blue);
			g.fillOval(p2.x-3, p2.y-3, 7, 7);
			g.setColor(Color.black);
			g.drawString(Integer.toString(datav.lastElement()), p2.x - 20, p2.y - 5);
			g.drawString(labelv.lastElement(), p2.x - 10, getHeight() - 20);
		}


	}
	public static int getMax(Vector<Integer> v) {
		int max = -Integer.MAX_VALUE;
		for(Integer i :v) {
			if(i > max) max = i;
		}	
		return max;
	}
	public static int getMin(Vector<Integer> v) {
		int	min = Integer.MAX_VALUE;
		for(Integer i :v) {
			if(i < min) min = i;
		}	
		return min;
	}
	public static Vector<Integer> minMax(Vector<Integer> v, double limitmax,double limitmin) {
		int max,min;
		double q;
		max = getMax(v);
		min = getMin(v);
		q = (limitmax-limitmin) / (max);
		Vector<Integer> r = new Vector<Integer>();
		for(Integer i : v) {
			if(max > 0) {
				double j = (i)* q + limitmin;	
				r.add((int)Math.round(j));
			}				
		}
		return r;

	}
	public drawGraph(Rectangle s,Vector<Integer> datav,Vector<String> labelv) {
		this.datav = datav;
		this.labelv = labelv;
		setBounds(s);
		setBackground(Color.white);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		setLayout(null);
		setVisible(true);
		repaint();
	}

}
