import cn.hutool.poi.excel.cell.FormulaCellValue;

public class Dissertation {
    private String title;
    private String year;
    private String author;
    private String source;
    private FormulaCellValue pdf;

    public FormulaCellValue getPdf() {
        return pdf;
    }

    public Dissertation setPdf(FormulaCellValue pdf) {
        this.pdf = pdf;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Dissertation setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getYear() {
        return year;
    }

    public Dissertation setYear(String year) {
        this.year = year;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Dissertation setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getSource() {
        return source;
    }

    public Dissertation setSource(String source) {
        this.source = source;
        return this;
    }
}
