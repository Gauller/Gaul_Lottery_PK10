package pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.DbHelper;
import data.DbProcess;

public class RecordData {
	
	DbProcess dp=new DbProcess();
	DbHelper dbh=new DbHelper();
	
	public RecordData()
	{
		
	}
	
	public Vector<Vector<String>> getRecord(String tablename)
	{
		Vector<Vector<String>> records=new Vector<Vector<String>>();
		if(tablename.equals("drecord"))
			if(this.ifReset())
			{
				this.clearRecord("tdrecord");
				this.clearRecord("drecord");
				return records;
			}
		
		if(this.ifEmpty(tablename))
			return records;
		@SuppressWarnings("rawtypes")
		List<Map> l_records=this.getAllRecords(tablename);
		for(int i=1;i<11;i++)
		{
			records.add(this.getStart());
			int length=records.size();
			while(true)
			{
				@SuppressWarnings("rawtypes")
				Map record=l_records.get(0);
				if(record.get("coldNum").toString().matches(i+""))
				{
					Vector<String> r=new Vector<String>();
					if(tablename.startsWith("t"))
						r.add(record.get("coldNum").toString()+" 道");
					else
						r.add(record.get("coldNum").toString()+" 码");
					r.add(record.get("lotteryNum").toString()+"  期开奖次数:");
					r.add(record.get("time").toString());
					records.add(r);
					l_records.remove(0);
					if(l_records.isEmpty())
						return records;
				}
				else
					break;
			}
			if(records.size()==length)
				records.remove(length-1);
		}
		return records;
	}
	
	public Vector<String> getStart()
	{
		Vector<String> start=new Vector<String>();
		//添加开始行目
		start.add("\u2605\u2605\u2605");
		start.add("\u2605\u2605\u2605\u2605\u2605\u2605\u2605");
		start.add("\u2605\u2605");
		
		return start;
	}
	
	public void addRecord(int coldNum,String reason,boolean ifColdNum)
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
		Pattern ptd = Pattern.compile("(\\d+)");
		Matcher mtd = ptd.matcher(reason);
		if(mtd.find())
		{
			this.addRecord(daytable,coldNum, mtd.group(0));
			this.addRecord(histable,coldNum, mtd.group(0));
		}
	}
	
	private boolean ifEmpty(String tablename)
	{
		String sql="select count(*) from "+tablename+" ;";
		String count=dbh.runSelect(sql)[0].get("count(*)").toString();
		return count.matches("0");
	}
	
	@SuppressWarnings("rawtypes")
	private List<Map> getAllRecords(String tablename)
	{
		String sql="select * from "+tablename+" order by coldNum,lotteryNum"; 
		Map[] m_records=dbh.runSelect(sql);
		List<Map> l_records=new ArrayList<Map>();
		for(Map record:m_records)
			l_records.add(record);
		return l_records;
	}
	
	public void clearRecord(String tablename)
	{
		String sql="delete from "+tablename+" ;";
		dbh.runUpdate(sql);
	}
	
	private void addRecord(String tablename,int coldNum,String lotteryNum)
	{
		String sql="select count(*),time from "+tablename+" where coldNum="+coldNum+" and lotteryNum="+lotteryNum+";";
		@SuppressWarnings("rawtypes")
		Map result=dbh.runSelect(sql)[0];
		String count=result.get("count(*)").toString();
		if(count.equals("0"))
		{
			sql="insert into "+tablename+" values("+coldNum+","+lotteryNum+",1);";
			dbh.runUpdate(sql);
		}
		else
		{
			int time=Integer.valueOf(result.get("time").toString())+1;
			sql="update "+tablename+" set time="+time+" where coldNum="+coldNum+" and lotteryNum="+lotteryNum+";";
			dbh.runUpdate(sql);
		}
	}
	
	private boolean ifReset()
	{
		String sql="select time from configuration";
		String time=dbh.runSelect(sql)[0].get("time").toString();
		String timenow=dp.getFormatedDateString("yyyy-MM-dd",new Date());
		boolean result= !time.equals(timenow);
		sql="update configuration set time='"+timenow+"';";
		dbh.runUpdate(sql);
		return result;
	}

}
