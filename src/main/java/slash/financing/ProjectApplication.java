package slash.financing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
@PropertySource(value = ".env", encoding = "UTF-8")
public class ProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

}
