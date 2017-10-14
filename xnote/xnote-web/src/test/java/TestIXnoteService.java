
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
public class TestIXnoteService {

	@Autowired IXNoteServie service;
	
	@Test
	public void testInteface() throws Exception {
		User user=new User();
		XNote xnote=service.createXnote("test xnote","xxxxxxxxxxx",12L,user);
		System.out.println("xnote create success"+xnote.getId());
		
		service.updateXnote(xnote.getId(), "test xnote update", "ssssssssss", user);
		System.out.println("xnote update success");
		
		service.deleteXnote(xnote.getId(), user);
		System.out.println("xnote delte success");
	}
	
	@Test
	public void testNimei() throws Exception {
		
	}
}
