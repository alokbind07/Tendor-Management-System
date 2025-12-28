package com.fresco.tenderManagement.service;

import com.fresco.tenderManagement.model.BiddingModel;
import com.fresco.tenderManagement.model.UserModel;
import com.fresco.tenderManagement.repository.BiddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BiddingService {

    @Autowired
    private BiddingRepository biddingRepository;

    @Autowired
    private UserService userService;

    public ResponseEntity<Object> postBidding(BiddingModel biddingModel) {

       try {
            String email = getCurrentUserEmail();
            if (email == null) {
                return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
            }

            UserModel user = userService.getUserByEmail(email);
            if (user == null || user.getRole() == null) {
                return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
            }

            if (!"BIDDER".equalsIgnoreCase(user.getRole().getRolename())) {
                return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
            }

            biddingModel.setBidderId(user.getId());
            biddingModel.setDateOfBidding(getCurrentDate());

            BiddingModel saved = biddingRepository.save(biddingModel);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (DataIntegrityViolationException dex) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getBidding(double bidAmount) {

        List<BiddingModel> list = biddingRepository.findByBidAmountGreaterThan(bidAmount);

        if (list == null || list.isEmpty()) {
            return new ResponseEntity<>("no data available", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateBidding(int id, BiddingModel model) {

         BiddingModel bidding = biddingRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request"));

         String email = getCurrentUserEmail();
         if (email == null) {
             return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
         }
     
         UserModel user = userService.getUserByEmail(email);
         if (user == null || user.getRole() == null) {
             return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
         }

         if (!"APPROVER".equalsIgnoreCase(user.getRole().getRolename())) {
        return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
        }
    
        if (model.getStatus() != null) {
            bidding.setStatus(model.getStatus());
        }
        BiddingModel updated = biddingRepository.save(bidding);
        return new ResponseEntity<>(updated, HttpStatus.OK);
        
    }

    public ResponseEntity<Object> deleteBidding(int id) {

         BiddingModel bidding = biddingRepository.findById(id).orElse(null);
         if (bidding == null) {
             return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
         }
     
         String email = getCurrentUserEmail();
         if (email == null) {
             return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
         }

        UserModel user = userService.getUserByEmail(email);
        if (user == null || user.getRole() == null) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    
        String roleName = user.getRole().getRolename();
        if ("APPROVER".equalsIgnoreCase(roleName) || user.getId() == bidding.getBidderId()) {
            biddingRepository.delete(bidding);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("you don’t have permission", HttpStatus.FORBIDDEN);
        }
    }

    private String getCurrentUserEmail(){
        Object principal = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                : null;

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }

    private String getCurrentDate(){
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }
}
