package dev.mxhit.jwt.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.mxhit.jwt.role.Role;
import dev.mxhit.jwt.role.RoleRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	@Autowired
	Logger log;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Override
	public User getUser(String username) {
		log.info("Searching the database for username: {}", username);

		return userRepository.findByUsername(username);
	}

	@Override
	public User saveUser(User user) {
		log.info("Saving new user: {}", user.getUsername());

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUsers() {
		log.info("Fetching all users");

		return userRepository.findAll();
	}

	@Override
	public Role saveRole(Role role) {
		log.info("Saving new role: {}", role.getName());

		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		User user = userRepository.findByUsername(username);

		Role role = roleRepository.findByName(roleName);

		log.info("Adding role, {}, to user, {}", role.getName(), user.getUsername());

		user.getRoles().add(role);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		log.info("Loading user {}", username);

		if (user == null) {
			log.error("{} not found in the database", username);

			throw new UsernameNotFoundException("User not found in the database");
		}

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				authorities);
	}
}
