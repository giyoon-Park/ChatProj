package chat01;

import DB.*;
import java.sql.*;

public class MembDao {
	ORCLJDBC db;
	Connection con;
	Statement stmt;
	PreparedStatement pstmt;
	ResultSet rs;
	Membsql mSQL;
	
	public MembDao() {
		db = new ORCLJDBC();
		mSQL = new Membsql();
	}
	
	// 로그인 디비 작업 전담 처리함수
	public int getLoginCnt(String id, String pw) {
		int cnt = 0;
		// 1. con
		con = db.getCon();
		// 2. sql
		String sql = mSQL.getSql(mSQL.SEL_ID_CNT);
		// 3. pstmt
		pstmt = db.getPSTMT(con,sql);
		try {
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			// 4. rs
			rs = pstmt.executeQuery();
			rs.next();
			// 5. cnt
			cnt = rs.getInt("cnt");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close(rs);
			db.close(pstmt);
			db.close(con);
		}
		return cnt;
	}

}
