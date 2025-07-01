package riannegreiros.xyz.enem_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import riannegreiros.xyz.enem_api.dto.*;
import riannegreiros.xyz.enem_api.service.ExamService;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ExamV1Controller {
    private final ExamService examService;

    public ExamV1Controller(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/exams")
    public List<ExamDto> getExams() {
        return examService.getExams();
    }

    @GetMapping("/exams/{year}")
    public ResponseEntity<ExamDetailDto> getExamDetails(@PathVariable int year) {
        try {
            return ResponseEntity.ok(examService.getExamDetails(year));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/exams/{year}/questions")
    public ResponseEntity<QuestionsResponseDto> getQuestions(
            @PathVariable int year,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String discipline) {

        if (limit > 50) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(examService.getQuestions(year, limit, offset, language, discipline));
    }

    @PostMapping("/exams/{year}/questions")
    public ResponseEntity<QuestionsResponseDto> getQuestionsByIndices(
            @PathVariable int year,
            @RequestBody QuestionIndicesDto requestBody,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String language) {

        if (requestBody.getIndices() == null || requestBody.getIndices().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (limit > 50) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                examService.getQuestionsByIndices(year, requestBody.getIndices(), limit, offset, language));
    }

    @GetMapping("/exams/{year}/questions/{index}")
    public ResponseEntity<QuestionDetailDto> getQuestion(
            @PathVariable int year,
            @PathVariable int index,
            @RequestParam(required = false) String language) {
        try {
            return ResponseEntity.ok(examService.getQuestion(year, index, language));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
