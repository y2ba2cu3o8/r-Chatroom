package tools;

import chatUI.DialogUI;
import dataBase.DialogDB;

/*
 * ����������ڴ������ݿ���ע�����ɾ���Ự����
 */
public class DialogRegDelTool {
	/*
	 * �����ݿ���ע��DialogUI
	 */
	public static void RegDialog(int JKNum,DialogUI dialog){
		DialogDB.dialogDB.put(String.valueOf(JKNum), dialog);
	}
	/*
	 * �����ݿ���ɾ��DialogUI
	 */
	public static void DelDialog(int JKNum){
		DialogDB.dialogDB.remove(String.valueOf(JKNum));
	}
}
