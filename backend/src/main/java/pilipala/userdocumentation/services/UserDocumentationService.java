package pilipala.userdocumentation.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import pilipala.userdocumentation.dto.UserDocumentationDTO;
import pilipala.userdocumentation.entities.User;
import pilipala.userdocumentation.entities.UserDocumentation;
import pilipala.userdocumentation.enums.DocumentType;
import pilipala.userdocumentation.exceptions.DesafioException;
import pilipala.userdocumentation.repositories.UserDocumentationRepository;
import pilipala.userdocumentation.repositories.UserRepository;

@Service
public class UserDocumentationService {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserDocumentationRepository documentationRepository;
	
	private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
	
	public UserDocumentationService(UserRepository userRepository, UserDocumentationRepository documentationRepository) {
        this.userRepository = userRepository;
        this.documentationRepository = documentationRepository;
    }
	
	@Transactional
	public Page<UserDocumentationDTO> findAll(Pageable pageable) {
        return documentationRepository.findAll(pageable)
                .map(doc -> new UserDocumentationDTO(
                		doc.getId(), 
                		doc.getType(), 
                		doc.getNumber(), 
                		doc.getDocument(), 
                		doc.getUser().getId()));
    }
	
	@Transactional
	public UserDocumentationDTO findById(Long id) {
	    UserDocumentation entity = documentationRepository.findById(id)
	        .orElseThrow(() -> new DesafioException("Documento não encontrado"));
	    return new UserDocumentationDTO(entity.getId(), entity.getType(), entity.getNumber(), entity.getDocument(), entity.getUser().getId());
	}
	
	@Transactional
	public UserDocumentationDTO insert(DocumentType type, String number, Long userId, MultipartFile file) {
	    if (file == null || file.isEmpty()) {
	        throw new DesafioException("Arquivo é obrigatório");
	    }
	    if (file.getSize() > MAX_FILE_SIZE) {
	        throw new DesafioException("Arquivo não pode exceder 2MB");
	    }
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new DesafioException("Usuário não encontrado"));

	    try {
	        UserDocumentation entity = new UserDocumentation();
	        entity.setType(type);
	        entity.setNumber(number);
	        entity.setDocument(file.getBytes());
	        entity.setUser(user);

	        entity = documentationRepository.save(entity);
	    
	        return new UserDocumentationDTO(entity.getId(), entity.getType(), entity.getNumber(), entity.getDocument(), user.getId());
	    } catch(IOException e) {
	    	throw new DesafioException("Erro ao processar o arquivo");
	    }catch (DataIntegrityViolationException e) {
	    	throw new DesafioException("Erro de integridade ao salvar documento");
	    }
	
}
	
	@Transactional
	public UserDocumentationDTO update(Long id, DocumentType type, String number, MultipartFile file) {
	    UserDocumentation entity = documentationRepository.findById(id)
	        .orElseThrow(() -> new DesafioException("Documento não encontrado"));

	    if (number == null || number.trim().isEmpty()) {
	        throw new DesafioException("Número do documento é obrigatório");
	    }

	    if (file != null && !file.isEmpty()) {
	        if (file.getSize() > MAX_FILE_SIZE) {
	            throw new DesafioException("Arquivo não pode exceder 2MB");
	        }
	        try {
	            entity.setDocument(file.getBytes());
	        } catch (IOException e) {
	            throw new DesafioException("Erro ao processar o arquivo");
	        }
	    }

	    entity.setType(type);
	    entity.setNumber(number);

	    entity = documentationRepository.save(entity);

	    return new UserDocumentationDTO(entity.getId(), entity.getType(), entity.getNumber(), entity.getDocument(), entity.getUser().getId());
	
}
	
	@Transactional
	public void delete(Long id) {
	    try {
	        documentationRepository.deleteById(id);
	    } catch (EmptyResultDataAccessException e) {
	        throw new DesafioException("Documento não encontrado");
	    } catch (DataIntegrityViolationException e) {
	        throw new DesafioException("Erro de integridade ao deletar documento");
	    }
	
}
}
