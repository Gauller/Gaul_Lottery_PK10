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
		// 定义一个字符串用来存储网页内容
		String result = null;
		// 定义一个缓冲字符输入流
		BufferedReader in = null;
		int i=1;
		while(true) {
			try {
				// 将string转成url对象
				URL realUrl = new URL(url);
				// 初始化一个链接到那个url的连接
				HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
				// 开始实际的连接
				connection.connect();
				connection.setReadTimeout(50000);
				InputStreamReader isr=new InputStreamReader(connection.getInputStream(),"utf-8");
				// 初始化 BufferedReader输入流来读取URL的响应
				in = new BufferedReader(isr);
				// 用来临时存储抓取到的每一行的数据
				String line;
				while ((line = in.readLine()) != null)
				{
					// 遍历抓取到的每一行并将其存储到result里面
					result += line + "\n";
				}
				break;
				
			} catch (Exception e)
			{
				if(i>10)
				{
					JOptionPane.showMessageDialog(null, "网络连接有误，请检查网络后重新启动程序", "网络连接失败", 
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
			} // 使用finally来关闭输入流
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
