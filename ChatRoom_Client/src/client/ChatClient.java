package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import dataBase.Figures;
import dataBase.ListInfo;
import msg.*;
import tools.DialogTool;
import tools.PackageTool;
import tools.ParseTool;

/**
 * ChatClient �������������������ͨ��
 * 
 * @author He11o_Liu
 */
public class ChatClient extends Thread {
	private String ServerIP;
	private int port;
	private Socket client;
	private static int OwnJKNum;// ����½�ɹ��󣬾͸�ChatClient��ΨһJK��
	private InputStream ins;
	private OutputStream ous;

	/**
	 * ChatClient�Ĺ�������
	 * 
	 * @param ServerIP
	 *            ��������IP
	 * @param port
	 *            �˿�
	 */
	public ChatClient(String ServerIP, int port) {
		this.ServerIP = ServerIP;
		this.port = port;
	}

	/**
	 * ���ϵĴ���ӷ�������������Ϣ
	 */
	public void run() {
		while (true) {
			try {
				processMsg();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("������������������Ͽ�����");
				JOptionPane.showMessageDialog(null, "��������Ͽ�����", "ERROR", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}

	/**
	 * ���ӵ�������
	 * 
	 * @return �Ƿ����ӵ�������
	 */
	public boolean ConnectServer() {
		try {
			client = new Socket(ServerIP, port);
			System.out.println("������������");
			ins = client.getInputStream();
			ous = client.getOutputStream();// ��ȡ�����ӵ����������
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return false;
	}

	/**
	 * Register ע���û�
	 * 
	 * @param NikeName
	 *            �ǳ�
	 * @param PassWord
	 *            ����
	 * @return ע��״̬
	 */
	public boolean Reg(String NikeName, String PassWord) {
		try {
			MsgReg mr = new MsgReg();
			int len = 33; // MsgReg�ĳ���Ϊ�̶���33
			byte type = 0x01; // MsgReg����Ϊ0x01

			// ����MsgReg�Ĳ���
			mr.setTotalLen(len);
			mr.setType(type);
			mr.setDest(Figures.ServerJK); // ��������JK��
			mr.setSrc(Figures.LoginJK);
			mr.setNikeName(NikeName);
			mr.setPwd(PassWord);

			// ���MsgReg
			byte[] sendMsg = PackageTool.packMsg(mr);
			ous.write(sendMsg);

			// ���շ������ķ�����Ϣ
			byte[] data = receiveMsg();

			// ������ת��Ϊ��
			MsgHead recMsg = ParseTool.parseMsg(data);

			if (recMsg.getType() != 0x11) {// ���ǻ�Ӧע����Ϣ
				System.out.println("ͨѶЭ�����");
				return false;
			}

			MsgRegResp mrr = (MsgRegResp) recMsg;
			// System.out.println("TestHere"+recMsg.getDest());
			if (mrr.getState() == 0) {
				/*
				 * ע��ɹ�
				 */
				// System.out.println("ע���JK��Ϊ" + mrr.getDest());
				JOptionPane.showMessageDialog(null, "ע��ɹ�\nJK��Ϊ" + mrr.getDest());
				return true;
			} else {
				/*
				 * ע��ʧ��
				 */
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("��������Ͽ�����");
		return false;
	}

	/**
	 * Login ����������͵�½����
	 * 
	 * @param id
	 * @param pwd
	 * @return �ܷ��½
	 */
	public int Login(int id, String pwd) {
		try {
			MsgLogin ml = new MsgLogin();
			int len = 23;
			byte type = 0x02;

			// ����MsgLogin�ĸ��ֶ���
			ml.setTotalLen(len);
			ml.setType(type);
			ml.setDest(Figures.ServerJK);
			ml.setSrc(id);
			ml.setPwd(pwd);
			// ���MsgLogin
			byte[] sendmsg = PackageTool.packMsg(ml);
			ous.write(sendmsg);
			// ���շ������ķ�����Ϣ
			byte[] data = receiveMsg();
			// ������ת��Ϊ��
			MsgHead recMsg = ParseTool.parseMsg(data);
			if (recMsg.getType() != 0x22) {// ���ǵ�½������Ϣ
				System.out.println("ͨѶЭ�����");
				return 5;
			}
			MsgLoginResp mlr = (MsgLoginResp) recMsg;
			byte resp = mlr.getState();
			if (resp == 0) {
				System.out.println("��½�ɹ�");
				OwnJKNum = id;
				return 0;
			} else if (resp == 1) {
				System.out.println("JK�Ż��������");
				return 1;
			} else if(resp == 2){
				System.out.println("����˺��Ѿ���½");
				return 2;
			} else {
				System.out.println("δ֪����");
				return 3;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("��������Ͽ�����");
		return 4;
	}

	/**
	 * ListInfo 
	 * ���պ����б�
	 * 
	 * @return
	 * @throws IOException
	 */
	public ListInfo getlist() throws IOException {
		byte[] data = receiveMsg();
		MsgHead recMsg = ParseTool.parseMsg(data);
		if (recMsg.getType() != 0x03) {
			System.out.println("ͨѶЭ�����");
			System.exit(0);
		}
		return packlist(recMsg);
	}

	public ListInfo packlist(MsgHead recMsg){
		ListInfo list = new ListInfo();
		MsgTeamList mtl = (MsgTeamList) recMsg;
		list.setNickName(mtl.getUserName());
		list.setJKNum(mtl.getDest());
		list.setPic(mtl.getPic());
		list.setListCount(mtl.getListCount());
		list.setListName(mtl.getListName());
		list.setBodyCount(mtl.getBodyCount());
		list.setBodyNum(mtl.getBodyNum());
		list.setBodypic(mtl.getBodyPic());
		list.setNikeName(mtl.getNikeName());
		list.setBodyState(mtl.getBodyState());
		return list;
	}
	
	/**
	 * �������������Ӻ���
	 * @param add_id
	 * @param list_name
	 * @return 0  �ɹ�
	 * @return 1 ���޴���
	 * @return 2 ���д���
	 * @return 3 ��������
	 * @return 4 ͨѶЭ�����
	 * @throws IOException
	 */
	public void SendaddFriend(int add_id, String list_name) throws IOException {
		MsgAddFriend maf = new MsgAddFriend();
		byte data[] = list_name.getBytes();
		int TotalLen = 17;
		TotalLen += data.length;
		byte type = 0x05;
		maf.setTotalLen(TotalLen);
		maf.setType(type);
		maf.setDest(Figures.ServerJK);
		maf.setSrc(OwnJKNum);
		maf.setAdd_ID(add_id);
		maf.setList_name(list_name);
		byte[] sendMsg = PackageTool.packMsg(maf);
		ous.write(sendMsg);
		ous.flush();
	}

//		System.out.println("Before");
//		// ���շ������ķ�����Ϣ
//		byte[] resp = receiveMsg();
//		
//		System.out.println("After");
//		// ������ת��Ϊ��
//		MsgHead recMsg = ParseTool.parseMsg(resp);
//		if (recMsg.getType() != 0x55) {// ���ǵ�½������Ϣ
//			System.out.println("ͨѶЭ�����");
//			return 4;
//		}
//		MsgAddFriendResp mafr = (MsgAddFriendResp) recMsg;
//		byte resp_state = mafr.getState();
//		if (resp_state == 0x00) {
//			System.out.println("��ӳɹ�");
//			return 0;
//		} else if (resp_state == 0x01) {
//			System.out.println("���޴���");
//			return 1;
//		} else if (resp_state == 0x02) {
//			System.out.println("���д���");
//			return 2;
//		} else if (resp_state == 0x03) {
//			System.out.println("����ͨѶ�б����");
//			return 3;
//		} else {
//			System.out.println("ͨѶЭ�����");
//		}
//		return 4;

	/**
	 * sendMsg ������Ϣ
	 * 
	 * @param to
	 * @param Msg
	 * @throws IOException
	 */
	public void sendMsg(int to, String Msg) throws IOException {
		MsgChatText mct = new MsgChatText();
		byte data[] = Msg.getBytes();
		int TotalLen = 13;
		TotalLen += data.length;
		byte type = 0x04;
		mct.setTotalLen(TotalLen);
		mct.setType(type);
		mct.setDest(to);
		mct.setSrc(OwnJKNum);
		mct.setMsgText(Msg);

		byte[] sendMsg = PackageTool.packMsg(mct);
		ous.write(sendMsg);
		ous.flush();
	}

	/*
	 * ����������ڴ��������м��ȡһ�����ȵ���Ϣ ��Ϣ����Ϊ��ǰ���һ������
	 * 
	 * @return byte[]�����ĳ�����Ϣ
	 */
	public byte[] receiveMsg() throws IOException {
		DataInputStream dis = new DataInputStream(ins);
		int totalLen = dis.readInt();
		System.out.println("TotalLen"+totalLen);
		// ��ȡtotalLen���ȵ�����
		byte[] data = new byte[totalLen - 4];
		dis.readFully(data);
		return data;
	}

	/**
	 * processMsg ���ܷ�������������Ϣ
	 * 
	 * @throws IOException
	 */
	public void processMsg() throws IOException {
		byte[] data = receiveMsg();
		// ������ת��Ϊ��
		MsgHead recMsg = ParseTool.parseMsg(data);
		byte MsgType = recMsg.getType();

		// ���ݲ�ͬ����Ϣ���д���
		if (MsgType == 0x04) {
			MsgChatText mct = (MsgChatText) recMsg;
			int from = mct.getSrc();
			String Msg = mct.getMsgText();
			DialogTool.ShowMessage(from, Msg);
		}
		else if(MsgType == 0x03){//���º����б�
			System.out.println("Refresh list");
			ListInfo list = packlist(recMsg);
			Figures.list.Refresh_List(list);
		}
		if (MsgType == 0x55){
//			System.out.println("Here");
			MsgAddFriendResp mafr = (MsgAddFriendResp) recMsg;
			byte result = mafr.getState();
			System.out.println("Add Friend Result "+result);
			if(Figures.afu != null){
//				System.out.println("To show Result");
				Figures.afu.showResult(result);
			}
		}
	}

	/*
	 * ���д����Ϊ���Դ���
	 */
	/*
	 * ���ڲ����Ƿ�ɹ������б�
	 */
	public void printList(ListInfo list) {
		System.out.println("Nick " + list.getNickName());
		System.out.print(" JK " + list.getJKNum());
		System.out.print(" Pic " + list.getPic());
		byte listCount = list.getListCount();
		System.out.println("listCount " + listCount);
		String[] listName = list.getListName();
		byte[] bodyCount = list.getBodyCount();
		int[][] bodyNum = list.getBodyNum();
		int[][] bodyPic = list.getBodypic();
		String[][] nikeName = list.getNikeName();
		byte[][] state = list.getBodyState();
		int i, j;
		for (i = 0; i < listCount; i++) {
			System.out.print("ListName " + listName[i]);
			System.out.println(" bodyCount " + bodyCount[i]);
			for (j = 0; j < bodyCount[i]; j++) {
				System.out.print(" JK " + bodyNum[i][j]);
				System.out.print(" Pic " + bodyPic[i][j]);
				System.out.print(" nikeName " + nikeName[i][j]);
				System.out.print(" State " + state[i][j]);
			}
			System.out.print("\n");
		}
	}

	/*
	 * �������Կͻ���
	 */
//
//	public static void main(String[] args) throws IOException {
//		System.out.println("���Կͻ��˿���");
//		ChatClient cc = new ChatClient("localhost", 9090);
//		if (cc.ConnectServer()) {
//			System.out.println("���ӷ��������");
//			int result = cc.Login(0, "123");
//			if (result==0){
//				System.out.println("��½���Գɹ�");
//				cc.printList(cc.getlist());
//				//cc.start();
//				result = cc.addFriend(5, "�µ��б�");
//				System.out.println(result);
//			} else {
//				System.out.println("��½ʧ��");
//			}
//		} else {
//			System.out.println("�޷����ӷ�����");
//		}
//
//	}

}
