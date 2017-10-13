package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import data.DbProcess;
import pojo.RecordData;

public class RecordPane {
	
	
	private Font fo=new Font("����",Font.BOLD,14);
	DbProcess dp=new DbProcess();
	
	public RecordPane(){
		
	}
	
	public JPanel getPane(boolean ifColdNum)
	{
		String daytable="";
		String histable="";
		if(ifColdNum)
		{
			daytable="drecord";
			histable="hrecord";
		}
		else
		{
			daytable="tdrecord";
			histable="threcord";
		}
		JPanel jp=new JPanel();
		jp.setLayout(null);
		
		Vector<String> v_title=new Vector<String>();
		String[] s_title={"����","��������","����"};
		for(String title:s_title)
			v_title.add(title);
		
		//���տ�����¼
		Vector<Vector<String>> v_daydata=new RecordData().getRecord(daytable);
		DefaultTableModel today_model=new DefaultTableModel();
		JTable today_table=new JTable(today_model);
		today_model.setDataVector(v_daydata, v_title);
		//���ñ�񲻿ɱ༭
		today_table.setEnabled(false);
		//���ñ��������
		today_table.setFont(new Font("SansSerif",0,14));
		//���ñ���п��и�
		this.setColumnWidth(today_table, 0, 70);
		this.setColumnWidth(today_table, 1, 110);
		this.setColumnWidth(today_table, 2, 70);
		today_table.setRowHeight(20);
		JScrollPane today_jsp=new JScrollPane(today_table);
		today_jsp.setBounds(5, 10, 250, 430);
		jp.add(today_jsp);
		
		
		JLabel today_jl=new JLabel("<html>��<br/>��<br/>��<br/>��<br/>��<br/>��<br/>Ϣ<br/>ͳ<br/>��</html>");
		today_jl.setBounds(260, 10, 40, 430);
		today_jl.setHorizontalAlignment(JLabel.CENTER);
		today_jl.setVerticalAlignment(JLabel.CENTER);
		today_jl.setFont(new Font("����",0,24));
		today_jl.setForeground(new Color(255,0,0));
		jp.add(today_jl);
		
		//��ʷ������¼
		Vector<Vector<String>> v_historydata=new RecordData().getRecord(histable);
		DefaultTableModel history_model=new DefaultTableModel();
		JTable history_table=new JTable(history_model);
		history_model.setDataVector(v_historydata, v_title);
		//���ñ�񲻿ɱ༭
		history_table.setEnabled(false);
		//���ñ��������
		history_table.setFont(new Font("SansSerif",0,14));
		//���ñ���п��и�
		this.setColumnWidth(history_table, 0, 70);
		this.setColumnWidth(history_table, 1, 110);
		this.setColumnWidth(history_table, 2, 70);
		history_table.setRowHeight(20);
		JScrollPane history_jsp=new JScrollPane(history_table);
		history_jsp.setBounds(520, 10, 250, 430);
		jp.add(history_jsp);

		
		JLabel history_jl=new JLabel("<html>��<br/>ʷ<br/>��<br/>��<br/>��<br/>��<br/>Ϣ<br/>ͳ<br/>��</html>");
		history_jl.setBounds(475, 10, 40, 430);
		history_jl.setHorizontalAlignment(JLabel.CENTER);
		history_jl.setVerticalAlignment(JLabel.CENTER);
		history_jl.setFont(new Font("����",0,24));
		history_jl.setForeground(new Color(255,0,0));
		jp.add(history_jl);
		
		
		//��ά��
		JLabel wc_jl=new JLabel("Wechat:");
		wc_jl.setFont(fo);
		wc_jl.setBounds(320,100,135,30);
		jp.add(wc_jl);
		
		ImageIcon icon=new ImageIcon("./data/image/2d.png");
		icon=new ImageIcon(icon.getImage().getScaledInstance(135, 135, Image.SCALE_DEFAULT));
		JLabel wci_jl=new JLabel();
		wci_jl.setIcon(icon);
		wci_jl.setBounds(320, 135, 135, 135);
		jp.add(wci_jl);
		
		//���ư�ť
		JButton day_jb=new JButton("������ռ�¼");
		day_jb.setFont(fo);
		day_jb.setBounds(320, 330, 135, 30);
		jp.add(day_jb);
		day_jb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				DefaultTableModel model =(DefaultTableModel) today_table.getModel();
				while(model.getRowCount()>0){
					model.removeRow(model.getRowCount()-1);
				}
				if(ifColdNum)
					new RecordData().clearRecord("drecord");
				else
					new RecordData().clearRecord("tdrecord");
			}
		});
		
		JButton history_jb=new JButton("�����ʷ��¼");
		history_jb.setFont(fo);
		history_jb.setBounds(320, 370, 135, 30);
		jp.add(history_jb);
		history_jb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				DefaultTableModel model =(DefaultTableModel) history_table.getModel();
				while(model.getRowCount()>0){
					model.removeRow(model.getRowCount()-1);
				}
				if(ifColdNum)
					new RecordData().clearRecord("hrecord");
				else
					new RecordData().clearRecord("threcord");
			}
		});
		
		return jp;
	}
	
	private void setColumnWidth(JTable table,int columnNum,int width)
	{
		TableColumnModel cm=table.getColumnModel();
		cm.getColumn(columnNum).setPreferredWidth(width);
		cm.getColumn(columnNum).setMaxWidth(width);
		cm.getColumn(columnNum).setMinWidth(width);
	}

}
