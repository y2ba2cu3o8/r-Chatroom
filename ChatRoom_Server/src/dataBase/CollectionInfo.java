package dataBase;

import java.sql.*;
import java.util.*;

/**
 * Created by hcyue on 2016/12/3.
 */
public class CollectionInfo {
    private int ownerJK;
    private List<UserInfo> members;
    private String name;
    private int id;

    /**
     * CollectionInfo
     * ��collection���ж�ȡ�����ݹ���info����
     * @param rs
     * @throws SQLException
     */
    CollectionInfo(ResultSet rs) throws SQLException {
        ownerJK = rs.getInt("user_id");
        name = rs.getString("name");
        id = rs.getInt("collection_id");
    }
    
    /**
     * getMembers
     * ��ȡһ��collection�еĶ���
     * @return List<UserInfo>
     * @throws Exception
     */
    public List<UserInfo> getMembers() throws Exception {
        if (members == null) {
            DBConnection conn = DBConnection.getInstance();
            UserModel userModel = new UserModel(conn);
            members = userModel.getUsersInCollection(id);
            conn.close();
        }
        return members;
    }
    
    /**
     * �������
     */
    public String toString() {
        return String.format("Collection: %s, id: %d, ownerJK: %d", name, id, ownerJK);
    }
    

    public int getOwnerJK() {
        return ownerJK;
    }

    public void setOwnerJK(int ownerJK) {
        this.ownerJK = ownerJK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
