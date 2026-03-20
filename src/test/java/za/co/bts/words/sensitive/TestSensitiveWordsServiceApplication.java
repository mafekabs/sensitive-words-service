package za.co.bts.words.sensitive;

import org.springframework.boot.SpringApplication;

public class TestSensitiveWordsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(SensitiveWordsServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
