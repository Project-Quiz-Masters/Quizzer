package com.example.quizzer.answeroption;

import com.example.quizzer.question.Question;
import com.example.quizzer.question.QuestionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quizzes/{quizId}/questions/{questionId}/options")
public class AnswerOptionController {

    private final AnswerOptionService answerOptionService;
    private final QuestionRepository questionRepository;

    public AnswerOptionController(AnswerOptionService answerOptionService, QuestionRepository questionRepository) {
        this.answerOptionService = answerOptionService;
        this.questionRepository = questionRepository;
    }

    @GetMapping
    public String listOptions(@PathVariable Long quizId, @PathVariable Long questionId, Model model) {
        Optional<Question> q = questionRepository.findById(questionId);
        if (q.isEmpty()) {
            model.addAttribute("error", "Question not found");
            return "error";
        }

        List<AnswerOption> options = answerOptionService.getAnswerOptionsByQuestion(questionId);
        model.addAttribute("quizId", quizId);
        model.addAttribute("question", q.get());
        model.addAttribute("options", options);
        model.addAttribute("newOption", new AnswerOption());
        return "answer-options";
    }

    @PostMapping
    public String addOption(@PathVariable Long quizId, @PathVariable Long questionId,
                            @RequestParam String text,
                            @RequestParam(defaultValue = "false") boolean correct,
                            Model model) {
        Optional<Question> q = questionRepository.findById(questionId);
        if (q.isEmpty()) {
            model.addAttribute("error", "Question not found");
            return "error";
        }

        AnswerOption option = new AnswerOption();
        option.setText(text);
        option.setCorrect(correct);
        option.setQuestion(q.get());
        answerOptionService.addAnswerOption(option);

        return "redirect:/quizzes/" + quizId + "/questions/" + questionId + "/options";
    }

    @PostMapping("/{answerOptionId}/delete")
    public String deleteOption(@PathVariable("quizId") Long quizId, @PathVariable("questionId") Long questionId, @PathVariable("answerOptionId") Long answerOptionId) {
        answerOptionService.deleteAnswerOption(answerOptionId);
        return "redirect:/quizzes/" + quizId + "/questions/" + questionId + "/options";
    }
}
