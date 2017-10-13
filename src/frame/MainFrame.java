package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import data.DbProcess;
import pojo.ColdData;
import pojo.NTList;
import pojo.TrackData;
import spider.Parser;

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	public static JTabbedPane num_tp=new JTabbedPane();
	public static JTabbedPane track_tp=new JTabbedPane();
	public static Vector<Vector<Vector<String>>> V_DATA=new Vector<Vector<Vector<String>>>();
	public static Vector<Map<String,Integer>> V_RECORD=new Vector<Map<String,Integer>>();
	public static Vector<Vector<Vector<String>>> T_DATA=new Vector<Vector<Vector<String>>>();
	public static Vector<Map<String,Integer>> T_RECORD=new Vector<Map<String,Integer>>();
	public static String output="";
	public static String toutput="";
	public static boolean ifTest=true;		//是否为测试期，即是否有print
	
	private Font fo1=new Font("黑体",0,16);
	private Font fo2=new Font("黑体",0,14);
	DbProcess dp=new DbProcess();
	
	public MainFrame()
	{
		initFrame();
	}
	
	public void initFrame()
	{
		//set frame
		this.setTitle("PK10计算推荐软件");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		//将窗体显示在屏幕中央		
		Toolkit tool = Toolkit.getDefaultToolkit();     //获取工具对象
		Dimension d = tool.getScreenSize();  	//获取当前屏幕的尺寸
		double h = d.getHeight();		//获取屏幕的宽高
		double w = d.getWidth();
		int x = (int)(w-800)/2;		//窗体的x轴和y轴（居中）
		int y = (int)(h-600)/2;
		this.setLocation(x, y);
		
		JPanel jp=new JPanel();
		this.add(jp);
		jp.setBounds(0,0,800,600);
		jp.setLayout(null);
		
		//更新数据库中彩票开奖数据
		JLabel jl=new JLabel("有开奖新数据，号码推荐中····");
		jl.setFont(new Font("黑体",0,24));
		jl.setBounds(200, 200, 400, 50);
		jl.setForeground(new Color(16));
		jp.add(jl);
		
		//set JTabbedPane
		JTabbedPane tabbedPane=new JTabbedPane();
		tabbedPane.setBounds(5, 5, 780, 480);
		tabbedPane.setFont(fo1);
		jp.add(tabbedPane);
		
		//冷码
		num_tp.setBounds(5,5,770,440);
		num_tp.setFont(fo2);
		tabbedPane.addTab("冷码", num_tp);
		
		//冷道
		track_tp.setBounds(5,5,770,440);
		track_tp.setFont(fo2);
		tabbedPane.addTab("冷道", track_tp);
		
		//码道记录
		JPanel nt_jp=new NTPane().getPane();
		nt_jp.setBounds(5, 5, 770, 440);
		tabbedPane.addTab("码道记录", nt_jp);
		
		List<Integer> confs=dp.getConfiguration(0);
		List<Integer> t_confs=dp.getConfiguration(1);
		//set Production Panel
		new Thread(new Runnable(){
			public void run()
			{
				try
				{
					//判断是否完成数据库更新
					while(!StartFrame.ifFinishUpdate)
					{
						try{
							Thread.sleep(500);
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
					//更新ntList
					new NTList().resetList();
					//更新推荐号码
					new ColdData().resetData(confs);
					new TrackData().resetData(t_confs);
					MainFrame.output="";
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							for(int i=0;i<9;i++)
							{
								jl.setVisible(false);
								JPanel cold=new ColdNum(i+1,confs.get(i)).getColdPanel();
								num_tp.addTab("冷"+(i+1)+"码", cold);
								JPanel coldtrack=new TrackPane(i+1,t_confs.get(i)).getColdPanel();
								track_tp.addTab("冷"+(i+1)+"道", coldtrack);
								if(MainFrame.V_RECORD.get(i).get("length")>1)
								{
									MainFrame.output=MainFrame.output+"冷"+(i+1)+".";
									Prompt.COLDNUM[i]=true;
								}
								else
									Prompt.COLDNUM[i]=false;
								if(MainFrame.T_RECORD.get(i).get("length")>1)
								{
									MainFrame.toutput=MainFrame.toutput+"冷"+(i+1)+".";
									Prompt.TRACKNUM[i]=true;
								}
								else
									Prompt.TRACKNUM[i]=false;
							}
							//开奖统计
							JPanel recordJp=new RecordPane().getPane(true);
							num_tp.addTab("开奖统计", recordJp);
							JPanel trecordJp=new RecordPane().getPane(false);
							track_tp.addTab("开奖统计", trecordJp);
							
							new Thread(new Runnable(){
								public void run(){
									if(!MainFrame.output.isEmpty())
									{
										new Prompt().setPrompt("恭喜发财！老板，冷码有推荐号码了！推荐类型："+MainFrame.output,true);
										new Prompt().setPrompt("恭喜发财！老板，冷道有推荐号码了！推荐类型："+MainFrame.toutput,false);
									}
								}
							}).start();
						}
					});
				}catch(Exception e1){
					e1.printStackTrace();
				}
				
				while(true)
				{
					String s_time=dp.getFormatedDateString("HH:mm:ss",new Date());
					String[] time=s_time.split(":");
					if(Integer.valueOf(time[0])>=9&&Integer.valueOf(time[0])<=23
							&&(Integer.valueOf(time[1])%5==2||Integer.valueOf(time[1])%5==3))
					{
						if(new Parser().parse())
						{
							new NTList().updateList();
							new ColdData().updateData();
							new TrackData().updateData();
							List<Integer> confs=dp.getConfiguration(0);
							List<Integer> t_confs=dp.getConfiguration(1);
							SwingUtilities.invokeLater(new Runnable(){
								public void run(){
									MainFrame.output="";
									MainFrame.toutput="";
									for(int i=0;i<9;i++)
									{
										JPanel cold=new ColdNum(i+1,confs.get(i)).getColdPanel();
										num_tp.setComponentAt(i, cold);
										JPanel coldtrack=new TrackPane(i+1,t_confs.get(i)).getColdPanel();
										track_tp.setComponentAt(i, coldtrack);
										if(MainFrame.V_RECORD.get(i).get("length")>1)
										{
											MainFrame.output=MainFrame.output+"冷"+(i+1)+".";
											Prompt.COLDNUM[i]=true;
										}
										else
											Prompt.COLDNUM[i]=false;
										if(MainFrame.T_RECORD.get(i).get("length")>1)
										{
											MainFrame.toutput=MainFrame.toutput+"冷"+(i+1)+".";
											Prompt.TRACKNUM[i]=true;
										}
										else
											Prompt.TRACKNUM[i]=false;
									}
									JPanel recordJp=new RecordPane().getPane(true);
									num_tp.setComponentAt(9, recordJp);
									JPanel trecordJp=new RecordPane().getPane(false);
									track_tp.setComponentAt(9, trecordJp);
									new Thread(new Runnable(){
										public void run(){
											if(!MainFrame.output.isEmpty())
											{
												new Prompt().setPrompt("恭喜发财！老板，冷码有推荐号码了！推荐类型："+MainFrame.output,true);
												new Prompt().setPrompt("恭喜发财！老板，冷道有推荐号码了！推荐类型："+MainFrame.toutput,false);
											}
										}
									}).start();
								}
							});
							try{
								Thread.sleep(1000*60*2);
							}catch(InterruptedException e){
								e.printStackTrace();
							}
						}
						else
						{
							try{
								Thread.sleep(5000);
							}catch(InterruptedException e){
								e.printStackTrace();
							}
						}
					}
					else
					{
						try{
							Thread.sleep(10000);
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}	
				}
			}
		}).start();
		
		
		
		//功能区
		JPanel function=new JPanel();
		jp.add(function);
		function.setBounds(5, 490, 780, 70);
		function.setBorder(BorderFactory.createTitledBorder("功能框"));
		function.setLayout(null);
		
		//开奖记录按钮
		JButton historyb=new JButton("开奖记录");
		historyb.setBounds(670, 25, 100, 30);
		historyb.setFont(fo1);
		function.add(historyb);
		historyb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new HistoryFrame().setVisible(true);
			}
		});
		
		//选择控制冷码还是冷道
		JComboBox<String> choice_jcb=new JComboBox<String>();
		choice_jcb.addItem("冷码");
		choice_jcb.addItem("冷道");
		choice_jcb.setBounds(30, 25, 100, 25);
		function.add(choice_jcb);
		
		//语音报告
		JCheckBox audio_jcb=new JCheckBox("语音报告");
		audio_jcb.setBounds(140, 25, 100, 30);
		function.add(audio_jcb);
		audio_jcb.setSelected(true);
		
		//气泡报告
		JCheckBox balloon_jcb=new JCheckBox("气泡报告");
		balloon_jcb.setBounds(250, 25, 100, 30);
		function.add(balloon_jcb);
		balloon_jcb.setSelected(true);
		
		choice_jcb.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e)
			{
				if(choice_jcb.getSelectedIndex()==0)
				{
					if(Prompt.AUDIO)
						audio_jcb.setSelected(true);
					else
						audio_jcb.setSelected(false);
					if(Prompt.BALLOON)
						balloon_jcb.setSelected(true);
					else
						balloon_jcb.setSelected(false);
				}
				else
				{
					if(Prompt.TAUDIO)
						audio_jcb.setSelected(true);
					else
						audio_jcb.setSelected(false);
					if(Prompt.TBALLOON)
						balloon_jcb.setSelected(true);
					else
						balloon_jcb.setSelected(false);
				}
			}
		});
		audio_jcb.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange()==ItemEvent.DESELECTED)
					if(choice_jcb.getSelectedIndex()==0)
						Prompt.AUDIO=false;
					else
						Prompt.TAUDIO=false;
				else
					if(choice_jcb.getSelectedIndex()==0)
						Prompt.AUDIO=true;
					else
						Prompt.TAUDIO=true;
			}
		});
		balloon_jcb.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange()==ItemEvent.DESELECTED)
					if(choice_jcb.getSelectedIndex()==0)
						Prompt.BALLOON=false;
					else
						Prompt.TBALLOON=false;
				else
					if(choice_jcb.getSelectedIndex()==0)
						Prompt.BALLOON=true;
					else
						Prompt.TBALLOON=true;
			}
		});
		
		
	}
}
