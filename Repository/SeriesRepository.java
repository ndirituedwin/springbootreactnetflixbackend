package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository extends JpaRepository<Series,Long> {
}
