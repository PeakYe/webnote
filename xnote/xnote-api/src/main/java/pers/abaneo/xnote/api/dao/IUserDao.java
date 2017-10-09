package pers.abaneo.xnote.api.dao;

import java.util.List;

import pers.abaneo.xnote.api.model.user.User;


public interface IUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    User selectByPrimaryKey(Long id);
    User selectByActiveAttr(User user);

    List<User> selectAll();

    int updateByPrimaryKey(User record);
}
