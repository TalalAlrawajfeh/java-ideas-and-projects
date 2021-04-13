package interactors;

import adapters.UseCase;
import beans.Receipt;
import constants.PaginationUseCasesParameters;
import entities.ReceiptEntity;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ReceiptRepository;
import utilities.DateUtility;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by u624 on 3/30/17.
 */
public class ListReceiptsUseCase implements UseCase<Map<PaginationUseCasesParameters, Object>> {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Override
    public void execute(Map<PaginationUseCasesParameters, Object> parametersMap) throws UseCaseException {
        ListPaginationUseCase<Receipt, ReceiptEntity> listPaginationUseCase = new ListPaginationUseCase<>(receiptRepository);
        parametersMap.put(PaginationUseCasesParameters.COMPARATOR, Comparator.<ReceiptEntity>comparingLong(r -> -DateUtility.parseDate(r.getDate()).getTime()));
        listPaginationUseCase.execute(parametersMap);
    }
}
