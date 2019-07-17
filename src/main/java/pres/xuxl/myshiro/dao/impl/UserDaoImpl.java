package pres.xuxl.myshiro.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import pres.xuxl.myshiro.entity.Permission;
import pres.xuxl.myshiro.entity.Role;

@Repository("userDao")
public class UserDaoImpl {
	
	@PersistenceContext
    private EntityManager em;
	
	@SuppressWarnings("unchecked")
	public Set<Role> findRoles(Long userId){
		String sql="select a from Role a inner join UserRole b on a.id=b.roleId"+
				" where b.userId=?1";
		Query query = em.createQuery(sql,Role.class);
		query.setParameter(1, userId);
		List<Role> l = query.getResultList();
		if(l!=null)
			return new HashSet<Role>(l);
		return null;
	}
	
	public Set<Permission> findPermissionByRoleId(Long roleId){
		
		String sql="select a from Permission a inner join RolePermission b on a.id=b.permissionId"+
				" where b.roleId=?1";
		Query query = em.createQuery(sql,Permission.class);
		query.setParameter(1, roleId);
		List<Permission> l = query.getResultList();
		if(l!=null)
			return new HashSet<Permission>(l);
		return null;
	}
}
