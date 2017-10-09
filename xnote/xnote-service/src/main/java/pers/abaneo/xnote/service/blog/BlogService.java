package pers.abaneo.xnote.service.blog;

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
public class BlogService extends BaseService implements IXNoteServie{
	@Autowired IXNoteDao blogDao;
	@Autowired IXNoteGroupDao dao;
	
	/* (non-Javadoc)
	 * @see pers.abaneo.xnote.service.blog.IBlogService#getUserBlog(java.lang.Long, java.lang.Long)
	 */
	@Override
	public XNote getUserBlog(Long id, User user){
		if(id==null||user==null)return null;
		XNote blog=new XNote();
		blog.setCreaterId(user.getId());
		blog.setId(id);
		
		List<XNote> bloglist = blogDao.selectByActiveAttr(blog, 1);
		if(bloglist!=null && bloglist.size()>0){
			return bloglist.get(0);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see pers.abaneo.xnote.service.blog.IBlogService#createBlog(java.lang.String, java.lang.String, pers.abaneo.userc.api.model.User)
	 */
	@Override
	public XNote createBlog(String title,String content,Long groupId, User user){
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
		XNote blog=new XNote();
		blog.setTitle(title);
		blog.setContent(content);
		blog.setCreateTime(new Date());
		blog.setCreaterId(user.getId());
		blog.setComments(0);
		blog.setOpposes(0);
		blog.setPraises(0);
		blog.setDeleted(0);
		blog.setGroupId(groupId);
		blogDao.insert(blog);
		return blog;
	}
	
	/* (non-Javadoc)
	 * @see pers.abaneo.xnote.service.blog.IBlogService#updateBlog(java.lang.Long, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public void updateBlog(Long id,String title,String content, User user){
		XNote blog=getUserBlog(id, user);
		if(blog==null){
			throw new RuntimeException("修改的结果为空");
		}
		blog.setTitle(title);
		blog.setContent(content);
		blogDao.updateByPrimaryKey(blog);
	}
	
	/* (non-Javadoc)
	 * @see pers.abaneo.xnote.service.blog.IBlogService#getBlogsByUser(pers.abaneo.userc.api.model.User)
	 */
	@Override
	public List<XNote> getBlogsByUser( User user){
		XNote blog=new XNote();
		blog.setCreaterId(user.getId());
//		return blogDao.selectByPrimaryKey()
		XNote query=new XNote();
		query.setCreaterId(user.getId());
		return blogDao.selectByActiveAttr(query, null);
	}

	/* (non-Javadoc)
	 * @see pers.abaneo.xnote.service.blog.IBlogService#deleteBlog(java.lang.Long, java.lang.Long)
	 */
	@Override
	public ResultModel deleteBlog(Long id, User user) {
		XNote blog=getUserBlog(id, user);
		if(blog==null){
			return new ResultModel(false, "不存在改博客");
		}
		blog.setDeleted(1);
		blogDao.updateByPrimaryKey(blog);
		return new ResultModel(true);
	}

	@Override
	public List<XNoteGroup> getBlogGroups(User user) {
		List<XNoteGroup> list = blogDao.selectGroupsByUserId(user.getId());
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
