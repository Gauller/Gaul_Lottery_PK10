package spider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import data.DbProcess;
import frame.MainFrame;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pojo.Data;

public class Parser {
	
    DbProcess dp= new DbProcess();
	private String updateUrl="https://dy.907794.com/api/pk10/getLotteryBase.php?issue=";
	private String dayUrl="http://www.163kai.com/api2/pk10/BaseList.php?date=";
	
	public Parser()
	{
		
	}
	
	public boolean parse()
	{
		Crawler cr = new Crawler();
		
		if(MainFrame.ifTest) System.out.println("��ʼ��ȡ����");
		//��ȡ����һ�ڲ�Ʊ
		String result = cr.crawl(this.updateUrl);
		if(result.isEmpty())
			return false;
		Data newestData = this.getData(result).get(0);
		int newestNum = Integer.valueOf(newestData.getNumber());
		int diff=newestNum-dp.getLatestNum();
		
		if(diff==0) {
			if(MainFrame.ifTest) System.out.println("����Ҫ����");
			//����Ҫ����
			dp.overUpdate();
			return false;
		}
		else if(diff==1) {
			dp.insert(newestData.getNumber(), newestData.getTracks(), newestData.getTime());
			dp.overUpdate();
			return true;
		}
		else {
			if(MainFrame.ifTest) System.out.println("��Ҫ����");
			//��ʼ����,�Ȼ�ȡ��������
			if(MainFrame.ifTest) System.out.println("���µ�һ��");
			String today=dp.getFormatedDateString("yyyy-MM-dd", new Date());
			result = cr.crawl(this.dayUrl+today);
			if(result.isEmpty())
				return false;
			List<Data> datas = this.getData(result);
			int index = Math.min(diff, datas.size());
			diff -= index;
			for(int i=0;i<index;i++) {
				Data thisdata = datas.get(i);
				dp.insert(thisdata.getNumber(), thisdata.getTracks(), thisdata.getTime());
			}
			//�ж��Ƿ���Ҫ������ȡ�ڶ�������
			if(diff>0) {
				if(MainFrame.ifTest) System.out.println("���µڶ���");
				//���ǰһ�������
				Calendar calendar = Calendar.getInstance();  //�õ�����
				calendar.setTime(new Date());//�ѵ�ǰʱ�丳������
				calendar.add(Calendar.DAY_OF_MONTH, -1);  //����Ϊǰһ��
				Date dBefore = calendar.getTime();   //�õ�ǰһ���ʱ��
				String yestoday = dp.getFormatedDateString("yyyy-MM-dd",dBefore);
				result = cr.crawl(this.dayUrl+yestoday);
				if(result.isEmpty())
					return false;
				datas = this.getData(result);
				index = Math.min(diff, 179);
				for(int i=0;i<index;i++) {
					Data thisdata = datas.get(i);
					dp.insert(thisdata.getNumber(), thisdata.getTracks(), thisdata.getTime());
				}
			}
		}
		//���ݿ������������
		if(MainFrame.ifTest) System.out.println("������������");
		dp.overUpdate();
		return true;
	}

	private List<Data> getData(String result) {
		List<Data> res = new ArrayList<>();
		JSONObject json_cont=JSONObject.fromObject(result.subSequence(4, result.length()));
		String str_res = json_cont.getString("result");
		JSONObject json_res=JSONObject.fromObject(str_res);
		String str_data = json_res.getString("data");
		JSONArray json_data=JSONArray.fromObject(str_data);
		for(int i=0;i<json_data.size();i++) {
			JSONObject json_d = JSONObject.fromObject(json_data.get(i));
			String number = json_d.getString("preDrawIssue");
			String time = json_d.getString("preDrawTime");
			String str_tracks = json_d.getString("preDrawCode");
			List<String> tracks = Arrays.asList(str_tracks.split(","));
			res.add(new Data(number,tracks,time));
		}
		return res;
	}
	
	/*
	try{
		File file=new File("e:/123.txt");
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fw=new FileWriter(file,true);
		fw.write(result);
		fw.close();
	}catch(IOException e) {
		e.printStackTrace();
	}
	*/

}
