package com.desafiolatam.desafioface.data;

import com.desafiolatam.desafioface.models.Developer;

import java.util.List;

public class DeveloperQueries {
    public List<Developer> all(){
        return Developer.listAll(Developer.class);
    }
}
