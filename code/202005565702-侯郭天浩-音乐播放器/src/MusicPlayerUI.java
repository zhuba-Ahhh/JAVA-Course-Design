import javax.media.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.awt.*;

public class MusicPlayerUI {
    private static MusicPlayerUI ui;
    JPanel mainPane, bottomPane, bottomCenterPane, bottomEastPane, 
            bottomWesPane, centerPane;
    LyricDisplayer lrcPane;
    JFrame mainFrame;
    JMenuBar menuBar;
    JMenuItem filMenu;
    JSplitPane centerSplitPane;
    JLabel nowTime, maxTime;
    JButton playButton, closeButton, lastButton, nextButton, muteButton;
    JSlider musicSlider, volumSlider;
    JList musicList;
    DefaultListModel musicListModel;
    Player player;
    static int listIndex = 0;
    static int volumeLevel = 0;
    public static int NULL_TARGET = -1;

    public MusicPlayerUI()
    {
        mainFrame = new JFrame("MusicPlayer");
        mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout());
        menuSet();
        centerPaneSet();
        bottomPaneSet();
        mainFrame.setContentPane(mainPane);
        mainFrame.setSize(1300, 800);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        new ButtonAction();
    }

    public void menuSet()
    {
        menuBar = new JMenuBar();
        menuBar.setLayout(new BorderLayout());
        filMenu = new JMenuItem("打开文件");

        menuBar.setPreferredSize(new Dimension(0, 50));
        filMenu.setPreferredSize(new Dimension(70, 0));

        menuBar.add(filMenu ,BorderLayout.WEST);
        mainFrame.setJMenuBar(menuBar);
    }

    public void bottomPaneSet()
    {
        // 创建底部面板
        bottomPane = new JPanel();
        bottomPane.setLayout(new BorderLayout());
        bottomPane.setPreferredSize(new Dimension(100, 100));

        // 将底部面板分为三部分
        // 创建第一部分，左侧面板包含上一首、下一首、播放与暂停按钮
        bottomWesPane = new JPanel();
        bottomWesPane.setLayout(new BorderLayout());

        // 创建第二部分，中间面板包含播放进度条、当前播放时间和总时长显示
        bottomCenterPane = new JPanel();
        bottomCenterPane.setLayout(new BorderLayout());

        // 创建第三部分，右侧面板包含终止按钮和音量调节条
        bottomEastPane = new JPanel();
        bottomEastPane.setLayout(new BorderLayout());

        // 终止播放按钮
        closeButton = new JButton("close");
        closeButton.setPreferredSize(new Dimension(100, 10));
        closeButton.setEnabled(false);

        // 静音按钮
        muteButton = new JButton("mute");
        muteButton.setPreferredSize(new Dimension(70, 0));

        // 进度条时间标记
        nowTime = new JLabel("00:00");
        nowTime.setPreferredSize(new Dimension(40, 0));
        maxTime = new JLabel("00:00");
        maxTime.setPreferredSize(new Dimension(70 ,0));
        // 创建进度条
        musicSlider = new JSlider(SwingConstants.HORIZONTAL);
        musicSlider.setValue(0);
        musicSlider.setEnabled(false);
        // 创建音量条
        volumSlider = new JSlider(SwingConstants.HORIZONTAL);        
        volumSlider.setValue(4);
        volumSlider.setMaximum(8);
        volumSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                player.getGainControl().setLevel(
                    (float)(ui.volumSlider.getValue() / 10.0));
                
            }
        });
        volumSlider.setPreferredSize(new Dimension(150, 0));

        // 播放与暂停按钮
        playButton = new JButton("play");
        playButton.setPreferredSize(new Dimension(100, 0));

        // 上一首按钮
        lastButton = new JButton("last");
        lastButton.setPreferredSize(new Dimension(70, 0));

        // 下一首按钮
        nextButton = new JButton("next");
        nextButton.setPreferredSize(new Dimension(70, 0));

        bottomWesPane.add(lastButton, BorderLayout.WEST);
        bottomWesPane.add(playButton, BorderLayout.CENTER);
        bottomWesPane.add(nextButton, BorderLayout.EAST);

        bottomCenterPane.add(musicSlider, BorderLayout.CENTER);
        bottomCenterPane.add(nowTime, BorderLayout.WEST);
        bottomCenterPane.add(maxTime, BorderLayout.EAST);

        bottomEastPane.add(volumSlider, BorderLayout.CENTER);
        bottomEastPane.add(closeButton, BorderLayout.WEST);
        bottomEastPane.add(muteButton, BorderLayout.EAST);

        bottomPane.add(bottomCenterPane, BorderLayout.CENTER);
        bottomPane.add(bottomEastPane, BorderLayout.EAST);
        bottomPane.add(bottomWesPane, BorderLayout.WEST);
        mainPane.add(bottomPane, BorderLayout.SOUTH);
    }

    public static MusicPlayerUI getUI() {
        if(ui == null)
        {
            ui = new MusicPlayerUI();
        }
        return ui;
    }
       
    public void centerPaneSet()
    {
        centerPane = new JPanel();
        musicListModel = new DefaultListModel<String>();
        musicList = new JList(musicListModel);
        musicList.setCellRenderer(new MyListCellRenderer());
        lrcPane = new LyricDisplayer();
        lrcPane.setVisible(false);

        centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                                        musicList, lrcPane);
        centerSplitPane.setDividerLocation(300);
                                    
        centerPane.setLayout(new BorderLayout());
        centerPane.add(centerSplitPane);
        mainPane.add(centerPane, BorderLayout.CENTER);
    }

    public class ButtonAction implements ActionListener{
        JFileChooser fileChooser;
        FileFilter filter;
        File openedFile;
        ButtonAction()
        {
            // 给按钮添加监听器
            filMenu.addActionListener(this);
            lastButton.addActionListener(this);
            nextButton.addActionListener(this);
            playButton.addActionListener(this);
            closeButton.addActionListener(this);
            muteButton.addActionListener(this);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == filMenu)
            {
                // 点击打开文件菜单项
                fileChooser = new JFileChooser(
                    "F:");
                filter = new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if(f.getName().toLowerCase().endsWith(".wav") 
                                        || f.isDirectory())
                        {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        return "*.wav;";
                    }
                };
                fileChooser.setFileFilter(filter);
                fileChooser.setFileSelectionMode(
                    JFileChooser.FILES_AND_DIRECTORIES);
                int key = fileChooser.showOpenDialog(null);
                if(key == JFileChooser.APPROVE_OPTION)
                {
                    openedFile = fileChooser.getSelectedFile();
                }
                
                Data.loadFile(openedFile);

            }
            else if(e.getSource() == playButton)
            {
                // 点击播放与暂停按钮
                if(player == null)
                {
                    if(musicListModel.isEmpty() != true)
                    {
                        playButton.setText("pause");
                        if(musicList.getSelectedIndex() < 0)
                        {
                            musicList.setSelectedIndex(0);
                        }
                        listIndex = musicList.getSelectedIndex();
                        try {
                            Data.initMusic((File)musicList.getSelectedValue());
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(mainPane, 
                        "没有可播放的文件，请点击菜单栏打开文件");
                    }
                }
                else if(player.getState() == player.Started)
                {
                    player.stop();
                    playButton.setText("play");
                }
                else
                {
                    player.start();
                    playButton.setText("pause");
                }
            }

            else if(e.getSource() == lastButton)
            {
                // 点击上一首按钮
                if(listIndex > 0)
                {
                    if(player != null)
                    {
                        Controller.closePlayer();
                    }
                    playButton.setText("pause");
                    listIndex--;
                    musicList.setSelectedIndex(listIndex);
                    try {
                        Data.initMusic((File)(musicList.getSelectedValue()));
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }

            else if(e.getSource() == nextButton)
            {
                // 点击下一首按钮
                if(listIndex < musicListModel.getSize() - 1)
                {
                    if(player != null)
                    {
                        Controller.closePlayer();
                    }
                    playButton.setText("pause");
                    listIndex++;
                    musicList.setSelectedIndex(listIndex);
                    try {
                        Data.initMusic((File)(musicList.getSelectedValue()));
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }

            else if(e.getSource() == closeButton)
            {
                // 点击终止按钮
                Controller.closePlayer();
                playButton.setText("play");
                lrcPane.setVisible(false);
            }

            else if(e.getSource() == muteButton)
            {
                // 点击静音按钮
                if(volumSlider.getValue() != 0)
                {
                    volumeLevel = volumSlider.getValue();
                    volumSlider.setValue(0);
                }
                else
                {
                    volumSlider.setValue(volumeLevel);
                }
            }
        }
    }
    public static void main(String[] args) throws IOException {
        getUI();
        new RunMusicThread().start();
    }
}
