package org.example.kt_tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KtTasksApplication {

  public static void main(String[] args) {
    SpringApplication.run(KtTasksApplication.class, args);

    //Alternative SpringApplication creation to explicitly set WebApplicationType in order to avoid internal errors
//    SpringApplication springApplication = new SpringApplication(KtTasksApplication.class);
//    springApplication.setWebApplicationType(WebApplicationType.SERVLET);
//    //OR
//    springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
//    springApplication.run(args);
  }

}
