package linms.linmsblog.model.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private String username;

    private String password;

    private String remember;

}
