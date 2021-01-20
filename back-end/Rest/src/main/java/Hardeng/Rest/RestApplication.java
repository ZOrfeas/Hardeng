package Hardeng.Rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import Hardeng.Rest.config.auth.TokenUtil;


@SpringBootApplication
public class RestApplication {
	private static Logger log = LoggerFactory.getLogger(RestApplication.class);

	public static void main(String[] args) {
		// log.info("Master key is: " + TokenUtil.generateMasterToken());
		SpringApplication.run(RestApplication.class, args);
	}

}
