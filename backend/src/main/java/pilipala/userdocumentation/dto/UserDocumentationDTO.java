package pilipala.userdocumentation.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pilipala.userdocumentation.enums.DocumentType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDocumentationDTO implements Serializable{
	private static final long serialVersionUID =1L;
	
	private Long id;
	@NotNull(message = "Tipo de Documento é obrigatório")
    private DocumentType type;
	
	@NotBlank(message = "Numero do documento  é obrigatório")
    private String number;
	
	@JsonIgnore
	@NotNull(message = "Documento é obrigatório")
	private byte[] document;
    private Long userId;
	    
}
