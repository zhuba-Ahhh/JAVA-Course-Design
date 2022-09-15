package cn.hykDemo;

import cn.hykDemo.bean.SoundBean;
import cn.hykDemo.daoimp.SoundDaoImp;

import java.util.List;

public class aaa {
    public static void main(String[] args) {
        SoundDaoImp sdi = new SoundDaoImp();
        List<SoundBean> all = sdi.findAll();
        System.out.println(all);
    }
}
