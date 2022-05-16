package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.dataTransferObjects.ShowDTO;
import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Performance;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.kafka.KafkaConstants;
import edu.uoc.epcsd.showcatalog.repositories.CategoryRepository;
import edu.uoc.epcsd.showcatalog.repositories.PerformanceRepository;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/show")
public class ShowController {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private KafkaTemplate<String, Show> kafkaTemplate;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Show> getAllShows() {
        log.trace("Get All shows");
        return showRepository.findAll();
    }

    @PostMapping("/")
    public ResponseEntity<Show> create(@Valid @RequestBody ShowDTO showDTO) {
        log.trace("Create show: " + showDTO);

        List<Category> listCategories = new ArrayList<>();

        for (Long id : showDTO.getCategories()){
            if(categoryRepository.findById(id).isPresent()){
                listCategories.add(categoryRepository.findById(id).get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        }

        Show show = Show.builder()
                .name(showDTO.getName())
                .capacity(showDTO.getCapacity())
                .duration(showDTO.getDuration())
                .image(showDTO.getImage())
                .price(showDTO.getPrice())
                .onSaleDate(showDTO.getOnSaleDate())
                .categories(listCategories)
                .build();

        Show showRet = showRepository.save(show);

        kafkaTemplate.send(KafkaConstants.SHOW_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_ADD, showRet);

        log.trace("Message to NoficationService sent");

        return ResponseEntity.ok(showRet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Show> deleteShow(@PathVariable Long id) {
        log.trace("Delete show with id: " + id);

        if(showRepository.findById(id).isPresent()){
            Show show = showRepository.findById(id).get();
            showRepository.delete(show);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<List<Show>> getShowsByName(@PathVariable String name){
        log.trace("Get Shows by name: " + name);

        if(showRepository.findShowsByName(name).get().size()>0){
            return ResponseEntity.ok(showRepository.findShowsByName(name).get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byCategory/id/{id}")
    public ResponseEntity<List<Show>> getShowsByCategoryId(@PathVariable Long id){
        log.trace("Get Show by idCategory: " + id);

        if(showRepository.findByCategoriesId(id).get().size()>0){
            return ResponseEntity.ok(showRepository.findByCategoriesId(id).get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byCategory/name/{name}")
    public ResponseEntity<List<Show>> getShowsByCategoryName(@PathVariable String name){
        log.trace("Get Show by nameCategory: " + name);

        if(showRepository.findByCategoriesName(name).get().size()>0){
            return ResponseEntity.ok(showRepository.findByCategoriesName(name).get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Show> getShowDetails(@PathVariable Long id) {
        log.trace("Get show Details: " + id);
        if(showRepository.findById(id).isPresent()){
            return ResponseEntity.ok(showRepository.findById(id).get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
