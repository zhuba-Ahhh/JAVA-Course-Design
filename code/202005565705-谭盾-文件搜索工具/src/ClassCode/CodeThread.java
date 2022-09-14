package ClassCode;
import java.io.*;
import java.nio.charset.Charset;
import info.monitorenter.cpdetector.io.*;

//�Զ�ʶ��������߳�
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

    public String getCode()//������ʶ�������
    { 
        return Code;
    }

    private String GetFileEncode(File file) throws Exception
    {
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        Charset charset = null;
        detector.add(new ParsingDetector(false));   // ParsingDetector�����ڼ��HTML��XML���ļ����ַ����ı���
        detector.add(JChardetFacade.getInstance()); // ��ɴ�����ļ��ı���ⶨ
        detector.add(ASCIIDetector.getInstance());  // ASCIIDetector����ASCII����ⶨ
        detector.add(UnicodeDetector.getInstance());// UnicodeDetector����Unicode�������Ĳⶨ
        charset = detector.detectCodepage(file.toURI().toURL());
        return charset.name();
    }
}