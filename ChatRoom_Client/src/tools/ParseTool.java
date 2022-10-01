package tools;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import msg.*;

/*
 * �������ڽ⹹��Ϣ
 */
public class ParseTool {

	/*
	 * �����ж�ȡ�̶����ȵ��ֽڣ�������ת��Ϊ�ַ���
	 * 
	 * @param dins: ��ȡ��������
	 * 
	 * @param len: ��ȡ���ֽڳ���
	 * 
	 * @return ת������ַ���
	 */
	private static String readString(DataInputStream dins, int len) throws IOException {
		byte[] data = new byte[len];
		dins.readFully(data);
		return new String(data).trim();
	}

	/*
	 * �˷������ڽ⹹��Ϣ
	 * 
	 * @param data �ֽ���Ϣ
	 * 
	 * @return ��Ϣ�⹹��ֵ��MsgHead������
	 */
	public static MsgHead parseMsg(byte[] data) throws IOException {
		int totalLen = data.length + 4; // ֮ǰ�Ѿ���ȡ��4���ֽڵĳ�����Ϣ
		ByteArrayInputStream bins = new ByteArrayInputStream(data);
		DataInputStream dins = new DataInputStream(bins);
		byte msgtype = dins.readByte();
		//System.out.println("Type"+msgtype);
		int dest = dins.readInt();
		int src = dins.readInt();
		if (msgtype == 0x01) {// �����ע����Ϣ
			String nikeName = readString(dins, 10);
			String pwd = readString(dins, 10);
			MsgReg mr = new MsgReg();
			mr.setTotalLen(totalLen);
			mr.setType(msgtype);
			mr.setDest(dest);
			mr.setSrc(src);
			mr.setNikeName(nikeName);
			mr.setPwd(pwd);
			return mr;
		}

		else if (msgtype == 0x11) { // �����ע�᷵����Ϣ
			byte state = dins.readByte();
			MsgRegResp mrr = new MsgRegResp();
			mrr.setTotalLen(totalLen);
			mrr.setType(msgtype);
			mrr.setDest(dest);
			mrr.setSrc(src);
			mrr.setState(state);
			return mrr;
		}

		else if (msgtype == 0x02) { // ����ǵ�½��Ϣ
			String pwd = readString(dins, 10);
			MsgLogin mli = new MsgLogin();
			mli.setTotalLen(totalLen);
			mli.setType(msgtype);
			mli.setDest(dest);
			mli.setSrc(src);
			mli.setPwd(pwd);
			return mli;
		}

		else if (msgtype == 0x22) { // ����ǵ�½������Ϣ
			byte state = dins.readByte();
			MsgLoginResp mlr = new MsgLoginResp();
			mlr.setTotalLen(totalLen);
			mlr.setType(msgtype);
			mlr.setDest(dest);
			mlr.setSrc(src);
			mlr.setState(state);
			return mlr;
		}

		else if (msgtype == 0x03) { // ����Ǻ����б�
			int i, j;

			String UserName = readString(dins, 10);// ��ȡ�û���
			int pic = dins.readInt();
			byte listCount = dins.readByte();// ��ȡ���ѷ������
			String listName[] = new String[listCount];
			byte bodyCount[] = new byte[listCount];

			int bodyNum[][];
			bodyNum = new int[listCount][];// ���õ�һά����
			
			int bodyPic[][];
			bodyPic = new int[listCount][];

			String nikeName[][];
			nikeName = new String[listCount][];// ���õ�һά����

			byte bodyState[][];
			bodyState = new byte[listCount][];// ���õ�һά����

			for (i = 0; i < listCount; i++) {
				listName[i] = readString(dins, 10);
				bodyCount[i] = dins.readByte();
				
				bodyNum[i] = new int[bodyCount[i]];// ���õڶ�ά����
				bodyPic[i] = new int[bodyCount[i]];
				nikeName[i] = new String[bodyCount[i]];// ���õڶ�ά����
				bodyState[i] = new byte[bodyCount[i]];// ���õڶ�ά����

				for (j = 0; j < bodyCount[i]; j++) {
					bodyNum[i][j] = dins.readInt();
					bodyPic[i][j] = dins.readInt();
					nikeName[i][j] = readString(dins, 10);
					bodyState[i][j] = dins.readByte();
				}

			}

			/*
			 * ��ȡ������д�����
			 */
			MsgTeamList mtl = new MsgTeamList();
			mtl.setUserName(UserName);
			mtl.setPic(pic);
			mtl.setTotalLen(totalLen);
			mtl.setType(msgtype);
			mtl.setDest(dest);
			mtl.setSrc(src);
			mtl.setListCount(listCount);
			mtl.setListName(listName);
			mtl.setBodyCount(bodyCount);
			mtl.setBodyNum(bodyNum);
			mtl.setBodyPic(bodyPic);
			mtl.setNikeName(nikeName);
			mtl.setBodyState(bodyState);

			return mtl;

		}
		
		else if (msgtype == 0x04) { //����Ǵ�����Ϣ
			MsgChatText mct = new MsgChatText();
			String msgText = readString(dins, totalLen-13);
			mct.setTotalLen(totalLen);
			mct.setType(msgtype);
			mct.setDest(dest);
			mct.setSrc(src);
			mct.setMsgText(msgText);
			
			return mct;
		}
		
		else if (msgtype == 0x05){ //��Ӻ���
			MsgAddFriend maf = new MsgAddFriend();
			int add_id = dins.readInt();
			String list_name = readString(dins, totalLen - 17);
			maf.setTotalLen(totalLen);
			maf.setType(msgtype);
			maf.setDest(dest);
			maf.setSrc(src);
			maf.setAdd_ID(add_id);
			maf.setList_name(list_name);
			return maf;
		}
		
		else if (msgtype == 0x55){ //��Ӻ��ѻ�ִ
			MsgAddFriendResp mafr = new MsgAddFriendResp();
			byte state = dins.readByte();
			mafr.setTotalLen(totalLen);
			mafr.setType(msgtype);
			mafr.setDest(dest);
			mafr.setSrc(src);
			mafr.setState(state);
			return mafr;
		}
		
		return null;

	}

}
