package com.example.QuizGame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class QuizController {

    private List<Question> questions = new ArrayList<>();

    public QuizController() {
        // kysymyksiä peliin
        questions.add(new Question(1, "Mikä on maailman suurin valtio pinta-alaltaan?", "Venäjä"));
        questions.add(new Question(2, "Mikä on Suomen pääkaupunki?", "Helsinki"));
        questions.add(new Question(3, "Mikä on suurin nisäkäs?", "Sinivalas"));
    }

    // Juurimappaus näyttää info/ohjetiedot
    @GetMapping("/")
    public String info() {
        return "Tervetuloa visailupeliin! Käytä /questions-endpointtia pelataksesi.";
    }

    // Get-mappaukset
    @GetMapping("/questions")
    public List<Question> getQuestions() {
        return questions;
    }

   @GetMapping("/questions/{id}")
public ResponseEntity<Question> getQuestionById(@PathVariable int id) {
    for (Question question : questions) {
        if (question.getId() == id) {
            return ResponseEntity.ok(question);
        }
    }
    throw new QuestionNotFoundException("Kysymystä ei löydy: " + id);
}


    // POST-mappaus
    @PostMapping("/answer")
    public String postAnswer(@RequestBody Answer answer) {
        for (Question question : questions) {
            if (question.getId() == answer.getQuestionId()) {
                if (answer.getAnswer().equalsIgnoreCase(question.getAnswer())) {
                    return "Oikein!";
                } else {
                    return "Väärin.";
                }
            }
        }
        throw new QuestionNotFoundException("Kysymystä ei löydy: " + answer.getQuestionId());
    }

    public static void main(String[] args) {
        SpringApplication.run(QuizController.class, args);
    }
}

class Question {
    private int id;
    private String question;
    private String answer;

    public Question(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}

class Answer {
    private int questionId;
    private String answer;

    public int getQuestionId() {
        return questionId;
    }

    public String getAnswer() {
        return answer;
    }
}
