package spider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;

public class Crawler
{
	public String crawl(String url)
	{
		// ����һ���ַ��������洢��ҳ����
		String result = null;
		// ����һ�������ַ�������
		BufferedReader in = null;
		int i=1;
		while(true) {
			try {
				// ��stringת��url����
				URL realUrl = new URL(url);
				// ��ʼ��һ�����ӵ��Ǹ�url������
				HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
				// ��ʼʵ�ʵ�����
				connection.connect();
				connection.setReadTimeout(50000);
				InputStreamReader isr=new InputStreamReader(connection.getInputStream(),"utf-8");
				// ��ʼ�� BufferedReader����������ȡURL����Ӧ
				in = new BufferedReader(isr);
				// ������ʱ�洢ץȡ����ÿһ�е�����
				String line;
				while ((line = in.readLine()) != null)
				{
					// ����ץȡ����ÿһ�в�����洢��result����
					result += line + "\n";
				}
				break;
				
			} catch (Exception e)
			{
				if(i>10)
				{
					JOptionPane.showMessageDialog(null, "���������������������������������", "��������ʧ��", 
							JOptionPane.ERROR_MESSAGE);
					System.exit(0);
					return "";
				}
				else
				{
					try{
						Thread.sleep(3000);
					}catch(Exception e3){
						e3.printStackTrace();
					}
					i++;
				}
			} // ʹ��finally���ر�������
			finally
			{
				try
				{
					if (in != null)
					{
						in.close();
					}
				} catch (Exception e2)
				{
					e2.printStackTrace();
				}
			}
		}
		return result;
		
	}
}
