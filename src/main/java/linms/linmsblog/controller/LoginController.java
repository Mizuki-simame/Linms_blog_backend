package linms.linmsblog.controller;


import linms.linmsblog.common.Result;
import linms.linmsblog.model.dto.LoginDTO;
import linms.linmsblog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public Result<String> Login(@RequestBody LoginDTO loginDTO) {
        return loginService.login(loginDTO);
    }

    @PostMapping("/user/logout")
    public Result<String> LoginOut() {
        return Result.ok();
    }

}
