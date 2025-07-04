package riannegreiros.xyz.enem_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import riannegreiros.xyz.enem_api.dto.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamService {

    private static final Logger logger = LoggerFactory.getLogger(ExamService.class);

    private final ObjectMapper objectMapper;

    public ExamService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<ExamDto> getExams() {
        logger.debug("Fetching all exams");
        try (InputStream is = new ClassPathResource("static/exams.json").getInputStream()) {
            List<ExamListItemDto> exams = objectMapper.readValue(is,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ExamListItemDto.class));
            List<ExamDto> result = new ArrayList<>();
            for (ExamListItemDto exam : exams) {
                int year = exam.getYear();
                String title = exam.getTitle();
                result.add(new ExamDto(year, title));
            }
            return result;
        } catch (IOException e) {
            logger.error("Failed to read exams.json: {}", e.getMessage());
            throw new RuntimeException("Failed to read exams.json", e);
        }
    }

    public ExamDetailDto getExamDetails(int year) {
        logger.debug("Fetching exam details for year {}", year);
        String path = String.format("static/%d/details.json", year);
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            ExamDetailsDto details = objectMapper.readValue(is, ExamDetailsDto.class);
            String title = details.getTitle();
            List<ExamDetailDto.LanguageDto> languages = new ArrayList<>();
            if (details.getLanguages() != null) {
                for (ExamDetailsDto.LanguageDto lang : details.getLanguages()) {
                    languages.add(new ExamDetailDto.LanguageDto(lang.getName(), lang.getValue()));
                }
            }
            List<ExamDetailDto.DisciplineDto> disciplines = new ArrayList<>();
            if (details.getDisciplines() != null) {
                for (ExamDetailsDto.DisciplineDto disc : details.getDisciplines()) {
                    disciplines.add(new ExamDetailDto.DisciplineDto(disc.getName(), disc.getValue()));
                }
            }
            List<ExamDetailDto.QuestionSummaryDto> questions = new ArrayList<>();
            if (details.getQuestions() != null) {
                for (ExamDetailsDto.QuestionSummaryDto q : details.getQuestions()) {
                    questions.add(
                            new ExamDetailDto.QuestionSummaryDto(q.getIndex(), q.getDiscipline(), q.getLanguage()));
                }
            }
            return new ExamDetailDto(year, title, languages, disciplines, questions);
        } catch (IOException e) {
            logger.error("Exam details not found for year {}: {}", year, e.getMessage());
            throw new RuntimeException("Exam details not found for year: " + year, e);
        }
    }

    public QuestionsResponseDto getQuestions(int year, Integer limit, Integer offset,
            String language, String discipline) {
        logger.debug("Fetching questions for year {}, limit={}, offset={}, language={}, discipline={}", year, limit,
                offset, language, discipline);
        limit = Math.min(limit, 50);
        List<QuestionDetailsDto> allQuestions = new ArrayList<>();
        int total = 0;
        String basePath = String.format("static/%d/questions/", year);
        try {
            ClassPathResource questionsDir = new ClassPathResource(basePath);
            String[] questionDirs = questionsDir.getFile().list();
            if (questionDirs == null)
                questionDirs = new String[0];
            for (String qDir : questionDirs) {
                String qPath = basePath + qDir + "/details.json";
                try (InputStream is = new ClassPathResource(qPath).getInputStream()) {
                    QuestionDetailsDto q = objectMapper.readValue(is, QuestionDetailsDto.class);
                    String qLang = q.getLanguage();
                    String qDisc = q.getDiscipline();
                    if ((language == null || language.equals(qLang))
                            && (discipline == null || discipline.equals(qDisc))) {
                        allQuestions.add(q);
                    }
                } catch (IOException ignore) {
                }
            }
            total = allQuestions.size();
            int toIndex = Math.min(offset + limit, total);
            List<QuestionDetailsDto> paged = (offset < total) ? allQuestions.subList(offset, toIndex) : List.of();
            var metadata = new QuestionsResponseDto.MetadataDto(limit, offset, total);
            return new QuestionsResponseDto(metadata, paged);
        } catch (IOException e) {
            logger.error("Failed to read questions for year {}: {}", year, e.getMessage());
            throw new RuntimeException("Failed to read questions for year: " + year, e);
        }
    }

    public QuestionDetailsDto getQuestion(int year, int index, String language) {
        logger.debug("Fetching question for year {}, index={}, language={}", year, index, language);
        String basePath = String.format("static/%d/questions/", year);
        String[] tryPaths;
        if (language != null) {
            tryPaths = new String[] {
                    basePath + index + "/details.json",
                    basePath + index + "-" + language + "/details.json"
            };
        } else {
            tryPaths = new String[] {
                    basePath + index + "/details.json",
                    basePath + index + "-ingles/details.json",
                    basePath + index + "-espanhol/details.json"
            };
        }
        for (String path : tryPaths) {
            try (InputStream is = new ClassPathResource(path).getInputStream()) {
                QuestionDetailsDto q = objectMapper.readValue(is, QuestionDetailsDto.class);
                String qLang = q.getLanguage();
                if (language != null && !language.equals(qLang)) {
                    continue;
                }
                return q;
            } catch (IOException ignore) {
                // Try next path
            }
        }
        logger.error("Question not found for year {}, index={}, language={}", year, index, language);
        throw new RuntimeException("Question not found for year: " + year + ", index: " + index
                + (language != null ? ", language: " + language : ""));
    }

    public QuestionsResponseDto getQuestionsByIndices(int year, List<Integer> indices,
            Integer limit, Integer offset, String language) {
        logger.debug("Fetching questions by indices for year {}, indices={}, limit={}, offset={}, language={}", year,
                indices, limit, offset, language);
        limit = Math.min(limit, 50);
        List<QuestionDetailsDto> questions = new ArrayList<>();
        for (Integer idx : indices) {
            try {
                questions.add(getQuestion(year, idx, language));
            } catch (Exception ignore) {
            }
        }
        int total = questions.size();
        int toIndex = Math.min(offset + limit, total);
        List<QuestionDetailsDto> paged = (offset < total) ? questions.subList(offset, toIndex) : List.of();
        var metadata = new QuestionsResponseDto.MetadataDto(limit, offset, total);
        return new QuestionsResponseDto(metadata, paged);
    }
}
