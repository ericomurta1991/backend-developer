package pilipala.userdocumentation.entities;

import java.io.Serializable;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pilipala.userdocumentation.enums.DocumentType;

@Entity
@Table(name = "tb_user_documentation")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDocumentation implements Serializable{
	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(nullable = false)
	private byte[] document;
	
	@Enumerated(EnumType.STRING)
	@Column(name= "type", nullable = false)
	private DocumentType type; //ex: CPF, CPNJ, Outros
	@Column(nullable = false)
	private String number; //numero documento
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;
	
	
	
	
}

