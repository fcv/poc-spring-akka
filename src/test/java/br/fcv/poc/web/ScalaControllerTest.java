package br.fcv.poc.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.inject.Inject;

import static br.fcv.poc.web.ContainsDistinctItemsMatcher.containsDistinctItems;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Dev: Tests implementation, specially the parts regarding async dispatch, is highly based on
 * http://callistaenterprise.se/blogg/teknik/2014/06/23/testing-non-blocking-rest-services-with-spring-mvc-and-spring-boot/
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ScalaControllerTest {

	@Inject
	public MockMvc mvc;

	@Test
	public void requestShouldBeHandledByMultipleThreadsAndByScalaImplementations() throws Exception {

		// based on http://callistaenterprise.se/blogg/teknik/2014/06/23/testing-non-blocking-rest-services-with-spring-mvc-and-spring-boot/
		MvcResult result =  mvc.perform(get("/api/rest/v1/instant").accept(APPLICATION_JSON))
				.andExpect(request().asyncStarted())
				.andReturn();

		mvc.perform(asyncDispatch(result))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
				.andExpect(jsonPath("$.provider", isA(String.class)))
				.andExpect(jsonPath("$.info.nano", isA(Number.class)))
				.andExpect(jsonPath("$.info.epochSecond", isA(Number.class)))
				// assert request is handled by different threads
				.andExpect(jsonPath("$.trace[*].threadName", containsDistinctItems()))
				// assert request is handled by Scala's Controller and Actor
				.andExpect(jsonPath("$.trace[*].className", hasItem(containsString("ScalaController"))))
				.andExpect(jsonPath("$.trace[*].className", hasItem(containsString("ScalaActor"))))
		;
	}

	@Test
	public void requestShouldBeHandledByMultipleThreadsAndByJavaActorWhenRequested() throws Exception {

		// based on http://callistaenterprise.se/blogg/teknik/2014/06/23/testing-non-blocking-rest-services-with-spring-mvc-and-spring-boot/
		MvcResult result =  mvc.perform(get("/api/rest/v1/instant?actorType=java").accept(APPLICATION_JSON))
				.andExpect(request().asyncStarted())
				.andReturn();

		mvc.perform(asyncDispatch(result))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
				.andExpect(jsonPath("$.provider", isA(String.class)))
				.andExpect(jsonPath("$.info.nano", isA(Number.class)))
				.andExpect(jsonPath("$.info.epochSecond", isA(Number.class)))
				// assert request is handled by different threads
				.andExpect(jsonPath("$.trace[*].threadName", containsDistinctItems()))
				// assert request is handled by Scala's Controller but by Java's Actor
				.andExpect(jsonPath("$.trace[*].className", hasItem(containsString("ScalaController"))))
				.andExpect(jsonPath("$.trace[*].className", hasItem(containsString("JavaActor"))))
		;
	}

}
