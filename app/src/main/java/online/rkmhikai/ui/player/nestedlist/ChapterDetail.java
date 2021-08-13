package online.rkmhikai.ui.player.nestedlist;


import java.util.List;

import online.rkmhikai.Tables.Content;

public class ChapterDetail {
    private String chapterTitle;
    private int totalLecture;
    private List<Content> lectureList;

    public ChapterDetail(String itemTitle, int totalLecture, List<Content> subItemList) {
        this.chapterTitle = itemTitle;
        this.totalLecture = totalLecture;
        this.lectureList = subItemList;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public int getTotalLecture() {
        return totalLecture;
    }

    public void setTotalLecture(int totalLecture) {
        this.totalLecture = totalLecture;
    }

    public List<Content> getLectureList() {
        return lectureList;
    }

    public void setLectureList(List<Content> lectureList) {
        this.lectureList = lectureList;
    }
}
