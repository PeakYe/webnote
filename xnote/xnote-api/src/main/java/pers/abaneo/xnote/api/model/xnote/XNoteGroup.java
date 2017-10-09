package pers.abaneo.xnote.api.model.xnote;

import java.util.List;

public class XNoteGroup {
	private Long id;
	private String name;
	private Long parentId;
	private Long userId;
	private List<XNote> notes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<XNote> getNotes() {
		return notes;
	}

	public void setNotes(List<XNote> notes) {
		this.notes = notes;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
