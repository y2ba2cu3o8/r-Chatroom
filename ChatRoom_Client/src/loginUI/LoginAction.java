package loginUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.ChatClient;
import dataBase.Figures;
import friendListUI.FriendListUI;


/**
 * LoginAction
 * ��½�ڼ䰴��������
 * ��½��ע�ᶼ���ô˼�����
 * ���Լ���������ť
 * ʵ�ֵ�½��ע�ᡢ�ύע����Ϣ�Ĺ���
 * @author He11o_Liu
 *
 */
public class LoginAction implements ActionListener {

	private JTextField userid_field;// Login�����ID��
	private JPasswordField password_field;// Login���������
//	private JTextField NikeName;// Register������ǳ�
//	private JTextField Rpassword;// Register���������
	private ChatClient cc; //LoginUI���������������ӷ�������ChatClient
	private boolean is_Registering = false;// ������������ж��Ƿ��Ѿ�����ע�����
	public static JFrame LoginJF;//��½�����Լ�ע������JF ���ڹرմ���

	public ChatClient getCc() {
		return cc;
	}

	public void setCc(ChatClient cc) {
		this.cc = cc;
	}

	public JTextField getUsername() {
		return userid_field;
	}

	public void setUsername(JTextField username) {
		this.userid_field = username;
	}

	public JPasswordField getPassword() {
		return password_field;
	}

	public void setPassword(JPasswordField password) {
		this.password_field = password;
	}

	public boolean isIs_Registering() {
		return is_Registering;
	}

	public void setIs_Registering(boolean is_Registering) {
		this.is_Registering = is_Registering;
	}

	/**
	 * ����������
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton jb = (JButton) e.getSource();
		//�����µİ���Ϊ��½
		if (jb.getText().equals("Login")) {
			int userid = Integer.valueOf(userid_field.getText());
			if (userid_field.getText().equals("")) {//������ID��Ϊ��
				JOptionPane.showMessageDialog(null, "ID��Ϊ��", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				String password = new String(password_field.getPassword());
				if (password.equals(""))
					JOptionPane.showMessageDialog(null, "���벻Ϊ��", "Error", JOptionPane.ERROR_MESSAGE);
				else{
					int result = cc.Login(userid, password) ;
					if(result == 0){//��������ȷ
						Figures.cc = cc;
						new FriendListUI();
						LoginJF.dispose();
					}
					else if (result == 1){//���������
						JOptionPane.showMessageDialog(null, "�û������������", "Error", JOptionPane.ERROR_MESSAGE);
						password_field.setText("");
					}
					else if(result == 2){
						JOptionPane.showMessageDialog(null, "���û��Ѿ���½", "Error", JOptionPane.ERROR_MESSAGE);
						password_field.setText("");
						userid_field.setText("");
					}
				}
			}
		} 
		//�����µİ���Ϊע�ᰴ��
		else if (jb.getText().equals("Register")) {
			//System.out.println("One click");
			if (!is_Registering) {
				new RegisterUI(this,cc); // ����ǰ����������ȥ
				is_Registering = true;
			}
		}
	}

}
