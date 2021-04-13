package persistence.beans;

import java.io.Serializable;

public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Boolean childrenPermitted;

	public Category() {
		/* java beans should have a default constructor */
	}

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

	public Boolean getChildrenPermitted() {
		return childrenPermitted;
	}

	public void setChildrenPermitted(Boolean childrenPermitted) {
		this.childrenPermitted = childrenPermitted;
	}

	@Override
	public int hashCode() {
		return name.hashCode() + childrenPermitted.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Category) {
			Category category = (Category) obj;
			return category.name.equals(name) && category.childrenPermitted.equals(childrenPermitted);
		} else {
			return super.equals(obj);
		}
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", childrenPermitted=" + childrenPermitted + "]";
	}
}
