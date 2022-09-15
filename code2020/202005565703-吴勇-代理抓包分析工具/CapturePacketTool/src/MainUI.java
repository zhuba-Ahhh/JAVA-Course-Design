import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class MainUI extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // 主布局管理控件
    JPanel mainPanel = new JPanel();
    Font mainFont = new Font("宋体", Font.PLAIN, 14);

    
    // 底部栏
    JPanel footBar = new JPanel();


    // 菜单栏控件
    JMenuBar mainMenuBar = new JMenuBar();
    JMenu mainMenuFile = new JMenu("文件(F)");
        JMenuItem mainMenuFileOpen = new JMenuItem("打开文件");
        JMenuItem mainMenuFileSave = new JMenuItem("保存文件");
        JMenuItem mainMenuFileQuit = new JMenuItem("退出");
    JMenu mainMenuEdit = new JMenu("编辑(E)");
    JMenu mainMenuView = new JMenu("视图(V)");
    JMenu mainMenuHop = new JMenu("跳转(G)");
    JMenu mainMenuCap = new JMenu("捕获(C)");

    // 初始化主菜单栏
    private void InitMainMenuBar() {
        mainMenuBar.setBackground(new Color(255, 255, 255, 255));
        mainMenuFile.setMnemonic('F');
        mainMenuEdit.setMnemonic('E');
        mainMenuView.setMnemonic('V');
        mainMenuHop.setMnemonic('G');
        mainMenuCap.setMnemonic('C');

        mainMenuCap.setFont(mainFont);
        mainMenuHop.setFont(mainFont);
        mainMenuView.setFont(mainFont);
        mainMenuEdit.setFont(mainFont);
        mainMenuFile.setFont(mainFont);
        mainMenuFileOpen.setFont(mainFont);
        mainMenuFileSave.setFont(mainFont);
        mainMenuFileQuit.setFont(mainFont);
        
        mainMenuBar.add(mainMenuFile);
        mainMenuBar.add(mainMenuEdit);
        mainMenuBar.add(mainMenuView);
        mainMenuBar.add(mainMenuHop);
        mainMenuBar.add(mainMenuCap);

        mainMenuFile.add(mainMenuFileOpen);
        mainMenuFile.add(mainMenuFileSave);
        mainMenuFile.addSeparator();
        mainMenuFile.add(mainMenuFileQuit);
        
        mainMenuFileOpen.setIcon(new ImageIcon("res\\daoru.png"));
        mainMenuFileSave.setIcon(new ImageIcon("res\\save.png"));
        mainMenuFileSave.setEnabled(false);
        mainMenuFileQuit.setIcon(new ImageIcon("res\\close.png"));
    }


    // 工具栏控件
    JToolBar mainToolBar = new JToolBar("工具栏", SwingConstants.HORIZONTAL);
    JButton mainToolBarStartBtn = new JButton(new ImageIcon("res\\play.png"));
    JButton mainToolBarStopBtn = new JButton(new ImageIcon("res\\stop.png"));

    JButton mainToolBarSaveFileBtn = new JButton(new ImageIcon("res\\daochu.png"));
    JButton mainToolBarOpenFileBtn = new JButton(new ImageIcon("res\\daoru.png"));
    JButton mainToolBarCloseFileBtn = new JButton(new ImageIcon("res\\basic_del.png"));

    JButton mainToolBarSettingBtn = new JButton(new ImageIcon("res\\setting-filling.png"));
    JButton mainToolBarPreviousItemBtn = new JButton(new ImageIcon("res\\arrow-up-circle.png"));
    JButton mainToolBarNextItemBtn = new JButton(new ImageIcon("res\\direction-down-circle.png"));
    JButton mainToolBarFirstItemBtn = new JButton(new ImageIcon("res\\top.png"));
    JButton mainToolBarLastItemBtn = new JButton(new ImageIcon("res\\bottom.png"));

    JButton mainToolBarShrinkFontBtn = new JButton(new ImageIcon("res\\icon_fd.png"));
    JButton mainToolBarEnlargeFontBtn = new JButton(new ImageIcon("res\\icon_sx.png"));
    // 初始化工具栏
    private void InitMainToolBar() {
        mainToolBarStartBtn.setBorderPainted(false);
        mainToolBarStartBtn.setMargin(new Insets(0, 0, 0, 0));
        mainToolBarStartBtn.setToolTipText("开始捕获");

        mainToolBarStopBtn.setBorderPainted(false);
        mainToolBarStopBtn.setMargin(new Insets(0, 0, 0, 0));
        mainToolBarStopBtn.setToolTipText("停止捕获");
        mainToolBarStopBtn.setEnabled(false);

        mainToolBarSettingBtn.setBorderPainted(false);
        mainToolBarSettingBtn.setMargin(new Insets(0, 0, 0, 0));
        mainToolBarSettingBtn.setToolTipText("配置捕获接口");

        mainToolBarFirstItemBtn.setBorderPainted(false);
        mainToolBarFirstItemBtn.setMargin(new Insets(0, 0, 0, 0));
        mainToolBarFirstItemBtn.setToolTipText("转到首个分组");
        mainToolBarFirstItemBtn.setEnabled(false);

        mainToolBarLastItemBtn.setBorderPainted(false);
        mainToolBarLastItemBtn.setMargin(new Insets(0, 0, 0, 0));
        mainToolBarLastItemBtn.setToolTipText("转到最新分组");
        mainToolBarLastItemBtn.setEnabled(false);

        mainToolBarNextItemBtn.setBorderPainted(false);
        mainToolBarNextItemBtn.setMargin(new Insets(0, 0, 0, 0));
        mainToolBarNextItemBtn.setToolTipText("转到下一个分组");
        mainToolBarNextItemBtn.setEnabled(false);

        mainToolBarPreviousItemBtn.setBorderPainted(false);
        mainToolBarPreviousItemBtn.setMargin(new Insets(0, 0, 0, 0));
        mainToolBarPreviousItemBtn.setToolTipText("转到上一个分组");
        mainToolBarPreviousItemBtn.setEnabled(false);

        mainToolBarOpenFileBtn.setBorderPainted(false);
        mainToolBarOpenFileBtn.setMargin(new Insets(0, 0, 0, 0));
        mainToolBarOpenFileBtn.setToolTipText("打开已经捕获的文件");
        mainToolBarOpenFileBtn.setEnabled(false);

        mainToolBarSaveFileBtn.setBorderPainted(false);
        mainToolBarSaveFileBtn.setMargin(new Insets(0, 0, 0, 0));
        mainToolBarSaveFileBtn.setToolTipText("保存已经捕获的文件");
        mainToolBarSaveFileBtn.setEnabled(false);

        mainToolBarCloseFileBtn.setBorderPainted(false);
        mainToolBarCloseFileBtn.setMargin(new Insets(0, 0, 0, 0));
        mainToolBarCloseFileBtn.setToolTipText("关闭已经捕获的文件");
        mainToolBarCloseFileBtn.setEnabled(false);

        mainToolBarShrinkFontBtn.setBorderPainted(false);
        mainToolBarShrinkFontBtn.setMargin(new Insets(0, 0, 0, 0));
        mainToolBarShrinkFontBtn.setToolTipText("缩小字体");
        mainToolBarShrinkFontBtn.setEnabled(false);

        mainToolBarEnlargeFontBtn.setBorderPainted(false);
        mainToolBarEnlargeFontBtn.setMargin(new Insets(0, 0, 0, 0));
        mainToolBarEnlargeFontBtn.setToolTipText("放大字体");
        mainToolBarEnlargeFontBtn.setEnabled(false);

        mainToolBar.add(mainToolBarStartBtn);
        // mainToolBar.addSeparator();
        mainToolBar.add(mainToolBarStopBtn);
        // mainToolBar.addSeparator();
        mainToolBar.add(mainToolBarSettingBtn);
        mainToolBar.addSeparator(new Dimension(40, 30));
        mainToolBar.add(mainToolBarOpenFileBtn);
        mainToolBar.add(mainToolBarCloseFileBtn);
        mainToolBar.add(mainToolBarSaveFileBtn);
        mainToolBar.addSeparator(new Dimension(70, 30));

        mainToolBar.add(mainToolBarPreviousItemBtn);
        mainToolBar.add(mainToolBarNextItemBtn);
        mainToolBar.add(mainToolBarFirstItemBtn);
        mainToolBar.add(mainToolBarLastItemBtn);
        mainToolBar.add(mainToolBarShrinkFontBtn);
        mainToolBar.add(mainToolBarEnlargeFontBtn);

        mainToolBar.setBackground(new Color(240, 240, 240, 255));
        mainToolBar.setFloatable(false);
    }

    // 搜索栏控件
    JPanel searchBar = new JPanel();
    BoxLayout searchBarBox = new BoxLayout(searchBar, BoxLayout.X_AXIS);
    JTextField searchBarInputArea = new JTextField();
    JButton searchBarExeBtn = new JButton(new ImageIcon("res\\search.png"));
    JButton searchBarClearBtn = new JButton(new ImageIcon("res\\close.png"));
    // 初始化搜索栏
    private void InitSearchBar() {
        searchBar.setLayout(searchBarBox);
        // searchBarInputArea.setMargin(new Insets(0, 0, 0, 0));
        searchBarInputArea.setBorder(BorderFactory.createEtchedBorder());  //设置蚀刻框
        searchBarInputArea.setFont(mainFont);
        searchBarExeBtn.setBorderPainted(false);
        searchBarExeBtn.setMargin(new Insets(0, 5, 0, 5));
        searchBarExeBtn.setToolTipText("搜索");
        searchBarExeBtn.setBackground(Color.WHITE);
        // searchBarExeBtn.setContentAreaFilled(false);
        searchBarExeBtn.setFocusPainted(false);
        searchBarClearBtn.setBorderPainted(false);
        searchBarClearBtn.setMargin(new Insets(0, 5, 0, 5));
        searchBarClearBtn.setToolTipText("清空");
        searchBarClearBtn.setBackground(Color.WHITE);
        // searchBarClearBtn.setContentAreaFilled(false);
        searchBarClearBtn.setFocusPainted(false);

        searchBar.add(searchBarInputArea);
        searchBar.add(searchBarExeBtn);
        searchBar.add(searchBarClearBtn);
    }


    // 数据显示区控件
    JSplitPane displayAreaSplitUp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JSplitPane displayAreaSplitDown = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    // 初始化数据显示区
    private void InitDisplayArea() {
        // displayArea.add(displayAreaSplitUp);
        displayAreaSplitUp.setRightComponent(displayAreaSplitDown);
        displayAreaSplitUp.setLeftComponent(displayArea1);
        displayAreaSplitDown.setLeftComponent(displayArea2);
        displayAreaSplitDown.setRightComponent(displayArea3);
        // displayAreaSplitUp.setResizeWeight(1);
        displayAreaSplitUp.setBackground(Color.WHITE);
        displayAreaSplitUp.setDividerSize(3);
        displayAreaSplitDown.setDividerSize(3);
        displayAreaSplitUp.setDividerLocation(250);
        displayAreaSplitDown.setDividerLocation(250);

        InitDisplayArea1();
        InitDisplayArea2();
        InitDisplayArea3();
    }
    

    // 主界面表格控件
    JScrollPane displayArea1 = new JScrollPane();
    JScrollBar displayArea1Bar = displayArea1.getVerticalScrollBar();
    JScrollPane displayArea2 = new JScrollPane();
    JScrollPane displayArea3 = new JScrollPane();
    String[] displayAreaTableColHeader = { "序号", "时间", "源地址", "目标地址", "协议类型", "长度", "简述" };
    String[][] displayAreaTableRow;
    DefaultTableModel displayArea1TableModel;
    MTable displayArea1Table;
    // 主界面表格配置
    private void InitDisplayArea1(){
        displayArea1TableModel = new DefaultTableModel(displayAreaTableRow, displayAreaTableColHeader);
        displayArea1Table = new MTable(displayArea1TableModel);
        displayArea1Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 设置为单选
        displayArea1Table.setRowHeight(30);
        displayArea1Table.setShowGrid(false);
        displayArea1Table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        displayArea1Table.getColumnModel().getColumn(0).setPreferredWidth(50);
        displayArea1Table.getColumnModel().getColumn(1).setPreferredWidth(50);
        displayArea1Table.getColumnModel().getColumn(2).setPreferredWidth(200);
        displayArea1Table.getColumnModel().getColumn(3).setPreferredWidth(200);
        displayArea1Table.getColumnModel().getColumn(4).setPreferredWidth(50);
        displayArea1Table.getColumnModel().getColumn(5).setPreferredWidth(50);
        displayArea1Table.getColumnModel().getColumn(6).setPreferredWidth(300);
        displayArea1Table.setFont(mainFont);
        displayArea1.setViewportView(displayArea1Table);
    }

    // 具体解析区域控件
    DefaultMutableTreeNode displayArea2TreeRoot = new DefaultMutableTreeNode();
    DefaultTreeModel displayArea2TreeModel = new DefaultTreeModel(displayArea2TreeRoot);
    JTree displayArea2Tree = new JTree(displayArea2TreeModel);
    // 数据包的具体解析显示区域
    private void InitDisplayArea2(){
        displayArea2Tree.setRootVisible(false);
        displayArea2Tree.setRowHeight(25);
        displayArea2Tree.setFont(mainFont);
        displayArea2Tree.expandPath(new TreePath(displayArea2TreeRoot.getPath()));
        DefaultTreeCellRenderer treeCellrCellRenderer = (DefaultTreeCellRenderer)displayArea2Tree.getCellRenderer();
        treeCellrCellRenderer.setLeafIcon(null);
        treeCellrCellRenderer.setClosedIcon(new ImageIcon("res\\add-circle.png"));
        treeCellrCellRenderer.setOpenIcon(new ImageIcon("res\\reduce.png"));
        displayArea2.setViewportView(displayArea2Tree);
    }

    // 包内容二进制格式显示区域控件
    JTextArea displayArea3Text = new JTextArea();
    // 数据包的二进制显示
    private void InitDisplayArea3(){
        displayArea3Text.setFont(mainFont);
        displayArea3Text.setEditable(false);
        displayArea3.setViewportView(displayArea3Text);
    }

    public MainUI() {
        setTitle("Capture Packet Tool");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPanel.setLayout(new MainLayout());
        mainPanel.add(mainToolBar);
        mainPanel.add(searchBar);
        mainPanel.add(displayAreaSplitUp);
        mainPanel.add(footBar);
        setContentPane(mainPanel);
        setSize(1000, 800);
        setJMenuBar(mainMenuBar);
        InitMainMenuBar();
        InitMainToolBar();
        InitSearchBar();
        InitDisplayArea();
        setMinimumSize(new Dimension(800, 600));
        // searchBar.setBackground(new Color(12, 124, 123));
        displayArea1.setBackground(new Color(23, 34, 45));
        displayArea2.setBackground(new Color(78, 33, 8));
        footBar.setBackground(new Color(90, 78, 23));
    }
}