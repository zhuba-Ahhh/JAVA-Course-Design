package ClassCode;
import java.io.*;
import java.util.*;

public class Settings
{
    private static File file;//�����ļ�
    private static Properties properties;//���ö�ȡ
    private static String searchPath;//���������ť����ı�
    private static String useFilter;//ʹ�õ�ɸѡ��
    private static boolean isCaps;//���ִ�Сд
    private static boolean isMatch;//ȫ��ƥ��
    private static boolean isOnlyDir;//�Ƿ�ֻ��ʾĿ¼
    private static boolean isRegEx;//ʹ��������ʽ
    private static boolean isFilter;//ʹ��ɸѡ��
    private static HashMap<String,String> Filter;//ɸѡ���洢

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

    public String RegExCreat(String word)//��ʹ��ɸѡ��
    {
        if(isRegEx) return word;
        String expression;
        if(isCaps&&isMatch) expression="^"+word+"(\\..*)?$";//���ִ�Сд��ȫ��ƥ��
        else if(isCaps&&!isMatch) expression=".*"+word+".*";//���ִ�Сд�Ҳ�ȫ��ƥ��
        else if(!isCaps&&isMatch) expression="^(?i)"+word+"(\\..*)?$";//�����ִ�Сд��ȫ��ƥ��
        else if(!isCaps&&!isMatch) expression=".*(?i)"+word+".*";//�����ִ�Сд�Ҳ�ȫ��ƥ��
        else expression=".*(?i)"+word+".*";
        System.out.println(expression);
        return expression;
    }

    public String RegExCreat(String FilterName,String word)//ʹ��ɸѡ��
    {
        String expression;
        if(isCaps&&isMatch) expression=word+"\\.";//���ִ�Сд��ȫ��ƥ��
        else if(isCaps&&!isMatch) expression=".*"+word+".*\\.";//���ִ�Сд�Ҳ�ȫ��ƥ��
        else if(!isCaps&&isMatch) expression="(?i)"+word+"\\.";//�����ִ�Сд��ȫ��ƥ��
        else if(!isCaps&&!isMatch) expression=".*(?i)"+word+".*\\.";//�����ִ�Сд�Ҳ�ȫ��ƥ��
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

    private static void updateSet()//����Filter.properties�ļ�
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

    public void addFilter(String tName,String tFilter)//��Ӻ��滻ɸѡ��
    {
        Filter.put(tName,tFilter);
        properties.setProperty(tName,tFilter);
        updateSet();
    }
    public void delFilter(String tName)//ɾ��ɸѡ��
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