package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.Cake;
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
public class RepoDBCakeTest {

    @Mock
    private EntityManager entityManager;

    private RepoDBCake repo;

    @BeforeEach
    void setUp() {
        // Test fără Hibernate, folosind doar mock-uri
        repo = new RepoDBCake() {{
            this.entityManager = entityManager;
        }};
    }

    @Test
    public void testGetById() {
        Cake cake = new Cake();
        cake.setId(1);
        when(entityManager.find(Cake.class, 1)).thenReturn(cake);
        Cake result = repo.getById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    public void testGetAll() {
        List<Cake> cakes = List.of(new Cake(), new Cake());
        TypedQuery<Cake> mockQuery = mock(TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(Cake.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(cakes);

        List<Cake> result = repo.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
