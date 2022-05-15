package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.dataTransferObjects.PerformanceDTO;
import edu.uoc.epcsd.showcatalog.entities.Performance;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.repositories.PerformanceRepository;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/performance")
public class PerformanceController {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private ShowRepository showRepository;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Performance> getAllPerformances() {
        log.trace("Get All Performances");
        return performanceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Performance> getById(@PathVariable Long id) {
        log.trace("Get Performance by id:" + id);

        if(performanceRepository.findById(id).isPresent()){;
            return ResponseEntity.ok(performanceRepository.findById(id).get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<Performance> create(@Valid @RequestBody PerformanceDTO performanceDTO) {
        log.trace("Create Performance:" + performanceDTO);

        if(showRepository.findById(performanceDTO.getShow()).isPresent()){
            return ResponseEntity.badRequest().build();
        }

        Performance performance = Performance.builder()
                .date(performanceDTO.getDate())
                .streamingURL(performanceDTO.getURL())
                .remainingSeats(performanceDTO.getRemainingSeats())
                .status(performanceDTO.getStatus())
                .build();

        Performance performanceRet = performanceRepository.save(performance);

        return ResponseEntity.ok(performanceRet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Performance> delete(@PathVariable Long id) {
        log.trace("Delete Performance with id: " + id);

        if(performanceRepository.findById(id).isPresent()){
            performanceRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byShowId/{id}")
    public ResponseEntity<List<Performance>> getPerformanceById(@PathVariable Long id){
        log.trace("Get Performances by Show's Id : " + id);

        if(!showRepository.findById(id).isPresent()){
            return ResponseEntity.badRequest().build();
        }

        Optional<List<Performance>> listPerformances = performanceRepository.findPerformancesByShow_Id(id);

        if(!listPerformances.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Performance> retPerformances= listPerformances.get();

        return ResponseEntity.ok(retPerformances);
    }
}
