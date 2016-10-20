package br.fcv.poc.web.random_generator;

import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RandomGeneratorController {

	private static final Logger logger = getLogger(RandomGeneratorController.class);

	private SimpMessageSendingOperations messageTemplate;

	public RandomGeneratorController(
			SimpMessageSendingOperations messageTemplate) {

		logger.debug("RandomGeneratorController(messagingTemplate: {})", messageTemplate);
		this.messageTemplate = requireNonNull(messageTemplate, "messageTemplate cannot be null");
	}

	@Scheduled(fixedDelay = 1000)
	public void broadcastChanges() {

		String random = UUID.randomUUID().toString();
		String destination = "/topic/random";
		messageTemplate.convertAndSend(destination, random);
		logger.debug("broadcast random value '{}' to '{}'", random, destination);
	}
}
