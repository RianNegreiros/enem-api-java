package riannegreiros.xyz.enem_api.dto;

import java.util.List;

public class ExamDetailsDto {
  private String title;
  private int year;
  private List<LanguageDto> languages;
  private List<DisciplineDto> disciplines;
  private List<QuestionSummaryDto> questions;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public List<LanguageDto> getLanguages() {
    return languages;
  }

  public void setLanguages(List<LanguageDto> languages) {
    this.languages = languages;
  }

  public List<DisciplineDto> getDisciplines() {
    return disciplines;
  }

  public void setDisciplines(List<DisciplineDto> disciplines) {
    this.disciplines = disciplines;
  }

  public List<QuestionSummaryDto> getQuestions() {
    return questions;
  }

  public void setQuestions(List<QuestionSummaryDto> questions) {
    this.questions = questions;
  }

  public static class LanguageDto {
    private String name;
    private String value;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

  public static class DisciplineDto {
    private String name;
    private String value;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

  public static class QuestionSummaryDto {
    private int index;
    private String discipline;
    private String language;

    public int getIndex() {
      return index;
    }

    public void setIndex(int index) {
      this.index = index;
    }

    public String getDiscipline() {
      return discipline;
    }

    public void setDiscipline(String discipline) {
      this.discipline = discipline;
    }

    public String getLanguage() {
      return language;
    }

    public void setLanguage(String language) {
      this.language = language;
    }
  }
}