package riannegreiros.xyz.enem_api.dto;

import java.util.List;

public class QuestionDetailsDto {
  private String title;
  private int index;
  private int year;
  private String language;
  private String discipline;
  private String context;
  private List<String> files;
  private String correctAlternative;
  private String alternativesIntroduction;
  private List<Alternative> alternatives;

  // Getters and setters
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getDiscipline() {
    return discipline;
  }

  public void setDiscipline(String discipline) {
    this.discipline = discipline;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public List<String> getFiles() {
    return files;
  }

  public void setFiles(List<String> files) {
    this.files = files;
  }

  public String getCorrectAlternative() {
    return correctAlternative;
  }

  public void setCorrectAlternative(String correctAlternative) {
    this.correctAlternative = correctAlternative;
  }

  public String getAlternativesIntroduction() {
    return alternativesIntroduction;
  }

  public void setAlternativesIntroduction(String alternativesIntroduction) {
    this.alternativesIntroduction = alternativesIntroduction;
  }

  public List<Alternative> getAlternatives() {
    return alternatives;
  }

  public void setAlternatives(List<Alternative> alternatives) {
    this.alternatives = alternatives;
  }

  public static class Alternative {
    private String letter;
    private String text;
    private String file;
    private boolean isCorrect;

    public String getLetter() {
      return letter;
    }

    public void setLetter(String letter) {
      this.letter = letter;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    public String getFile() {
      return file;
    }

    public void setFile(String file) {
      this.file = file;
    }

    public boolean isCorrect() {
      return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
      this.isCorrect = isCorrect;
    }
  }
}