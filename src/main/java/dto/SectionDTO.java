package dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SectionDTO {
	public void parseContentAsJson() {
	    if (this.name != null && this.name.equals("contact") && this.content != null) {
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	            Map<String, String> map = mapper.readValue(this.content, new TypeReference<Map<String, String>>() {});
	            this.phone = map.get("phone");
	            this.email = map.get("email");
	            this.address = map.get("address");
	            this.workingHours = map.get("workingHours");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}


	private Long id;
	private String name;
	private String content;
	private byte[] image;
	private String imageName;
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public SectionDTO(Long id, String name, String content, String address, String phone, String email,
			String workingHours) {
		this.id = id;
		this.name = name;
		this.content = content;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.workingHours = workingHours;
	}

	private boolean hasImage;
	private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String address;
    private String phone;
    private String email;
    private String workingHours;
	private ContactInfo contactInfo;

	public static class ContactInfo {
		private String phone;
		private String email;
		private String address;
		private String workingHours;

		public ContactInfo() {
			
		}
		
		public ContactInfo(String phone, String email, String address, String workingHours) {
			this.phone = phone;
			this.email = email;
			this.address = address;
			this.workingHours = workingHours;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getWorkingHours() {
			return workingHours;
		}

		public void setWorkingHours(String workingHours) {
			this.workingHours = workingHours;
		}

		// Getters and Setters
		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

	}
	public ContactInfo getContactInfo() {
        if (address != null || phone != null || email != null || workingHours != null) {
            return new ContactInfo(address, phone, email, workingHours);
        }
        return null;
    }

	public SectionDTO() {
	}

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public boolean isHasImage() {
		return hasImage;
	}

	public void setHasImage(boolean hasImage) {
		this.hasImage = hasImage;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(String workingHours) {
		this.workingHours = workingHours;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

}
