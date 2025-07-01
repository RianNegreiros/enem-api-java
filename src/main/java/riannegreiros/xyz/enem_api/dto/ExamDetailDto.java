package riannegreiros.xyz.enem_api.dto;

import java.util.List;

public class ExamDetailDto {
    private int year;
    private String title;
    private List<LanguageDto> languages;
    private List<DisciplineDto> disciplines;
    private List<QuestionSummaryDto> questions;

    public ExamDetailDto(int year, String title, List<LanguageDto> languages,
            List<DisciplineDto> disciplines, List<QuestionSummaryDto> questions) {
        this.year = year;
        this.title = title;
        this.languages = languages;
        this.disciplines = disciplines;
        this.questions = questions;
    }

    public int getYear() {
        return year;
    }

    public String getName() {
        return title;
    }

    public List<LanguageDto> getLanguages() {
        return languages;
    }

    public List<DisciplineDto> getDisciplines() {
        return disciplines;
    }

    public List<QuestionSummaryDto> getQuestions() {
        return questions;
    }

    public static class LanguageDto {
        private String name;
        private String value;

        public LanguageDto(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

    public static class DisciplineDto {
        private String name;
        private String value;

        public DisciplineDto(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

    public static class QuestionSummaryDto {
        private int index;
        private String discipline;
        private String language;

        public QuestionSummaryDto(int index, String discipline, String language) {
            this.index = index;
            this.discipline = discipline;
            this.language = language;
        }

        public int getIndex() {
            return index;
        }

        public String getDiscipline() {
            return discipline;
        }

        public String getLanguage() {
            return language;
        }
    }
}
