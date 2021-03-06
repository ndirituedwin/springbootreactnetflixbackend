package com.ndirituedwin;

import com.ndirituedwin.Config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
@EntityScan(basePackageClasses = {NetflixCloneApplication.class,
		Jsr310JpaConverters.class}

)
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class NetflixCloneApplication {
	@PostConstruct
	void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}


	public static void main(String[] args) {
		SpringApplication.run(NetflixCloneApplication.class, args);
	}

}
