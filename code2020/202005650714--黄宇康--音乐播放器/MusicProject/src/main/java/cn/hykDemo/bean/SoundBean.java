package cn.hykDemo.bean;

import lombok.*;

@Data
@AllArgsConstructor  //全参构造
@NoArgsConstructor   //无参构造
@ToString            //toString
@Setter
@Getter

public class SoundBean {
    private String filePath;
    private String addDate;
    private String lrcPath;
}
