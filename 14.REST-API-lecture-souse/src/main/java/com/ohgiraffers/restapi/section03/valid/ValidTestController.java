package com.ohgiraffers.restapi.section03.valid;

import com.ohgiraffers.restapi.section02.responseentity.ResponseMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/valid")
public class ValidTestController {
    // 에러를 발생시켜서 에러를 처리할 수 있는 컨트롤러를 따로 만들것
    private List<UserDTO> users;

    public ValidTestController() {
        users = new ArrayList<>();
        users.add(new UserDTO(1,"user01","pass01","cat", LocalDate.now()));
        users.add(new UserDTO(2,"user02","pass02","dog", LocalDate.now()));
        users.add(new UserDTO(3,"user03","pass03","cow", LocalDate.now()));
        users.add(new UserDTO(4,"user04","pass04","monkey", LocalDate.now()));
    }

    @PostMapping("/users")
    public ResponseEntity<?> registUser(@Valid @RequestBody UserDTO newUser){
        System.out.println("body 로 들어온 UserDTO : " + newUser);

        return ResponseEntity.created(URI.create("/valid/users/" + "userNo")).build();
    }

    @GetMapping("users/{userNo}")
    public ResponseEntity<?> findUserByNo (@PathVariable int userNo) throws UserNotFoundException {

        //Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        //Body
        List<UserDTO> fundUserList = users.stream().filter(user -> user.getNo() == userNo).toList();

        UserDTO fundUser = null;
        if(fundUserList.size() > 0){
            // userNo 이 일치하는 회원이 있으면
            fundUser = fundUserList.get(0);
        } else {
            throw new UserNotFoundException("회원 정보를 찾을 수 없습니다.");
        }

        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("user",fundUser);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ResponseMessage(200,"조회 성공",responseMap));
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

}
