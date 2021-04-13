package interactors;

import adapters.UseCase;
import beans.Receipt;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ReceiptRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

/**
 * Created by u624 on 3/25/17.
 */
public class ListAllReceiptsUseCase implements UseCase<List<Receipt>> {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Override
    public void execute(List<Receipt> receipts) throws UseCaseException {
        if (Objects.isNull(receipts)) {
            throw new UseCaseException("Receipts parameter cannot be null");
        }
        StreamSupport.stream(receiptRepository.findAll().spliterator(), false)
                .forEach(r -> receipts.add(r.convert()));
    }
}
