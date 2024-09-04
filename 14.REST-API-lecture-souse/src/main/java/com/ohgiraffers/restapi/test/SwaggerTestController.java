package com.ohgiraffers.restapi.test;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Tag : API 들의 그룹을 짓기 위한 어노테이션
@Tag(name = "Spring boot Swagger 연동 API(user 기능)")
@RestController
@RequestMapping("/board")
public class SwaggerTestController {

    private List<BoardDTO> post;

    public SwaggerTestController() {
        post = new ArrayList<>();
        post.add(new BoardDTO(1,"title01","content01"));
        post.add(new BoardDTO(2,"title02","content02"));
        post.add(new BoardDTO(3,"title03","content03"));
    }

    @Operation(summary = "게시글 전체조회" , description = "전체게시글 목록 조회")
    @GetMapping("/boardList")
    public ResponseEntity<ResponseMessage> findAllPosts() {


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType(
                        "application",
                        "json",
                        Charset.forName("UTF-8")
                )
        );


        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("post", post);

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
                            name ="boardId" ,
                            description = "사용자 화면에서 넘어오는 user 의 pk"
                    )
            })
    @GetMapping("/board/{boardId}")
    public ResponseEntity<ResponseMessage> findPostByBoardId(@PathVariable int boardId){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType(
                        "application",
                        "json",
                        Charset.forName("UTF-8")
                )
        );

        BoardDTO foundUser = post.stream().
                filter(post -> post.getBoardId() == boardId).toList().get(0);
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("board",foundUser);

        return ResponseEntity
                .ok().headers(headers)
                .body(new ResponseMessage(200,"성공",responseMap));
    }


    @PostMapping("/post")
    public ResponseEntity<?> newPost(@RequestBody BoardDTO newPost){
        System.out.println("newPost = " + newPost);


        int latUserNo = post.get(post.size() - 1 ).getBoardId();

        newPost.setBoardId(latUserNo + 1);

        post.add(newPost);
        return ResponseEntity

                .created(URI.create("/entity/posts/"+post.get(post.size() -1).getBoardId()))
                .build();
    }

    @PutMapping("/post/{boardId}")
    public ResponseEntity<?> modifyUser(@PathVariable int boardId, @RequestBody BoardDTO modifyInfo){

        BoardDTO foundUser = post.stream().filter(user -> user.getBoardId() == boardId).toList().get(0);

        foundUser.setBoardId(modifyInfo.getBoardId());
        foundUser.setTitle(modifyInfo.getTitle());
        foundUser.setContent(modifyInfo.getContent());
        return ResponseEntity.created(URI.create("/entity/posts/" + boardId)).build();
    }
    @Operation(summary = "유저 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "회원정보 삭제 성공"),
            @ApiResponse(responseCode = "400",description = "잘못 입력된 파마미터")
    })
    @DeleteMapping("/post/{boardId}")
    public ResponseEntity<?> removeUser(@PathVariable int boardId){
        BoardDTO foundUser = post.stream().filter(user -> user.getBoardId() == boardId).toList().get(0);
        post.remove(foundUser);
        return ResponseEntity.noContent().build();
    }
}
