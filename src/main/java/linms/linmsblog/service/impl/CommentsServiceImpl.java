package linms.linmsblog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import linms.linmsblog.common.Result;
import linms.linmsblog.common.uilts.JwtUtils;
import linms.linmsblog.config.RabbitMqConfig;
import linms.linmsblog.mapper.CommentsMapper;
import linms.linmsblog.mapper.UserMapper;
import linms.linmsblog.model.dto.CommentsDTO;
import linms.linmsblog.model.dto.MailDTO;
import linms.linmsblog.model.entity.Comments;
import linms.linmsblog.model.entity.User;
import linms.linmsblog.service.CommentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private UserMapper userMapper;

    private final RabbitTemplate rabbitTemplate;

    private final RabbitMqConfig rabbitMqConfig;

     private final String img= "https://lin123-test.oss-cn-guangzhou.aliyuncs.com/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202025-03-09%20203235.png ";


    @Override
    public Result<List<Comments>> getComments(String id) {
        LambdaQueryWrapper<Comments> wrapper = new LambdaQueryWrapper<Comments>()
                .eq(Comments::getArticleId, id);
        List<Comments> comments = commentsMapper.selectList(wrapper);
        return Result.ok(comments);
    }

    @Override
    public Result<Comments> addComments(CommentsDTO commentsDTO, HttpServletRequest request) {
       String userName = null;
       Integer userId = 0;
        String token = request.getHeader("authorization");
        if (token != null) {
            token = token.substring(7);
           userName = JwtUtils.parseToken(token);
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, userName)
                    .select(User::getUserId);
            userId = userMapper.selectOne(wrapper).getUserId();
        }
        Comments comments = new Comments();
        comments.setArticleId(commentsDTO.getArticleId());
        comments.setContent(commentsDTO.getContent());
        comments.setParentId(commentsDTO.getParentId());
        comments.setAvatar(img);
        comments.setUsername(userName);
        comments.setUserId(userId);
        commentsMapper.insert(comments);
        MailDTO mailDTO = new MailDTO();
        mailDTO.setUserId(userId);
        mailDTO.setCommit(commentsDTO.getContent());
        rabbitTemplate.convertAndSend(rabbitMqConfig.getMailQueue(), mailDTO);
        log.info("sent message with text: {}", mailDTO.getCommit());
        return Result.ok(comments);
    }
}
