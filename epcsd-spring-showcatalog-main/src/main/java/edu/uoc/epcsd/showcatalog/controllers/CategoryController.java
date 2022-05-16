package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.dataTransferObjects.CategoryDTO;
import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.repositories.CategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        log.trace("Get All Categories");

        return categoryRepository.findAll();
    }

    @PostMapping("/")
    public ResponseEntity<Category> create(@Valid @RequestBody CategoryDTO categoryDTO) {
        log.trace("Create Category: " + categoryDTO);

        if(categoryRepository.findCategoryByName(categoryDTO.getName()).isPresent()){
            return ResponseEntity.badRequest().build();
        }

        Category category = Category.builder().name(categoryDTO.getName()).build();
        Category categoryRet = categoryRepository.save(category);

        return ResponseEntity.ok(categoryRet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> delete(@PathVariable Long id) {
        log.trace("Delete Category with id: " + id);

        if(categoryRepository.findById(id).isPresent()){
            categoryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        log.trace("Get Category by Id: " + id);
        if(categoryRepository.findById(id).isPresent()){;
            return ResponseEntity.ok(categoryRepository.findById(id).get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<Category> getByName(@PathVariable String name) {
        log.trace("Get Category by Name: " + name);
        if(categoryRepository.findCategoryByName(name).isPresent()){;
            return ResponseEntity.ok(categoryRepository.findCategoryByName(name).get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
