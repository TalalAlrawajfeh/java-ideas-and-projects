package interactors;

import adapters.Convertable;
import adapters.UseCase;
import constants.PaginationUseCasesParameters;
import exceptions.UseCaseException;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by u624 on 3/31/17.
 */
public class ListPaginationUseCase<B, E extends Convertable<B>> implements UseCase<Map<PaginationUseCasesParameters, Object>> {
    private static final int PAGE_SIZE = 50;

    private CrudRepository<E, Serializable> repository;

    public ListPaginationUseCase(CrudRepository<E, Serializable> repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Map<PaginationUseCasesParameters, Object> parametersMap) throws UseCaseException {
        Iterable<E> entities = repository.findAll();
        if (Objects.nonNull(entities)) {
            long entitiesCount = StreamSupport.stream(entities.spliterator(), true).count();
            setTotalNumberOfPages(parametersMap, entitiesCount);
            prepareAndSetPageItems(parametersMap, entities, entitiesCount);
        } else {
            parametersMap.put(PaginationUseCasesParameters.TOTAL_NUMBER_OF_PAGES, 0);
        }
    }

    private void setTotalNumberOfPages(Map<PaginationUseCasesParameters, Object> paginationUseCasesParameters, long entities) {
        paginationUseCasesParameters.put(PaginationUseCasesParameters.TOTAL_NUMBER_OF_PAGES,
                getTotalNumberOfPages(entities));
    }

    private int getTotalNumberOfPages(float receiptsCount) {
        return (int) Math.ceil(receiptsCount / (float) PAGE_SIZE);
    }

    private Integer getPageNumber(Map<PaginationUseCasesParameters, Object> parametersMap) {
        return (Integer) parametersMap.get(PaginationUseCasesParameters.PAGE_NUMBER);
    }

    private List<E> getList(Iterable<E> entities) {
        return StreamSupport.stream(entities.spliterator(), true)
                .collect(Collectors.toList());
    }

    private void prepareAndSetPageItems(Map<PaginationUseCasesParameters, Object> parametersMap, Iterable<E> entities, long entitiesCount) {
        List<E> entitiesList = getList(entities);
        entitiesList.sort((Comparator<? super E>) parametersMap.get(PaginationUseCasesParameters.COMPARATOR));
        parametersMap.put(PaginationUseCasesParameters.PAGE_ITEMS_LIST,
                getReceiptsPage(entitiesCount, getPageNumber(parametersMap), entitiesList));
    }

    private List<?> getReceiptsPage(long entitiesCount, Integer pageNumber, List<E> entitiesList) {
        List<B> entitiesPage = new ArrayList<>();
        for (int i = (pageNumber - 1) * PAGE_SIZE; i < pageNumber * PAGE_SIZE && i < entitiesCount; i++) {
            entitiesPage.add(entitiesList.get(i).convert());
        }
        return entitiesPage;
    }
}
