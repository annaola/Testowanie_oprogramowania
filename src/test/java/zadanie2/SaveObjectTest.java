package zadanie2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveObjectTest {

    @Captor
    private ArgumentCaptor<Exception> exceptionCaptor;

    @Mock
    private Session session;

    @Mock
    private DbLogger dbLogger;

    @InjectMocks
    private GenericDAO genericDAO = new GenericDAO();

    @Test
    void testSaveSuccessful() throws Exception {
        Object o = new Object();
        genericDAO.save(o);

        verify(session).open();
        verify(session).openTransaction();
        verify(session).save(o);
        verify(session).commitTransaction();
        verify(session).close();
    }

    @Test
    void testSessionOpenException() throws SessionOpenException {
        Object o = new Object();

        doThrow(new SessionOpenException()).when(session).open();

        assertThrows(SessionOpenException.class, () -> {
            genericDAO.save(o);
        });
    }

    @Test
    void testCommitException() throws Exception {
        Object o = new Object();

        doThrow(new CommitException()).when(session).commitTransaction();
        doNothing().when(dbLogger).log(exceptionCaptor.capture());

        genericDAO.save(o);
        assertTrue(exceptionCaptor.getValue() instanceof CommitException);
        verify(session).rollbackTransaction();
        verify(session).close();
    }
}
