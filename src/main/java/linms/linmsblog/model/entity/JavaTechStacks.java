package linms.linmsblog.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;

/**
 * Java基础技术栈表(JavaTechStacks)表实体类
 *
 * @author makejava
 * @since 2025-03-09 14:53:04
 */
@Data
@TableName("java_tech_stacks")
public class JavaTechStacks{

    private Integer id;
//技术栈名称
    private String name;
//核心作用
    private String purpose;
//关键库/工具
    private String keyTools;
//典型示例
    private String examples;
//分类（基础/框架/工具等）
    private String category;
//图片
    private String image;

    private Integer categoryId;
}

