package interactors;

import beans.Receipt;
import entities.ReceiptEntity;
import entities.builders.ReceiptEntityBuilder;
import exceptions.UseCaseException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import persistence.ReceiptRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by u624 on 4/3/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ListAllReceiptsUseCaseTests {
    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private ListAllReceiptsUseCase listAllReceiptsUseCase = new ListAllReceiptsUseCase();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void GivenNullReceiptsListThenUseCaseExceptionShouldBeThrown() throws UseCaseException {
        expectUseCaseException("Receipts parameter cannot be null");
        listAllReceiptsUseCase.execute(null);
    }

    @Test
    public void GivenReceiptListThenAllReceiptEntitiesShouldBeAddedToIt() throws UseCaseException {
        List<Receipt> receipts = new ArrayList<>();
        ReceiptEntity firstReceiptEntity = new ReceiptEntityBuilder().setId(1L).build();
        ReceiptEntity secondReceiptEntity = new ReceiptEntityBuilder().setId(2L).build();
        List<ReceiptEntity> receiptEntities = Arrays.asList(firstReceiptEntity,
                secondReceiptEntity);
        Mockito.doReturn(receiptEntities).when(receiptRepository).findAll();
        listAllReceiptsUseCase.execute(receipts);
        assertEquals(2, receipts.size());
        assertTrue(receipts.contains(firstReceiptEntity.convert()));
        assertTrue(receipts.contains(secondReceiptEntity.convert()));
    }

    private void expectUseCaseException(String expectedMessage) {
        expectedException.expect(UseCaseException.class);
        expectedException.expectMessage(expectedMessage);
    }
}
