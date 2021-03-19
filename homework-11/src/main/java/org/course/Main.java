package org.course;

//import com.github.cloudyrock.spring.v5.EnableMongock;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.course.api.pojo.BookShort;
import org.course.domain.*;
import org.course.repositories.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


//@EnableMongock
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
