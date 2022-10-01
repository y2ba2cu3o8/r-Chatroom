package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

import dataBase.*;
import msg.*;
import tools.*;

public class ServerThread extends Thread {
    public boolean isSending = false;
    private Socket client;
    private OutputStream ous;
    private int userJK;
    private boolean isOnline = false;
//	private UserModel model;

    public int getUserJK() {
        return userJK;
    }

    public ServerThread(Socket client) {
        this.client = client;
    }

    public void run() {
        while (!isOnline) { // ���߳��пͻ���δ��½
            try {
                processLogin();
            } catch (Exception e) {

				/*
                 * �ͻ��˶Ͽ�����
				 */
                System.out.println(client.getRemoteSocketAddress() + "�ѶϿ�");
                isOnline = false;

                try {
                    client.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            }
        }
        while (isOnline) { // ���߳��пͻ����ѵ�½
            //��ʼ�����б�
            try {
                processChat();
            } catch (Exception e) {
				/*
				 * �ͻ��˶Ͽ�����
				 */
                System.out.println(client.getRemoteSocketAddress() + "�ѶϿ�");
                ThreadRegDelTool.DelThread(userJK);// ���߳����ݿ��м�ɾ��������Ϣ
                isOnline = false;
                try {
                    broadcastState();
                } catch (SQLException | IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                try {
                    client.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            }
        }
    }

    /*
     * �÷������ڴ���ӿͻ��˴���������Ϣ (δ��¼)
     */
    private void processLogin() throws Exception {
        //connect to DataBase
        DBConnection conn = DBConnection.getInstance();
        UserModel model = new UserModel(conn);

        ous = client.getOutputStream();
        InputStream ins = client.getInputStream();
        DataInputStream dis = new DataInputStream(ins);

        MsgHead msg = MsgHead.readMessageFromStream(dis);

		/*
		 * ��������Բ�ͬ����Ϣ���д���
		 */

        // �������������ע����Ϣ
        if (msg.getType() == 0x01) {
            MsgReg mr = (MsgReg) msg;

            // ע���û�

            UserInfo newUser = model.createUser(mr.getPwd(), mr.getNikeName(), 1);
            int JKNum = newUser.getJKNum();

			/*
			 * ������׼��������Ϣ
			 */
            byte state = 0;
            MsgRegResp mrr = new MsgRegResp(JKNum, state);
            mrr.send(ous);
        }

        // ����������ǵ�½��Ϣ
        else if (msg.getType() == 0x02) {
            MsgLogin ml = (MsgLogin) msg;

            byte checkmsg;// ��������״̬��Ϣ
            if (ThreadDB.threadDB.containsKey(String.valueOf(ml.getSrc()))) {//�Ѿ�������
                checkmsg = 2;
            } else if (model.userAuthorization(ml.getSrc(), ml.getPwd())) {// �����֤���û�����
                checkmsg = 0;
            } else {
                checkmsg = 1;
            }

			/*
			 * ������׼��������Ϣ
			 */
            MsgLoginResp mlr = new MsgLoginResp(checkmsg);
            mlr.send(ous);

			/*
			 * �����½������ɣ� ���ͺ����б�
			 */
            if (checkmsg == 0) {
                userJK = ml.getSrc();
                ThreadRegDelTool.RegThread(this); // ���߳����ݿ���ע������߳�
                sendFriendList();
                isOnline = true;// �����ѵ�¼�ͻ���
                broadcastState();
            }

        }
        conn.close();

    }

    private void broadcastState() throws SQLException, IOException {
        DBConnection conn = DBConnection.getInstance();
        UserModel model = new UserModel(conn);
        UserInfo user = model.getUserByJK(userJK);
        for (int i = 0; i < user.getCollectionCount(); i++) {
            for (int j = 0; j < user.getBodyCount()[i]; j++) {
                ServerThread st = ThreadDB.threadDB.get(String.valueOf(user.getBodyNum()[i][j]));
                if (st != null) {
                    st.sendFriendList();
                }
            }
        }
        conn.close();
    }

    /*
     * �÷������ڴ���ӿͻ��˴���������Ϣ (�ѵ�¼)
     */
    public void processChat() throws Exception {
        InputStream ins = client.getInputStream();
        DataInputStream dis = new DataInputStream(ins);

        int totalLen = dis.readInt();
        byte[] data = new byte[totalLen - 4];
        dis.readFully(data);
        MsgHead msg = ParseTool.parseMsg(data);// �������Ϣ

		/*
		 * ��������Բ�ͬ����Ϣ���д���
		 */

        if (msg.getType() == 0x04) {//����յ����Ƿ�����Ϣ����
            MsgChatText mct = (MsgChatText) msg;
            int from = mct.getSrc();
            int to = mct.getDest();
            String msgText = mct.getMsgText();
//			System.out.println("Sending Test!!");
//			System.out.println("From "+from+" To "+to+" Text "+msgText);

            if (!ChatTool.sendMsg(from, to, msgText)) {
                System.out.println("SaveOnServer");

                //���浽��������
                ChatTool.saveOnServer(from, to, msgText);
            }
        } else if (msg.getType() == 0x05) {//����ܵ���Ӻ��ѵ�����
            System.out.println("Add friend request");
            MsgAddFriend maf = (MsgAddFriend) msg;
            int own_jk = maf.getSrc();
            int add_jk = maf.getAdd_ID();
            String list_name = maf.getList_name();
            DBConnection conn = DBConnection.getInstance();
            UserModel model = new UserModel(conn);
            int result = model.addFriend(add_jk, own_jk, list_name);
            System.out.println("Add finish " + result);
            MsgAddFriendResp mafr = new MsgAddFriendResp();
            mafr.setSrc(Figures.ServerJK);
            mafr.setDest(own_jk);
            mafr.setTotalLen(14);
            mafr.setType((byte) 0x55);
            if (result == 0) {//success
                model.addFriend(own_jk, add_jk, "����Ӻ���");
                //send add_jk new list
                mafr.setState((byte) 0);
                //send own_jk new list
            } else if (result == 1) {//�����������
                mafr.setState((byte) 1);
            } else if (result == 2) {//����Ѿ������������
                mafr.setState((byte) 2);
            } else if (result == 3) {//�����б�ʧ��
                mafr.setState((byte) 3);
            }
            mafr.send(ous);

            sendFriendList();

            //send Add_JK Friend list
            model.addFriend(own_jk, add_jk, list_name);
            //��������߸����б�
            ServerThread st = ThreadDB.threadDB.get(String.valueOf(add_jk));
            if (st != null) {
                st.sendFriendList();
            }
            conn.close();
        }

    }

    /**
     * ���ͺ����б�
     *
     * @throws IOException
     * @throws SQLException
     */
    private void sendFriendList() throws IOException, SQLException {
        System.out.println("���ͺ����б�");

        DBConnection conn = DBConnection.getInstance();
        UserModel model = new UserModel(conn);
        UserInfo user = model.getUserByJK(userJK);
        MsgTeamList mtl = new MsgTeamList(user);
        mtl.send(ous);
        conn.close();
    }
	
	/*
	 * �÷����������û�����������������Ϣ
	 */

    public void sendMsg(int from, String msg) throws IOException {
        MsgChatText mct = new MsgChatText(from, userJK, msg);
        mct.send(ous);
    }
}
