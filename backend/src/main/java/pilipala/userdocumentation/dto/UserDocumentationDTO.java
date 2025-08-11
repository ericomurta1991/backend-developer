package pilipala.userdocumentation.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
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
	
	@Schema(description = "ID do documento", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	private Long id;
	
	@NotNull(message = "Tipo de Documento é obrigatório")
	@Schema(description = "Tipo do documento", example = "CPF", allowableValues = {"CPF", "CNPJ", "Outros"})
    private DocumentType type;
	
	@NotBlank(message = "Numero do documento  é obrigatório")
	@Schema(description = "Número do documento", example = "123.456.789-00")
    private String number;
	
	@JsonIgnore
	@NotNull(message = "Documento é obrigatório")
	@Schema(description = "Arquivo do documento em base64", accessMode = Schema.AccessMode.READ_ONLY)
	private byte[] document;
    private Long userId;
	    
}
