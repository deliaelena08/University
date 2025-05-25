package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.OrderStatusHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RepoDBOrderStatusTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<OrderStatusHistory> query;

    private RepoDBOrderStatus repo;

    @BeforeEach
    void setUp() {
        repo = new RepoDBOrderStatus() {{
            this.entityManager = entityManager;
        }};
    }

    @Test
    void testFindByOrderId() {
        List<OrderStatusHistory> list = List.of(new OrderStatusHistory(), new OrderStatusHistory());

        when(entityManager.createQuery(anyString(), eq(OrderStatusHistory.class))).thenReturn(query);
        when(query.setParameter(eq("orderId"), anyInt())).thenReturn(query);
        when(query.getResultList()).thenReturn(list);

        var result = repo.findByOrderId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
