package za.co.doctorpatient.management.system.model;

import za.co.doctorpatient.management.system.roles.Role;

public class User {
	
	private int id;
	private String username;
	private Role role;
	
	public User(int id, String username, Role role) {
		super();
		this.id = id;
		this.username = username;
		this.role = role;
	}
	
	public int getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Role getRole() {
		return role;
	}
	
	public boolean isAdmin() {
		return role == Role.ADMIN;
	}
}