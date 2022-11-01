package com.group.libraryapp.dto.user.request;

public class UserCreateRequest {

  private String name;

  public UserCreateRequest(String name, Integer age) {
    this.name = name;
    this.age = age;
  }

  private Integer age;

  public String getName() {
    return name;
  }

  public Integer getAge() {
    return age;
  }

}
