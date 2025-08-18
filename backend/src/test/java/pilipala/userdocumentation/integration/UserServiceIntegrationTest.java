package pilipala.userdocumentation.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pilipala.userdocumentation.dto.UserDTO;
import pilipala.userdocumentation.entities.User;
import pilipala.userdocumentation.repositories.UserRepository;
import pilipala.userdocumentation.services.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();  //limpeza de tabela antes de cada teste
        String nameTeste = "Usuário Teste";
        String cpf = "12345678901";

        User user = new User();
        user.setName(nameTeste);
        user.setCpf(cpf);
        this.savedUser = userRepository.save(user);
    }

    @Test
    void findById_ShouldReturnUserDTO_WhenUserExists(){
        UserDTO userDTO = userService.findById(savedUser.getId());

        assertNotNull(userDTO);
        assertEquals(savedUser.getName(), userDTO.getName());
        assertEquals(savedUser.getCpf(), userDTO.getCpf());
    }


}
