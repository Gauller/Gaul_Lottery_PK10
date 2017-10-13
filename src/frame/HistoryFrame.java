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
	Font fo=new Font("����",0,18);
	
	public HistoryFrame()
	{
		this.initFrame();
	}
	
	public void initFrame()
	{
		//set frame
		this.setTitle("PK10������¼");
		this.setSize(700, 700);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		//��������ʾ����Ļ����		
		Toolkit tool = Toolkit.getDefaultToolkit();     //��ȡ���߶���
		Dimension d = tool.getScreenSize();  	//��ȡ��ǰ��Ļ�ĳߴ�
		double h = d.getHeight();		//��ȡ��Ļ�Ŀ��
		double w = d.getWidth();
		int x = (int)(w-700)/2;		//�����x���y�ᣨ���У�
		int y = (int)(h-700)/2;
		this.setLocation(x, y);
		
		
		
		//���ñ�����
		Vector<String> v_title=new Vector<String>(12);
		String[] v={"����ʱ��","�����ں�","1","2","3","4","5","6","7","8","9","10"};
		for(String s:v)
			v_title.add(s);
		//���ñ������
		
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
		
		//���ñ��
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
		//���ñ�񲻿ɱ༭
		table.setEnabled(false);
		//���ñ��������
		table.setFont(fo);
		//�������ݾ���
		DefaultTableCellRenderer r =new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class,r);
		//���ñ���п��и�
		this.setColumnWidth(table, 0, 180);
		this.setColumnWidth(table, 1, 80);
		
		table.setRowHeight(42);
		//���ñ�������
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
