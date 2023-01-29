package others;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class GridBagMeneger {
	GridBagConstraints gbc;
	GridBagLayout gbl;
	Container con;
	public GridBagMeneger(GridBagLayout gbl,Container con){
		this.gbl = gbl;
		this.con = con;
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
	}
	public void gblAdd(Component c, int x, int y, int w, int h) {

		gbc.gridx = x;
		gbc.gridy = y; 
		gbc.gridwidth  = w;	
		gbc.gridheight = h;	

		gbl.setConstraints(c, gbc); 

		con.add(c);
	}
}
