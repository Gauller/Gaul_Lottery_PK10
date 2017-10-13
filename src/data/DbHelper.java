package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

public class DbHelper {
	
	private final String DRIVER="org.sqlite.JDBC";
	
	private final String URL="jdbc:sqlite:data/database/lottery.db";
	
	public DbHelper()
	{
		Connection c = null;
		try{
			Class.forName(DRIVER);
			c = DriverManager.getConnection(URL);
			c.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void runUpdate(String sql)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			Class.forName(DRIVER);
			c = DriverManager.getConnection(URL);
			c.setAutoCommit(false);
			
			stmt = c.createStatement();
			
			stmt.executeUpdate(sql);
		} catch ( Exception e ) {
			e.printStackTrace();
		}finally{
			try{
				stmt.close();
				c.commit();
				c.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Map[] runSelect(String sql)
	{
		Connection c = null;
		Statement st=null;
		ResultSet rs = null;
		try
		{
			Class.forName(DRIVER);
			c = DriverManager.getConnection(URL);
			
			st=c.createStatement();
			rs=st.executeQuery(sql);
			Result result=ResultSupport.toResult(rs);
			Map[] rows=result.getRows();
			return rows;
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}finally{
			try{
				rs.close();
				st.close();
				c.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
