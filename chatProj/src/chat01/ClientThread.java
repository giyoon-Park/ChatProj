package chat01;

import java.net.*;
import java.io.*;
import java.util.*;

public class ClientThread extends Thread {
	/*
		이 프로그램은 클라이언트와 대화를 할 목적으로 만든 것이다.
		따라서 먼저 만든 메인 ChatServer01 클래스와 상호 참조가 되어져야 한다.
	*/
	ChatServer01 main;
	
	// 필수 변수
	// 통신 변수
	Socket socket;
	// 내용을 작성할 변수
	PrintWriter pw;
	BufferedReader br;
	// 클라이언트의 정보를 저장할 변수
	String ip;
	
	public ClientThread() {}
	public ClientThread(ChatServer01 m, Socket s) throws Exception {
		this.main = m;
		this.socket = s;
		
		// 원칙적으로 아래부분을 예외처리를 해야하는 부분이다.
		// 문제는 이 클래스의 목적은 해당 클라이언트와 통신할 목적으로 만들어 졌다.
		// 따라서 스트림을 만들지 못하면 대화를 할 수 없다.
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		// 위 부분이 정상적으로 처리가 되었으면 이제 통신을 할 수 있는 상태가 되었다.
		
		pw = new PrintWriter(out);
		InputStreamReader tmp = new InputStreamReader(in);
		br = new BufferedReader(tmp);
		
		InetAddress inet = socket.getInetAddress();
		ip = inet.getHostAddress();
	}
	
	// 클라이언트의 메세지를 처리할 함수
	public void sendReceive(String msg) {
		// msg 안에는 클라이언트가 준 대화내용이 기억되어 있다.
		// 우리는 이 대화내용을 "[ip 님] 대화내용" 의 형식으로 사용할 것이다.
		
		// 응답 내용을 꾸미자.
		msg = "[" + ip + " 님]" + msg;	// ==> [192.168.0.21 님] 메세지~~
		
		// 이 내용을 접속한 모든 클라이언트에게 보내야 한다.
		int size = main.clientList.size();
		
		// 참고
		//		응답은 동시에 하지 못하고 한사람씩 반복해서 응답해줘야 한다.
		synchronized(main.clientList) {
			for (int i = 0; i < size; i++) {
				// 클라이언트 정보를 꺼낸다.
				ClientThread tmp = (ClientThread)main.clientList.get(i);
				tmp.pw.println(msg);
				tmp.pw.flush();
			}
		}
	}
	
	@Override
	public void run() {
		// 할일
		try {
			// 요청받고
			while(true) {
				String msg = br.readLine();
				if(msg == null || msg.length() == 0) {
					break;
				}
				// 만약 데이터베이스에 저장할 예정이면 이 부근에서 처리
				
				// 응답하고
				sendReceive(msg);
			}
		} catch (Exception e) {
			// 예외가 발생하면 이 스레드는 사용할 수없는 스레드다.
			// 따라서 자신이 사용하던 모든 자원을 반환해줘야 겠다.
			try {
				pw.close();
				br.close();
				socket.close();
			} catch (Exception e1) {}
			
			// 이 라인을 실행하면 해당클라이언트는 접속이 끊어진 상태가 되고
			// 따라서 클라이언트 리스트에서 빼줘야겠다.
			main.clientList.remove(this);
		}
	}
}
