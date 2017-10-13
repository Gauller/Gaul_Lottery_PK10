package pojo;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import data.DbHelper;
import data.DbProcess;

public class NTList {
	
	private Vector<String> title=new Vector<String>();
	private Vector<Vector<String>> datas=new Vector<Vector<String>>();
	
	private DbHelper dbh=new DbHelper();
	private DbProcess dp=new DbProcess();
	
	public NTList()
	{
		
	}
	
	public void resetTable(int track, int num)
	{
		//只选择一个号码
		if(num<=10)
		{
			title.addElement("");
			title.addElement(num+"号");
		}
		//选择了所有号码
		else
		{
			title.addElement("");
			for(int i=1;i<=10;i++)
				title.addElement(i+"号");
		}
		//选择了一个车道
		if(track<=10)
		{
			this.datas=this.getNTList(track, num);
			datas.get(0).add(0,"第"+track+"道");
		}
		//选择了所有车道
		else
		{
			this.datas=this.getNTList(track, num);
			for(int i=1;i<=10;i++)
				datas.get(i-1).add(0,"第"+i+"道");
		}
	}

	public Vector<String> getTitle() {
		return title;
	}

	public void setTitle(Vector<String> title) {
		this.title = title;
	}

	public Vector<Vector<String>> getDatas() {
		return datas;
	}

	public void setDatas(Vector<Vector<String>> datas) {
		this.datas = datas;
	}
	
	private Vector<Vector<String>> getNTList(int track,int num)
	{
		Vector<Vector<String>> datas=new Vector<Vector<String>>();
		if(track<=10)
		{
			String sql="select * from ntlist where track="+track+";";
			@SuppressWarnings("rawtypes")
			Map[] rows=dbh.runSelect(sql);
			if(num<=10)
			{
				Vector<String> data=new Vector<String>();
				data.add(rows[0].get("n"+num).toString());
				datas.add(data);
			}
			else
			{
				Vector<String> data=new Vector<String>();
				for(int i=1;i<=10;i++)
					data.add(rows[0].get("n"+i).toString());
				datas.add(data);
			}
		}
		else
		{
			if(num<=10)
			{
				String sql="select n"+num+" from ntlist;";
				@SuppressWarnings("rawtypes")
				Map[] rows=dbh.runSelect(sql);
				for(int i=0;i<10;i++)
				{
					Vector<String> data=new Vector<String>();
					data.add(rows[i].get("n"+num).toString());
					datas.add(data);
				}
			}
			else
			{
				String sql="select * from ntlist;";
				@SuppressWarnings("rawtypes")
				Map[] rows=dbh.runSelect(sql);
				for(@SuppressWarnings("rawtypes") Map row:rows)
				{
					Vector<String> data=new Vector<String>();
					for(int i=1;i<=10;i++)
						data.add(row.get("n"+i).toString());
					datas.add(data);
				}
			}
		}
		return datas;
	}
	
	public void resetList()
	{
		String delete="delete from ntlist;";
		dbh.runUpdate(delete);
		int lastNum=dp.getLatestNum();
		for(int i=0;i<10;i++)
		{
			String insert="insert into ntlist values("+(i+1);
			String sql="select t"+(i+1)+",max(number) from data group by t"+(i+1)+" order by t"+(i+1)+";";
			@SuppressWarnings("rawtypes")
			Map[] results=dbh.runSelect(sql);
			for(@SuppressWarnings("rawtypes") Map result:results)
			{
				int num=Integer.valueOf(result.get("max(number)").toString());
				num=lastNum-num+1;
				insert=insert+","+num;
			}
			insert+=");";
			dbh.runUpdate(insert);
		}
	}
	
	public void updateList()
	{
		//获取最新一条数据
		Vector<Data> data=dp.getDatas(1);
		List<String> tracks=data.get(0).getTracks();
		String sql="";
		for(int i=1;i<=10;i++)
		{
			sql="update ntlist set n"+i+"=n"+i+"+1;";
			dbh.runUpdate(sql);
		}
		for(int i=0;i<10;i++)
		{
			int num=Integer.valueOf(tracks.get(i));
			sql="update ntlist set n"+num+"=1 where track="+(i+1)+";";
			dbh.runUpdate(sql);
		}
	}

}
