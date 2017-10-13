package data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.Vector;

import pojo.Data;

public class DbProcess {
	
	DbHelper dbh=new DbHelper();
	
	public DbProcess()
	{
		
	}
	
	public int getNumInDb()
	{
		String sql="select count(*) from data;";
		String numst=dbh.runSelect(sql)[0].get("count(*)").toString();
		int num=Integer.valueOf(numst);
		return num;
	}
	
	public void insert(String dayData,List<String> track,String time)
	{
		String sql="insert into data values ("+dayData+",";
		for(int i=0;i<10;i++)
			sql=sql+"'"+track.get(i)+"',";
		sql=sql+"'"+time+"');";
		dbh.runUpdate(sql);
	}
	
	//获取数据库中最新的期号
	public int getLatestNum()
	{
		if(this.getNumInDb()==0)
			return 0;
		String sql="select max(number) from data;";
		return Integer.valueOf(dbh.runSelect(sql)[0].get("max(number)").toString());
	}
	
	//获取数据库中前num项数据
	public Vector<Data> getDatas(int num)
	{
		Vector<Data> datas=new Vector<Data>();
		String sql = "select * from data order by number DESC limit "+num+";";
		@SuppressWarnings("rawtypes")
		Map[] rows=dbh.runSelect(sql);
		for(@SuppressWarnings("rawtypes") Map row:rows)
		{
			List<String> tracks=new ArrayList<String>();
			for(int i=1;i<=10;i++)
			{
				tracks.add(row.get("t"+i).toString());
			}
			Data data=new Data(row.get("number").toString(),tracks,row.get("time").toString());
			datas.add(data);
		}
		return datas;
	}
	
	public List<Integer> getConfiguration(int a)
	{
		List<Integer> confs=new ArrayList<Integer>();
		String sql="select * from configuration;";
		@SuppressWarnings("rawtypes")
		Map row=dbh.runSelect(sql)[0];
		//获取冷码参数
		if(a==0)
			for(int i=1;i<11;i++)
				confs.add(Integer.valueOf(row.get("t"+i).toString()));
		else
			for(int i=1;i<11;i++)
				confs.add(Integer.valueOf(row.get("n"+i).toString()));
		
		return confs;
	}
	/*
	public List<String> getEarlestNum(int track,int conf,int coldNum)
	{
		String sql="select distinct(t"+track+") from (select t"+track+" from data order by number DESC limit "+(conf-1)+");";
		List<String> result=new ArrayList<String>();
		@SuppressWarnings("rawtypes")
		Map[] rows=dbh.runSelect(sql);
		//判断是否有符合冷码要求的数字
		if(10-rows.length<coldNum)
		{
			result.add("0");
			return result;
		}
		//符合条件，继续执行
		List<String> allNum=this.getAllNum();
		List<String> earlestNum=new ArrayList<String>();
		List<Integer> earlestLNum=new ArrayList<Integer>();
		for(@SuppressWarnings("rawtypes") Map row:rows)
		{
			String num=row.get("t"+track).toString();
			if(allNum.contains(num))
				allNum.remove(num);
		}
		for(String num:allNum)
		{
			sql="select max(number) from data where t"+track+"='"+num+"' order by number DESC;";
			int nowLNum;
			try{
				nowLNum=Integer.valueOf(dbh.runSelect(sql)[0].get("max(number)").toString());
			}catch(Exception e){
				nowLNum=0;
			}
			boolean ifInsert=false;
			for(int i=0;i<earlestNum.size();i++)
			{
				if(nowLNum<earlestLNum.get(i))
				{
					earlestLNum.add(i, nowLNum);
					earlestNum.add(i, num);
					ifInsert=true;
					break;
				}
			}
			if(!ifInsert)
			{
				earlestLNum.add(nowLNum);
				earlestNum.add(num);
			}
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
	*/
	
	
	/*
	private List<String> getAllNum() 
	{
		List<String> allNum=new ArrayList<String>();
		for(int i=1;i<=9;i++)
			allNum.add("0"+i);
		allNum.add("10");
		return allNum;
	}
	*/
	public void clearAll()
	{
		String sql="delete from data;";
		dbh.runUpdate(sql);
	}
	
	public void changeConf(int a, int track, int conf)
	{
		String sql="";
		if(a==0)
			sql="update configuration set t"+track+"="+conf+";";
		else
			sql="update configuration set n"+track+"="+conf+";";
		dbh.runUpdate(sql);
	}
	
	public boolean ifOver()
	{
		String sql="select over from configuration";
		String s_over=dbh.runSelect(sql)[0].get("over").toString();
		return s_over.equals("1");
	}
	
	public void startUpdate()
	{
		String sql="update configuration set over=0";
		dbh.runUpdate(sql);
	}
	
	public void overUpdate()
	{
		String sql="update configuration set over=1";
		dbh.runUpdate(sql);
	}
	
	
	
	public String getFormatedDateString(String format,Date date)
	{    
        int newTime=(int)(8 * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) 
        {
            timeZone = TimeZone.getDefault();
        } 
        else 
        {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }
    
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }
	
	public boolean login(String user, String pwd)
	{
		String sql="select count(*) from users where username='"+user+"' and password='"+pwd+"';";
		String result=dbh.runSelect(sql)[0].get("count(*)").toString();
		
		return result.equals("1");
	}
	
	public void storeUP(String user, String pwd)
	{
		String sql="update configuration set  store=1, user='"+user+"', password='"+pwd+"';";
		dbh.runUpdate(sql);
	}
	
	public boolean ifStore()
	{
		String sql="select store from configuration;";
		String result=dbh.runSelect(sql)[0].get("store").toString();
		
		return result.equals("1");
	}
	
	public void changeStore()
	{
		String sql="update configuration set store=0;";
		dbh.runUpdate(sql);
	}
	
	public void changePwd(String user,String pwd)
	{
		String sql="update users set password='"+pwd+"' where username='"+user+"';";
		dbh.runUpdate(sql);
	}
	
	@SuppressWarnings("rawtypes")
	public Map getUAP()
	{
		String sql="select user, password from configuration;";
		Map result=dbh.runSelect(sql)[0];
		return result;
	}
	
	public Vector<String> getseDate()
	{
		String sql="select sdate, edate from configuration;";
		@SuppressWarnings("rawtypes")
		Map row=dbh.runSelect(sql)[0];
		Vector<String> result=new Vector<String>();
		try{
			result.add("1");
			result.add(row.get("sdate").toString());
			result.add(row.get("edate").toString());
		}catch(Exception e)
		{
			result.clear();
			result.add("0");
		}
		return result;
	}
	
	
	
	
	
}
