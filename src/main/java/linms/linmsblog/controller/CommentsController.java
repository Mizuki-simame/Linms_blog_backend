package linms.linmsblog.controller;


import jakarta.servlet.http.HttpServletRequest;
import linms.linmsblog.common.Result;
import linms.linmsblog.common.uilts.JwtUtils;
import linms.linmsblog.model.dto.CommentsDTO;
import linms.linmsblog.model.entity.Comments;
import linms.linmsblog.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping("/comments/{id}")
    public Result<List<Comments>> getComments(@PathVariable String id) {
        return commentsService.getComments(id);
    }

    @PostMapping("/comments")
    public Result<Comments> addComments(@RequestBody CommentsDTO commentsDTO, HttpServletRequest request) {
        return commentsService.addComments(commentsDTO, request);
    }
}
