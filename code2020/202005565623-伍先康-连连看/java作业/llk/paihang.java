package llk;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class paihang{
    
    public static int[] sorce1=new int[5];
    public static int[] sorce2=new int[5];
    public static int[] sorce3=new int[5];
    public static int[] sorce4=new int[5];

    public paihang(){
        JFrame jf = new JFrame("排行榜");
        jf.setSize(500, 500);
        Box vBox = Box.createVerticalBox();
        JLabel label01 = new JLabel();
        int[] sorce=new int[5];
        if(GamePanel.n==4){
            for(int i=0;i<4;i++){
                for(int j=i+1;j<5;j++){
                    if(sorce1[j]>sorce1[i]){
                        int k=sorce1[i];
                        sorce1[i]=sorce1[j];
                        sorce1[j]=k;
                    }
                }
            }
            for(int i=0;i<5;i++)
                sorce[i]=sorce1[i];
        }
        if(GamePanel.n==6){
            for(int i=0;i<4;i++){
                for(int j=i+1;j<5;j++){
                    if(sorce2[j]>sorce2[i]){
                        int k=sorce2[i];
                        sorce2[i]=sorce2[j];
                        sorce2[j]=k;
                    }
                }
            }
            for(int i=0;i<5;i++)
                sorce[i]=sorce2[i];
        }
        if(GamePanel.n==8){
            for(int i=0;i<4;i++){
                for(int j=i+1;j<5;j++){
                    if(sorce3[j]>sorce3[i]){
                        int k=sorce3[i];
                        sorce3[i]=sorce3[j];
                        sorce3[j]=k;
                    }
                }
            }
            for(int i=0;i<5;i++)
                sorce[i]=sorce3[i];
        }
        if(GamePanel.n==10){
            for(int i=0;i<4;i++){
                for(int j=i+1;j<5;j++){
                if(sorce4[j]>sorce4[i]){
                        int k=sorce4[i];
                        sorce4[i]=sorce4[j];
                        sorce4[j]=k;
                    }
                }
            }
            for(int i=0;i<5;i++)
                sorce[i]=sorce4[i];
        }
        io();
        label01.setText("第一名："+sorce[0]+"                     ");
        label01.setFont(new Font(null, Font.PLAIN, 20));  // 设置字体，null 表示使用默认字体
        JLabel label02 = new JLabel();
        label02.setText("第二名："+sorce[1]+"                     ");
        label02.setFont(new Font(null, Font.PLAIN, 20));  // 设置字体，null 表示使用默认字体
        JLabel label03 = new JLabel();
        label03.setText("第三名："+sorce[2]+"                     ");
        label03.setFont(new Font(null, Font.PLAIN, 20));  // 设置字体，null 表示使用默认字体
        JLabel label04 = new JLabel();
        label04.setText("第四名："+sorce[3]+"                     ");
        label04.setFont(new Font(null, Font.PLAIN, 20));  // 设置字体，null 表示使用默认字体
        JLabel label05 = new JLabel();
        label05.setText("第五名："+sorce[4]+"                     ");
        label05.setFont(new Font(null, Font.PLAIN, 20));  // 设置字体，null 表示使用默认字体
        vBox.add(label01);
        vBox.add(label02);
        vBox.add(label03);
        vBox.add(label04);
        vBox.add(label05);
        jf.setContentPane(vBox);
        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

    public void io(){

        File file=new File("llk\\bangdan.txt");//我们在该类的位置创建一个新文件
		FileWriter f=null;//创建文件写入对象
		BufferedWriter f1=null;//创建字符流写入对象
	
		try {
			//这里把文件写入对象和字符流写入对象分开写了
			f=new FileWriter("llk\\bangdan.txt");
			f1=new BufferedWriter(f);
            for(int i=1;i<=4;i++){
                f1.write("第"+i+"关的历史分数排名：");
                f1.newLine();
                for(int j=0;j<5;j++){
                    if(i==1){
                        f1.write(sorce1[j]+"");
				        f1.newLine();//换行操作
                    }
                    if(i==2){
                        f1.write(sorce2[j]+"");
				        f1.newLine();
                    }
                    if(i==3){
                        f1.write(sorce3[j]+"");
				        f1.newLine();
                    }
                    if(i==4){
                        f1.write(sorce4[j]+"");
				        f1.newLine();
                    }
                }
            }
		} catch (Exception e) {
			// TODO: handle exception
		}finally {//如果没有catch 异常，程序最终会执行到这里
			try {
				f1.close();
				f.close();//关闭文件
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
    }
    //将文件中的数据导入到连连看的排行榜中
    public paihang(int w){
        File file=new File("llk\\bangdan.txt");
		FileReader f=null;//文件读取对象
		BufferedReader f1=null;//字符流对象
		try {
			f=new FileReader(file);
			f1=new BufferedReader(f);
			String str=null;
            for(int i=1;i<=4;i++){
                f1.readLine();
                for(int j=0;j<5;j++){
                    if(i==1){
                        sorce1[j]=Integer.parseInt(f1.readLine());
                    }
                    if(i==2){
                        sorce2[j]=Integer.parseInt(f1.readLine());
                    }
                    if(i==3){
                        sorce3[j]=Integer.parseInt(f1.readLine());
                    }
                    if(i==4){
                        sorce4[j]=Integer.parseInt(f1.readLine());
                    }
                }
            }
				
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				f1.close();
				f.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
    }
}
