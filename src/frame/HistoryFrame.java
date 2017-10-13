package frame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import data.DbProcess;
import pojo.Data;

public class HistoryFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DbProcess dp=new DbProcess();
	Font fo=new Font("黑体",0,18);
	
	public HistoryFrame()
	{
		this.initFrame();
	}
	
	public void initFrame()
	{
		//set frame
		this.setTitle("PK10开奖记录");
		this.setSize(700, 700);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		//将窗体显示在屏幕中央		
		Toolkit tool = Toolkit.getDefaultToolkit();     //获取工具对象
		Dimension d = tool.getScreenSize();  	//获取当前屏幕的尺寸
		double h = d.getHeight();		//获取屏幕的宽高
		double w = d.getWidth();
		int x = (int)(w-700)/2;		//窗体的x轴和y轴（居中）
		int y = (int)(h-700)/2;
		this.setLocation(x, y);
		
		
		
		//设置表格标题
		Vector<String> v_title=new Vector<String>(12);
		String[] v={"开奖时间","开奖期号","1","2","3","4","5","6","7","8","9","10"};
		for(String s:v)
			v_title.add(s);
		//设置表格数据
		
		Vector<Vector> v_data=new Vector<Vector>();
		Vector<Data> datas=dp.getDatas(80);
		for(Data d1:datas)
		{
			Vector data=new Vector(12);
			data.add(d1.getTime());
			data.add(d1.getNumber());
			List<String> tracks=d1.getTracks();
			data.addAll(this.setNumWithColor(tracks));
			v_data.add(data);
		}
		
		//设置表格
		DefaultTableModel model=new DefaultTableModel(v_data, v_title);
		JTable table=new JTable(model){
			public Class getColumnClass(int column)
			{
				return getValueAt(0, column).getClass();
			}
		};
		//model.setDataVector(v_data, v_title);
		//model.setValueAt(new ImageIcon("./data/image/7.png"), 1, 1);
		
		JScrollPane jsp=new JScrollPane(table);
		jsp.setBorder(new EmptyBorder(5,5,5,5));
		this.setContentPane(jsp);
		//设置表格不可编辑
		table.setEnabled(false);
		//设置表格中字体
		table.setFont(fo);
		//设置内容居中
		DefaultTableCellRenderer r =new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class,r);
		//设置表格列宽行高
		this.setColumnWidth(table, 0, 180);
		this.setColumnWidth(table, 1, 80);
		
		table.setRowHeight(42);
		//设置标题字体
		table.getTableHeader().setFont(fo);
	}
	
	private Vector setNumWithColor(List<String> tracks)
	{
		Vector data=new Vector();
		for(int i=0;i<tracks.size();i++)
		{
			String num=tracks.get(i);
			switch(num){
			case "01":
				data.add(new ImageIcon("./data/image/1.png"));
				break;
			case "02":
				data.add(new ImageIcon("./data/image/2.png"));
				break;
			case "03":
				data.add(new ImageIcon("./data/image/3.png"));
				break;
			case "04":
				data.add(new ImageIcon("./data/image/4.png"));
				break;
			case "05":
				data.add(new ImageIcon("./data/image/5.png"));
				break;
			case "06":
				data.add(new ImageIcon("./data/image/6.png"));
				break;
			case "07":
				data.add(new ImageIcon("./data/image/7.png"));
				break;
			case "08":
				data.add(new ImageIcon("./data/image/8.png"));
				break;
			case "09":
				data.add(new ImageIcon("./data/image/9.png"));
				break;
			case "10":
				data.add(new ImageIcon("./data/image/10.png"));
				break;
			}
		}		
		return data;
	}
	
	private void setColumnWidth(JTable table,int columnNum,int width)
	{
		TableColumnModel cm=table.getColumnModel();
		cm.getColumn(columnNum).setPreferredWidth(width);
		cm.getColumn(columnNum).setMaxWidth(width);
		cm.getColumn(columnNum).setMinWidth(width);
	}
	
}
