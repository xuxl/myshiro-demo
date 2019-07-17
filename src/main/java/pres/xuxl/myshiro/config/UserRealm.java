package pres.xuxl.myshiro.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import pres.xuxl.myshiro.entity.Permission;
import pres.xuxl.myshiro.entity.Role;
import pres.xuxl.myshiro.entity.UserInfo;
import pres.xuxl.myshiro.service.UserService;

/**
 * 自定义Realm，实现授权与认证
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 用户授权
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
        PrincipalCollection principalCollection) {
        
        System.out.println("===执行授权===");

        Subject subject = SecurityUtils.getSubject();
        UserInfo user = (UserInfo)subject.getPrincipal();
        if(user != null){
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            // 角色与权限字符串集合
            Collection<String> rolesCollection = new HashSet<>();
            Collection<String> premissionCollection = new HashSet<>();
			// 读取并赋值用户角色与权限
            Set<Role> roles = userService.findRoles(user.getId());
            for(Role role : roles){
                rolesCollection.add(role.getName());
                Set<Permission> permissions = userService.findPermissionByRoleId(role.getId());
                for (Permission permission : permissions){
                    premissionCollection.add(permission.getName());
                }
                info.addStringPermissions(premissionCollection);
            }
            info.addRoles(rolesCollection);
            return info;
        }
        return null;
    }

    /**
     * 用户认证
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
        AuthenticationToken authenticationToken) throws AuthenticationException {

        System.out.println("===执行认证===");

        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        UserInfo user = userService.findByName(token.getUsername());//

        if(user == null){
            throw new UnknownAccountException();
        }

        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getName());

        return new SimpleAuthenticationInfo(user, user.getPassword(),
                credentialsSalt, getName());
    }
    
    /**
     * shiro刷新权限
     */
    public static void reloadAuthorizing() {
           
    	RealmSecurityManager rsm = (RealmSecurityManager)SecurityUtils.getSecurityManager();
    	UserRealm realm = (UserRealm)rsm.getRealms().iterator().next();
    	realm.clearAuthz();
    }
    
    public void clearAuthz(){
    	this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
    

    // 模拟Shiro用户加密，假设用户密码为aa
    public static void main(String[] args){
        // 用户名
        String username = "aa";
        // 用户密码
        String password = "aa";
        // 加密方式
        String hashAlgorithName = "MD5";
        // 加密次数
        int hashIterations = 1024;
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        Object obj = new SimpleHash(hashAlgorithName, password, 
                                    credentialsSalt, hashIterations);
        System.out.println(obj);
    }
}
