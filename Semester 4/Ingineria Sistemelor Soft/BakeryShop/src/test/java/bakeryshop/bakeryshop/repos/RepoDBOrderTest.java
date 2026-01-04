package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepoDBOrderTest {

    @Mock
    private EntityManager entityManager;

    private RepoDBOrder repo;

    @BeforeEach
    void setUp() {
        repo = new RepoDBOrder() {{
            this.entityManager = entityManager;
        }};
    }

    @Test
    void testGetById() {
        Order order = new Order();
        order.setId(1);

        when(entityManager.find(Order.class, 1)).thenReturn(order);

        Order result = repo.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetAll() {
        List<Order> orders = List.of(new Order(), new Order());
        TypedQuery<Order> query = mock(TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(orders);

        List<Order> result = repo.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testSave() {
        Order order = new Order();
        when(entityManager.getTransaction()).thenReturn(mock(javax.persistence.EntityTransaction.class));
        when(entityManager.merge(order)).thenReturn(order);

        Order saved = repo.save(order);

        assertNotNull(saved);
        verify(entityManager).merge(order);
    }

    @Test
    void testUpdate() {
        Order order = new Order();
        when(entityManager.getTransaction()).thenReturn(mock(javax.persistence.EntityTransaction.class));
        when(entityManager.merge(order)).thenReturn(order);

        Order updated = repo.update(order);

        assertNotNull(updated);
        verify(entityManager).merge(order);
    }

    @Test
    void testDelete() {
        Order order = new Order();
        order.setId(1);

        when(entityManager.find(Order.class, 1)).thenReturn(order);
        when(entityManager.getTransaction()).thenReturn(mock(javax.persistence.EntityTransaction.class));

        repo.delete(1);

        verify(entityManager).remove(order);
    }
}
