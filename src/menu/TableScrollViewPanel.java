package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import order.Bill;

public class TableScrollViewPanel extends JPanel{
	private Vector<Vector<String>> data = new Vector<Vector<String>>();
	private Vector<String> columns = new Vector<String>();
	private DefaultTableModel model = new DefaultTableModel();
	private JTable table = new JTable();
	public TableScrollViewPanel(Rectangle s,Vector<Vector<String>> data,Vector<String> columns) {
		if(data != null)
		this.data= data;
		this.columns = columns;
		setBounds(s);
		setLayout(null);
		setBackground(Color.white);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		setUp();
	}
	public void addRow(Vector<String> data) {
		model.addRow(data);
	}
	public void cancelSelectedProduct() {
		int row =table.getSelectedRow();
		if(row >= 0) {
			int cnt = Integer.parseInt(model.getValueAt(row,1).toString());
			int price = Bill.WonFormatToInt(model.getValueAt(row, 2).toString())/cnt;
			cnt--;
			if(cnt <= 0) model.removeRow(row);
			else {
				model.setValueAt(""+cnt, row, 1);
				model.setValueAt(Bill.intToWonFormat(cnt*price), row, 2);
			}
		}
	}
	public void removeRow() {
		int row = table.getSelectedRow();
		if(row >= 0) model.removeRow(row);
	}
	public void initModel() {
		model = new DefaultTableModel(columns, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};		
		if(data.size() > 0) {
			for(Vector<String> v : data) {
				model.addRow(v);
			}
		}
		table.setModel(model);
	}
	public void cancelAllRow() {
		data = new Vector<Vector<String>>();
		initModel();
	}
	public void setData(Vector<Vector<String>> data) {
		this.data = data;
	}
	public Vector<Vector<String>> getTableData(){
		return model.getDataVector();
	}
	public void setUp() {
		removeAll();
		initModel();
		table.getTableHeader().setFont(new Font("",Font.BOLD,16));
		table.setFont(new Font("",Font.PLAIN,16));
		table.setRowHeight(28);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	
		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(0,0,getWidth(),getHeight());
		add(sp);
		repaint();
	}
}
