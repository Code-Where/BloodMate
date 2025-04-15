package com.asdeveloper.BloodMate.Camps;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/camps")
public class CampController {

    private final CampService campService;

    public CampController(CampService campService) {
        this.campService = campService;
    }

    @GetMapping
    public List<Camps> getAllCamps() {
        return campService.getAllCamps();
    }

    @GetMapping("/{id}")
    public Camps getCamp(@PathVariable Long id) {
        return campService.getCampById(id);
    }

    @GetMapping("/filter")
    public List<Camps> filterCamps(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String organizerBy
    ) 
    {
        return campService.filterCamps(title, location, city, organizerBy);
    }


    @PostMapping
    public Camps createCamp(@RequestBody Camps camp) {
        return campService.createCamp(camp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Camps> updateCamp(@PathVariable Long id, @RequestBody Camps camp) {
        Camps updated = campService.updateCamp(id, camp);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } 
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public String deleteCamp(@PathVariable Long id) {
        campService.deleteCamp(id);
        return "Camp Deleted Successfully";
    }
}
