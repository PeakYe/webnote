package pers.abaneo.xnote.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	private FileManager fileManager;
	
	@Value("${file.upload.path}")
	private String uploadPath;

	@RequestMapping("/create")
	public ResultModel create(String content, String title,Long group, User user) {
		if (StringUtils.isEmpty(title)) {
			return new ResultModel(false, "标题不能为空");
		}
		XNote xnote = servie.createXnote(title, content,group, user);
		if(xnote==null){
			return new ResultModel(false);
		}
		return new ResultModel(true).setData(xnote.getId());
	}

	@RequestMapping("/update")
	public ResultModel update(Long id, String content, String title, User user) {
		servie.updateXnote(id, title, content, user);
		return new ResultModel(true);
	}
	
	@RequestMapping("/update/move")
	public ResultModel update(Long id, Long to, User user) {
		servie.moveXnote(id,to, user);
		return new ResultModel(true);
	}

	@RequestMapping("/delete")
	public ResultModel delete(Long id, User user) {
		return servie.deleteXnote(id, user);
	}

	@RequestMapping("/detail")
	public ResultModel get(Long id, User user) {
		XNote xnote = servie.getUserXnote(id, user);
		if (xnote == null) {
			return new ResultModel(false, "null");
		} else {
			return new ResultModel(true).setData(xnote);
		}
	}

	@RequestMapping("/detaillist")
	public ResultModel list(User user) {
		List<XNote> list = servie.getXnotesByUser(user);
		return new ResultModel(true).setData(list);
	}
	
	@RequestMapping("/view")
	public ResultModel view(Long id) {
		XNote xnote = servie.getXnote(id);
		if (xnote == null) {
			return new ResultModel(false, "null");
		} else {
			return new ResultModel(true).setData(xnote);
		}
	}
	
	@RequestMapping("/group/detaillist")
	public ResultModel listGroup(User user) {
		List<XNoteGroup> list = servie.getXnoteGroups(user);
		return new ResultModel(true).setData(list);
	}

	@RequestMapping("/upload/img")
	public ResultModel uploadImg(String imgData){
		if(this.fileManager==null){
			fileManager=new FileManager();
			fileManager.setUploadPath(uploadPath);
		}
		File file = fileManager.saveCodeImg(imgData);
		return new ResultModel(file.getName());
	}
	
	@RequestMapping("{fileName}.{suffix}")
	public void readImg(@PathVariable("fileName") String fileName, @PathVariable("suffix") String suffix,
			HttpServletResponse response) throws IOException {
		if(this.fileManager==null){
			fileManager=new FileManager();
			fileManager.setUploadPath(uploadPath);
		}
//		response.setContentType("image/*");
		fileManager.readFile(fileName+"."+suffix, response);
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
