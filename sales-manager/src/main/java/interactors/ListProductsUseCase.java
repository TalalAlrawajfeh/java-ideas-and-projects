package interactors;

import adapters.UseCase;
import beans.Product;
import constants.PaginationUseCasesParameters;
import entities.ProductEntity;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ProductRepository;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by u624 on 3/31/17.
 */
public class ListProductsUseCase implements UseCase<Map<PaginationUseCasesParameters, Object>> {
    @Autowired
    ProductRepository productRepository;

    @Override
    public void execute(Map<PaginationUseCasesParameters, Object> parametersMap) throws UseCaseException {
        ListPaginationUseCase<Product, ProductEntity> listPaginationUseCase = new ListPaginationUseCase<>(productRepository);
        parametersMap.put(PaginationUseCasesParameters.COMPARATOR, Comparator.comparing(ProductEntity::getCode));
        listPaginationUseCase.execute(parametersMap);
    }
}
