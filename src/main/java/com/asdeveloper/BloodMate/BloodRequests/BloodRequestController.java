package com.asdeveloper.BloodMate.BloodRequests;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/blood-requests")
public class BloodRequestController {
    private final BloodRequestService service;

    public BloodRequestController(BloodRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<BloodRequest> getAllRequests() {
        return service.getAllRequests();
    }
    
    @GetMapping("/{id}")
    public BloodRequest getRequestById(@PathVariable Long id) {
        return service.getRequestById(id);
    }

    @GetMapping("/filter")
    public List<BloodRequest> getFilteredRequests(
            @RequestParam(required = false) String city,
            @RequestParam(required = false)  String bloodGroup,
            @RequestParam(required = false) String urgencyLevel,
            @RequestParam(required = false) Long requesterId,
            @RequestParam(required = false) Long donorId,
            @RequestParam(required = false) Boolean unassigned) {

        return service.getFilteredRequests(city, bloodGroup, urgencyLevel, requesterId, donorId, unassigned);
    }

    @PostMapping("/{requesterId}")
    public BloodRequest createRequest(@RequestBody BloodRequest request, @PathVariable Long requesterId) {
        return service.createBloodRequest(request, requesterId);
    }

    @PutMapping("/donationComplete/{id}")
    public BloodRequest putMethodName(@PathVariable Long id) {
        return service.updateDonationCompleted(id);
    }

    @PutMapping("/{requestId}/assign-donor/{donorId}")
    public BloodRequest assignDonor(@PathVariable Long requestId, @PathVariable Long donorId) {
        return service.assignDonor(requestId, donorId);
    }

    @PutMapping("/{requestId}/unassign-donor")
    public BloodRequest unassignDonor(@PathVariable Long requestId) {
        return service.unassignDonor(requestId);
    }

    @DeleteMapping("/{id}")
    public void deleteRequest(@PathVariable Long id) {
        service.deleteRequest(id);
    }
}
