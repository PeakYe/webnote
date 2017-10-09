

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import pers.abaneo.xnote.api.dao.IXNoteGroupDao;
import pers.abaneo.xnote.api.model.xnote.XNoteGroup;

@RunWith(SpringRunner.class)
public class TestIXnoteGroupDao {

	@Autowired IXNoteGroupDao dao;
	
	@Test
	public void testInteface() throws Exception {
		XNoteGroup group=new XNoteGroup();
		group.setId(12L);
		group.setName("test node");
		group.setUserId(13L);
		dao.insert(group);
		System.out.println(group.getId());
		group.setName("zzzzzz");
		dao.updateByPrimaryKey(group);
		group=dao.selectByPrimaryKey(group.getId());
		System.out.println(group.getName());
	}
	
	
}
