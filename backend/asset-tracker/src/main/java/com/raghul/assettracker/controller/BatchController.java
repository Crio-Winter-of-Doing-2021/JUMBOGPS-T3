package com.raghul.assettracker.controller;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raghul.assettracker.service.BatchService;

@RestController
@RequestMapping("/api/batch")
public class BatchController {
	
	@Resource
	private BatchService batchService;
	
	@GetMapping("/start")
	public ResponseEntity<?> insertIntoTable(){
		batchService.insertRecords();
		return ResponseEntity.ok().build();
	}

}
