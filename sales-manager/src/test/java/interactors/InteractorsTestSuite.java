package interactors;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by u624 on 4/1/17.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AddProductsUseCaseTests.class,
        AddReceiptsUseCaseTests.class,
        DeleteProductUseCaseTests.class,
        DeleteReceiptsUseCaseTests.class,
        EditProductUseCaseTests.class,
        EditReceiptUseCaseTests.class,
        GetProductUseCaseTests.class,
        ListAllProductsUseCaseTests.class,
        ListAllReceiptsUseCaseTests.class,
        ListProductsUseCaseTests.class,
        ListReceiptsUseCaseTests.class,
        LoginUseCaseTests.class
})
public class InteractorsTestSuite {
}
