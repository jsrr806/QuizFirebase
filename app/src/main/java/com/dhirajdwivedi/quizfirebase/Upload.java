package com.dhirajdwivedi.quizfirebase;

/**
 * Created by Dhiraj on 16-01-2018.
 */

public class Upload {
    public String name;
    public String url;

    public Upload(){

    }

    public Upload(String name, String url){
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
