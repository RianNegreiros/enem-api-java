package riannegreiros.xyz.enem_api.dto;

import java.util.Map;

public class QuestionDetailDto {
    private int index;
    private String text;
    private Map<String, String> options;
    private String answer;
    private String discipline;
    private String language;

    public QuestionDetailDto(int index, String text, Map<String, String> options,
                           String answer, String discipline, String language) {
        this.index = index;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.discipline = discipline;
        this.language = language;
    }

    public int getIndex() {
        return index;
    }

    public String getText() {
        return text;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }

    public String getDiscipline() {
        return discipline;
    }

    public String getLanguage() {
        return language;
    }
}
