package za.co.doctorpatient.management.system.model;

import java.util.Objects;

public class Doctor {
	private int id;
	private String firstName;
	private String lastName;
	private String specialization;
	private String email;
	
	public Doctor() {}
	
	public Doctor(String firstName, String lastName, String specialization, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.specialization = specialization;
		this.email = email;
	}

	public Doctor(int id, String firstName, String lastName, String specialization, String email) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.specialization = specialization;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, firstName, id, lastName, specialization);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Doctor other = (Doctor) obj;
		return Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName) && id == other.id
				&& Objects.equals(lastName, other.lastName) && Objects.equals(specialization, other.specialization);
	}

	@Override
	public String toString() {
		return "Doctor [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", specialization="
				+ specialization + ", email=" + email + "]";
	}
}