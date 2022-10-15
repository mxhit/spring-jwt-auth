package dev.mxhit.jwt.user;

import java.util.List;

import dev.mxhit.jwt.role.Role;

public interface UserService {
	/**
	 * @param username
	 * @return user object found in the database
	 */
	User getUser(String username);
	
	/**
	 * @param user
	 * @return user object saved in the database
	 */
	User saveUser(User user);
	
	/**
	 * @return list of users
	 */
	List<User> getAllUsers();
	
	/**
	 * @param role
	 * @return
	 */
	Role saveRole(Role role);
	
	/**
	 * @param username
	 * @param roleName
	 */
	void addRoleToUser(String username, String roleName);
}
