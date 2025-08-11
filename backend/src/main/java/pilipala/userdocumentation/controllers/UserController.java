package pilipala.userdocumentation.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import pilipala.userdocumentation.dto.UserDTO;
import pilipala.userdocumentation.exceptions.StandardError;
import pilipala.userdocumentation.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	 @Autowired
	    private UserService service;
	 	
	 	@Operation(summary = "Listar usuários paginados", description = "Retorna uma lista paginada de usuários")
	 	@ApiResponses(value = {
	 		@ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
	 		@ApiResponse(responseCode = "400", description = "Parâmetros inválidos (page ou limit)", content = @Content(schema = @Schema(implementation = StandardError.class)))
	 	})
	    @GetMapping
	    public ResponseEntity<Page<UserDTO>> findAll(
	            @RequestParam(value = "page", defaultValue = "0") Integer page,
	            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
	            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
	            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy) {

	        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

	        Page<UserDTO> list = service.findAllPaged(pageRequest);
	        return ResponseEntity.ok().body(list);
	    }

	 	
	 	
	 	@Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo seu ID")
	 	@ApiResponses(value = {
	 			@ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(schema = @Schema(implementation = UserDTO.class))),
	 			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = StandardError.class)))
	 	})
	 	
	    @GetMapping(value = "/{id}")
	    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
	        UserDTO dto = service.findById(id);
	        return ResponseEntity.ok().body(dto);
	    }
	    
	    @Operation(summary = "Criar novo usuário", description = "Insere um novo usuário com os dados informados.")
	    @ApiResponses(value = {
	    		@ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(schema = @Schema(implementation = UserDTO.class))),
	    		@ApiResponse(responseCode = "400", description = "Dados inválidos ou CPF ja cadastradado", content = @Content(schema = @Schema(implementation = StandardError.class)))
	    })
	    @PostMapping
	    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserDTO dto) {
	        dto = service.insert(dto);
	        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
	        return ResponseEntity.created(uri).body(dto);
	    }


	    
	    @Operation(
	    		summary = "Atualiza um usuário existente",
	    		description = "Recebe um UserDTO e atualiza os dados do usuário com ID informado.")
	    @ApiResponses(value = {
	    		@ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(schema = @Schema(implementation = UserDTO.class))),
	    		@ApiResponse(responseCode = "400", description = "Dados inválidos ou incompletos", content = @Content(schema = @Schema(implementation = StandardError.class))), 
	    		@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content =  @Content(schema = @Schema(implementation = StandardError.class))),
	    		
	    })
	    
	    @PutMapping(value = "/{id}")
	    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
	        dto = service.update(id, dto);
	        return ResponseEntity.ok().body(dto);
	    }
	    
	    
	    @Operation(
	    		summary = "Excluir usuário",
	    		description = "Remove um usuário existente pelo ID fornecido.")
	    @ApiResponses(value = {
	    		  @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
	    		  @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content(schema = @Schema(implementation = StandardError.class)) ),
	    		  @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = StandardError.class)))
	    		  
	    })
	    @DeleteMapping(value = "/{id}")
	    public ResponseEntity<Void> delete(@PathVariable Long id) {
	        service.delete(id);
	        return ResponseEntity.noContent().build();
	    }
}
