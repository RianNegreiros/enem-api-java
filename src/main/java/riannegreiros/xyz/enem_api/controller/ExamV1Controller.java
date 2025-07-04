package riannegreiros.xyz.enem_api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import riannegreiros.xyz.enem_api.dto.*;
import riannegreiros.xyz.enem_api.service.ExamService;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ExamV1Controller {
    private static final Logger logger = LoggerFactory.getLogger(ExamV1Controller.class);
    private final ExamService examService;

    public ExamV1Controller(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/exams")
    public List<ExamDto> getExams() {
        logger.info("GET /exams called");
        return examService.getExams();
    }

    @GetMapping("/exams/{year}")
    public ResponseEntity<ExamDetailDto> getExamDetails(@PathVariable int year) {
        logger.info("GET /exams/{} called", year);
        try {
            return ResponseEntity.ok(examService.getExamDetails(year));
        } catch (Exception e) {
            logger.error("Exam details not found for year {}: {}", year, e.getMessage());
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

        logger.info("GET /exams/{}/questions called with limit={}, offset={}, language={}, discipline={}", year, limit,
                offset, language, discipline);
        if (limit > 50) {
            logger.warn("Limit {} is greater than 50", limit);
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

        logger.info("POST /exams/{}/questions called with indices={}, limit={}, offset={}, language={}", year,
                requestBody.getIndices(), limit, offset, language);
        if (requestBody.getIndices() == null || requestBody.getIndices().isEmpty()) {
            logger.warn("Empty indices in POST /exams/{}/questions", year);
            return ResponseEntity.badRequest().build();
        }

        if (limit > 50) {
            logger.warn("Limit {} is greater than 50", limit);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                examService.getQuestionsByIndices(year, requestBody.getIndices(), limit, offset, language));
    }

    @GetMapping("/exams/{year}/questions/{index}")
    public ResponseEntity<QuestionDetailsDto> getQuestion(
            @PathVariable int year,
            @PathVariable int index,
            @RequestParam(required = false) String language) {
        logger.info("GET /exams/{}/questions/{} called with language={}", year, index, language);
        try {
            return ResponseEntity.ok(examService.getQuestion(year, index, language));
        } catch (Exception e) {
            logger.error("Question not found for year {}, index {}: {}", year, index, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
