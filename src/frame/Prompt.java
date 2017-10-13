package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;

public class Prompt extends JWindow{
	
	private static final long serialVersionUID = 1L;
	public static boolean AUDIO=true;
	public static boolean BALLOON=true;
	public static boolean TAUDIO=true;
	public static boolean TBALLOON=true;
	public static boolean[] COLDNUM={false,false,false,false,false,false,false,false,false};
	public static boolean[] TRACKNUM={false,false,false,false,false,false,false,false,false};
	private Font fo=new Font("黑体",1,16);
	
	public Prompt()
	{
		
	}
	
	public void setPrompt(String prompt,boolean ifColdNum)
	{
		if(ifColdNum)
		{
	        try{
	        	if(BALLOON)
				{
			        this.initFrame(prompt,ifColdNum);
			        this.setVisible(true);
				}
				if(AUDIO)
				{
					this.playAudio("./data/audio/wpre.wav");
					Thread.sleep(6000);
					for(int i=0;i<COLDNUM.length;i++)
					{
						if(COLDNUM[i])
						{
							this.playAudio("./data/audio/w"+(i+1)+".wav");
							Thread.sleep(1000);
						}
					}
				}
				else
					Thread.sleep(11000);
	        }catch(Exception e){
	        	
	        }
	        this.setVisible(false);
		}
		else
		{
			try{
	        	if(TBALLOON)
				{
			        this.initFrame(prompt,ifColdNum);
			        this.setVisible(true);
				}
				if(TAUDIO)
				{
					this.playAudio("./data/audio/mpre.wav");
					Thread.sleep(6000);
					for(int i=0;i<TRACKNUM.length;i++)
					{
						if(TRACKNUM[i])
						{
							this.playAudio("./data/audio/m"+(i+1)+".wav");
							Thread.sleep(1000);
						}
					}
				}
				else
					Thread.sleep(11000);
	        }catch(Exception e){
	        	
	        }
	        this.setVisible(false);
		}
	}
	
	private void playAudio(String filename)
	{
		try {
            File file = new File(filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);  
            clip.start();  
        } catch (UnsupportedAudioFileException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (LineUnavailableException e) {  
            e.printStackTrace();  
        } 
	}
	
	
	public void initFrame(String prompt,boolean ifColdNum)
	{
		//set frame
		int weight=240;
		int height=80;
		this.setSize(weight, height);
		//将窗体显示在屏幕中央		
		Toolkit tool = Toolkit.getDefaultToolkit();     //获取工具对象
		Dimension d = tool.getScreenSize();  	//获取当前屏幕的尺寸
		double h = d.getHeight();		//获取屏幕的宽高
		double w = d.getWidth();
		int x = (int)(w-weight);		//窗体的x轴和y轴（居中）
		int y = (int)(h-height-50);
		this.setLocation(x, y);
		JTextArea message = new JTextArea();
		
		message.setText(prompt);
		message.setFont(fo);
		message.setSize(230, 70);
		message.setLineWrap(true);
		if(ifColdNum)
			message.setBackground(new Color(99,255,255));
		else
			message.setBackground(new Color(0,255,99));
		this.add(message);
		message.setBorder(new EmptyBorder(5,5,5,5));
	}

}
