package others;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class PictureEdit extends JFrame{
	int xmin,xmax,ymin,ymax;
	int rowc,colc;
	int b = -1;
	int range = 1;
	Color c = Color.black;
	int mcolor = -1;
	int drawing = 0;
	locationPanel[][] panels;
	Color[][] prePanels;
	TitledBorder[][] prePanelsBorder;
	Stack<Color[][]> preC;
	Stack<TitledBorder[][]> preB;
	PictureEdit(int row,int col){
		rowc = row;
		colc = col;
		panels = new locationPanel[row][col];
		prePanels = new Color[row][col];
		prePanelsBorder = new TitledBorder[row][col];
		preC = new Stack<Color[][]>();
		preB = new Stack<TitledBorder[][]>();
		setSize(1000,1000);
		setLayout(new GridLayout(0, col,0,0));
		for(int i = 0 ; i<row;i++)
			for(int j = 0 ; j<col;j++){
				locationPanel tmp = new locationPanel(j,i);
				tmp.addMouseListener(new click());
				tmp.setBackground(Color.WHITE);
				tmp.setBorder(new TitledBorder(new LineBorder(Color.white,1)));
				panels[i][j] = tmp;
				add(panels[i][j]);
			}
		addKeyListener(new key());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		xmin = xmax = ymin = ymax = -1;
	}
	class key extends KeyAdapter{
		public void keyPressed(java.awt.event.KeyEvent e) {
			if(drawing == 0) {
			if(e.isControlDown()) {
				b = 1;
			}
			if(e.getKeyChar() == 'z' ) {
				if(!preC.isEmpty()) {
					prePanels = preC.pop();
					prePanelsBorder = preB.pop();
					for(int i = 0 ; i <rowc ; i++)
						for(int j = 0 ; j <colc ; j++) {
							panels[i][j].setBackground(prePanels[i][j]);
							panels[i][j].setBorder(prePanelsBorder[i][j]);
						}

				}
			}
			if(e.isAltDown()) b= 2;
			if(e.getKeyChar() == 'q') {
				c = Color.black;
			}
			if(e.getKeyChar() == 'w') {
				c = Color.white;
			}
			if(e.getKeyChar() == 'e') {
				c = Color.red;
			}
			if(e.getKeyChar() == 'r') {
				c = Color.blue;
			}
			if(e.getKeyChar() == 'a') {
				c = Color.yellow;
			}
			if(e.getKeyChar() == 's') {
				c = Color.green;
			}
			if(e.getKeyChar() == 'd') {
				c = Color.gray;
			}
			if(e.getKeyChar() == 'f') {
				c = Color.pink;
			}
			if(e.getKeyChar() == '1') {
				range = 1;
			}
			if(e.getKeyChar() == '2') {
				range = 3;
			}
			if(e.getKeyChar() == '3') {
				range = 5;
			}
			if(e.getKeyChar() == '4') {
				range = 7;
			}
			if(e.getKeyChar() == '5') {
				range = 9;
			}
			if(e.getKeyChar() == '6') {
				range = 11;
			}
			if(e.getKeyChar() == '7') {
				range = 13;
			}
			if(e.getKeyChar() == '8') {
				range = 15;
			}
			if(e.getKeyChar() == 'p') {
				preSave();
				for(int i = 0 ; i<rowc;i++) {
					for(int j = 0 ; j<colc;j++) {
						panels[i][j].setBackground(c);
					}
				}
			}
			if(e.getKeyChar() == 'o') {
				preSave();
				for(int i = 0 ; i<rowc;i++) {
					for(int j = 0 ; j<colc;j++) {
						panels[i][j].setBorder(new TitledBorder(new LineBorder(c,1)));
					}
				}
			}
			}
		}
	}
	public void preSave() {
		Color[][] tmpprePanels = new Color[rowc][colc];
		TitledBorder[][] tmpprePanelsBorder = new TitledBorder[rowc][colc];
		for(int i = 0 ; i < rowc ;i++)
			for(int j = 0 ; j <colc ; j++) {
				tmpprePanels[i][j] = panels[i][j].getBackground();
				tmpprePanelsBorder[i][j] = (TitledBorder)panels[i][j].getBorder();
			}
		preC.push(tmpprePanels);
		preB.push(tmpprePanelsBorder);	
	}
	class click extends MouseAdapter{
		int color = 0;
		Object o ;
		locationPanel p;
		public int rangeTo(int i,int min,int max) {
			if(i >= max) return (max-1);
			else if(i < 0) return 0;
			else return i;
		}
		public void coloring(locationPanel p) {
			int xmin,xmax,ymin,ymax;
			xmin = rangeTo(p.x-(range/2),0,colc);
			xmax = rangeTo(p.x+(range/2),0,colc);
			ymin = rangeTo(p.y-(range/2),0,colc);
			ymax = rangeTo(p.y+(range/2),0,colc);
			for(int i = ymin ; i <= ymax ;i++)
				for(int j = xmin ; j <=xmax ; j++) {
					if(b != 1)panels[i][j].setBackground(c);
					if(b > 0)panels[i][j].setBorder(new TitledBorder(new LineBorder(c,1)));
				}


		}
		public void mousePressed(MouseEvent e) {
			preSave();
			o = e.getSource();			
			p = (locationPanel)o;
			coloring(p);
			mcolor = 1;
			drawing = 1;
			
		}
		public void mouseEntered(MouseEvent e) {
			o = e.getSource();			
			p = (locationPanel)o;
			if(mcolor != -1) {
				coloring(p);
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			mcolor = -1;
			b = -1;
			drawing = 0;
		}

	}
	class locationPanel extends JPanel{
		int x;
		int y;
		public locationPanel(int x,int y) {
			this.x = x;
			this.y =y;
		}
	}

	public static void main(String[] args) {
		int row;
		int col;
		PictureEdit pic = null;
		Scanner input = new Scanner(System.in);
		while(true) {
			System.out.print("input row :");
			row = input.nextInt();
			System.out.print("input col :");
			col = input.nextInt();
			if(row > 0 && col > 0 ) {
				pic = new PictureEdit(row, col);
			}
		}
	}
}
