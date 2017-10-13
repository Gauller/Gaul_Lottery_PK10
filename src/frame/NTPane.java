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
	
	private Font fo=new Font("宋体",0,12);
	private Font content = new Font ("黑体",0,14);
	
	public NTPane()
	{
		
	}
	
	public JPanel getPane()
	{
		JPanel jp=new JPanel();
		jp.setLayout(null);
		
		JLabel t_jl=new JLabel("查询赛道号：");
		t_jl.setFont(fo);
		jp.add(t_jl);
		t_jl.setBounds(5,5,80,20);
		
		JComboBox<String> t_jcb=new JComboBox<String>();
		t_jcb.setBounds(90, 5, 80, 20);
		jp.add(t_jcb);
		
		JLabel n_jl=new JLabel("查询码号：");
		n_jl.setFont(fo);
		jp.add(n_jl);
		n_jl.setBounds(190,5,80,20);
		
		JComboBox<String> n_jcb=new JComboBox<String>();
		n_jcb.setBounds(275, 5, 80, 20);
		jp.add(n_jcb);
		
		for(int i=1;i<=10;i++)
		{
			t_jcb.addItem("第"+i+"道");
			n_jcb.addItem(i+"号");
		}
		t_jcb.addItem("全部");
		n_jcb.addItem("全部");
		
		JButton check_jb=new JButton("查询");
		check_jb.setBounds(375, 5, 100, 20);
		jp.add(check_jb);
		
		//数据展示
		DefaultTableModel model=new DefaultTableModel();
		JTable table=new JTable(model);
		//设置表格不可编辑
		table.setEnabled(false);
		//设置内容居中
		DefaultTableCellRenderer r =new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class,r);
		//设置行高列宽
		table.setRowHeight(20);
		//设置字体
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
