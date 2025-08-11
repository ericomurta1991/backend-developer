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

import pilipala.userdocumentation.dto.UserDocumentationDTO;
import pilipala.userdocumentation.enums.DocumentType;
import pilipala.userdocumentation.services.UserDocumentationService;

@RestController
@RequestMapping("/user-documentation")
public class UserDocumentationController {
	
	@Autowired
	private UserDocumentationService service;
	
	
	@GetMapping
	public ResponseEntity<Page<UserDocumentationDTO>> findAll(
			@RequestParam(value = "page", defaultValue = "0") int Page,
			@RequestParam(value = "limit", defaultValue = "10") int limit){
	    
		Pageable pageable = PageRequest.of(Page, limit);
		Page<UserDocumentationDTO> list = service.findAll(pageable);
	    return ResponseEntity.ok(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDocumentationDTO> findById(@PathVariable Long id) {
	     UserDocumentationDTO dto = service.findById(id);
	     return ResponseEntity.ok(dto);
	    }
	    
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDocumentationDTO> insert(
            @RequestParam("type") DocumentType type,
            @RequestParam("number") String number,
            @RequestParam("userId") Long userId,
            @RequestParam("file") MultipartFile file) {

        UserDocumentationDTO dto = service.insert(type, number, userId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
	
	 @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<UserDocumentationDTO> update(
	            @PathVariable Long id,
	            @RequestParam("type") DocumentType type,
	            @RequestParam("number") String number,
	            @RequestParam("file") MultipartFile file) {

	        UserDocumentationDTO dto = service.update(id, type, number, file);
	        return ResponseEntity.ok(dto);
	    }
	
	  @DeleteMapping("/{id}")
	    public ResponseEntity<Void> delete(@PathVariable Long id) {
	        service.delete(id);
	        return ResponseEntity.noContent().build();
	    }
	
	
	
	
	
	
}
