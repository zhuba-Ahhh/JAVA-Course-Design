package ClassCode;
import java.util.*;
import java.io.File;
import java.util.regex.Pattern;

//文件搜索线程,使用BFS进行搜索
public class SearchThread extends Thread
{
    private static File file;
    private static String expression;
    private static FileTableModel fileModel;

    public SearchThread(File tfile,String texpression,FileTableModel tfileModel)
    {
        file=tfile;
        expression=texpression;
        fileModel=tfileModel;
    }

    @Override
    public void run()
    {
        Queue<File> dirQueue=new LinkedList<>();
        dirQueue.offer(file);
        while(dirQueue.size()>0)
        {
            File tfile=dirQueue.poll();
            File[] files=tfile.listFiles();
            if(files==null) continue;
            for(File f:files)
            {
                if(f.isFile())
                {
                    if(Pattern.matches(expression,f.getName()))
                    {
                        setTableModel(f);
                    }
                }
                else if(f.isDirectory())
                {
                    if(Pattern.matches(expression,f.getName()))
                    {
                        setTableModel(f);
                    }
                    dirQueue.offer(f);//文件目录压栈,层数为当前访问的文件目录层数+1
                }
            }
        }
    }

    private void setTableModel(File file)
    {
        Object[] res=new Object[]
        {
            file,
            file.getPath(),
            file.isFile()?Long.valueOf(file.length()):Long.valueOf(-1),
            Long.valueOf(file.lastModified())
        };
        fileModel.addRow(res);
    }
}
