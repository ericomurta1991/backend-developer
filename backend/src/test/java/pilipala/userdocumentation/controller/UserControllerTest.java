package pilipala.userdocumentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import pilipala.userdocumentation.controllers.UserController;
import pilipala.userdocumentation.entities.User;
import pilipala.userdocumentation.exceptions.GlobalExceptionHandler;
import pilipala.userdocumentation.repositories.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp(){
        String nameTeste = "Usuário teste";
        String cpfTeste = "123456789";
        userRepository.deleteAll();

        User user = new User();
        user.setName(nameTeste);
        user.setCpf(cpfTeste);

        this.savedUser = userRepository.save(user);
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() throws Exception {
        mockMvc.perform(get("/users/{id}", savedUser.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(savedUser.getName()))
                .andExpect(jsonPath("$.cpf") .value(savedUser.getCpf()));
    }

    @Test
    void findById_ShouldReturn404_WhenNotExists() throws Exception{
        mockMvc.perform(get("/users/{id}", 999)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}
