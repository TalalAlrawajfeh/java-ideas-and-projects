package persistence.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Term implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String label;
	private String purpose;

	private Category category;
	private Term parent;
	private List<Term> children;

	public Term() {
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Term getParent() {
		return parent;
	}

	public void setParent(Term parent) {
		this.parent = parent;
	}

	public List<Term> getChildren() {
		return children;
	}

	public void setChildren(List<Term> children) {
		this.children = children;
	}

	@Override
	public int hashCode() {
		return name.hashCode() + label.hashCode() + purpose.hashCode() + category.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Term) {
			Term term = (Term) obj;
			return term.getName().equals(name) && term.getLabel().equals(label) && term.purpose.equals(purpose)
					&& areTermCategoriesEqual(term);
		} else {
			return super.equals(obj);
		}
	}

	private boolean areTermCategoriesEqual(Term term) {
		return Objects.isNull(term.category) ? Objects.isNull(category) : term.category.equals(category);
	}

	@Override
	public String toString() {
		return "Term [id=" + id + ", name=" + name + ", label=" + label + ", purpose=" + purpose + ", category="
				+ category + ", parent=" + parent + ", children=" + children + "]";
	}
}
