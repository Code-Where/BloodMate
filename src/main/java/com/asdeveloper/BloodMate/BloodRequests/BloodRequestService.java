package com.asdeveloper.BloodMate.BloodRequests;

import com.asdeveloper.BloodMate.Users.User;
import com.asdeveloper.BloodMate.Users.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodRequestService {
    @Autowired
    private final BloodRequestRepository bloodRequestRepository;
    @Autowired
    private final UserRepository userRepository;

    public BloodRequestService(BloodRequestRepository bloodRequestRepository, UserRepository userRepository) {
        this.bloodRequestRepository = bloodRequestRepository;
        this.userRepository = userRepository;
    }

    public List<BloodRequest> getAllRequests() {
        return bloodRequestRepository.findAll();
    }

    public BloodRequest getRequestById(Long id) {
        return bloodRequestRepository.findById(id).orElse(null);
    }
    
    public List<BloodRequest> getFilteredRequests(String city, String bloodGroup, String urgencyLevel, Long requesterId, Long donorId, Boolean unassigned) {
        return bloodRequestRepository.findByFilters(city, bloodGroup, urgencyLevel, requesterId, donorId, unassigned);
    }

    public BloodRequest createBloodRequest(BloodRequest request, Long requesterId) {
        Optional<User> requester = userRepository.findById(requesterId);
        requester.ifPresent(request::setRequester);
        return bloodRequestRepository.save(request);
    }

    public BloodRequest updateDonationCompleted(Long id){
        BloodRequest bloodRequest = bloodRequestRepository.getReferenceById(id);
        bloodRequest.setIsDonationCompleted(true);
        return bloodRequestRepository.save(bloodRequest);
    }

    public BloodRequest assignDonor(Long requestId, Long donorId) {
        Optional<BloodRequest> bloodRequest = bloodRequestRepository.findById(requestId);
        Optional<User> donor = userRepository.findById(donorId);
        
        if (bloodRequest.isPresent() && donor.isPresent()) {
            BloodRequest request = bloodRequest.get();
            request.setDonor(donor.get());
            return bloodRequestRepository.save(request);
        }
        return null;
    }

    public BloodRequest unassignDonor(Long requestId) {
        Optional<BloodRequest> bloodRequest = bloodRequestRepository.findById(requestId);
    
        if (bloodRequest.isPresent()) {
            BloodRequest request = bloodRequest.get();
            request.setDonor(null);  
            return bloodRequestRepository.save(request);
        }
        return null;
    }

    public String deleteRequest(Long id) {
        bloodRequestRepository.deleteById(id);
        return "Deleted Successfully";
    }
}
