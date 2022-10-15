package dev.mxhit.jwt.role;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.mxhit.jwt.user.UserService;
import dev.mxhit.jwt.utils.RoleToUserForm;

@RestController
@RequestMapping(path = "/api/role")
public class RoleController {
	@Autowired
	Logger log;
	
	@Autowired
	UserService userService;
	
	@PostMapping("/save")
	public ResponseEntity<Role> saveRole(@RequestBody Role role) {
		log.info("Calling endpoint /role/save");
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());

		return ResponseEntity.created(uri).body(userService.saveRole(role));
	}
	
	@PostMapping("/addToUser")
	public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
		log.info("Calling endpoint /role/addToUser");
		
		userService.addRoleToUser(form.getUsername(), form.getRoleName());
		
		return ResponseEntity.ok().build();
	}
}
