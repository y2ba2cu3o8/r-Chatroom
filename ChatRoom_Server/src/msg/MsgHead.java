package msg;

import tools.PackageTool;
import tools.ParseTool;

import java.io.*;

/*
 * ��ϢͷΪ���е���Ϣ�Ĺ�����
 */
public class MsgHead {
	private int totalLen;
	private byte type;
	private int dest;
	private int src;

	public int getTotalLen() {
		return totalLen;
	}

	public void setTotalLen(int totalLen) {
		this.totalLen = totalLen;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	/**
	 * �� `socket` �������ж�ȡ�������� Message
	 * @param stream ������
	 * @return �ͻ��˷��͵���Ϣ
	 * @throws IOException IO�쳣
	 */
	public static MsgHead readMessageFromStream(DataInputStream stream) throws IOException {
		int totalLen = stream.readInt();
		byte[] data = new byte[totalLen - 4];
		stream.readFully(data);
		MsgHead message = ParseTool.parseMsg(data);// �������Ϣ
		return message;
	}
	public void send(OutputStream outputStream) throws IOException {
		byte[] message = this.packMessage();// ���������Ϣ���
		outputStream.write(message);
		outputStream.flush();
	}

	protected void packMessageHead(DataOutputStream dous) throws IOException {
		dous.writeInt(getTotalLen());
		dous.writeByte(getType());
		dous.writeInt(getDest());
		dous.writeInt(getSrc());
	}
	protected void writeString(DataOutputStream dous, int len, String s) throws IOException {
		byte[] data = s.getBytes();
		if (data.length > len) {
			throw new IOException("д�볤�ȳ���");
		}
		dous.write(data);
		while (data.length < len) {
			dous.writeByte('\0');
			len--;
		}
	}
	public byte[] packMessage() throws IOException {
		ByteArrayOutputStream bous = new ByteArrayOutputStream();
		DataOutputStream dous = new DataOutputStream(bous);
		packMessageHead(dous);
		dous.flush();
		byte[] data = bous.toByteArray();
		return data;
	}
}
