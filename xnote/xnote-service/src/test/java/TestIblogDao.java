
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import pers.abaneo.xnote.api.dao.IXNoteDao;
import pers.abaneo.xnote.api.model.xnote.XNote;

@RunWith(SpringRunner.class)
public class TestIblogDao {

	@Autowired IXNoteDao dao;
	
	@Test
	public void testInteface() throws Exception {
		dao.selectAll();
		XNote blog=new XNote();
		blog.setCreaterId(2000L);
		List<XNote> list=dao.selectByActiveAttr(blog, 1);
		System.out.println(list);
	}
	
	
}
