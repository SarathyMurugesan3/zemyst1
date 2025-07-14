package entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "testimonials")
public class Testimonial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String message;
	
	private String position;
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] image;
	
	private String imageName;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
	
	public Testimonial() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	
	
	
}



