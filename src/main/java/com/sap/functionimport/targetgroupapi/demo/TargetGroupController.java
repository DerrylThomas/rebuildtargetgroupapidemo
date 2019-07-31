package com.sap.functionimport.targetgroupapi.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TargetGroupController {
    
    @Autowired
    TargetGroupService targetGroupService;
    
    @RequestMapping("/")
    public String greeting() {
    	return "Hello! Your app is up and running :)";
    }

    @PostMapping("/rebuildTargetGroup")
    public ResponseEntity<String> greeting(@RequestBody TargetGroupRequestEntity targetGroupRequestEntity, @RequestHeader("Authorization") String authorization) {
    	return targetGroupService.rebuildTargetGroup(targetGroupRequestEntity,authorization);
    }
}
