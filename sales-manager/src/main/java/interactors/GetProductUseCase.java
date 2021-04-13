package interactors;

import adapters.UseCase;
import constants.GetProductsUseCaseParameters;
import entities.ProductEntity;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ProductRepository;

import java.util.Map;
import java.util.Objects;

/**
 * Created by u624 on 3/31/17.
 */
public class GetProductUseCase implements UseCase<Map<GetProductsUseCaseParameters, Object>> {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void execute(Map<GetProductsUseCaseParameters, Object> parametersMap) throws UseCaseException {
        String code = (String) parametersMap.get(GetProductsUseCaseParameters.PRODUCT_CODE);
        if (Objects.isNull(code)) {
            throw new UseCaseException("Product code parameter cannot be null");
        }
        ProductEntity productEntity = productRepository.findByCode(code);
        if (Objects.isNull(productEntity)) {
            throw new UseCaseException("Product doesn't exist");
        }
        parametersMap.put(GetProductsUseCaseParameters.PRODUCT, productEntity.convert());
    }
}
