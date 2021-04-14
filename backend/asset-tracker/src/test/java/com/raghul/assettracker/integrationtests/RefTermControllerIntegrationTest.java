package com.raghul.assettracker.integrationtests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.raghul.assettracker.AssetTrackerApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = AssetTrackerApplication.class
		)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class RefTermControllerIntegrationTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	public void RefTermController() throws Exception{
		MultiValueMap< String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("key", "assetType");
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/refterm").queryParams(queryParams)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

}
