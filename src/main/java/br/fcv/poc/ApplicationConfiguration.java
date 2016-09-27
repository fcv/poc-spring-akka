package br.fcv.poc;

import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import akka.actor.AbstractExtensionId;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ExtendedActorSystem;
import akka.actor.Extension;
import akka.actor.IndirectActorProducer;
import akka.actor.Props;

/**
 * <p>
 * Spring {@link Configuration} class responsible for managing, instantiating
 * and wiring up, {@link ActorRef} instances.
 * </p>
 * <p>
 * Implementation based on https://github.com/typesafehub/activator-akka-java-spring.
 * </p>
 * @author veronez
 *
 */
@Configuration
public class ApplicationConfiguration {

	private static final Logger logger = getLogger(ApplicationConfiguration.class);

	private ApplicationContext applicationContext;

	@Autowired
	public ApplicationConfiguration(ApplicationContext applicationContext) {
		this.applicationContext = requireNonNull(applicationContext, "applicationContext cannot be null");
	}

	@Bean
	public ActorSystem newActorSystem() {
		ActorSystem system = ActorSystem.create("poc-spring-akka");
		SpringExtensionId.instance().get(system).initialize(applicationContext);
		return system;
	}

	@Bean
	@Scope(SCOPE_PROTOTYPE)
	public ActorRef newActor(InjectionPoint injectionPoint, ActorSystem system) {

		logger.debug("newActor(injectionPoint: {}, system: {})", injectionPoint, system);
		ActorDesc actorDesc = null;
		Annotation[] annotations = injectionPoint.getAnnotations();
		for (int i = 0; actorDesc == null && i < annotations.length; i++) {
			Annotation annotation = annotations[i];
			if (annotation instanceof ActorDesc) {
				actorDesc = (ActorDesc) annotation;
			}
		}

		Class<? extends Actor> type = (Class<? extends Actor>) actorDesc.value();
		String name = actorDesc.name();
		if ("".equals(name)) {
			name = type.getSimpleName();
		}

		Props props = SpringExtensionId.instance().get(system).props(type);
		logger.debug("about to retrieve ActorRef out of ActorSystem.actorOf(props: {}, name: {}) based on ActorDesc: {}",
				props, name, actorDesc);
		return system.actorOf(props, name);
	}
}

class SpringExtensionId extends AbstractExtensionId<SpringExtension> {

	private static final SpringExtensionId INSTANCE = new SpringExtensionId();

	@Override
	public SpringExtension createExtension(ExtendedActorSystem system) {
		return new SpringExtension();
	}

	public static SpringExtensionId instance() {
		return INSTANCE;
	}
}

class SpringExtension implements Extension {

	private volatile ApplicationContext applicationContext;

	public void initialize(ApplicationContext applicationContext) {
		this.applicationContext = requireNonNull(applicationContext, "applicationContext cannot be null");
	}

	public Props props(String actorBeanName) {
		return Props.create(SpringActorByNameProducer.class, applicationContext, actorBeanName);
	}

	public Props props(Class<? extends Actor> actorType) {
		return Props.create(SpringActorByTypeProducer.class, applicationContext, actorType);
	}
}

class SpringActorByNameProducer implements IndirectActorProducer {

	private final ApplicationContext applicationContext;
	private final String actorBeanName;

	public SpringActorByNameProducer(ApplicationContext applicationContext, String actorBeanName) {
		this.applicationContext = requireNonNull(applicationContext, "applicationContext cannot be null");
		this.actorBeanName = requireNonNull(actorBeanName, "actorBeanName cannot be null");
	}

	@Override
	public Actor produce() {
		return applicationContext.getBean(actorBeanName, Actor.class);
	}

	@Override
	public Class<? extends Actor> actorClass() {
		@SuppressWarnings("unchecked")
		Class<? extends Actor> actorClass = (Class<? extends Actor>) applicationContext.getType(actorBeanName);
		return actorClass;
	}
}

class SpringActorByTypeProducer implements IndirectActorProducer {

	private final ApplicationContext applicationContext;
	private final Class<? extends Actor> actorType;

	public SpringActorByTypeProducer(ApplicationContext applicationContext, Class<? extends Actor> actorType) {
		this.applicationContext = requireNonNull(applicationContext, "applicationContext cannot be null");
		this.actorType = requireNonNull(actorType, "actorType cannot be null");
	}

	@Override
	public Actor produce() {
		return applicationContext.getBean(actorType);
	}

	@Override
	public Class<? extends Actor> actorClass() {
		return actorType;
	}
}
