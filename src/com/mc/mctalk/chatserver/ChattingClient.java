package com.mc.mctalk.chatserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import com.google.gson.Gson;
import com.mc.mctalk.view.ChattingFrame;
import com.mc.mctalk.view.ChattingRoomListPanel;
import com.mc.mctalk.view.MainFrame;
import com.mc.mctalk.vo.ChattingRoomVO;
import com.mc.mctalk.vo.MessageVO;
import com.mc.mctalk.vo.UserVO;

public class ChattingClient {
	private String TAG = "ChattingClient : ";
	private Socket socket = null;
//	final private String SERVER_IP= "127.0.0.1";
	final private String SERVER_IP= "70.12.109.103";
	final private int SERVER_PORT= 8888;

	private BufferedWriter bw = null;
	private BufferedReader br = null;
	private UserVO loginUserVO;
	private String loginUserID;
	private Map<String, ChattingFrame> htChattingGUI; //채팅방 GUI 맵
	private Map<String, ChattingRoomVO> htRoomVO; //채팅방  맵
	private Map<String, MainFrame> htMainFrame; //채팅방 GUI 맵
	private Gson gson = new Gson();

	public ChattingClient(UserVO vo){
		this.loginUserVO = vo;
		this.loginUserID = vo.getUserID();
		this.htChattingGUI = new Hashtable<>();
		this.htRoomVO = new Hashtable<>();
		this.htMainFrame = new Hashtable<>();

		if(socket==null){
			startClient();
		}
	}
	//채팅 정보 전송을 위한 룸정보와 채팅방 유아이와 채팅 수/발신의 연결을 위한 세팅
	public void setChattingGUI(ChattingFrame chattingGUI, ChattingRoomVO roomVO){
		System.out.println(TAG + "setChattingGUI()");
		//채팅 창을 더블클릭했을때 선택된 방의 ID값을 맵에 저장
		this.htRoomVO.put(roomVO.getChattingRoomID(), roomVO);
		//채팅 창을 더블클릭했을때 선택된 방의 UI Frame값을 맵에 저장
		this.htChattingGUI.put(roomVO.getChattingRoomID(), chattingGUI);
	}
	
	//유저가 로그인할 때에 실행됨
	public void startClient(){
		try {
			socket = new Socket(InetAddress.getByName(SERVER_IP),SERVER_PORT);
			bw = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream(),"EUC_KR"));
			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream(),"EUC_KR"));
			
			//서버 연결 후 user 정보 JSON 전송.
			if(loginUserVO != null){
				String loginUserInfo = gson.toJson(loginUserVO);
				bw.write(loginUserInfo+ "\n");
				bw.flush();
			}
			new Reciever().start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//서버로 부터 메시지를 받는 내부 쓰레드 클래스
	class Reciever extends Thread{
		@Override
		public void run() {
			String reveiveMsg;
			try {
				while (true) {
					reveiveMsg = br.readLine();
					//JSON으로 받음
					MessageVO vo = gson.fromJson(reveiveMsg, MessageVO.class);
					//json으로 받은 메시지에 딸려온 roomID로 채팅방 맵에서 해당 UI를 찾아옴
					ChattingFrame cf = htChattingGUI.get(vo.getRoomVO().getChattingRoomID());
					htMainFrame.get(loginUserID).changePanel("chattingList");
					//해당 roomID를 가진 ChattingFrame으로 텍스트 전송
					if(cf != null){
						cf.recieveMessageFromChattingClient(reveiveMsg+"\n");
					}else if(cf==null){
						//// 열고자하는 채팅방 룸아이디에 해당하는 GUI가 없다면 예외가 발생하지 않음 조건문으로 서버에 반송 
						System.out.println("반송 시작  ");
						sendback(vo);
						System.out.println("반송 완료");
					}
				}
			} catch (IOException e) {
				stopClient();
//				e.printStackTrace();
			}
		}
	}
	public void sendback(MessageVO vo){
		try {
			//반송할때는 반송시키는 유저아이디를 입력시킨후 반송. 
			vo.setSendUserID(loginUserID);
			String messageInfo = gson.toJson(vo);
			bw.write(messageInfo+ "\n");
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void sendMsg(String roomID, String msg){
		try {
			if(roomID != null){
				MessageVO messageVO = new MessageVO();
				messageVO.setRoomVO(htRoomVO.get(roomID));
				messageVO.setSendUserID(loginUserID);
				messageVO.setSendUserName(loginUserVO.getUserName());
				messageVO.setMessage(msg);
				messageVO.setSendTime(getCurrentTimeForMsg());
				
				//Json 으로 변환 후 개행 치환 처리
				//그대로 보내면 서버에서 readLine으로 잘라서 인식해 버리므로
				//'/n'로 임시로 치환
				String messageInfo = gson.toJson(messageVO);
				messageInfo = messageInfo.replace("\\n", "/n");
				
				bw.write(messageInfo+ "\n");
				bw.flush();
			}else{
				System.out.println("roomVO is null!!!");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}// send() 메소드 종료	

	//로그 아웃시 연결 필요
	public void stopClient(){
		try {
			if(socket!=null && !socket.isClosed()){
				System.out.println(TAG + "stopClient()");
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public UserVO getLoginUserVO() {
		return loginUserVO;
	}
	public ChattingFrame getHtChattingGUI(String roomID) {
		System.out.println(TAG + "getHtChattingGUI()");
		ChattingFrame returnCf = htChattingGUI.get(roomID);
		return returnCf;
	}
	public void removeHtChattingGUI(String roomID) {
		System.out.println(TAG + "removeHtChattingGUI()");
		htChattingGUI.remove(roomID);
	}
	
	public MainFrame getHtMainFrame(String loginID) {
		MainFrame returnCrp = htMainFrame.get(loginID);
		return returnCrp;
	}
	public void setHtMainFrame(String loginID, MainFrame mainFrame) {
		this.htMainFrame.put(loginID, mainFrame);
	}
	
	//메시지 전송 시간 구하기
	public String getCurrentTimeForMsg(){
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strTime = dayTime.format(new Date(time));
		return strTime;
	}
}
