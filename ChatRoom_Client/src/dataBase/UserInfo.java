package dataBase;

/*
 * �û�������Ϣ
 */
public class UserInfo {
	/*
	 * �û�������Ϣ
	 */

	private int JKNum;// ������û���JK��
	private String NickName;// ������û����ǳ�
	private String PassWord;// ������û���IP
	private int Pic;

	/*
	 * �û�������Ϣ
	 */

	private byte listCount;// �����ж��������
	private String ListName[];// ����ÿ�����������
	private byte[] bodyCount;// ÿ���ж��ٸ���
	private int bodyNum[][];// ÿ�����ѵ�JK��
	private int bodypic[][];//����ͷ��
	private String nikeName[][];// ÿ�����ѵ��ǳ�

	public byte getListCount() {
		return listCount;
	}

	public void setListCount(byte listCount) {
		this.listCount = listCount;
	}

	public String[] getListName() {
		return ListName;
	}

	public void setListName(String[] listName) {
		ListName = listName;
	}

	public byte[] getBodyCount() {
		return bodyCount;
	}

	public void setBodyCount(byte[] bodyCount) {
		this.bodyCount = bodyCount;
	}

	public int[][] getBodyNum() {
		return bodyNum;
	}

	public void setBodyNum(int[][] bodyNum) {
		this.bodyNum = bodyNum;
	}

	public String[][] getNikeName() {
		return nikeName;
	}

	public void setNikeName(String[][] nikeName) {
		this.nikeName = nikeName;
	}

	public int getJKNum() {
		return JKNum;
	}

	public void setJKNum(int jKNum) {
		JKNum = jKNum;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getPassWord() {
		return PassWord;
	}

	public void setPassWord(String passWord) {
		PassWord = passWord;
	}

	public boolean equals(UserInfo compare) {
		if (compare.getJKNum() == JKNum && compare.getPassWord().equals(PassWord)) {
			return true;
		}
		return false;
	}

	public int[][] getBodypic() {
		return bodypic;
	}

	public void setBodypic(int bodypic[][]) {
		this.bodypic = bodypic;
	}

	public int getPic() {
		return Pic;
	}

	public void setPic(int pic) {
		Pic = pic;
	}
}
