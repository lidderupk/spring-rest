package com.upkar.springdemo.controller;

import com.upkar.springdemo.model.Book;
import com.upkar.springdemo.model.EmptyJsonResponse;
import com.upkar.springdemo.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/api")
public class SampleRestController {

	private Logger logger = LoggerFactory.getLogger(SampleRestController.class);

    @GetMapping(path = "/hello-map")
    public Map<String, Boolean> getHello(HttpServletResponse resp){
        Map<String, Boolean> map = new HashMap<String, Boolean>(1){{put("result", Boolean.TRUE);}};
        return map;
    }

    @GetMapping(path = "/hello-map-immutable")
    public Map<String, String> getHelloStringNew(HttpServletResponse resp){
        return Collections.singletonMap("message", "Unauthorized");
    }

    @GetMapping(path = "/hello-array")
    public List<String> getHelloArray(HttpServletResponse resp){
        List result = Arrays.asList("first", "second", "third", "fourth");
        return result;

    }

    @GetMapping(path = "/hello-string")
    public ResponseEntity<String> getHelloString(HttpServletResponse resp){
        // this does not result in application/json
        // it returns text/html;charset=UTF-8
        return new ResponseEntity<String>("hello-string", HttpStatus.OK);
    }

    @GetMapping("/empty")
    public ResponseEntity something() {
        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }
}
