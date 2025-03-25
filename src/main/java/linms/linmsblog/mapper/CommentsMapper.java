package linms.linmsblog.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import linms.linmsblog.model.entity.Comments;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {
}
