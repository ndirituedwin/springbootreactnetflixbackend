package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.Films;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmsRepository extends JpaRepository<Films,Long> {
}
