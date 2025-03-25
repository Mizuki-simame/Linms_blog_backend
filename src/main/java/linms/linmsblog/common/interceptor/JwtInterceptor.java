package linms.linmsblog.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import linms.linmsblog.common.uilts.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;


@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求（跨域预检）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/user/login") || requestURI.contains("/user/register")) {
            return true; // 直接放行登录和注册路径
        }

        // 从请求头获取Token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            sendError(response, "缺少Token");
            return false;
        }

        // 提取并验证Token
        token = token.substring(7);
        String username = JwtUtils.parseToken(token);
        if (username == null) {
            sendError(response, "Token无效或已过期");
            return false;
        }

        // 将用户名存入请求属性，供后续使用
        request.setAttribute("username", username);
        return true;
    }

    // 返回错误信息
    private void sendError(HttpServletResponse response, String message) throws IOException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"code\": 401, \"message\": \"" + message + "\"}");
    }
}