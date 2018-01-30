package com.dhirajdwivedi.quizfirebase;

/**
 * Created by Dhiraj on 18-01-2018.
 */

public class Question {
    private String statement;
    private String option1,option2,option3,option4;
    private int answer,marks;

    public Question(String statement,String option1,String option2,String option3,String option4,int answer,int marks){
        this.answer=answer;
        this.option1=option1;
        this.option2=option2;
        this.option3=option3;
        this.option4=option4;
        this.statement=statement;
        this.marks=marks;
    }

    public int getAnswer(){
        return answer;
    }

    public int getMarks() {
        return marks;
    }

    public String getOption1(){
        return option1;
    }
    public String getOption2(){
        return option2;
    }
    public String getOption3(){
        return option3;
    }
    public String getOption4(){
        return option4;
    }
    public String getStatement(){
        return statement;
    }

    public void setStatement(String statement){
        this.statement=statement;
    }
    public void setOption1(String option1){
        this.option1=option1;
    }
    public void setOption2(String option2){
        this.option2=option2;
    }
    public void setOption3(String option3){
        this.option3=option3;
    }
    public void setOption4(String option4){
        this.option4=option4;
    }
    public void setAnswer(int answer){
        this.answer=answer;
    }
}
