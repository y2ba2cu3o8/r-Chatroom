package tools;

import dataBase.ThreadDB;
import server.ServerThread;

/*
 * ������߰������¹���
 * 1.����½�Ŀͻ���Ӧ���̱߳��浽�߳����ݿ�
 * 2.�����ߵĿͻ���Ӧ���̴߳����ݿ���ɾ��
 */
public class ThreadRegDelTool {
	// ע�ᵽ�߳����ݿ�
	public static void RegThread(ServerThread thread) {
		ThreadDB.threadDB.put(String.valueOf(thread.getUserJK()), thread);
	}

	// ���߳����ݿ��м�ɾ��
	public static void DelThread(int UserJK) {
//		System.out.println("Del JK");
		ThreadDB.threadDB.remove(String.valueOf(UserJK));
	}

}
