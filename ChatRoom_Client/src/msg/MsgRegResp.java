package msg;

/*
 * ������Ӧ��ע��״̬��Ϣ��
 */
public class MsgRegResp extends MsgHead {
	private byte state;

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

}
