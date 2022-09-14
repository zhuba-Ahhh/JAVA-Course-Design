package ClassCode;
import java.io.*;
import java.util.*;

public class Settings
{
    private static File file;//设置文件
    private static Properties properties;//设置读取
    private static String searchPath;//点击搜索按钮后的文本
    private static String useFilter;//使用的筛选器
    private static boolean isCaps;//区分大小写
    private static boolean isMatch;//全字匹配
    private static boolean isOnlyDir;//是否只显示目录
    private static boolean isRegEx;//使用正则表达式
    private static boolean isFilter;//使用筛选器
    private static HashMap<String,String> Filter;//筛选器存储

    private final String MyBlog="https://dkpluto.com/";

    public Settings(File tfile)
    {
        file=tfile;
        searchPath="C:\\";
        isCaps=false;
        isMatch=false;
        isOnlyDir=false;
        isRegEx=false;
        isFilter=false;
        Filter=new HashMap<>();
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
            properties=new Properties();
            properties.load(bufferedReader);
            Set<Object> keyValue=properties.keySet();
            for(Object object:keyValue)
            {
                Filter.put((String)object,properties.getProperty((String)object));
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public String RegExCreat(String word)//不使用筛选器
    {
        if(isRegEx) return word;
        String expression;
        if(isCaps&&isMatch) expression="^"+word+"(\\..*)?$";//区分大小写且全字匹配
        else if(isCaps&&!isMatch) expression=".*"+word+".*";//区分大小写且不全字匹配
        else if(!isCaps&&isMatch) expression="^(?i)"+word+"(\\..*)?$";//不区分大小写且全字匹配
        else if(!isCaps&&!isMatch) expression=".*(?i)"+word+".*";//不区分大小写且不全字匹配
        else expression=".*(?i)"+word+".*";
        System.out.println(expression);
        return expression;
    }

    public String RegExCreat(String FilterName,String word)//使用筛选器
    {
        String expression;
        if(isCaps&&isMatch) expression=word+"\\.";//区分大小写且全字匹配
        else if(isCaps&&!isMatch) expression=".*"+word+".*\\.";//区分大小写且不全字匹配
        else if(!isCaps&&isMatch) expression="(?i)"+word+"\\.";//不区分大小写且全字匹配
        else if(!isCaps&&!isMatch) expression=".*(?i)"+word+".*\\.";//不区分大小写且不全字匹配
        else expression=".*(?i)"+word+".*\\.";
        expression+="(";
        String[] tFilter=Filter.get(FilterName).split(",");
        if(tFilter==null||tFilter.length==0) return RegExCreat(word);
        for(int i=0;i<tFilter.length;++i)
        {
            expression+=tFilter[i];
            if(i+1!=tFilter.length)
                expression+='|';
        }
        expression+=")$";
        System.out.println(FilterName+" "+expression);
        return expression;
    }

    private static void updateSet()//更新Filter.properties文件
    {
        try
        {
            FileOutputStream out=new FileOutputStream(file);
            properties.store(out,null);
            out.flush();
            out.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void setsearchPath(String Path)
    {
        searchPath=Path;
    }
    public String getsearchPath()
    {
        return searchPath;
    }

    public void setuseFilter(String use)
    {
        useFilter=use;
    }
    public String getuseFilter()
    {
        return useFilter;
    }

    public void setisCaps(boolean is)
    {
        isCaps=is;
    }
    public boolean getisCaps()
    {
        return isCaps;
    }

    public void setisMatch(boolean is)
    {
        isMatch=is;
    }
    public boolean getisMatch()
    {
        return isMatch;
    }

    public void setisOnlyDir(boolean is)
    {
        isOnlyDir=is;
    }
    public boolean getisOnlyDir()
    {
        return isOnlyDir;
    }

    public void setisRegEx(boolean is)
    {
        isRegEx=is;
    }
    public boolean getisRegEx()
    {
        return isRegEx;
    }

    public void setisFilter(boolean is)
    {
        isFilter=is;
    }
    public boolean getisFilter()
    {
        return isFilter;
    }

    public void addFilter(String tName,String tFilter)//添加和替换筛选器
    {
        Filter.put(tName,tFilter);
        properties.setProperty(tName,tFilter);
        updateSet();
    }
    public void delFilter(String tName)//删除筛选器
    {
        Filter.remove(tName);
        properties.remove(tName);
        updateSet();
    }
    public String getFilterValue(String tName)
    {
        return Filter.get(tName);
    }
    public Set<String> getFilterName()
    {
        return Filter.keySet();
    }
    public HashMap<String,String> getFilter()
    {
        return Filter;
    }

    public String getWeb()
    {
        return MyBlog;
    }
}