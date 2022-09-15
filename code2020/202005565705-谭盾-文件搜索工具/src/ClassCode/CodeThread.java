package ClassCode;
import java.io.*;
import java.nio.charset.Charset;
import info.monitorenter.cpdetector.io.*;

//自动识别内码的线程
public class CodeThread extends Thread
{
    private File file;
    private String Code;

    public CodeThread(File file)
    {
        this.file=file;
    }

    @Override
    public void run()
    {
        try
        {
            Code=GetFileEncode(file);
        } catch (Exception e)
        {
            e.printStackTrace();
        } 
    }

    public String getCode()//返回所识别的内码
    { 
        return Code;
    }

    private String GetFileEncode(File file) throws Exception
    {
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        Charset charset = null;
        detector.add(new ParsingDetector(false));   // ParsingDetector可用于检查HTML、XML等文件或字符流的编码
        detector.add(JChardetFacade.getInstance()); // 完成大多数文件的编码测定
        detector.add(ASCIIDetector.getInstance());  // ASCIIDetector用于ASCII编码测定
        detector.add(UnicodeDetector.getInstance());// UnicodeDetector用于Unicode家族编码的测定
        charset = detector.detectCodepage(file.toURI().toURL());
        return charset.name();
    }
}