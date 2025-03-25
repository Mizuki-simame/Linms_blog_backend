package linms.linmsblog.common.uilts;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class ImageUploadUtil {

    //阿里域名
    public static final String ALI_DOMAIN="";

    public static String uploadImage(MultipartFile file) throws IOException {
        //生成文件名
        String originalFilename = file.getOriginalFilename();
        String ext="."+ FilenameUtils.getExtension((originalFilename));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName=uuid+ext;

        //域名节点
        String endpoint="";
        String accessKeyId="";
        String accessKeySecret="";

        //OSS客服端对象

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(
                "lin123-test",
                fileName,
                file.getInputStream()
        );
        ossClient.shutdown();

        return ALI_DOMAIN+fileName;
    }

}
