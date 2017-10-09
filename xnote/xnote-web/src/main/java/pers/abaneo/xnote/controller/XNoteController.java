package pers.abaneo.xnote.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import pers.abaneo.xnote.api.model.user.User;
import pers.abaneo.xnote.api.model.xnote.XNote;
import pers.abaneo.xnote.api.model.xnote.XNoteGroup;
import pers.abaneo.xnote.api.service.IXNoteServie;
import pers.abaneo.web.utils.fileuploader.FileManager;
import pers.abaneo.web.utils.model.ResultModel;

@RestController
@RequestMapping("service/xnote")
public class XNoteController {
	@Autowired
	IXNoteServie servie;

	@RequestMapping("/create")
	public ResultModel create(String content, String title,Long group, User user) {
		if (StringUtils.isEmpty(title)) {
			return new ResultModel(false, "标题不能为空");
		}
		XNote blog = servie.createBlog(title, content,group, user);
		return new ResultModel(true).setData(blog.getId());
	}

	@RequestMapping("/update")
	public ResultModel update(Long id, String content, String title, User user) {
		servie.updateBlog(id, title, content, user);
		return new ResultModel(true);
	}

	@RequestMapping("/delete")
	public ResultModel delete(Long id, User user) {
		return servie.deleteBlog(id, user);
	}

	@RequestMapping("/get")
	public ResultModel get(Long id, User user) {
		XNote blog = servie.getUserBlog(id, user);
		if (blog == null) {
			return new ResultModel(false, "null");
		} else {
			return new ResultModel(true).setData(blog);
		}
	}

	@RequestMapping("/list")
	public ResultModel list(User user) {
		List<XNote> list = servie.getBlogsByUser(user);
		return new ResultModel(true).setData(list);
	}
	
	@RequestMapping("/listGroup")
	public ResultModel listGroup(User user) {
		List<XNoteGroup> list = servie.getBlogGroups(user);
		return new ResultModel(true).setData(list);
	}

	@RequestMapping("/uploadImg")
	public ResultModel uploadImg(MultipartHttpServletRequest request) throws IllegalStateException, IOException {
		List<String> fileNames = FileManager.saveFiles(request);
		return new ResultModel(fileNames);
	}
	
	@RequestMapping("/group/create")
	public ResultModel createGroup(@RequestParam String name,Long parentId,User user){
		XNoteGroup group=servie.createGroup(name,parentId,user);
		return new ResultModel(group);
	}
	
	@RequestMapping("/group/delete")
	public ResultModel deleteGroup(@RequestParam Long id){
		servie.deleteGroup(id);
		return new ResultModel(true,"").setData(id);
	}

}
