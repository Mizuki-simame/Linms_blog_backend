package linms.linmsblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpSession;
import linms.linmsblog.common.Result;
import linms.linmsblog.common.uilts.JwtUtils;
import linms.linmsblog.common.uilts.PasswordUtils;
import linms.linmsblog.mapper.UserMapper;
import linms.linmsblog.model.dto.LoginDTO;
import linms.linmsblog.model.entity.User;
import linms.linmsblog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<String> login(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if(user == null) {
            return Result.fail("用户不存在");
        }
        if (!PasswordUtils.checkPassword(loginDTO.getPassword(),user.getPassword())) {
            return Result.fail("密码错误");
        }
        String token = JwtUtils.generateToken(user.getUsername());
        return Result.ok(token);
    }

    @Override
    public Result<String> loginOut(LoginDTO loginDTO) {
        return Result.ok("OK");
    }
}
