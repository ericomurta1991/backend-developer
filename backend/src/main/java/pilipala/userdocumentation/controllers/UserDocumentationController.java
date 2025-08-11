package pilipala.userdocumentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import pilipala.userdocumentation.dto.UserDocumentationDTO;
import pilipala.userdocumentation.enums.DocumentType;
import pilipala.userdocumentation.exceptions.StandardError;
import pilipala.userdocumentation.services.UserDocumentationService;

@RestController
@RequestMapping("/user-documentation")
public class UserDocumentationController {
	
	@Autowired
	private UserDocumentationService service;
	
	
	@Operation(summary = "Lista todas as documentações com paginação")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista paginada de documentações",content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Page.class))),
			@ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos",  content = @Content(schema = @Schema(implementation = StandardError.class)))
	})
	@GetMapping
	public ResponseEntity<Page<UserDocumentationDTO>> findAll(
			@RequestParam(value = "page", defaultValue = "0") int Page,
			@RequestParam(value = "limit", defaultValue = "10") int limit){
	    
		Pageable pageable = PageRequest.of(Page, limit);
		Page<UserDocumentationDTO> list = service.findAll(pageable);
	    return ResponseEntity.ok(list);
	}
	
	@Operation(summary = "Busca uma documentação pelo ID")
	@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Documentação encontrada",content = @Content(schema = @Schema(implementation = UserDocumentationDTO.class))),
		    @ApiResponse(responseCode = "404", description = "Documentação não encontrada",content = @Content(schema = @Schema(implementation = StandardError.class)))
		})
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDocumentationDTO> findById(@PathVariable Long id) {
	     UserDocumentationDTO dto = service.findById(id);
	     return ResponseEntity.ok(dto);
	    }
	    
	
	
	@Operation(summary = "Cria uma nova documentação para o usuário")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Documentação criada com sucesso",  content = @Content(schema = @Schema(implementation = UserDocumentationDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos ou arquivo maior que 2MB",content = @Content(schema = @Schema(implementation = StandardError.class)))
	})
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDocumentationDTO> insert(
            @RequestParam("type") DocumentType type,
            @RequestParam("number") String number,
            @RequestParam("userId") Long userId,
            @RequestParam("file") MultipartFile file) {

        UserDocumentationDTO dto = service.insert(type, number, userId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
	
	
	@Operation(summary = "Atualiza uma documentação existente pelo ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Documentação atualizada com sucesso", content = @Content(schema = @Schema(implementation = UserDocumentationDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos ou arquivo maior que 2MB",  content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "404", description = "Documentação não encontrada", content = @Content(schema = @Schema(implementation = StandardError.class)))
	})
	 @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<UserDocumentationDTO> update(
	            @PathVariable Long id,
	            @RequestParam("type") DocumentType type,
	            @RequestParam("number") String number,
	            @RequestParam("file") MultipartFile file) {

	        UserDocumentationDTO dto = service.update(id, type, number, file);
	        return ResponseEntity.ok(dto);
	    }
	
	
	@Operation(summary = "Deleta uma documentação pelo ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Documentação deletada com sucesso"),
			@ApiResponse(responseCode = "404", description = "Documentação não encontrada", content = @Content(schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(responseCode = "400", description = "Erro de integridade ao deletar documento", content = @Content(schema = @Schema(implementation = StandardError.class)))
	})
	@DeleteMapping("/{id}")
	    public ResponseEntity<Void> delete(@PathVariable Long id) {
	        service.delete(id);
	        return ResponseEntity.noContent().build();
	    }
	
	
	
	
	
	
}
