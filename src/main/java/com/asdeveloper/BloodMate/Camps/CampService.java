package com.asdeveloper.BloodMate.Camps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CampService {

    @Autowired
    private CampRepository campRepository;

    public List<Camps> getAllCamps() {
        return campRepository.findAll();
    }

    public Camps getCampById(Long id) {
        return campRepository.findById(id).orElse(null);
    }

    public List<Camps> filterCamps(String title, String location, String city, String organizerBy){
        return campRepository.findCampsByFilters(title, location, city, organizerBy);
    }

    public Camps createCamp(Camps camp) {
        return campRepository.save(camp);
    }

    public Camps updateCamp(Long id, Camps updatedCamp) {
        return campRepository.findById(id).map(camp -> {
            camp.setTitle(updatedCamp.getTitle());
            camp.setLocation(updatedCamp.getLocation());
            camp.setCity(updatedCamp.getCity());
            camp.setDate(updatedCamp.getDate());
            camp.setOrganizerBy(updatedCamp.getOrganizerBy());
            return campRepository.save(camp);
        }).orElse(null);
    }
    

    public void deleteCamp(Long id) {
        campRepository.deleteById(id);
    }
}
