package edu.uoc.epcsd.showcatalog.repositories;

import edu.uoc.epcsd.showcatalog.entities.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    Optional<List<Performance>> findPerformancesByShow_Name(String name);
    Optional<List<Performance>> findPerformancesByShow_Id(Long id);
}
