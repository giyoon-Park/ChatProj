package chat01;
/**
 * 	이 클래스는 채팅을 할 수 있는 폼을 제공하고
 * 	한 줄의 문자열을 손쉽게 주고받기 위한 클래스
 * @author	박기윤
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.StringTokenizer;
import java.io.*;

public class ChatClient01 {
	/*
		1. 폼을 만든다.
		2. 서버에 접속(통신준비)
		3. 통신을 한다.
			1) 데이터를 보내고
			2) 데이터 받고
	*/
	
	// 폼에서 사용할 변수
	JFrame frame;
	JTextField field;
	JTextArea area;
	JScrollPane sp;
	
	// 통신에서 사용할 변수
	Socket socket;
	PrintWriter pw;
	BufferedReader br;
	
	String sid;
	String spw;
	/*
		이 프로그램은 단순히 문자만 주고받는 기능으로 만들 예정이다
		한줄 문자열을 손쉽게 주고받기 위해서
			readLine()
			println()
	*/
	public ChatClient01() {
		frame = new JFrame("# 채팅 클라이언트 #");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
			}
		});
		
		area = new JTextArea();
		area.setBackground(Color.green);
		area.setEditable(false);
		sp = new JScrollPane(area);
		
		// 글 입력창
		field = new JTextField();
		JButton btn = new JButton("SEND");
		
		// 버튼에 이벤트 추가
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 이 함수는
				// 버튼을 클릭했을 경우 실행되는 함수이고
				// 또는 텍스트필드에서 엔터키 누른경우
				// 또는 리스트에서 더블클릭한 경우
				// 메뉴를 선택한 경우
				
				// 할일
				// 텍스트 필드의 내용을 알아내고
				String msg = field.getText();
				if(msg == null || msg.length() == 0) {
					return;
				}
				// 이 라인을 실행한다는 의미는
				// 입력된 내용이 있다는 의미이고
				// 서버에 그 내용을 보내면 된다.
				
				pw.println(sid + "]" + msg);
				pw.flush();
				
				// 입력창 내용 지우고
				field.setText("");
			}
		});
		
		JPanel p1 = new JPanel(new BorderLayout());
		p1.add("Center", field);
		p1.add("East", btn);
		
		// frame에 추가하자
		frame.add("Center", sp);
		frame.add("South", p1);
		
		frame.setSize(400, 600);
		frame.setVisible(true);
		
		while(true) {
			String str = JOptionPane.showInputDialog("아이디/비밀번호 입력");
			dvdStr(str);
			MembDao mDao = new MembDao();
			int cnt = mDao.getLoginCnt(sid, spw);
			if(cnt == 0 ) {
				continue;
			} else {
				break;
			}
		}
		
		// 여기까지 화면이 준비되었고, 이제 통신 준비를 한다.
		try {
			// 서버에 연결을 시도하고
			socket = new Socket("192.168.0.21", 7788);
			// 소켓이 만들어지면 서버에 접속이 된 경우이므로
			// 통신할 스트림을 준비한다.
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			pw = new PrintWriter(out);
			
			InputStreamReader tmp = new InputStreamReader(in);
			br = new BufferedReader(tmp);
		} catch (Exception e) {
			// 통신을 준비하는 과정에 문제가 발생한 경우이므로
			// 이 프로그램이 더 이상 실행할 의미가 없게 됬다.
			close();
		}
		
		// 모든 준비가 끝났으므로 데이터를 받을 준비한다.
		ReceiveThread t = new ReceiveThread(this);
		t.start();
	}
	
	// 아아디 비밀번호 분리 처리함수
	public void dvdStr(String str) {
		StringTokenizer token = new StringTokenizer(str, "/");
		String[] arr = new String[2];
		int idx = 0;
		while(token.hasMoreTokens()) {
			arr[idx++] = token.nextToken();
		}
		sid = arr[0];
		spw = arr[1];
	}
	
	public void close() {
		try {
			pw.close();
			br.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		new ChatClient01();
	}
}
