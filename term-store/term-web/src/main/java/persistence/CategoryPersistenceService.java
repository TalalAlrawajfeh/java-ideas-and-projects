package persistence;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import persistence.beans.Category;
import persistence.exceptions.PersistenceServiceException;
import persistence.jpa.entities.CategoryEntity;

public class CategoryPersistenceService {
	private PersistenceContext persistenceContext = new PersistenceContext();

	public List<Category> listAll() {
		List<? extends PersistenceEntity<Category>> categoryEntities = persistenceContext
				.getCategoryPersistenceProcessor().findAll(CategoryEntity.class, 0);

		return categoryEntities.stream().map(c -> c.acquireBean()).collect(Collectors.toList());
	}

	public Category search(Category category) {
		List<CategoryEntity> categories = persistenceContext.getCategoryPersistenceProcessor().findByFields(
				CategoryEntity.class, Arrays.asList(new EntityPersistenceFieldImpl<>("name", category.getName())));

		return categories.isEmpty() ? null : categories.get(0).acquireBean();
	}

	public void save(Category category) throws PersistenceServiceException {
		checkIfCategoryExists(category);

		persistenceContext.getCategoryPersistenceProcessor().save(categoryToCategoryEntity(category));
		persistenceContext.getPersistenceProvider().commitChanges();
	}

	private void checkIfCategoryExists(Category category) throws PersistenceServiceException {
		Category searchResult = search(category);
		if (searchResult != null) {
			if (category.getName().equalsIgnoreCase(searchResult.getName())) {
				throw new PersistenceServiceException("A category with the same name already exists.");
			}
		}
	}

	public void update(Category oldCategory, Category newCategory) {
		CategoryEntity newCategoryEntity = categoryToCategoryEntity(newCategory);
		newCategoryEntity.setId(search(oldCategory).getId());

		persistenceContext.getCategoryPersistenceProcessor().update(newCategoryEntity);
		persistenceContext.getPersistenceProvider().commitChanges();
	}

	private CategoryEntity categoryToCategoryEntity(Category category) {
		CategoryEntity categoryEntity = new CategoryEntity();
		categoryEntity.setId(category.getId());
		categoryEntity.setName(category.getName());
		categoryEntity.setChildrenPermitted(category.getChildrenPermitted());

		return categoryEntity;
	}

	public void delete(Category category) {
		persistenceContext.getCategoryPersistenceProcessor().deleteById(CategoryEntity.class, search(category).getId());
		persistenceContext.getPersistenceProvider().commitChanges();
	}
}
