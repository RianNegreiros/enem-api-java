package riannegreiros.xyz.enem_api.dto;

public class ExamDto {
    private int year;
    private String title;

    public ExamDto(int year, String title) {
        this.year = year;
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public String getName() {
        return title;
    }
}
