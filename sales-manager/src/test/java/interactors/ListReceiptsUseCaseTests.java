package interactors;

import beans.Receipt;
import beans.builders.ReceiptBuilder;
import constants.PaginationUseCasesParameters;
import entities.ReceiptEntity;
import entities.builders.ReceiptEntityBuilder;
import exceptions.UseCaseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import persistence.ReceiptRepository;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by u624 on 3/31/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ListReceiptsUseCaseTests {
    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private ListReceiptsUseCase listReceiptsUseCase = new ListReceiptsUseCase();
    Map<PaginationUseCasesParameters, Object> paginationUseCasesParametersObjectMap;

    @Before
    public void setup() {
        paginationUseCasesParametersObjectMap = new EnumMap<>(PaginationUseCasesParameters.class);
    }

    @Test
    public void WhenNoReceiptsExistThenTotalNumberOfPagesShouldBeZeroAndPageItemsNull() throws Exception {
        Mockito.doReturn(null).when(receiptRepository).findAll();
        executeListReceiptsUseCase(1);
        assertEquals(0, getTotalNumberOfPages());
        assertEquals(null, getPageItemsList());
    }

    @Test
    public void GivenPageNumberGreaterThanNumberOfPagesThenPageItemsListShouldBeEmpty() throws Exception {
        Mockito.doReturn(Collections.singletonList(new ReceiptEntityBuilder()
                .setDate("01-01-2017 00:00:00")
                .build())).when(receiptRepository).findAll();
        executeListReceiptsUseCase(2);
        assertEquals(0, getPageItemsList().size());
    }

    @Test
    public void GivenUnorderedReceiptsListThenReceiptsShouldBeOrderedFromNewToOld() throws Exception {
        Mockito.doReturn(getUnorderedReceiptEntities()).when(receiptRepository).findAll();
        executeListReceiptsUseCase(1);
        List<Receipt> expectedOrderedReceipts = getOrderedReceipts();
        List<Receipt> actualOrderedReceipts = getPageItemsList();
        for (int i = 0; i < expectedOrderedReceipts.size(); i++) {
            assertEquals(expectedOrderedReceipts.get(i).getDate(),
                    actualOrderedReceipts.get(i).getDate());
        }
    }

    @Test
    public void Given140ReceiptsThenThereShouldBe3PagesAndAllPagesShouldCountainExactly50PagesExceptLastPageShouldContainAtMost50Receipts() throws Exception {
        Mockito.doReturn(get140ReceiptEntities()).when(receiptRepository).findAll();
        executeListReceiptsUseCase(1);
        assertEquals(3, getTotalNumberOfPages());
        assertEquals(50, getPageItemsCount());
        executeListReceiptsUseCase(2);
        assertEquals(50, getPageItemsCount());
        executeListReceiptsUseCase(3);
        assertEquals(40, getPageItemsCount());
    }

    @Test
    public void GivenReceiptsWithDifferentDatesThenPagesShouldContainReceiptsInCorrectPositions() throws Exception {
        Mockito.doReturn(getReceiptEntitiesWithDifferentDates()).when(receiptRepository).findAll();
        executeListReceiptsUseCase(1);
        List<Receipt> receipts = getPageItemsList();
        assertEquals("09-01-2017 00:00:00", receipts.get(0).getDate());
        assertEquals("08-01-2017 00:00:00", receipts.get(1).getDate());
        assertEquals("08-01-2017 00:00:00", receipts.get(48).getDate());
        assertEquals("07-01-2017 00:00:00", receipts.get(49).getDate());
        executeListReceiptsUseCase(2);
        receipts = getPageItemsList();
        assertEquals("06-01-2017 00:00:00", receipts.get(0).getDate());
        assertEquals("05-01-2017 00:00:00", receipts.get(1).getDate());
        assertEquals("05-01-2017 00:00:00", receipts.get(48).getDate());
        assertEquals("04-01-2017 00:00:00", receipts.get(49).getDate());
        executeListReceiptsUseCase(3);
        receipts = getPageItemsList();
        assertEquals("03-01-2017 00:00:00", receipts.get(0).getDate());
        assertEquals("02-01-2017 00:00:00", receipts.get(1).getDate());
        assertEquals("02-01-2017 00:00:00", receipts.get(48).getDate());
        assertEquals("01-01-2017 00:00:00", receipts.get(49).getDate());
    }

    private int getTotalNumberOfPages() {
        return (int) paginationUseCasesParametersObjectMap.get(PaginationUseCasesParameters.TOTAL_NUMBER_OF_PAGES);
    }

    private void executeListReceiptsUseCase(int pageNumber) throws UseCaseException {
        paginationUseCasesParametersObjectMap.put(PaginationUseCasesParameters.PAGE_NUMBER, pageNumber);
        listReceiptsUseCase.execute(paginationUseCasesParametersObjectMap);
    }

    private List<Receipt> getPageItemsList() {
        return (List<Receipt>) paginationUseCasesParametersObjectMap.get(PaginationUseCasesParameters.PAGE_ITEMS_LIST);
    }

    private int getPageItemsCount() {
        return getPageItemsList().size();
    }

    private Iterable<ReceiptEntity> getUnorderedReceiptEntities() {
        List<ReceiptEntity> receipts = new ArrayList<>();
        receipts.add(getReceiptEntity("06-01-2017 00:00:00"));
        receipts.add(getReceiptEntity("02-01-2017 00:00:00"));
        receipts.add(getReceiptEntity("03-01-2017 00:00:00"));
        receipts.add(getReceiptEntity("01-01-2017 00:00:00"));
        receipts.add(getReceiptEntity("05-01-2017 00:00:00"));
        receipts.add(getReceiptEntity("04-01-2017 00:00:00"));
        return receipts;
    }

    private List<Receipt> getOrderedReceipts() {
        List<Receipt> receipts = new ArrayList<>();
        receipts.add(getReceipt("06-01-2017 00:00:00"));
        receipts.add(getReceipt("05-01-2017 00:00:00"));
        receipts.add(getReceipt("04-01-2017 00:00:00"));
        receipts.add(getReceipt("03-01-2017 00:00:00"));
        receipts.add(getReceipt("02-01-2017 00:00:00"));
        receipts.add(getReceipt("01-01-2017 00:00:00"));
        return receipts;
    }

    private Iterable<ReceiptEntity> getReceiptEntitiesWithDifferentDates() {
        List<ReceiptEntity> receipts = new ArrayList<>();
        receipts.add(getReceiptEntity("01-01-2017 00:00:00"));
        receipts.add(getReceiptEntity("03-01-2017 00:00:00"));
        receipts.add(getReceiptEntity("04-01-2017 00:00:00"));
        receipts.add(getReceiptEntity("06-01-2017 00:00:00"));
        receipts.add(getReceiptEntity("07-01-2017 00:00:00"));
        receipts.add(getReceiptEntity("09-01-2017 00:00:00"));
        add48ReceiptEntities(receipts, "02-01-2017 00:00:00");
        add48ReceiptEntities(receipts, "05-01-2017 00:00:00");
        add48ReceiptEntities(receipts, "08-01-2017 00:00:00");
        return receipts;
    }

    private ReceiptEntity getReceiptEntity(String date) {
        return new ReceiptEntityBuilder()
                .setDate(date)
                .build();
    }


    private Receipt getReceipt(String date) {
        return new ReceiptBuilder()
                .setDate(date)
                .build();
    }

    private Iterable<ReceiptEntity> get140ReceiptEntities() {
        List<ReceiptEntity> receipts = new ArrayList<>();
        for (int i = 1; i <= 140; i++) {
            receipts.add(getReceiptEntity("01-01-2017 00:00:00"));
        }
        return receipts;
    }


    private void add48ReceiptEntities(List<ReceiptEntity> receipts, String date) {
        for (int i = 2; i <= 49; i++) {
            receipts.add(getReceiptEntity(date));
        }
    }
}
