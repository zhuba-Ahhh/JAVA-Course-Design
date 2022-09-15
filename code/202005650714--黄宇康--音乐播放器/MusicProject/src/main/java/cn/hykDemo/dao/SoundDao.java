package cn.hykDemo.dao;

import cn.hykDemo.bean.SoundBean;

import java.util.List;

public interface SoundDao {

    // 1.根据分组，查找数据库中所有歌曲
    List<SoundBean> findAll();
    // 2.根据分组，添加歌曲

    // 3.根据分组，删除歌曲

    // 4.根据某歌查找某歌词路径
    List<SoundBean> findPlayPath(String name);
}
