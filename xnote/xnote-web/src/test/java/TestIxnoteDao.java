
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pers.abaneo.xnote.XnoteWeb;
import pers.abaneo.xnote.api.dao.IXNoteDao;
import pers.abaneo.xnote.api.model.xnote.XNote;
import pers.abaneo.xnote.api.model.xnote.XNoteGroup;


@RunWith(SpringRunner.class)
@SpringBootTest(classes=XnoteWeb.class)
public class TestIxnoteDao {

	@Autowired IXNoteDao dao;
	
//	@Test
	public void testInteface() throws Exception {
		dao.selectAll();
		XNote xnote=new XNote();
		xnote.setCreaterId(2000L);
		List<XNote> list=dao.selectByActiveAttr(xnote, 1);
		System.out.println(list);
	}
	
	@Test
	public void testSelectGroupsByUserId(){
		List<XNoteGroup> groups=dao.selectGroupsByUserId(20L);
		if(groups==null){
			System.out.println("null");
			return;
		}
		for(XNoteGroup group:groups){
			System.out.println("group"+group.getName());
			System.out.println(group.getName());
		}
	}
	
	
}
