package br.fcv.poc.web;

import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.hasItem;

import static br.fcv.poc.web.ContainsDistinctItemsMatcher.containsDistinctItems;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Dev: Tests implementation, specially the parts regarding async dispatch, is highly based on
 * http://callistaenterprise.se/blogg/teknik/2014/06/23/testing-non-blocking-rest-services-with-spring-mvc-and-spring-boot/
  */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JavaControllerTest {

	@Inject
	public MockMvc mvc;

	@Test
	public void requestShouldBeHandledByMultipleThreadsAndByJavaImplementations() throws Exception {

		MvcResult result =  mvc.perform(get("/api/rest/v1/instant?controllerType=java&actorType=java").accept(APPLICATION_JSON))
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
				// assert request is handled by Java's Controller and Actor
				.andExpect(jsonPath("$.trace[*].className", hasItem(containsString("JavaController"))))
				.andExpect(jsonPath("$.trace[*].className", hasItem(containsString("JavaActor"))))
		;
	}

	@Test
	public void requestShouldBeHandledByMultipleThreadsAndByScalaActorWhenRequested() throws Exception {

		MvcResult result =  mvc.perform(get("/api/rest/v1/instant?controllerType=java&actorType=scala").accept(APPLICATION_JSON))
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
				// assert request is handled by Java's Controller but by Scala's Actor
				.andExpect(jsonPath("$.trace[*].className", hasItem(containsString("JavaController"))))
				.andExpect(jsonPath("$.trace[*].className", hasItem(containsString("ScalaActor"))))
		;
	}

}
