package riannegreiros.xyz.enem_api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import riannegreiros.xyz.enem_api.dto.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExamService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ExamDto> getExams() {
        try (InputStream is = new ClassPathResource("static/exams.json").getInputStream()) {
            List<Map<String, Object>> exams = objectMapper.readValue(is, new TypeReference<>() {
            });
            List<ExamDto> result = new ArrayList<>();
            for (Map<String, Object> exam : exams) {
                int year = (int) exam.get("year");
                String title = (String) exam.get("title");
                result.add(new ExamDto(year, title));
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read exams.json", e);
        }
    }

    public ExamDetailDto getExamDetails(int year) {
        String path = String.format("static/%d/details.json", year);
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            Map<String, Object> details = objectMapper.readValue(is, new TypeReference<>() {
            });
            String title = (String) details.get("title");
            List<ExamDetailDto.LanguageDto> languages = new ArrayList<>();
            List<?> langsRaw = (List<?>) details.get("languages");
            for (Object l : langsRaw) {
                Map<String, String> langMap = objectMapper.convertValue(l, new TypeReference<>() {
                });
                languages.add(new ExamDetailDto.LanguageDto(langMap.get("name"), langMap.get("value")));
            }
            List<ExamDetailDto.DisciplineDto> disciplines = new ArrayList<>();
            List<?> discsRaw = (List<?>) details.get("disciplines");
            for (Object d : discsRaw) {
                Map<String, String> discMap = objectMapper.convertValue(d, new TypeReference<>() {
                });
                disciplines.add(new ExamDetailDto.DisciplineDto(discMap.get("name"), discMap.get("value")));
            }
            List<ExamDetailDto.QuestionSummaryDto> questions = new ArrayList<>();
            List<?> qsRaw = (List<?>) details.get("questions");
            for (Object q : qsRaw) {
                Map<String, Object> qMap = objectMapper.convertValue(q, new TypeReference<>() {
                });
                int idx = (int) qMap.get("index");
                String disc = (String) qMap.get("discipline");
                String lang = (String) qMap.get("language");
                questions.add(new ExamDetailDto.QuestionSummaryDto(idx, disc, lang));
            }
            return new ExamDetailDto(year, title, languages, disciplines, questions);
        } catch (IOException e) {
            throw new RuntimeException("Exam details not found for year: " + year, e);
        }
    }

    public QuestionsResponseDto getQuestions(int year, Integer limit, Integer offset,
            String language, String discipline) {
        limit = Math.min(limit, 50);
        List<QuestionDetailDto> allQuestions = new ArrayList<>();
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
                    Map<String, Object> q = objectMapper.readValue(is, new TypeReference<>() {
                    });
                    String qLang = (String) q.get("language");
                    String qDisc = (String) q.get("discipline");
                    if ((language == null || language.equals(qLang))
                            && (discipline == null || discipline.equals(qDisc))) {
                        int idx = (int) q.get("index");
                        String text = (String) q.getOrDefault("text", (String) q.get("context"));
                        Map<String, String> options = new java.util.HashMap<>();
                        if (q.containsKey("alternatives")) {
                            List<Map<String, Object>> alternatives = (List<Map<String, Object>>) q.get("alternatives");
                            for (Map<String, Object> alt : alternatives) {
                                options.put((String) alt.get("letter"), (String) alt.get("text"));
                            }
                        }
                        String answer = (String) q.getOrDefault("answer", q.get("correctAlternative"));
                        allQuestions.add(new QuestionDetailDto(idx, text, options, answer, qDisc, qLang));
                    }
                } catch (IOException ignore) {
                }
            }
            total = allQuestions.size();
            int toIndex = Math.min(offset + limit, total);
            List<QuestionDetailDto> paged = (offset < total) ? allQuestions.subList(offset, toIndex) : List.of();
            var metadata = new QuestionsResponseDto.MetadataDto(limit, offset, total);
            return new QuestionsResponseDto(metadata, paged);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read questions for year: " + year, e);
        }
    }

    public QuestionDetailDto getQuestion(int year, int index, String language) {
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
                Map<String, Object> q = objectMapper.readValue(is, new TypeReference<>() {
                });
                String qLang = (String) q.get("language");
                if (language != null && !language.equals(qLang)) {
                    continue;
                }
                String text = (String) q.getOrDefault("text", (String) q.get("context"));
                Map<String, String> options = new java.util.HashMap<>();
                if (q.containsKey("alternatives")) {
                    List<Map<String, Object>> alternatives = (List<Map<String, Object>>) q.get("alternatives");
                    for (Map<String, Object> alt : alternatives) {
                        options.put((String) alt.get("letter"), (String) alt.get("text"));
                    }
                }
                String answer = (String) q.getOrDefault("answer", q.get("correctAlternative"));
                String discipline = (String) q.get("discipline");
                return new QuestionDetailDto(index, text, options, answer, discipline, qLang);
            } catch (IOException ignore) {
                // Try next path
            }
        }
        throw new RuntimeException("Question not found for year: " + year + ", index: " + index
                + (language != null ? ", language: " + language : ""));
    }

    public QuestionsResponseDto getQuestionsByIndices(int year, List<Integer> indices,
            Integer limit, Integer offset, String language) {
        limit = Math.min(limit, 50);
        List<QuestionDetailDto> questions = new ArrayList<>();
        for (Integer idx : indices) {
            try {
                questions.add(getQuestion(year, idx, language));
            } catch (Exception ignore) {
            }
        }
        int total = questions.size();
        int toIndex = Math.min(offset + limit, total);
        List<QuestionDetailDto> paged = (offset < total) ? questions.subList(offset, toIndex) : List.of();
        var metadata = new QuestionsResponseDto.MetadataDto(limit, offset, total);
        return new QuestionsResponseDto(metadata, paged);
    }
}
