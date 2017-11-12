package pers.abaneo.xnote.api.service;

import java.util.List;

import pers.abaneo.xnote.api.model.user.User;
import pers.abaneo.xnote.api.model.xnote.XNote;
import pers.abaneo.xnote.api.model.xnote.XNoteGroup;
import pers.abaneo.web.utils.model.ResultModel;

public interface IXNoteServie {

	XNote getUserXnote(Long xnoteId,  User user);
	
	XNote getXnote(Long xnoteId);

	XNote createXnote(String title, String content,Long groupId, User User);

	void updateXnote(Long id, String title, String content, User user);
	void moveXnote(Long id, Long to, User user);

	List<XNote> getXnotesByUser(User User);
	
	List<XNoteGroup> getXnoteGroups(User user);

	ResultModel deleteXnote(Long id, User user);

	XNoteGroup createGroup(String name,Long parentGroupId, User user);

	void deleteGroup(Long id);

}