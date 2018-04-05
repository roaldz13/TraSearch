package com.example.taquio.trasearch.SearchLogic;

/**
 * Created by Del Mar on 3/14/2018.
 */

public class DataFilter {
  private String[] wordFilters = {"crafts", "craft", "diy", "how",
          "recycle", "recycling", "recycable", "learn", "making", "handmade",
          "handcraft", "handcrafted", "recover", "recovering", "recovery",
          "reprocess", "reutilized", "utilized", "utilize", "eco-friendly",
          "ecofriendly", "eco-friend", "ecofriend", "composting", "disposal"};

  private String[] phraseFilters = {"how to", "do it", "make it", "solid waste",
          "resource recover", "resource recovery", "eco friendly", "eco friend",
          "environmentally friendly", "do it yourself", "do it your own",
          "learn how to", "make your own", "make it your own", "solid waste management"};

  public String[] getWordFilters() {
    return wordFilters;
  }

  public String[] getPhraseFilters() {
    return phraseFilters;
  }
}