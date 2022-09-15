package cn.hykDemo.daoimp;

import cn.hykDemo.bean.SoundBean;
import cn.hykDemo.dao.SoundDao;
import cn.hykDemo.utils.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class SoundDaoImp implements SoundDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<SoundBean> findAll() {
        String sql = "select * from sound";
        List<SoundBean> listSound = template.query(sql, new BeanPropertyRowMapper<SoundBean>(SoundBean.class));
        return listSound;
    }

    @Override
    public List<SoundBean> findPlayPath(String name) {
        System.out.println(name);
        String sql = "select lrcPath from sound where filePath = ?";
        List<SoundBean> query = template.query(sql, new BeanPropertyRowMapper<>(SoundBean.class), name);
        return query;
    }
}
