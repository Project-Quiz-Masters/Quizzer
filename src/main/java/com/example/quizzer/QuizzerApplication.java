package com.example.quizzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class QuizzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizzerApplication.class, args);
	}

	@Controller
	static class RootController {
		@GetMapping("/")
		public String root() {
			return "redirect:/quizzes";
		}
	}
}
