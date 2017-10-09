package pers.abaneo.xnote.api.dao;

import pers.abaneo.xnote.api.model.xnote.XNoteGroup;

public interface IXNoteGroupDao {
    int deleteByPrimaryKey(Long id);

    int insert(XNoteGroup record);

    XNoteGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKey(XNoteGroup record);
    
}