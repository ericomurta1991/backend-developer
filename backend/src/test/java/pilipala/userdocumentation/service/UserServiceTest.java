package pilipala.userdocumentation.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pilipala.userdocumentation.dto.UserDTO;
import pilipala.userdocumentation.entities.User;
import pilipala.userdocumentation.exceptions.DesafioException;
import pilipala.userdocumentation.repositories.UserRepository;
import pilipala.userdocumentation.services.UserService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.Optional;


public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    private User user;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        Long existingId = 1L;

        user = new User();
        user.setId(existingId);
        user.setName("Teste usuário");
        user.setCpf("1234567890");
    }

    @Test
    void findById_ShouldReturnUserDTO_WhenUserExists(){
        Long existingId = 1L;
        when(repository.findById(1L)).thenReturn(Optional.of(user));

       UserDTO result = service.findById(existingId);

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getCpf(), result.getCpf());


    }

    @Test
    void findById_ShouldReturnUserDTO_WhenUserDoesNotExist(){
        Long noExistingId = 100L;
        when(repository.findById(noExistingId)).thenReturn(Optional.empty());

        assertThrows(DesafioException.class, () -> {
            service.findById(noExistingId);
        }); 
    }



}
