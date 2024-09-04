package com.ohgiraffers.restapi.section05.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

// Tag : API 들의 그룹을 짓기 위한 어노테이션
@Tag(name = "Spring boot Swagger 연동 API(user 기능)")
@RestController
@RequestMapping("/swagger")
public class SwaggerTestController {

    private List<UserDTO> users;

    public SwaggerTestController() {
        users = new ArrayList<>();
        users.add(new UserDTO(1,"user01","pass01","bear", LocalDate.now()));
        users.add(new UserDTO(2,"user02","pass02","cat", LocalDate.now()));
        users.add(new UserDTO(3,"user03","pass03","dog", LocalDate.now()));
    }

    @Operation(summary = "유저 전체조회" , description = "전체회원 목록 조회")
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType(
                        "application",
                        "json",
                        Charset.forName("UTF-8")
                )
        );


        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users);

        ResponseMessage responseMessage = new ResponseMessage(
                200,
                "조회 성공!",
                responseMap
        );

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @Operation(
            summary = "회원번호로 회원 조회",
            description = "회원번호를 통해 회원 조회",
            parameters = {
                    @Parameter(
                            name ="userNo" ,
                            description = "사용자 화면에서 넘어오는 user 의 pk"
                    )
            })
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable int userNo){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType(
                        "application",
                        "json",
                        Charset.forName("UTF-8")
                )
        );

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


        int latUserNo = users.get(users.size() - 1 ).getNo();

        newUser.setNo(latUserNo + 1);

        users.add(newUser);
        return ResponseEntity

                .created(URI.create("/entity/users/"+users.get(users.size() -1).getNo()))
                .build();
    }

    @PutMapping("/users/{userNo}")
    public ResponseEntity<?> modifyUser(@PathVariable int userNo, @RequestBody UserDTO modifyInfo){

        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).toList().get(0);

        foundUser.setId(modifyInfo.getId());
        foundUser.setPwd(modifyInfo.getPwd());
        foundUser.setName(modifyInfo.getName());
        return ResponseEntity.created(URI.create("/entity/users/" + userNo)).build();
    }
    @Operation(summary = "유저 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "회원정보 삭제 성공"),
            @ApiResponse(responseCode = "400",description = "잘못 입력된 파마미터")
    })
    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo){
        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).toList().get(0);
        users.remove(foundUser);
        return ResponseEntity.noContent().build();
    }
}
