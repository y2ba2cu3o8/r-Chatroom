package dataBase;

import java.util.HashMap;
import java.util.Map;
import server.ServerThread;

/*
 * ��������������̵߳����ݿ�
 * ����Hash Map������
 * ֻ���ڵ�½��Ż����������ݿ�
 */
public class ThreadDB {
	public static Map<String, ServerThread> threadDB = new HashMap<String, ServerThread>();
}
