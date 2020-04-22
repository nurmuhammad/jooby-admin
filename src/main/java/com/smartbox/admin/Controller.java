package com.smartbox.admin;

import io.jooby.Context;
import io.jooby.annotations.*;

@Path("/")
public class Controller {

  @GET
  public String sayHi() {
    return "Welcome to Jooby!";
  }

  @GET("/2")
  public String sayHi2() {
    return "Jooby-2";
  }

  @GET("/3")
  public String sayHi3() {
    return "Jooby-4";

  }

  @GET("/4")
  public String sayHi4() {
    return "Jooby-4";

  }

}
