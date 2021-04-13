package persistence.jpa.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import persistence.PersistenceEntity;
import persistence.beans.Term;

@Entity
@Table(name = "terms")
public class TermEntity implements Serializable, PersistenceEntity<Term> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "term_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "term_name")
	private String name;

	@Column(name = "term_label")
	private String label;

	@Column(name = "term_purpose")
	private String purpose;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "term_category")
	private CategoryEntity category;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "term_parent")
	private TermEntity parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private List<TermEntity> children;

	public TermEntity() {
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

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	public TermEntity getParent() {
		return parent;
	}

	public void setParent(TermEntity parent) {
		this.parent = parent;
	}

	public List<TermEntity> getChildren() {
		return children;
	}

	public void setChildren(List<TermEntity> children) {
		this.children = children;
	}

	@Override
	public Term acquireBean() {
		Term term = new Term();

		term.setId(id);
		term.setName(name);
		term.setLabel(label);
		term.setPurpose(purpose);

		if (!Objects.isNull(category))
			term.setCategory(category.acquireBean());
		if (!Objects.isNull(parent))
			term.setParent(parent.acquireBean());

		return term;
	}
}
