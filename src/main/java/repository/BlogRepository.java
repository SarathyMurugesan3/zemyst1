package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.BlogPost;

public interface BlogRepository extends JpaRepository<BlogPost,Long> {

	boolean existsByTitle(String title);
}
