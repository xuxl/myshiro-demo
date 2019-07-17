package pres.xuxl.myshiro.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name="shiro_role_permission")
@DynamicInsert
public class RolePermission {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="role_id")
	private Long roleId;
	
	@Column(name="permission_id")
	private Long permissionId;

}
