package pers.abaneo.xnote.service.xnote;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pers.abaneo.xnote.api.dao.IXNoteDao;
import pers.abaneo.xnote.api.dao.IXNoteGroupDao;
import pers.abaneo.xnote.api.model.user.User;
import pers.abaneo.xnote.api.model.xnote.XNote;
import pers.abaneo.xnote.api.model.xnote.XNoteGroup;
import pers.abaneo.xnote.api.service.IXNoteServie;
import pers.abaneo.xnote.service.support.BaseService;
import pers.abaneo.web.utils.model.ResultModel;

@Service
public class XnoteService extends BaseService implements IXNoteServie{
	@Autowired IXNoteDao xnoteDao;
	@Autowired IXNoteGroupDao dao;
	
	/* (non-Javadoc)
	 * @see pers.abaneo.xnote.service.xnote.IXnoteService#getUserXnote(java.lang.Long, java.lang.Long)
	 */
	@Override
	public XNote getUserXnote(Long id, User user){
		if(id==null||user==null)return null;
		XNote xnote=new XNote();
		xnote.setCreaterId(user.getId());
		xnote.setId(id);
		
		List<XNote> xnotelist = xnoteDao.selectByActiveAttr(xnote, 1);
		if(xnotelist!=null && xnotelist.size()>0){
			return xnotelist.get(0);
		}
		return null;
	}
	@Override
	public XNote getXnote(Long id){
		if(id==null)return null;
		XNote xnote=new XNote();
		xnote.setId(id);
		List<XNote> xnotelist = xnoteDao.selectByActiveAttr(xnote, 1);
		if(xnotelist!=null && xnotelist.size()>0){
			return xnotelist.get(0);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see pers.abaneo.xnote.service.xnote.IXnoteService#createXnote(java.lang.String, java.lang.String, pers.abaneo.userc.api.model.User)
	 */
	@Override
	public XNote createXnote(String title,String content,Long groupId, User user){
		if(title==null){
			logger.debug("title null");
			return null;
		}
		XNoteGroup group=dao.selectByPrimaryKey(groupId);
		if(group==null){
			logger.debug("group null");
			return null;
		}
		if(group.getUserId().longValue()!=user.getId().longValue()){
			logger.debug("group id != user id");
			return null;
		}
		XNote xnote=new XNote();
		xnote.setTitle(title);
		xnote.setContent(content);
		xnote.setCreateTime(new Date());
		xnote.setCreaterId(user.getId());
		xnote.setComments(0);
		xnote.setOpposes(0);
		xnote.setPraises(0);
		xnote.setDeleted(0);
		xnote.setGroupId(groupId);
		xnoteDao.insert(xnote);
		return xnote;
	}
	
	/* (non-Javadoc)
	 * @see pers.abaneo.xnote.service.xnote.IXnoteService#updateXnote(java.lang.Long, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public void updateXnote(Long id,String title,String content, User user){
		XNote xnote=getUserXnote(id, user);
		if(xnote==null){
			throw new RuntimeException("修改的结果为空");
		}
		xnote.setTitle(title);
		xnote.setContent(content);
		xnoteDao.updateByPrimaryKey(xnote);
	}
	
	/* (non-Javadoc)
	 * @see pers.abaneo.xnote.service.xnote.IXnoteService#getXnotesByUser(pers.abaneo.userc.api.model.User)
	 */
	@Override
	public List<XNote> getXnotesByUser( User user){
		XNote xnote=new XNote();
		xnote.setCreaterId(user.getId());
//		return xnoteDao.selectByPrimaryKey()
		XNote query=new XNote();
		query.setCreaterId(user.getId());
		return xnoteDao.selectByActiveAttr(query, null);
	}

	/* (non-Javadoc)
	 * @see pers.abaneo.xnote.service.xnote.IXnoteService#deleteXnote(java.lang.Long, java.lang.Long)
	 */
	@Override
	public ResultModel deleteXnote(Long id, User user) {
		XNote xnote=getUserXnote(id, user);
		if(xnote==null){
			return new ResultModel(false, "不存在改博客");
		}
		xnote.setDeleted(1);
		xnoteDao.updateByPrimaryKey(xnote);
		return new ResultModel(true);
	}

	@Override
	public List<XNoteGroup> getXnoteGroups(User user) {
		List<XNoteGroup> list = xnoteDao.selectGroupsByUserId(user.getId());
		if(list!=null){
			List<XNote> dels=new LinkedList<XNote>();
			for(XNoteGroup group:list){
				for(XNote note:group.getNotes()){
					if(note.getId()==null){
//						group.getNotes().remove(note);
						dels.add(note);
					}
				}
				group.getNotes().removeAll(dels);
			}
			
		}
		return list;
	}

	@Override
	public XNoteGroup createGroup(String name,Long groupId, User user) {
		XNoteGroup group=new XNoteGroup();
		group.setName(name);
		group.setParentId(groupId);
		group.setUserId(user.getId());
		dao.insert(group);
		return group;
	}
	
	@Override
	public void deleteGroup(Long id) {
		dao.deleteByPrimaryKey(id);
	}
	
}
