package de.ff.jf.bftag.alarmmonitor;

public class Keyword {
    private String keyword;
    private String stage;

    public Keyword() {
    }

    public Keyword(String keyword, String stage) {
        this.keyword = keyword;
        this.stage = stage;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
