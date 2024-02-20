package hb403.geoexplore.UserStorage;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class UserStorage {

	public static void main(String[] args) {
		SpringApplication.run(UserStorage.class, args);
	}

	@Bean
	CommandLineRunner initUser(UserRepository userRepository) {
		return args -> {
			User user1 = new User("John", "john@somemail.com", "password");
			User user2 = new User("Jane", "jane@somemail.com", "123");
			User user3 = new User("Justin", "justin@somemail.com", "Uhdjodicdoi");
			userRepository.save(user1);
			userRepository.save(user2);
			userRepository.save(user3);

		};
	}

}
