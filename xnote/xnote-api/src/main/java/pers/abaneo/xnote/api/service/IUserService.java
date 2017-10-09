package pers.abaneo.xnote.api.service;

import java.util.List;

import pers.abaneo.web.utils.model.ResultModel;
import pers.abaneo.xnote.api.model.user.User;

public interface IUserService {
	public ResultModel registerUser(User user);

	public User getUser(Long id);

	public User getUserByActiveAttr(User user);

	public List<User> getUserList();
}
