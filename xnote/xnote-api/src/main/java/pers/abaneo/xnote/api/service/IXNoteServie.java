package pers.abaneo.xnote.api.service;

import java.util.List;

import pers.abaneo.xnote.api.model.user.User;
import pers.abaneo.xnote.api.model.xnote.XNote;
import pers.abaneo.xnote.api.model.xnote.XNoteGroup;
import pers.abaneo.web.utils.model.ResultModel;

public interface IXNoteServie {

	XNote getUserBlog(Long blogId,  User user);

	XNote createBlog(String title, String content,Long groupId, User User);

	void updateBlog(Long id, String title, String content, User user);

	List<XNote> getBlogsByUser(User User);
	
	List<XNoteGroup> getBlogGroups(User user);

	ResultModel deleteBlog(Long id, User user);

	XNoteGroup createGroup(String name,Long parentGroupId, User user);

	void deleteGroup(Long id);

}