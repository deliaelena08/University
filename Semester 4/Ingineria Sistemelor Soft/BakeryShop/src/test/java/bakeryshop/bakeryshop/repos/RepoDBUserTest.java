package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RepoDBUserTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<User> query;

    private RepoDBUser repo;

    @BeforeEach
    void setUp() {
        repo = new RepoDBUser() {
            {
                this.entityManager = entityManager;
            }
        };
    }

    @Test
    void testLoginSuccess() {
        User user = new User();
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter(eq("username"), any())).thenReturn(query);
        when(query.setParameter(eq("password"), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(user);

        User result = repo.login("john", "pass");

        assertNotNull(result);
    }

    @Test
    void testLoginFail() {
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NoResultException.class);

        User result = repo.login("john", "wrong");

        assertNull(result);
    }
}
