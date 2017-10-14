package pers.abaneo.xnote.api.dao;

import java.util.List;

import pers.abaneo.xnote.api.model.xnote.XNote;
import pers.abaneo.xnote.api.model.xnote.XNoteGroup;

public interface IXNoteDao {
    int deleteByPrimaryKey(Long id);

    int insert(XNote record);

    XNote selectByPrimaryKey(Long id);

    List<XNote> selectAll();
    
    List<XNote> selectByActiveAttr(XNote xnote,Integer limit);

    int updateByPrimaryKey(XNote record);
    
    List<XNoteGroup> selectGroupsByUserId(Long userId);
    
}