package linms.linmsblog.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import linms.linmsblog.model.entity.Articles;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticlesMapper extends BaseMapper<Articles> {
}
