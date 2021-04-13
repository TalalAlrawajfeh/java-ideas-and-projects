package persistence.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import persistence.PersistenceEntity;
import persistence.beans.Category;

@Entity
@Table(name = "categories")
public class CategoryEntity implements Serializable, PersistenceEntity<Category> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "category_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@Column(name = "category_name")
	private String name;

	@Column(name = "children_permitted")
	private Boolean childrenPermitted;

	public CategoryEntity() {
		/* jpa entities must have default constructors */
	}

	@Override
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
	public Category acquireBean() {
		Category category = new Category();

		category.setId(id);
		category.setName(name);
		category.setChildrenPermitted(childrenPermitted);

		return category;
	}
}
