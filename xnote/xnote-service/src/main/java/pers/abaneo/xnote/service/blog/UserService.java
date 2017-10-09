package pers.abaneo.xnote.service.blog;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import pers.abaneo.web.utils.model.ResultModel;
import pers.abaneo.xnote.api.dao.IUserDao;
import pers.abaneo.xnote.api.model.user.User;
import pers.abaneo.xnote.api.service.IUserService;
import pers.abaneo.xnote.api.service.IXNoteServie;

@Service
public class UserService implements IUserService {

	@Autowired
	IUserDao dao;
	
	@Autowired
	IXNoteServie noteService;

	@Override
	public User getUser(Long id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<User> getUserList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultModel registerUser(User user) {
		if (StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getPassword())
				|| StringUtils.isEmpty(user.getName())) {
			return new ResultModel(false, "ERR_PARAM").setErrCode(1000);
		}
		User query = new User();
		// valid email
		query.setEmail(user.getEmail());
		User exist = dao.selectByActiveAttr(query);
		if (exist != null) {
			return new ResultModel(false, "电子邮件已注册").setErrCode(1001);
		}
		query.setEmail(null);
		// valid name
		query.setName(user.getName());
		exist = dao.selectByActiveAttr(query);
		if (exist != null) {
			return new ResultModel(false, "用户名已注册").setErrCode(1002);
		}

		User user2 = new User();
		user2.setName(user.getName());
		user2.setPassword(user.getPassword());
		user2.setEmail(user.getEmail());
		user2 = createUser(user2);
		if (user2 != null) {
			noteService.createGroup("常用文件夹",null, user2);
			noteService.createGroup("临时文件",null, user2);
			noteService.createGroup("垃圾箱",null, user2);
			
			return new ResultModel(true).setData(user2.getId());
		} else {
			return new ResultModel("ERR").setSuccess(false).setErrCode(1003);
		}
	}

	private User createUser(User user) {
		user.setCreateTime(new Date());
		dao.insert(user);
		return user;
	}

	public User getUserByActiveAttr(User user) {
		return dao.selectByActiveAttr(user);
	}

}
