// dto/ContactFormDTO.java
package dto;

public class ContactFormDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String company;
    private String subject;
    private String message;
    private int rating;
    @Override
    public String toString() {
        return "ContactFormDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", company='" + company + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", rating=" + rating +
                '}';
    }
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

    // Getters and Setters
    
    
    
}
