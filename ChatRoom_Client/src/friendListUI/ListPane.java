package friendListUI;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import dataBase.Figures;
import dataBase.ListInfo;
import dataBase.UserInfo;

/*
 * ����Ǻ����б��JPabel
 * ���캯����Ҫ��������б���Ϣ
 */
public class ListPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private ListInfo list;
	private Member user[][];
	private byte listCount ;
	private byte[] bodyCount;
	private byte[][] state;

	public ListPane(ListInfo list) {
		super();
		this.list = list;
		initialize();
	}

	private void initialize() {
		listCount = list.getListCount();
		String[] listName = list.getListName();
		bodyCount = list.getBodyCount();
		int[][] bodyNum = list.getBodyNum();
		int[][] bodyPic = list.getBodypic();
		String[][] nikeName = list.getNikeName();
		state = list.getBodyState();
		ListName[] list = new ListName[listCount];
		user = new Member[listCount][];
		int i, j;
		for (i = 0; i < listCount; i++) {
			user[i] = new Member[bodyCount[i]];
			for (j = 0; j < bodyCount[i]; j++) {
				int pic = bodyPic[i][j];
				int num = bodyNum[i][j];
				String name = nikeName[i][j];
				byte State = state[i][j];
				user[i][j] = new Member(pic, name, num, State);

			}
			list[i] = new ListName(listName[i], user[i]);
			this.add(list[i]);
			for (j = 0; j < bodyCount[i]; j++) {
				this.add(user[i][j]);
			}

		}
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setSize(272, 450);
//		this.setLocation(20, 5);
		this.setLocation(0, 0);
	}

	public UserInfo findUserByJK(int JKNum){
		UserInfo result = new UserInfo();
		for(int i = 0;i<listCount;i++){
			for(int j = 0; j<bodyCount[i];j++){
				if(user[i][j].getMemberJKNum() == JKNum){
					result.setJKNum(JKNum);
					result.setNickName(user[i][j].getNickname());
					result.setPic(user[i][j].getPic());
					break;
				}
			}
		}
		return result;
	}
	
	public void Hav_Mem_Msg(int JKNum){
		for(int i = 0;i<listCount;i++){
			for(int j = 0; j<bodyCount[i];j++){
				if(user[i][j].getMemberJKNum() == JKNum){
					user[i][j].hav_msg();
				}
			}
		}
	}
	
	public void Refresh_List(ListInfo new_list){
		byte new_listCount = new_list.getListCount();
		byte[] new_bodyCount = new_list.getBodyCount();
		byte[][] state = new_list.getBodyState();
		boolean has_new_member = false;
		boolean has_new_list = false;
		if(new_listCount == listCount){
			for(int i = 0; i< listCount;i++){
				if(new_bodyCount[i]!=bodyCount[i]){
					has_new_member = true;
					break;
				}
				for(int j = 0;j < bodyCount[i];j++){
					user[i][j].set_state(state[i][j]);
				}
			}
		}
		else{
			has_new_list = true;
		}
		if(has_new_member || has_new_list){
			this.removeAll();
			list = new_list;
			initialize();
			
		}
	}
}
