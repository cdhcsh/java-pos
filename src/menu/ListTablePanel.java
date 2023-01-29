package menu;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicListUI.ListSelectionHandler;
import javax.swing.table.DefaultTableModel;

public class ListTablePanel extends JPanel{
	private DefaultTableModel  dtm = null;
	private Vector<String> columns = null;
	private Vector<String[]> data = null;
	public ListTablePanel(Rectangle s,Vector<String> columns,Vector<String[]> data) {
		columns = columns;
		data = data;
		setBounds(s);
		setBackground(Color.white);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		setLayout(null);
		setUp();
	}
	private void setUp() {
		DefaultTableModel dtm = new DefaultTableModel(columns,0) {
			public boolean isCellEditable(int row, int column) {
				return false;  //셀 편집불가코드
			}
		};
		for(String[] d : data) {
			dtm.addRow(d);
		}		
		JTable jt = new JTable(dtm);
		jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane(jt);
		scroll.setBounds(0,0,this.getWidth(),this.getHeight());
		add(scroll);
	}
}
