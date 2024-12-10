package com.ev_centers.project.controller;

import com.ev_centers.project.entity.EvOwner;
import com.ev_centers.project.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody HashMap<String,String> loginRequest){
        HashMap<String, Object> response = new HashMap<>();
        try{
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            if(username == null || password ==null || username.isEmpty() || password.isEmpty()){
                response.put("msg","Enter the required fields");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if("Admin".equals(username) && "admin123".equals(password)){
                response.put("msg","Login successful");
                response.put("username",username);
                return ResponseEntity.ok(response);
            }
            else{
                response.put("msg","Invalid username and password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e){
            response.put("msg","An error occur during login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/add-ev-owner")
    public ResponseEntity<?> addEvowner(@RequestBody EvOwner evOwner){
        HashMap<String,Object> response = new HashMap<>();
        try{
            EvOwner addedOwner = adminService.addEvowner(evOwner);
            response.put("msg","EVOwner added successfully");
            response.put("OwnerId",addedOwner.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (RuntimeException e){
            response.put("msg",e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        catch (Exception e){
            response.put("msg","An error occured while adding owner");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




}
