package ClassCode;

import java.io.File;
import javax.swing.RowFilter;
import java.util.regex.Pattern;

//筛选器重写
public class FileTableFilter<M,I> extends RowFilter<M,I>
{
    private String expression;
    private boolean isOnlyDir;

    public FileTableFilter(String searchName,Settings settings)//根据搜索名称和设置产生正则表达式
    {
        if(!settings.getisFilter())
            expression=settings.RegExCreat(searchName);
        else
            expression=settings.RegExCreat(settings.getuseFilter(),searchName);
        isOnlyDir=settings.getisOnlyDir();
    }

    @Override
    public boolean include(Entry<? extends M, ? extends I> entry)
    {
        File file=(File)entry.getValue(0);
        if(isOnlyDir)//仅显示文件夹
        {
            if(file.isDirectory()&&Pattern.matches(expression,file.getName()))
                return true;
            return false;
        }
        else
        {
            if(Pattern.matches(expression,file.getName()))
                return true;
            return false;
        }
    }
}
