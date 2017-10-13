package frame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import data.DbProcess;
import pojo.TrackData;

public class TrackPane {
	
	int coldNum=0;
	int confNum=9;
	DbProcess dp=new DbProcess();
	private Font fo=new Font("����",0,12);
	private static boolean ifset=true;
	
	public TrackPane(int coldNum,int confNum)
	{
		this.coldNum=coldNum;
		this.confNum=confNum;
	}
	
	public JPanel getColdPanel()
	{
		JPanel jp=new JPanel();
		jp.setLayout(null);
		
		JLabel jl=new JLabel("�Ƽ�������δ�������");
		jl.setFont(fo);
		jp.add(jl);
		jl.setBounds(5,5,150,20);
		
		JTextField jt=new JTextField();
		jt.setBounds(160, 5, 50, 20);
		jt.setHorizontalAlignment(JLabel.CENTER);
		jt.setText(this.confNum+"");
		jp.add(jt);
		
		JButton jb=new JButton("��������");
		jb.setFont(fo);
		jb.setBounds(220, 5, 100, 20);
		jp.add(jb);
		jb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				String s_num=jt.getText();
				if(!s_num.matches("[1-9][0-9]*"))
					JOptionPane.showMessageDialog(null, "�������ݲ�����Ч���֣�����������", "��������", 
							JOptionPane.ERROR_MESSAGE);
				else 
				{
					int i_num=Integer.valueOf(s_num);
					if(i_num>169)
						JOptionPane.showMessageDialog(null, "������169���ڵ�����", "��������", 
								JOptionPane.ERROR_MESSAGE);
					else
					{
						confNum=i_num;
						//�������ݿ�������
						dp.changeConf(1,coldNum, confNum);
						//��������
						new TrackData().resetOneData(coldNum,confNum);
						//����ҳ��
						JPanel cold=new TrackPane(coldNum,confNum).getColdPanel();
						MainFrame.track_tp.setComponentAt(coldNum-1, cold);
						
					}
				}
			}
		});
		
		//������
		Vector<String> v_title=new Vector<String>(7);
		String[] titles={"�Ƽ���","�Ƽ�����","����","����","�Ƽ�����","�Ƿ��н�","��������"};
		for(String title:titles)
			v_title.add(title);
		
		//�������
		Vector<Vector<String>> v_data=MainFrame.T_DATA.get(coldNum-1);
		
		DefaultTableModel model=new DefaultTableModel();
		JTable table=new JTable(model);
		model.setDataVector(v_data, v_title);
		
		//���ñ�񲻿ɱ༭
		table.setEnabled(false);
		//�������ݾ���
		DefaultTableCellRenderer r =new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class,r);
		//���ñ���п��и�
		this.setColumnWidth(table, 0, 60);
		this.setColumnWidth(table, 1, 220);
		this.setColumnWidth(table, 2, 50);
		this.setColumnWidth(table, 3, 80);
		this.setColumnWidth(table, 4, 120);
		this.setColumnWidth(table, 5, 80);
		
		table.setRowHeight(20);
		
		
		JScrollPane jsp=new JScrollPane(table);
		jsp.setBounds(5, 30, 760, 390);
		jp.add(jsp);
		
		jsp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if(ifset)
				{
					jsp.getVerticalScrollBar().setValue(e.getAdjustable().getMaximum());
					ifset=false;
				}
			}
		});
		
		JScrollBar bar=jsp.getVerticalScrollBar();
		int x=bar.getValue();
		for(int i=100;i<2000000000;i+=100)
		{
			bar.setValue(i);
			if(x==bar.getValue())
				break;
			x=bar.getValue();
		}
		
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
