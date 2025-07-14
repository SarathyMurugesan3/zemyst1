package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Testimonial;

public interface TestimonialRepository extends JpaRepository<Testimonial,Long> {

}
