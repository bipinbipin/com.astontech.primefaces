package com.astontech.primefaces;

import com.sun.faces.config.FacesInitializer;
import org.apache.catalina.Context;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

import org.springframework.context.annotation.Bean;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.annotation.HandlesTypes;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class PrimefacesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrimefacesApplication.class, args);
	}

	@Bean
//	@ConditionalOnMissingBean(NonEmbeddedServletContainerFactory.class)
	public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();

		tomcat.addContextCustomizers(new TomcatContextCustomizer() {
			@Override
			public void customize(Context context) {
				// register FacesInitializer
				context.addServletContainerInitializer(new FacesInitializer(),
						getServletContainerInitializerHandlesTypes(FacesInitializer.class));

				// add configuration from web.xml
				context.addWelcomeFile("helloface.xhtml");

				// register additional mime-types that Spring Boot doesn't register
				context.addMimeMapping("eot", "application/vnd.ms-fontobject");
				context.addMimeMapping("ttf", "application/x-font-ttf");
				context.addMimeMapping("woff", "application/x-font-woff");
			}
		});

		return tomcat;
	}

	private Set<Class<?>> getServletContainerInitializerHandlesTypes(Class<? extends ServletContainerInitializer> sciClass) {
		HandlesTypes annotation = sciClass.getAnnotation(HandlesTypes.class);
		if (annotation == null) {
			return Collections.emptySet();
		}

		Class[] classesArray = annotation.value();
		Set<Class<?>> classesSet = new HashSet<Class<?>>(classesArray.length);
		for (Class clazz: classesArray) {
			classesSet.add(clazz);
		}

		return classesSet;
	}
}
