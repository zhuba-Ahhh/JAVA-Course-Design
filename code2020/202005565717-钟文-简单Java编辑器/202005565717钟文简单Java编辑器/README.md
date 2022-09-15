# CodeEasy

#### 介绍
Java课程设计，一款简洁的Java开发工具。CodeEasy具有文本编辑器的基本功能，同时用户不需要主动配置Java环境变量，只要选择正确的JDK路径，CodeEasy会在编译运行程序时设置好临时的环境变量。借助开源中国第三方包cpdetector.jar，CodeEasy可实现文本文件的自动内码识别（只能识别流行的编码）。同时，CodeEasy可执行特定的编译，运行命令和windows部分命令，并在模拟终端显示相应内容。

#### 软件架构
* 开发工具：IntelliJ IDEA 2021.2.1
* JDK版本：jdk1.8.0_131


#### 安装教程

1. 使用IDEA将项目打包为jar包（jar包的编码为UTF-8）
2. 如有需要利用exe4j工具将jar包转化为exe文件（同样需要jre环境才能运行）
3. 无论是运行jar包还是exe文件，都需要将项目中的images文件夹与其放在同一目录下。这是由util.ImageUtils类决定的，用户可以根据修改。

#### 使用说明

1. CodeEasy在启动时会模拟按下shift键以切换语言（所以说该软件适用于默认语言为中文的主机）

2. 第一次使用CodeEasy时，会在项目目录下创建一个名为config.json文件，它是整个项目的配置文件，以下是对config.json文件中元素含义的说明：

   * font对象，包括元素（name：字体名字，style：字体类型，size：字体大小）

   * imageWidthAndHeight：软件中所有图标的统一宽高

   * jdkPath：用户主机所安装JDK的绝对路径，例如"D:\\java\\jdk1.8.0_131"

   * nowPath：用户最近打开或保存文件的父目录

   * recentFiles：用户最近打开的不超过5个文件的绝对路径

   * workPath：config.json文件所在位置的绝对路径

3. CodeEasy中可使用的快捷键：

   * Ctrl + N：新建文件

   * Ctrl + O：打开文件

   * Ctrl + S：保存文件

   * Ctrl + C：复制内容

   * Ctrl + V：粘贴内容

   * Ctrl + X：剪切内容

   * Ctrl + Z：撤销操作

   * Ctrl + A：代码提示（功能很弱）

   * Alt + C：普通编译

   * Alt + R：普通运行

4. 用户首次使用时需要选择JDK路径，说明：配置->JDK路径

   ![image-20211229232004476](img/image-20211229232004476.png)

5. 点击环境变量是否配置成功按钮判断所选路径是否正确

   ![image-20211229232136372](img/image-20211229232136372.png)

6. 工具栏上按钮都有文字提示，剩余按钮和相应功能就由户自己摸索，有过一定其他文本工具使用经验的用户可以很快上手CodeEasy的使用。
7. 特别说明：本软件只能运行终端Java程序，不能运行GUI程序。同时，不能运行Java项目，最好是运行单个的Java文件。由于作者的自身水平有限和考虑不周，软件可能存在Bug，希望用户在发现Bug时可以及时反馈并谅解。
