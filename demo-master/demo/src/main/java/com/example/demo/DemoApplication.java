package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	CommandLineRunner runner (StudentRepository repository, MongoTemplate mongoTemplate){
		return args -> {
			Adress adress =new Adress(
					"Turkey","06422","Ankara"
			);
			String email="gulnebsd2@gmail.com";
			Student student=new Student(
					"Nebi","Gul",email,
					Gender.MALE,adress, List.of("Computer Science","Maths"), BigDecimal.TEN,
					LocalDateTime.now()
			);
			//usingMangoTemplateAndQuery(repository, mongoTemplate, email, student);
			repository.findStudentByEmail(email)
					.ifPresentOrElse(student1 -> {
						System.out.println(student1 + "already exists");
					},()->{System.out.println("Inserting Student " + student);
						repository.insert(student);});
		};
	}

	private void usingMangoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) throws IllegalAccessException {
		Query query=new Query();
		query.addCriteria(Criteria.where("email").is(email));

		List<Student> students= mongoTemplate.find(query,Student.class);
		if (students.size()>1){
			throw  new IllegalAccessException(
					"found many students with email" + email);
		}
		if (students.isEmpty()) {
			System.out.println("Inserting Student " + student);
			repository.insert(student);
		}
		else {
			System.out.println(student + "already exists");
		}
	}

}
