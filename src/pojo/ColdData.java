package pojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import data.DbHelper;
import data.DbProcess;
import frame.MainFrame;

public class ColdData {
	
	private DbProcess dp=new DbProcess();
	private DbHelper dbh=new DbHelper();
	
	public ColdData()
	{
		
	}
	
	public Vector<Vector<String>> getRecom(int coldNum, int confNum)
	{
		Vector<Vector<String>> recoms=new Vector<Vector<String>>();
		recoms.add(this.getStart());
		recoms.addAll(this.getData(coldNum,confNum));
		return recoms;
	}
	
	public Vector<String> getStart()
	{
		Vector<String> start=new Vector<String>();
		//添加开始行目
		start.add("\u2605\u2605\u2605");
		start.add("\u2605\u2605\u2605\u2605\u2605\u2605\u2605\u2605\u2605");
		start.add("\u2605\u2605");
		start.add("\u2605\u2605\u2605\u2605");
		start.add("\u2605\u2605\u2605\u2605\u2605\u2605\u2605");
		start.add("\u2605\u2605\u2605\u2605");
		start.add("\u2605\u2605\u2605\u2605\u2605\u2605\u2605\u2605\u2605");
		
		return start;
	}
	
	private Vector<Vector<String>> getData(int coldNum, int confNum)
	{
		Vector<Vector<String>> datas=new Vector<Vector<String>>();
		int nextNum=dp.getLatestNum()+1;
		for(int i=1;i<=10;i++)
		{
			List<String> result=this.getEarlestNum(i, confNum, coldNum);
			if(!result.get(0).equals("0"))
			{
				Vector<String> data=new Vector<String>();
				//添加期号
				data.add(nextNum+"");
				//添加推荐号码
				data.add(result.get(1));
				//添加赛道
				data.add("第"+i+"道");
				//添加手数及推荐理由
				int lNum=Integer.valueOf(result.get(2));
				data.add("第"+(lNum+1-confNum)+"手");
				data.add("连续"+lNum+"期未出现");
				//添加是否中奖
				data.add("等待开奖");
				//添加推荐时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				data.add(sdf.format(new Date()));
				datas.add(data);
			}
		}
		return datas;
	}
	
	public void resetData(List<Integer> confs)
	{
		//清空推荐数据
		MainFrame.V_DATA.clear();
		MainFrame.V_RECORD.clear();
		//重新推荐数据
		for(int i=0;i<10;i++)
		{
			Map<String,Integer> m_record = new HashMap<String,Integer>();
			int start=0;
			try{
				start=MainFrame.V_DATA.get(i).size();
			}catch(Exception e){
				start=0;
			}
			m_record.put("start", start);
			Vector<Vector<String>> data=this.getRecom(i+1,confs.get(i));
			MainFrame.V_DATA.add(data);
			m_record.put("length", data.size());
			MainFrame.V_RECORD.add(m_record);
		}
	}
	
	public void resetOneData(int coldNum,int confNum)
	{
		//清空冷coldNum码所对应的推荐数据
		MainFrame.V_RECORD.remove(coldNum-1);
		//重新推荐数据
		Map<String,Integer> m_record = new HashMap<String,Integer>();
		int start=0;
		try{
			start=MainFrame.V_DATA.get(coldNum-1).size();
		}catch(Exception e){
			start=0;
		}
		m_record.put("start", start);
		Vector<Vector<String>> data=this.getRecom(coldNum,confNum);
		MainFrame.V_DATA.get(coldNum-1).addAll(data);
		m_record.put("length", data.size());
		MainFrame.V_RECORD.add(coldNum-1,m_record);
	}
	
	public void updateData()
	{
		//获取最新一条数据
		Vector<Data> data=dp.getDatas(1);
		List<String> tracks=data.get(0).getTracks();
		//更新数据中的开奖情况
		for(int i=0;i<MainFrame.V_RECORD.size();i++)
		{
			int start=MainFrame.V_RECORD.get(i).get("start");
			int length=MainFrame.V_RECORD.get(i).get("length");
			if(length!=0)
			{
				for(int j=start+1;j<start+length;j++)
				{
					for(int k=1;k<=10;k++)
					{
						if(MainFrame.V_DATA.get(i).get(j).get(2).indexOf(k+"道")>=0)
						{
							MainFrame.V_DATA.get(i).get(j).remove(5);
							if(MainFrame.V_DATA.get(i).get(j).get(1).indexOf(tracks.get(k-1))>=0)
							{
								MainFrame.V_DATA.get(i).get(j).add(5, "\u2714");
								new RecordData().addRecord(i+1,MainFrame.V_DATA.get(i).get(j).get(4),true);
							}
							else
								MainFrame.V_DATA.get(i).get(j).add(5, "\u2718");
							break;
						}
					}
				}
			}
			for(int j=MainFrame.V_DATA.get(i).size();j>300;j--)
			{
				MainFrame.V_DATA.get(i).remove(0);
				MainFrame.V_RECORD.get(i).replace("start", MainFrame.V_RECORD.get(i).get("start")-1);
			}
		}
		//添加新数据
		List<Integer> confs=dp.getConfiguration(0);
		for(int i=0;i<10;i++)
		{
			Map<String,Integer> m_record = new HashMap<String,Integer>();
			m_record.put("start", MainFrame.V_DATA.get(i).size());
			Vector<Vector<String>> data1=this.getRecom(i+1,confs.get(i));
			MainFrame.V_DATA.get(i).addAll(data1);
			m_record.put("length", data1.size());
			MainFrame.V_RECORD.remove(i);
			MainFrame.V_RECORD.add(i,m_record);
			
		}
	}
	
	public List<String> getEarlestNum(int track,int conf,int coldNum)
	{
		List<String> result=new ArrayList<String>();
		String sql="select * from ntlist where track="+track+";";
		@SuppressWarnings("rawtypes")
		Map row=dbh.runSelect(sql)[0];
		List<String> earlestNum=new ArrayList<String>();
		List<Integer> earlestLNum=new ArrayList<Integer>();
		for(int i=1;i<=10;i++)
		{
			boolean ifInsert=false;
			int num=Integer.valueOf(row.get("n"+i).toString());
			if(num>=conf)
			{
				for(int j=0;j<earlestNum.size();j++)
				{
					if(num>earlestLNum.get(j))
					{
						earlestNum.add(j,i<10?"0"+i:""+i);
						earlestLNum.add(j,num);
						ifInsert=true;
						break;
					}
				}
				if(!ifInsert)
				{
					earlestNum.add(i<10?"0"+i:""+i);
					earlestLNum.add(num);
				}
			}
		}
		if(earlestNum.size()<coldNum)
		{
			result.add("0");
			return result;
		}
		while(earlestNum.size()>coldNum)
			earlestNum.remove(coldNum);
		Collections.sort(earlestNum);
		String r_num="";
		for(int i=0;i<earlestNum.size()-1;i++)
			r_num+=earlestNum.get(i)+",";
		r_num+=earlestNum.get(earlestNum.size()-1);
		result.add("1");
		result.add(r_num);
		try
		{
			result.add(earlestLNum.get(coldNum-1)+"");
		}catch(Exception e){
			System.out.println(1);
		}
		return result;
	}
}
