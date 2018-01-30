package com.dhirajdwivedi.quizfirebase;

import java.util.ArrayList;

/**
 * Created by Dhiraj on 18-01-2018.
 */

public class Quiz {
    private String name;
    private ArrayList<Question> questions;
    Integer totalMarks;

    public Quiz(){
        this.questions=new ArrayList<>();
        this.totalMarks=0;
    }

    public void addQuestion(Question question){
        questions.add(question);
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }
    public Integer getTotalMarks(){return totalMarks;}
    public ArrayList<Question> getQuestions(){
        return questions;
    }
}
