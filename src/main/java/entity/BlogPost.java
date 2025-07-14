package entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "blog_posts")
public class BlogPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] image;
	
	private String imageName;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
	
	public BlogPost() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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



