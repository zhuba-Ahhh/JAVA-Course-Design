## 程序环境说明

    将文件夹doc->jpcap下的
        Jpcap.dll放入jdk的bin目录下
        jpcap.jar导入lib
        点击WinPcap_4_1_2.exe安装WinPcap
    
    编辑器使用的是vsCode + java插件
    jdk 使用1.8

    主类为App类,主要功能实现也写在App类中
    过程:
        1.保证电脑联网,单击工具栏的捕获按钮，或配置按钮，均可实现抓包。
        2.抓到的数据包将显示在表格中，
        3.单击表中的任意行，就会在下面的两个显示框内显示对该数据包的详细解析
        4.另外可以通过工具栏中的保存文件，打开文件按钮实现对捕获的文件进行保存或展示
        5.通过搜索栏中键入TCP然后搜索，可只展示捕获到的TCP包



## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).