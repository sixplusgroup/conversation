package finley.gmair.knowledgebase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


//@ComponentScan({"finley.gmair.dao", "finley.gmair.service", "finley.gmair.controller"})
@SpringBootApplication
public class KnowledgeBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowledgeBaseApplication.class, args);
    }

}
