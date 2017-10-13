package frame;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import data.DbProcess;
import spider.Parser;

public class StartFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static boolean ifRemember=false;
	public static boolean ifFinishUpdate=false;
	
	private Font fo=new Font("����",0,16);
	private Font title=new Font("����",0,14);
	private DbProcess dp=new DbProcess();
	
	public StartFrame()
	{
		ifRemember=dp.ifStore();
		this.initFrame();
		String date=dp.getFormatedDateString("yyyy-MM-dd",new Date());
		Vector<String> sedate=dp.getseDate();
		if(sedate.get(0).equals("1"))
		{
			if(date.compareTo(sedate.get(1))<0||date.compareTo(sedate.get(2))>0)
			{
				JOptionPane.showMessageDialog(null, "����ѹ�ʹ�����ޣ������»�ȡʹ��Ȩ��", "����ѹ���", 
						JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}
	
	public static void main(String args[])
	{
		EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StartFrame().setVisible(true);
            }
        });
		
		//�������ݿ�������
		new Parser().parse();
		ifFinishUpdate=true;
	}

	public void initFrame()
	{
		//set frame
		int height=350;
		int width=200;
		this.setTitle("PK10�����Ƽ����");
		this.setSize(height, width);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		//��������ʾ����Ļ����		
		Toolkit tool = Toolkit.getDefaultToolkit();     //��ȡ���߶���
		Dimension d = tool.getScreenSize();  	//��ȡ��ǰ��Ļ�ĳߴ�
		double h = d.getHeight();		//��ȡ��Ļ�Ŀ��
		double w = d.getWidth();
		int x = (int)(w-height)/2;		//�����x���y�ᣨ���У�
		int y = (int)(h-width)/2;
		this.setLocation(x, y);
		
		JPanel jp=new JPanel();
		this.add(jp);
		jp.setBounds(0,0,350,170);
		jp.setLayout(null);
		
		JTabbedPane tabbedPane=new JTabbedPane();
		tabbedPane.setBounds(5, 5, 335, 160);;
		tabbedPane.setFont(title);
		jp.add(tabbedPane);
		
		JPanel login=this.getLoginP();
		tabbedPane.addTab("��¼", login);
		
		JPanel change=this.getChangeP();
		tabbedPane.addTab("����", change);	
	}

	private JPanel getLoginP()
	{
		JPanel jp=new JPanel();
		jp.setLayout(null);
		jp.setBounds(5, 5, 330, 130);
		
		JLabel user_jl=new JLabel("�˺�");
		user_jl.setFont(fo);
		user_jl.setBounds(10, 10, 50, 30);
		jp.add(user_jl);
		
		JTextField user_jtf=new JTextField();
		user_jtf.setBounds(65, 10, 150, 30);
		jp.add(user_jtf);
		
		JLabel pwd_jl=new JLabel("����");
		pwd_jl.setFont(fo);
		pwd_jl.setBounds(10, 50, 50, 30);
		jp.add(pwd_jl);
		
		JPasswordField pwd_jtf=new JPasswordField(20);
		pwd_jtf.setBounds(65, 50, 150, 30);
		jp.add(pwd_jtf);
		
		JButton confirm=new JButton("ȷ��");
		confirm.setFont(fo);
		confirm.setBounds(235, 10, 85, 30);
		jp.add(confirm);
		confirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				String user=user_jtf.getText();
				String pwd=new String(pwd_jtf.getPassword());
				boolean right=dp.login(user, pwd);
				if(right)
				{
					if(ifRemember)
						dp.storeUP(user, pwd);
					else
						dp.changeStore();
					closeFrame();
					new MainFrame().setVisible(true);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "��������˺�������������������", "��������", 
							JOptionPane.ERROR_MESSAGE);
					user_jtf.setText(null);
					pwd_jtf.setText(null);
				}
			}
		});
		
		JButton exit=new JButton("�˳�");
		exit.setFont(fo);
		exit.setBounds(235, 50, 85, 30);
		jp.add(exit);
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		JCheckBox rem_jcb=new JCheckBox("�����˺�����");
		rem_jcb.setBounds(10, 90, 150, 30);
		jp.add(rem_jcb);
		if(ifRemember)
			rem_jcb.setSelected(true);
		rem_jcb.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange()==ItemEvent.DESELECTED)
					StartFrame.ifRemember=false;
				else
					StartFrame.ifRemember=true;
			}
		});
		
		if(ifRemember)
		{
			@SuppressWarnings("rawtypes")
			Map result=dp.getUAP();
			rem_jcb.setSelected(true);
			user_jtf.setText(result.get("user").toString());
			pwd_jtf.setText(result.get("password").toString());
		}
		else
			rem_jcb.setSelected(false);
		
		return jp;
	}
	
	private void closeFrame()
	{
		this.dispose();
	}

	private JPanel getChangeP()
	{
		JPanel jp=new JPanel();
		jp.setLayout(null);
		
		JLabel user_jl=new JLabel("�˺�");
		user_jl.setFont(fo);
		user_jl.setBounds(10, 10, 50, 30);
		jp.add(user_jl);
		
		JTextField user_jtf=new JTextField();
		user_jtf.setBounds(65, 10, 80, 30);
		jp.add(user_jtf);
		
		JLabel pwd1_jl=new JLabel("ԭ����");
		pwd1_jl.setFont(fo);
		pwd1_jl.setBounds(10, 50, 50, 30);
		jp.add(pwd1_jl);
		
		JPasswordField pwd1_jtf=new JPasswordField(20);
		pwd1_jtf.setBounds(65, 50, 80, 30);
		jp.add(pwd1_jtf);
		
		JLabel pwd2_jl=new JLabel("������");
		pwd2_jl.setFont(fo);
		pwd2_jl.setBounds(160, 10, 70, 30);
		jp.add(pwd2_jl);
		
		JPasswordField pwd2_jtf=new JPasswordField(20);
		pwd2_jtf.setBounds(235, 10, 80, 30);
		jp.add(pwd2_jtf);
		
		JLabel pwd3_jl=new JLabel("ȷ������");
		pwd3_jl.setFont(fo);
		pwd3_jl.setBounds(160, 50, 70, 30);
		jp.add(pwd3_jl);
		
		JPasswordField pwd3_jtf=new JPasswordField(20);
		pwd3_jtf.setBounds(235, 50, 80, 30);
		jp.add(pwd3_jtf);
		
		JButton reset=new JButton("���");
		reset.setFont(fo);
		reset.setBounds(145, 90, 80, 30);
		jp.add(reset);
		reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				user_jtf.setText(null);
				pwd1_jtf.setText(null);
				pwd2_jtf.setText(null);
				pwd3_jtf.setText(null);
			}
		});
		
		
		JButton confirm=new JButton("ȷ��");
		confirm.setFont(fo);
		confirm.setBounds(235, 90, 80, 30);
		jp.add(confirm);
		confirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				String user=user_jtf.getText();
				String pwd1=new String(pwd1_jtf.getPassword());
				String pwd2=new String(pwd2_jtf.getPassword());
				String pwd3=new String(pwd3_jtf.getPassword());
				//�ж�ȷ�������Ƿ�һ��
				if(pwd2.equals(pwd3))
				{
					//�ж�ԭ�����Ƿ���ȷ
					if(dp.login(user, pwd1))
					{
						dp.changePwd(user, pwd2);
						JOptionPane.showMessageDialog(null, "�����޸ĳɹ�", "�޸ĳɹ�", 
								JOptionPane.INFORMATION_MESSAGE);
					}
					else
						JOptionPane.showMessageDialog(null, "�˺Ż�ԭ�����������������", "��������", 
								JOptionPane.ERROR_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(null, "��������������벻��ͬ������������", "��������", 
							JOptionPane.ERROR_MESSAGE);
			}
		});
		
		return jp;
	}

}
