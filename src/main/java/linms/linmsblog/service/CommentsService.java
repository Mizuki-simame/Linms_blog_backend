package linms.linmsblog.service;

import jakarta.servlet.http.HttpServletRequest;
import linms.linmsblog.common.Result;
import linms.linmsblog.model.dto.CommentsDTO;
import linms.linmsblog.model.entity.Comments;

import java.util.List;

public interface CommentsService {

    Result<List<Comments>> getComments(String id);

    public Result<Comments> addComments(CommentsDTO commentsDTO, HttpServletRequest request);

}
