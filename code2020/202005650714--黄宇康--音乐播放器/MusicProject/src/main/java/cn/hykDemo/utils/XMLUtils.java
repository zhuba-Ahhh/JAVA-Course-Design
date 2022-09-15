package cn.hykDemo.utils;

import cn.hykDemo.bean.SoundBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class XMLUtils {
    //读取所有已创建的歌单
    public static List<String> readAllGroup(){
        //1.创建一个List<String>集合对象
        List<String> groupList = new ArrayList<>();
        //2.创建一个SAXReader对象
        SAXReader reader = new SAXReader();
        //3.读取Document对象
        try {
            Document dom = reader.read(XMLUtils.class.getClassLoader().getResourceAsStream("MusicGroup.xml"));
        //4.读取根元素
            Element root = dom.getRootElement();
            if (root == null){
                return groupList;
            }
            //5.读取所有根元素下的<group>子元素
            List<Element> groupEleList = root.elements("group");
            if (groupEleList == null || groupEleList.size() == 0){
                return groupList;
            }
            //6.遍历每个<group>元素，获取他的name属性的值(标签名)
            for (Element ele : groupEleList){
                groupList.add(ele.attributeValue("name"));
            }
        }catch (DocumentException e) {
            e.printStackTrace();
        }
        return groupList;
    }

    //2.像MusicGroup.xml中添加一个新歌单
    public static void addGroup(String groupName){
        SAXReader reader = new SAXReader();
        try {
            Document dom = reader.read(XMLUtils.class.getClassLoader().getResourceAsStream("MusicGroup.xml"));
            //读取元素
            Element root = dom.getRootElement();
            //像根元素下添加一个新的<group>元素
            Element groupEle = root.addElement("group");
            groupEle.addAttribute("name",groupName);
            groupEle.addAttribute("addDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            OutputFormat outputFormat = OutputFormat.createCompactFormat();
            outputFormat.setEncoding("UTF-8");
            outputFormat.setExpandEmptyElements(true);//生成完整的标签

            try {
                XMLWriter xmlWriter = new XMLWriter(new FileWriter(new File("MusicGroup.xml")),outputFormat);
                xmlWriter.write(dom);
                xmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    //3.向文件中写入”某个歌单“的歌曲:
    public static void insertSounds(String groupName,List<File> fileList){
        SAXReader reader = new SAXReader();
        try {
            System.out.println("添加歌曲");
            InputStream resourceAsStream = XMLUtils.class.getClassLoader().getResourceAsStream("MusicGroup.xml");
            System.out.println("开始添加");
            Document dom = reader.read(resourceAsStream);
            Element root = dom.getRootElement();
            List<Element> groupEleList = root.elements("group");
            for (Element ele : groupEleList){
                if (ele.attributeValue("name").equals(groupName)){
                    for (File file: fileList) {

                        Element soundEle = ele.addElement("sound");
                        Element filePathEle = soundEle.addElement("filePath");
                        Element addDateEle = soundEle.addElement("addDate");
                        filePathEle.setText(file.getAbsolutePath());
                        addDateEle.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                    }
                    break;
                }
            }
            OutputFormat outputFormat = OutputFormat.createCompactFormat();
            outputFormat.setEncoding("UTF-8");
            outputFormat.setExpandEmptyElements(true);//生成完整的标签

            try {
                XMLWriter xmlWriter = new XMLWriter(new FileWriter(new File("MusicGroup.xml")),outputFormat);
                xmlWriter.write(dom);
                xmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }catch (DocumentException e){
            e.printStackTrace();
        }
    }




    //删除分组
    public static void deleteGroup(String groupName) {
        SAXReader reader = new SAXReader();
        try {
            Document dom = reader.read(XMLUtils.class.getClassLoader().getResourceAsStream("MusicGroup.xml"));
            Element root = dom.getRootElement();
            List<Element> groupEleList = root.elements("group");
            for (Element ele : groupEleList){
                if (ele.attributeValue("name").equals(groupName)){
                    //调用父元素的删除方法
                    root.remove(ele);
                    break;
                }
            }

            OutputFormat outputFormat = OutputFormat.createCompactFormat();
            outputFormat.setEncoding("UTF-8");
            outputFormat.setExpandEmptyElements(true);//生成完整的标签

            try {
                XMLWriter xmlWriter = new XMLWriter(new FileWriter(new File("MusicGroup.xml")),outputFormat);
                xmlWriter.write(dom);
                xmlWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (DocumentException e){
            e.printStackTrace();
        }
    }

    //读取上次关闭时的播放信息：
    public static String [] readPrevPlayInfo(){
        SAXReader reader = new SAXReader();
        try {
            Document dom = reader.read(XMLUtils.class.getClassLoader().getResourceAsStream("playInfo.xml"));
            //读取根元素
            Element root = dom.getRootElement();
            if (root == null){
                return null;
            }
            //读取下面的子元素
            Element ele = root.element("currentGroup");
            if (ele == null){
                return null;
            }
            String groupName = ele.attributeValue("name");
            //读取元素下面的子元素:
            Element indexEle = ele.element("currentIndex");
            if (indexEle == null){
                return null;
            }
            String index = indexEle.getText();

            String [] strArray = new String[2];
            strArray[0] = groupName;
            strArray[1] = index;

            return strArray;
        }catch (DocumentException e){
            e.printStackTrace();
        }
        return null;
    }

    //读取某个歌单的所有歌曲:
    public static List<SoundBean> findSoundByGroupName(String groupName){
        SAXReader reader = new SAXReader();
        List<SoundBean> soundList = new ArrayList<>();
        try {
            Document dom = reader.read(XMLUtils.class.getClassLoader().getResourceAsStream("MusicGroup.xml"));
            Element root = dom.getRootElement();
            List<Element> eleList = root.elements("group");
            for (Element ele : eleList){
                if (ele.attributeValue("name").equals(groupName)){

                    //获取下面所有的子元素：
                    List<Element> soundEleList = ele.elements("sound");
                    for (Element soundEle : soundEleList){
                        SoundBean soundBean = new SoundBean();
                        soundBean.setFilePath(soundEle.elementText("filePath"));
                        soundBean.setAddDate(soundEle.elementText("addDate"));
                        System.out.println(soundBean);
                        soundList.add(soundBean);
                    }
                    return soundList;
                }
            }
        }catch (DocumentException e){
            e.printStackTrace();
        }
        return soundList;
    }
}












