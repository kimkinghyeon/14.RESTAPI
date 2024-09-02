package com.ohgiraffers.restapi.section01.response;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Controller
//@ResponseBody
@RestController
@RequestMapping("/response")
public class ResponseTestController {
    // 문자열 응답
    @GetMapping("/hello")
    public String helloword() {
        System.out.println("helloword");
        return "helloword";
    }
    // 기본 자료형 test
    @GetMapping("/random")
    public int getRandomNumber() {
        return (int)(Math.random()*10) + 1;
    }

    // Object 타입 응답
    @GetMapping("/message")
    public Message message() {
        return new Message(200, "Hello World");
    }

    // List 타입 응답
    @GetMapping("/list")
    public List<String> getList(){
        return List.of(new String[] {"1","2","3"});
    }

    // map 타입 응답
    @GetMapping("/map")
    public Map<Integer,String> getMap() {
        Map<Integer,String> messageMap = new HashMap<>();
        messageMap.put(200,"정상응답");
        messageMap.put(404,"페이지를 찾을 수 없음");
        messageMap.put(500,"서버내부 오류");
        return messageMap;
    }

    // images 타입 응답
    /*
    image response
        produces 설정을 해주지 않으면 이미지가 텍스트로 변형된 형태그대로 출력한다.
        produces 를 통해 response header 의 content-type(MIME) 에 대한 설정을 해줄 수 있다.
    */

    @GetMapping(value = "/image", produces =  MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage() throws IOException {
        return getClass().getResourceAsStream("/images/다운로드 (1).png").readAllBytes();
    }

    // ResponseEntity 를 이용한 응답
    @GetMapping("/entity")
    public ResponseEntity<Message> messageResponseEntity(){
        return ResponseEntity.ok(new Message(200,"응답하라 오버"));
    }
}
