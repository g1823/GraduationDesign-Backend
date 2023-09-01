package com.albummanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AlbumManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlbumManagementApplication.class, args);
    }

}
