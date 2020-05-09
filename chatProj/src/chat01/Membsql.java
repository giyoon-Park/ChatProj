package chat01;

public class Membsql {
	public final int SEL_ID_CNT = 1001;
	
	public String getSql(int code) {
		StringBuffer buff = new StringBuffer();
		
		switch(code) {
		case SEL_ID_CNT:
			buff.append("SELECT ");
			buff.append("	COUNT(*) cnt ");
			buff.append("FROM ");
			buff.append("	member ");
			buff.append("WHERE ");
			buff.append("	id = ? ");
			buff.append("	AND pw = ? ");
			break;
		}
		return buff.toString();
	}
}
