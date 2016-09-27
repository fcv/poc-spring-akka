package br.fcv.poc;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * <p>
 * Describes {@link ActorRef} instance to be injected. Example:
 * <pre>
 *   // ...
 *   &#64;Inject
 *   &#64;ActorDesc(MyJavaActor.class)
 *   private ActorRef actor
 *   
 *   &#64;Inject
 *   &#64;ActorDesc(value = MyJavaActor.class, name = "anotherActorName")
 *   private ActorRef actor
 *   // ...
 * </pre>
 * </p>
 *
 * @author veronez
 * @see ApplicationConfiguration#newActor(org.springframework.beans.factory.InjectionPoint, ActorSystem)
 *
 */
@Target({FIELD, METHOD, TYPE, PARAMETER})
@Retention(RUNTIME)
public @interface ActorDesc {

	Class<? extends Actor> value();

	String name() default "";

}
