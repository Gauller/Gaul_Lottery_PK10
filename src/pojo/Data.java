package pojo;

import java.util.List;

public class Data {
	
	String number;
	List<String> tracks;
	String time;
	
	public Data()
	{
		
	}
	public Data(String number,List<String> tracks,String time)
	{
		this.number=number;
		this.tracks=tracks;
		this.time=time;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<String> getTracks() {
		return tracks;
	}
	public void setTracks(List<String> tracks) {
		this.tracks = tracks;
	}
	
	
	

}
