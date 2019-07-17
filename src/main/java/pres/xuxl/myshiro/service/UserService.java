package pres.xuxl.myshiro.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pres.xuxl.myshiro.dao.PermissionDao;
import pres.xuxl.myshiro.dao.UserDao;
import pres.xuxl.myshiro.entity.Permission;
import pres.xuxl.myshiro.entity.Role;
import pres.xuxl.myshiro.entity.UserInfo;

@Service("userService")
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PermissionDao permissionDao;
	
	public UserInfo findByName(String userName){
		return userDao.findByName(userName);
	}
	
	public Set<Role> findRoles(Long userId){
		
		return userDao.findRoles(userId);
	}
	
	public Set<Permission> findPermissionByRoleId(Long roleId){
		
		return userDao.findPermissionByRoleId(roleId);
	}
	
	public List<Permission> findAllPermission(){
		return permissionDao.findAll();
	}
	
	public Map<String, String> loadPermissionFromDB(Map<String, String> filterMap){
		
		List<Permission> list = permissionDao.findAll();
		if(list != null){
			for(Permission per : list){
				filterMap.put(per.getUrl(), "perms["+per.getName()+"]");
			}
		}
		return filterMap;
	}

}
