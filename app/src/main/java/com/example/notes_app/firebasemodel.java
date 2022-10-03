package com.example.notes_app;

public class firebasemodel {
    public static String title;
    public static String content;
    public firebasemodel(){}
    public firebasemodel(String title,String content){
        this.title=title;
        this.content=content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

