package linms.linmsblog.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadDTO {

    private MultipartFile markdown;

    private MultipartFile cover;

    private int categoryId;

    private int techStackId;

}
