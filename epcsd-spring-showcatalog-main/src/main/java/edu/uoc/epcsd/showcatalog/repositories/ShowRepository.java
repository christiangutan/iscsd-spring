package edu.uoc.epcsd.showcatalog.repositories;

import edu.uoc.epcsd.showcatalog.entities.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface  ShowRepository extends JpaRepository<Show, Long> {

    Optional<List<Show>> findShowsByName(String name);
    Optional<List<Show>> findByCategoriesId(Long id);
    Optional<List<Show>> findByCategoriesName(String name);
}
