package chat01;
/**
 * 	채팅을 관리하는 서버 클래스
 * @author	박기윤
 *
 *	1. 접속 대기 소켓 준비
 *	2. 접속 받는다.
 *	3. 접속자가 독립적으로 사용할 스레드 클래스를 만들어줘야 한다.
 *	4. 이 안에서 데이터를 주고 받도록 해야한다.
 *		데이터를 줄때는 모든 접속자에게 데이터를 주도록 처리한다.
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer01 {
	ServerSocket server;
	/*
		채팅 프로그램은 TCP 프로토콜로 통신이 이루어져야 한다.
		접속형 통신 프로그램을 제작해야한다는 말이다.
		
		접속형 통신에서 서버에서 사용하는 소켓은 ServerSocket 이다.
		
		내가 준 데이터는 다른 클라이언트에게도 전달해야 한다.
		따라서 서버입장에서는
		자신에게 접속한 클라이언트의 정보를 알고 있어야 한다.
	*/
	
	// 서버에게 접속한 클라이언트의 정보를 누적해서 기억할 변수
	ArrayList clientList;
	public ChatServer01() {
		// 접속대기 소켓을 준비한다.
		try {
			server = new ServerSocket(7788);
			clientList = new ArrayList();
		} catch (Exception e) {
			// 만약 접속대기 소켓을 만들 때 문제가 발생했다면
			// 이 프로그램은 더이상 실행할 의미가 없으므로 즉시 종료시켜준다.
			System.exit(0);
		}
		
		// 접속을 받는다.
		// 계속해서 받아야하므로 반복처리한다.
		System.out.println("*** Server Start ***");
		while(true) {
			try {
				Socket socket = server.accept();
				// 여기서 직접 대화를 나눴고
				// 각각의 클라이언트와 대화하는 프로그램을 독립적으로 만들 예정이다.
				// <== 메인 프로그램이 다시 접속을 받을 수 있기 때문이다.
				
				// 현재 접속자와 실제 통신을
				// 다음 프로그램을 가동 시켜줘야 한다.(스레드 프로그램)
				ClientThread t = new ClientThread(this, socket);
				clientList.add(t);
				// t 안에는 해당 클라이언트에 대한 모든 정보를
				// 변수로 준비하고 있으므로 t를 통째로 넣어주면
				// 필요할 때 필요한 정보를 꺼내서 사용할 수 있을 것이다.
				t.start();
			} catch (Exception e) {
				System.out.println("### 접속불량 : " + e);
			}
		}
	}

	public static void main(String[] args) {
		new ChatServer01();
	}

}
