package frame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import pojo.NTList;

public class NTPane {
	
	private Font fo=new Font("����",0,12);
	private Font content = new Font ("����",0,14);
	
	public NTPane()
	{
		
	}
	
	public JPanel getPane()
	{
		JPanel jp=new JPanel();
		jp.setLayout(null);
		
		JLabel t_jl=new JLabel("��ѯ�����ţ�");
		t_jl.setFont(fo);
		jp.add(t_jl);
		t_jl.setBounds(5,5,80,20);
		
		JComboBox<String> t_jcb=new JComboBox<String>();
		t_jcb.setBounds(90, 5, 80, 20);
		jp.add(t_jcb);
		
		JLabel n_jl=new JLabel("��ѯ��ţ�");
		n_jl.setFont(fo);
		jp.add(n_jl);
		n_jl.setBounds(190,5,80,20);
		
		JComboBox<String> n_jcb=new JComboBox<String>();
		n_jcb.setBounds(275, 5, 80, 20);
		jp.add(n_jcb);
		
		for(int i=1;i<=10;i++)
		{
			t_jcb.addItem("��"+i+"��");
			n_jcb.addItem(i+"��");
		}
		t_jcb.addItem("ȫ��");
		n_jcb.addItem("ȫ��");
		
		JButton check_jb=new JButton("��ѯ");
		check_jb.setBounds(375, 5, 100, 20);
		jp.add(check_jb);
		
		//����չʾ
		DefaultTableModel model=new DefaultTableModel();
		JTable table=new JTable(model);
		//���ñ�񲻿ɱ༭
		table.setEnabled(false);
		//�������ݾ���
		DefaultTableCellRenderer r =new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class,r);
		//�����и��п�
		table.setRowHeight(20);
		//��������
		table.setFont(content);
		
		JScrollPane jsp=new JScrollPane(table);
		jsp.setBounds(5, 40, 760, 400);
		jsp.setVisible(false);
		jp.add(jsp);
		
		
		check_jb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				int track=t_jcb.getSelectedIndex()+1;
				int num=n_jcb.getSelectedIndex()+1;
				NTList ntl=new NTList();
				ntl.resetTable(track, num);
				
				Vector<String> title=ntl.getTitle();
				Vector<Vector<String>> datas=ntl.getDatas();
				model.setDataVector(datas, title);
				TableColumnModel cm=table.getColumnModel();
				for(int i=0;i<cm.getColumnCount();i++)
				{
					cm.getColumn(i).setPreferredWidth(68);
					cm.getColumn(i).setMaxWidth(68);
					cm.getColumn(i).setMinWidth(68);
				}
				jsp.setVisible(true);
			}
		});
		return jp;
	}

}
