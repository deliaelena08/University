package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RepoDBOrderItemTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<OrderItem> query;

    private RepoDBOrderItem repo;

    @BeforeEach
    void setUp() {
        repo = new RepoDBOrderItem() {
            {
                this.entityManager = entityManager;
            }
        };
    }

    @Test
    void testFindByOrderId() {
        List<OrderItem> list = List.of(new OrderItem(), new OrderItem());

        when(entityManager.createQuery(anyString(), eq(OrderItem.class))).thenReturn(query);
        when(query.setParameter("orderId", 1)).thenReturn(query);
        when(query.getResultList()).thenReturn(list);

        List<OrderItem> result = repo.findByOrderId(1);

        assertEquals(2, result.size());
    }
}
