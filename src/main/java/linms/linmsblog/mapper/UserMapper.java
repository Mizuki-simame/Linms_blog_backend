package linms.linmsblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import linms.linmsblog.model.entity.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<User> {

}