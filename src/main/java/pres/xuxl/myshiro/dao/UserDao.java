package pres.xuxl.myshiro.dao;

import java.util.Set;

import pres.xuxl.myshiro.entity.Permission;
import pres.xuxl.myshiro.entity.Role;
import pres.xuxl.myshiro.entity.UserInfo;

public interface UserDao extends BaseDao<UserInfo, Long>{

	public UserInfo findByName(String name);
	public Set<Role> findRoles(Long userId);
	public Set<Permission> findPermissionByRoleId(Long roleId);
}
