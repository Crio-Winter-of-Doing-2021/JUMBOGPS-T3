package com.raghul.assettracker.controller;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raghul.assettracker.service.RefTermService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v1/refterm")
@CrossOrigin
@Api( tags = "Reference Values")
public class RefTermController {
	
	@Resource
	private RefTermService refTermService;
	
	/**
	 * Method to get the reference values
	 * @param key
	 * @return List<RefTerm> list of reference
	 */
	@GetMapping("")
	ResponseEntity<?> getReferenceValues(@RequestParam("key") String key) {
	
		return ResponseEntity.ok().body(refTermService.getRefTermsValue(key));
	}

}
