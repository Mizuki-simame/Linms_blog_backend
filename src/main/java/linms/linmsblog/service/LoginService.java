package linms.linmsblog.service;

import jakarta.servlet.http.HttpSession;
import linms.linmsblog.common.Result;
import linms.linmsblog.model.dto.LoginDTO;

public interface LoginService {
    Result<String> login(LoginDTO loginDTO);

    Result<String> loginOut(LoginDTO loginDTO);
}
