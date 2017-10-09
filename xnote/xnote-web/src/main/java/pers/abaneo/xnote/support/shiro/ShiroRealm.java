package pers.abaneo.xnote.support.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import pers.abaneo.xnote.api.model.user.User;
import pers.abaneo.xnote.api.service.IUserService;

public class ShiroRealm extends AuthorizingRealm{

	@Autowired IUserService service;
	/**
	 * 获取授权，即权限信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		String username=(String) pc.fromRealm(getName()).iterator().next();
		if(username!=null){
			SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
			info.addStringPermission("A");
			return info;
		}
		return null;
	}

	/**
	 * 获取认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken atoken) throws AuthenticationException {
		UsernamePasswordToken utoken=(UsernamePasswordToken) atoken;
		String username=utoken.getUsername();
		if(username!=null){
			//get user
			User query=new User();
			query.setName(username);
			User user=service.getUserByActiveAttr(query);
			
			if(user!=null){
				return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
			}
		}
		return null;
	}
}
