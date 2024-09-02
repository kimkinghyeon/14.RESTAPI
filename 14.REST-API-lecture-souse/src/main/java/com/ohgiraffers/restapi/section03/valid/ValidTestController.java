package com.ohgiraffers.restapi.section03.valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/valid")
public class ValidTestController {
    // 에러를 발생시켜서 에러를 처리할 수 있는 컨트롤러를 따로 만들것

    @GetMapping("users/{userNo}")
    public ResponseEntity<?> findUser() throws userException{

        // 항상 UserNotFoundException 을 던지게 함
        boolean check = true;
        if(check){
            throw new userException("회원정보 없음");
        }
        return ResponseEntity.ok().build();
    }
}
