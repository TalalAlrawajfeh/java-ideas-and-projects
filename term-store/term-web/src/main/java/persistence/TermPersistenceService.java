package persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import persistence.beans.Category;
import persistence.beans.Term;
import persistence.jpa.entities.CategoryEntity;
import persistence.jpa.entities.TermEntity;

public class TermPersistenceService {
	private PersistenceContext persistenceContext = new PersistenceContext();
	private CategoryPersistenceService categoryPersistenceService = new CategoryPersistenceService();

	public List<Term> search(Term term) {
		List<EntityPersistenceField<?>> fields = new ArrayList<>();

		if (term.getName() != null)
			fields.add(new EntityPersistenceFieldImpl<String>("name", term.getName()));
		if (term.getLabel() != null)
			fields.add(new EntityPersistenceFieldImpl<String>("label", term.getLabel()));
		if (term.getPurpose() != null)
			fields.add(new EntityPersistenceFieldImpl<String>("purpose", term.getLabel()));
		if (term.getCategory() != null) {
			CategoryEntity categoryEntity = persistenceContext.getCategoryPersistenceProcessor()
					.findByFields(CategoryEntity.class,
							Arrays.asList(new EntityPersistenceFieldImpl<String>("name", term.getCategory().getName())))
					.get(0);
			fields.add(new EntityPersistenceFieldImpl<CategoryEntity>("category", categoryEntity));
		}

		return persistenceContext.getTermPersistenceProcessor().findByFields(TermEntity.class, fields).stream()
				.map(t -> t.acquireBean()).collect(Collectors.toList());
	}

	public Term searchById(Long id) {
		TermEntity termEntity = persistenceContext.getTermPersistenceProcessor().findById(TermEntity.class, id);
		return termEntity.acquireBean();
	}

	public void save(Term term) {
		TermEntity termEntity = termToTermEntity(term);
		persistenceContext.getTermPersistenceProcessor().save(termEntity);
		persistenceContext.getPersistenceProvider().commitChanges();
		persistenceContext.getPersistenceProvider().getPersistenceManager().refresh(getRootEntity());
	}

	public void update(Term term) {
		TermEntity termEntity = persistenceContext.getTermPersistenceProcessor().findById(TermEntity.class,
				term.getId());
		termEntity.setName(term.getName());
		termEntity.setLabel(term.getLabel());
		termEntity.setPurpose(term.getPurpose());
		termEntity.setCategory(categoryToCategoryEntity(categoryPersistenceService.search(term.getCategory())));

		persistenceContext.getTermPersistenceProcessor().update(termEntity);
		persistenceContext.getPersistenceProvider().commitChanges();
	}

	public void delete(Term term) {
		TermEntity termEntity = persistenceContext.getTermPersistenceProcessor().findById(TermEntity.class,
				term.getId());
		persistenceContext.getTermPersistenceProcessor().delete(termEntity);
		persistenceContext.getPersistenceProvider().commitChanges();
	}

	private TermEntity termToTermEntity(Term term) {
		if (Objects.isNull(term))
			return null;

		TermEntity termEntity = new TermEntity();
		termEntity.setId(term.getId());
		termEntity.setName(term.getName());
		termEntity.setLabel(term.getLabel());
		termEntity.setPurpose(term.getPurpose());
		termEntity.setCategory(categoryToCategoryEntity(term.getCategory()));
		termEntity.setParent(termToTermEntity(term.getParent()));

		return termEntity;
	}

	private CategoryEntity categoryToCategoryEntity(Category category) {
		if (Objects.isNull(category))
			return null;

		CategoryEntity categoryEntity = new CategoryEntity();
		categoryEntity.setId(category.getId());
		categoryEntity.setName(category.getName());
		categoryEntity.setChildrenPermitted(category.getChildrenPermitted());

		return categoryEntity;
	}

	public Term getRoot() {
		return getRootEntity().acquireBean();
	}

	private TermEntity getRootEntity() {
		return persistenceContext.getTermPersistenceProcessor()
				.findByFields(TermEntity.class, Arrays.asList(new EntityPersistenceFieldImpl<String>("name", "ROOT"),
						new EntityPersistenceFieldImpl<String>("label", "ROOT")))
				.get(0);
	}

	public boolean checkIfTermNameExists(Term term) {
		List<TermEntity> children = persistenceContext.getTermPersistenceProcessor()
				.findById(TermEntity.class, term.getParent().getId()).getChildren();
		return children.stream().filter(t -> term.getName().equalsIgnoreCase(t.getName())).findAny().isPresent();
	}

	public Term traverseRootDownToDepth(int depth) {
		TermEntity rootEntity = getRootEntity();
		Term term = rootEntity.acquireBean();
		traverseTreeDownToDepth(rootEntity, term, depth);
		return term;
	}

	public Term traverseTermByIdDownToDepth(Long id, int depth) {
		TermEntity termEntity = persistenceContext.getTermPersistenceProcessor().findById(TermEntity.class, id);
		if (Objects.isNull(termEntity))
			return null;
		Term term = termEntity.acquireBean();
		traverseTreeDownToDepth(termEntity, term, depth);
		return term;
	}

	public Term traverseTermByIdUpToRoot(Long id) {
		TermEntity termEntity = persistenceContext.getTermPersistenceProcessor().findById(TermEntity.class, id);
		if (Objects.isNull(termEntity))
			return null;
		return traverseTreeUpToRoot(termEntity, null);
	}

	private void traverseTreeDownToDepth(TermEntity termEntity, Term term, Integer depth) {
		if (depth == 0)
			return;
		term.setChildren(new ArrayList<>());
		for (Iterator<TermEntity> iterator = termEntity.getChildren().iterator(); iterator.hasNext();) {
			TermEntity childEntity = iterator.next();
			Term child = childEntity.acquireBean();
			term.getChildren().add(child);
			traverseTreeDownToDepth(childEntity, child, depth - 1);
		}
	}

	private Term traverseTreeUpToRoot(TermEntity termEntity, Term term) {
		TermEntity parentEntity = termEntity.getParent();
		Term currentTerm;

		if (term == null) {
			termEntity.getChildren();
			currentTerm = termEntity.acquireBean();
			currentTerm.setChildren(
					termEntity.getChildren().stream().map(t -> t.acquireBean()).collect(Collectors.toList()));
		} else {
			currentTerm = term;
		}

		if (Objects.nonNull(parentEntity)) {
			Term parent = parentEntity.acquireBean();

			parent.setChildren(parentEntity.getChildren().stream()
					.map(t -> currentTerm.getId().equals(t.getId()) ? currentTerm : t.acquireBean())
					.collect(Collectors.toList()));

			return traverseTreeUpToRoot(parentEntity, parent);
		}

		return currentTerm;
	}
}
