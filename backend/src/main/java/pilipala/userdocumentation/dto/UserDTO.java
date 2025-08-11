package pilipala.userdocumentation.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.caelum.stella.bean.validation.CPF;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pilipala.userdocumentation.entities.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO implements Serializable{
	private static final long serialVersionUID =1L;
	@Schema(description = "ID do usuário", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	private long id;
	@NotBlank
	@Size(max = 50, message = "Nome deve ter no maximo 50 caracteres")
	@Schema(description = "Nome completo do usuário", example = "PiliPala")
	private String name;
	@NotBlank(message = "CPF é obrigatório")
	@CPF(message = "CPF inválido")
	@Schema(description = "CPF do usuário no formato XXX.XXX.XXX-XX", example = "123.456.789-00")
	private String cpf;
	
	private List<UserDocumentationDTO> documentations = new ArrayList<>();
	
	
	public UserDTO (User entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.cpf = entity.getCpf();
		
		this.documentations = entity.getDocumentations().stream()
	                .map(doc -> new UserDocumentationDTO(
	                        doc.getId(),
	                        doc.getType(),
	                        doc.getNumber(),
	                        doc.getDocument(),
	                        entity.getId()))
	                .collect(Collectors.toList());
	}
}
