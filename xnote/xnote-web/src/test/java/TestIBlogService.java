
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pers.abaneo.xnote.XnoteWeb;
import pers.abaneo.xnote.api.model.user.User;
import pers.abaneo.xnote.api.model.xnote.XNote;
import pers.abaneo.xnote.api.service.IXNoteServie;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=XnoteWeb.class)
public class TestIBlogService {

	@Autowired IXNoteServie service;
	
	@Test
	public void testInteface() throws Exception {
		User user=new User();
		XNote blog=service.createBlog("test blog","xxxxxxxxxxx",12L,user);
		System.out.println("blog create success"+blog.getId());
		
		service.updateBlog(blog.getId(), "test blog update", "ssssssssss", user);
		System.out.println("blog update success");
		
		service.deleteBlog(blog.getId(), user);
		System.out.println("blog delte success");
	}
	
	@Test
	public void testNimei() throws Exception {
		
	}
}
