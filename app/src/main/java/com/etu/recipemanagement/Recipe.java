package com.etu.recipemanagement;

import java.util.ArrayList;

public class Recipe {

    private Integer id;
    private Integer user_id;

    private String name;
    private String desc;

    private ArrayList<String> ingredient_names;
    private ArrayList<String> ingredient_photo_paths;

    private ArrayList<String> cooking_steps;
    private ArrayList<String> cooking_step_photo_paths;

    private ArrayList<String> final_photo_paths;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<String> getIngredient_names() {
        return ingredient_names;
    }

    public void setIngredient_names(ArrayList<String> ingredient_names) {
        this.ingredient_names = ingredient_names;
    }

    public ArrayList<String> getIngredient_photo_paths() {
        return ingredient_photo_paths;
    }

    public void setIngredient_photo_paths(ArrayList<String> ingredient_photo_paths) {
        this.ingredient_photo_paths = ingredient_photo_paths;
    }

    public void setCooking_steps(ArrayList<String> cooking_steps) {
        this.cooking_steps = cooking_steps;
    }

    public void setCooking_step_photo_paths(ArrayList<String> cooking_step_photo_paths) {
        this.cooking_step_photo_paths = cooking_step_photo_paths;
    }

    public ArrayList<String> getFinal_photo_paths() {
        return final_photo_paths;
    }

    public void setFinal_photo_paths(ArrayList<String> final_photo_paths) {
        this.final_photo_paths = final_photo_paths;
    }

    public ArrayList<String> getCooking_step_photo_paths() {
        return cooking_step_photo_paths;
    }

    public ArrayList<String> getCooking_steps() {
        return cooking_steps;
    }

}
