package io.mosip.preregistration.notification;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for NotificationApplication.
 * 
 * @author Sanober Noor
 * @since 1.0.0
 *
 */

@SpringBootApplication(scanBasePackages= {"io.mosip.preregistration.*","io.mosip.kernel.auth.*"})
public class NotificationApplication implements CommandLineRunner {

	/**
	 * Main method for NotificationApplication.
	 * 
	 * @param args
	 *            the arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
