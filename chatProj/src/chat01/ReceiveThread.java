package chat01;
/**
 * 	이 클래스는 서버에서 전송된 데이터를 받는 프로그램
 * 	이 프로그램은 스레드로 처리를 해서 독립적으로 실행되도록 해줘야 한다.
 * @author	박기윤
 *
 */
public class ReceiveThread extends Thread {
	/*
		이 클래스는 ChatClient01 클래스의 객체를 기억하고 있어야 한다.
		왜냐하면 서버에서 받은 메시지를 ChatClient01 의 JTextArea에 출력을 해줘야하기 때문에...
	*/
	
	ChatClient01 main;
	public ReceiveThread(ChatClient01 main) {
		this.main = main;
	}
	
	public void run() {
		// 할일
		// 데이터를 계속받는다.
		try {
			// 서버가 몇번 데이터를 보낼지 알 수 없으므로
			while(true) {
				// 데이터 받는다.
				// 스트림을 이용해서 받아야 한다.
				String msg = main.br.readLine();
				if(msg == null || msg.length() == 0) {
					break;
				}
				// 받은 데이터 출력한다.
				main.area.append(msg + "\r\n");
				// 받은 데이터를 출력할 위치는 폼의 JTextArea가 되기 때문에...
				main.sp.getVerticalScrollBar().setValue(main.sp.getVerticalScrollBar().getMaximum());
			}
		} catch (Exception e) {
			// 데이터를 받는 동안 예외발생하면 이 프로그램은 실행에 문제가 있다는 것이 되므로
			// 더이상 실행할 의미가 없다.
			// 따라서 이 프로그램 자체를 종료를 시켜야 한다.
			main.close();
		}
	}
}
