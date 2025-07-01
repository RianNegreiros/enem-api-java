package riannegreiros.xyz.enem_api.dto;

import java.util.List;

public class ExamListItemDto {
  private String title;
  private int year;
  private List<DisciplineDto> disciplines;
  private List<LanguageDto> languages;

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

  public List<DisciplineDto> getDisciplines() {
    return disciplines;
  }

  public void setDisciplines(List<DisciplineDto> disciplines) {
    this.disciplines = disciplines;
  }

  public List<LanguageDto> getLanguages() {
    return languages;
  }

  public void setLanguages(List<LanguageDto> languages) {
    this.languages = languages;
  }

  public static class DisciplineDto {
    private String label;
    private String value;

    public String getLabel() {
      return label;
    }

    public void setLabel(String label) {
      this.label = label;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

  public static class LanguageDto {
    private String label;
    private String value;

    public String getLabel() {
      return label;
    }

    public void setLabel(String label) {
      this.label = label;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }
}