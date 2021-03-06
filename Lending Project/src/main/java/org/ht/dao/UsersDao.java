package org.ht.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.ht.pojo.Users;

public interface UsersDao {

    List<Users> queryUserslimits(Map<String, Object> map);
    List<Users> queryUserslimitss(@Param("unickname") String unickname);
    List<Users> userList();
	int insert(Users users);

	Users byNameFindUsers(@Param("unickname") String unickname, @Param("upassword") String upassword);
	/**
	 * @param map
	 * @explain users pageing query
	 */
	public List<Users> queryUser(Map<String, Object> map);
}
