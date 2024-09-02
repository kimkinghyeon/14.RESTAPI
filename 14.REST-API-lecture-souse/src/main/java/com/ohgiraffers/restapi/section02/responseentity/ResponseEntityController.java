package com.ohgiraffers.restapi.section02.responseentity;

import org.apache.catalina.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/entity")
public class ResponseEntityController {
    /*
    ResponseEntity
    결과 데이터와 HTTP 상태 코드를 직접 제어 할 수 있는 클래스
    HttpStatus,HttpHeaders,HttpBody 를 포함하고 있다.
    =>  우리가 응답의 상태코드, 헤더, 바디 를 좀 더
        다양한 상황에 적절시 대등 할 수 있게 해준다.
    * */
    private List<UserDTO> users;

    public ResponseEntityController() {
        users = new ArrayList<>();
        users.add(new UserDTO(1,"user01","pass01","bear", LocalDate.now()));
        users.add(new UserDTO(2,"user02","pass02","cat", LocalDate.now()));
        users.add(new UserDTO(3,"user03","pass03","dog", LocalDate.now()));
    }

    // user 정보 전체조회
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {

        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType(
                        "application",
                        "json",
                        Charset.forName("UTF-8")
                )
        );

        // body
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users);

        ResponseMessage responseMessage = new ResponseMessage(
                200,
                "조회 성공!",
                responseMap
        );

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable int userNo){
        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType(
                        "application",
                        "json",
                        Charset.forName("UTF-8")
                )
        );
        // body

        // PathVariable 로 받은 userNo 와 일치하는 회원정보 하나 가져오기
        UserDTO foundUser = users.stream().
                filter(user -> user.getNo() == userNo).toList().get(0);
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("user",foundUser);

        return ResponseEntity
                .ok().headers(headers)
                .body(new ResponseMessage(200,"성공",responseMap));
    }

    // 회원 추가
    @PostMapping("/users")
    public ResponseEntity<?> regist(@RequestBody UserDTO newUser){
        System.out.println("newUser = " + newUser);

        // 마지막에 위치한 유저 번호
        int latUserNo = users.get(users.size() - 1 ).getNo();
        // 마지막 위치한 유저정보 + 1
        newUser.setNo(latUserNo + 1);
        // 유저 추가
        users.add(newUser);
        return ResponseEntity
                // restfull
                // 201 상태코드 -> 등록관련(성공했을때)
                .created(URI.create("/entity/users/"+users.get(users.size() -1).getNo()))
                .build();
    }
    // 수정 201
    @PutMapping("/users/{userNo}")
    public ResponseEntity<?> modifyUser(@PathVariable int userNo, @RequestBody UserDTO modifyInfo){
        // 수정할 유저 정보 찾아오기
        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).toList().get(0);
        // 수정할 정보를 RequestBody 로 받아서 id , pwd , name 수정
        foundUser.setId(modifyInfo.getId());
        foundUser.setPwd(modifyInfo.getPwd());
        foundUser.setName(modifyInfo.getName());
        return ResponseEntity.created(URI.create("/entity/users/" + userNo)).build();
    }

    // 유저 삭제 204
    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo){
        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).toList().get(0);
        users.remove(foundUser);
        return ResponseEntity.noContent().build();
    }
}

