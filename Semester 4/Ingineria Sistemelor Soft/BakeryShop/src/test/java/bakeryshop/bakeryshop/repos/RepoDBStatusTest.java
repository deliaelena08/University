package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RepoDBStatusTest {

    @Mock
    private EntityManager entityManager;

    private RepoDBStatus repo;

    @BeforeEach
    void setUp() {
        repo = new RepoDBStatus() {{
            this.entityManager = entityManager;
        }};
    }

    @Test
    void testGetById() {
        Status status = new Status();
        status.setId(1);

        when(entityManager.find(Status.class, 1)).thenReturn(status);

        Status result = repo.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetAll() {
        List<Status> statuses = List.of(new Status(), new Status());
        var query = mock(javax.persistence.TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(Status.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(statuses);

        List<Status> result = repo.getAll();

        assertEquals(2, result.size());
    }
}
