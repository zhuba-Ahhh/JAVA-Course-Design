import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import jpcap.JpcapCaptor;
import jpcap.JpcapWriter;
import jpcap.NetworkInterface;
import jpcap.packet.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class App {

    static MainUI ui = null;

    int indexOfNetworkInterface;
    NetworkInterface[] networkInterfacesList = JpcapCaptor.getDeviceList();
    static int fontSize = 14;   //可变字体大小
    final static int packetsListMaxLen = 4096;  //最大捕获包数
    static int curPacketsCnt = 0;   //当前数组中包的数量
    static long FirstSec = 0;   //首个时间戳
    static Packet[] packetsList = new Packet[packetsListMaxLen];    //Packet存储数组
    static String[] packetsTypeList = new String[packetsListMaxLen];    //Packet对应类型
    StringBuffer sBuffer = new StringBuffer(256);   
    JpcapCaptor jpcapCaptor = null; //对应接口适配器
    static boolean capThreadIsStopped = false;  //抓包线程暂停
    static CapThread capThreads = null;
    static SavePacketThread savePacketThread = null;

    public App() {
        ui = new MainUI();
        ui.displayArea1Table.SetPacketsTypeList(packetsTypeList);
        MainUIMenuBarBtnAddEvent(); //主菜单栏添加事件
        MainUIToolBarBtnAddAction(); //工具栏添加事件
        MainUISearchBarAddEvent();  //搜索栏添加事件
        MainUIDisplayArea1TableAddEvent();  //表格展示区的添加事件
        ui.setVisible(true);
    }

    private void MainUIMenuBarBtnAddEvent() {
        // 打开文件按钮监听事件
        ui.mainMenuFileOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(curPacketsCnt != 0) {
                    RestartCap();
                }
                JFileChooser fileChooser = new JFileChooser();
                int state = fileChooser.showOpenDialog(ui);
                if(state == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // System.out.println(selectedFile.getPath());
                    try {
                        JpcapCaptor captor = JpcapCaptor.openFile(selectedFile.getAbsolutePath());
                        Packet packet;
                        String[] tmpRow;
                        for(int i = 0; i < packetsListMaxLen; i++)
                        {
                            packet = captor.getPacket();
                            if(packet == null || packet.datalink == null)
                                break;
                            packetsList[curPacketsCnt++] = packet;
                        }
                        for(int i = 0; i < curPacketsCnt; i++)
                        {
                            tmpRow = PacketParseData(packetsList[i], i, i);
                            ui.displayArea1TableModel.addRow(tmpRow);
                        }
                        if(ui.displayArea1Table.getRowCount() != 0) {
                            SetComponentEnable(true);
                        }
                        else {
                            SetComponentEnable(false);
                        }
                    } catch (IOException e1) {
                        MyJDialog md = new MyJDialog(ui, "无法打开文件" + selectedFile.getName(), "警告");
                        md.okBtn.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                md.dispose();
                            }
                            
                        });
                        md.exitBtn.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                md.dispose();
                            }
                            
                        });
                    }
                    
                }
                // JpcapCaptor captor = JpcapCaptor.openFile(filename)
            }
            
        });
        // 保存文件监听事件
        ui.mainMenuFileSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int state = fileChooser.showSaveDialog(ui);
                if(state == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    savePacketThread = new SavePacketThread(jpcapCaptor, file.getAbsolutePath(),
                            packetsList, curPacketsCnt);
                    savePacketThread.start();
                }
            }
            
        });
        // 退出
        ui.mainMenuFileQuit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ui.dispose();
            }
            
        });
    }
    private void MainUIToolBarBtnAddAction() {
        // 开始捕获按钮监听事件
        ui.mainToolBarStartBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                ui.mainToolBarStopBtn.setEnabled(true);
                ui.mainToolBarStartBtn.setEnabled(false);
                ui.mainToolBarSettingBtn.setEnabled(false);

                SetComponentEnable(false);
                capThreadIsStopped = false;

                // if (networkInterfacesList == null) {
                // networkInterfacesList = JpcapCaptor.getDeviceList();
                if(curPacketsCnt != 0)
                    RestartCap();
                capThreads = new CapThread();
                capThreads.start();
                // }
            }

        });

        // 停止捕获按钮事件
        ui.mainToolBarStopBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                capThreadIsStopped = true;
                ui.mainToolBarStartBtn.setEnabled(true);
                ui.mainToolBarSettingBtn.setEnabled(true);
                ui.mainToolBarStopBtn.setEnabled(false);
                if(curPacketsCnt != 0){
                    SetComponentEnable(true);
                }

            }
        });

        // 网络接口配置按钮事件
        ui.mainToolBarSettingBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SelectNetworkInterfaceDialog childWin = new SelectNetworkInterfaceDialog(ui, networkInterfacesList);
                boolean state = childWin.GetCapState();
                if(state) {
                    ui.mainToolBarStopBtn.setEnabled(true);
                    ui.mainToolBarStartBtn.setEnabled(false);
                    SetComponentEnable(false);
                    capThreadIsStopped = false;
                    if(curPacketsCnt != 0)
                        RestartCap();
                    capThreads = new CapThread(childWin.GetfilterString(), childWin.GetisMixMode(), childWin.Getsnaplens(), childWin.GetselectedInterfacesIndex(), childWin.GetmaxCapPacketsCnt());
                    capThreads.start();
                }
            }

        });

        // 打开文件按钮事件
        ui.mainToolBarOpenFileBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ui.mainMenuFileOpen.doClick();
            }
            
        });

        // 关闭当前文件按钮事件
        ui.mainToolBarCloseFileBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int cnt = ui.displayArea1Table.getRowCount();
                for(int i = 0; i < cnt; i++)
                    ui.displayArea1TableModel.removeRow(0);
                
                curPacketsCnt = 0;
                
                if (ui.displayArea2TreeRoot.getChildCount() != 0) {
                    DefaultMutableTreeNode tmp1 = (DefaultMutableTreeNode) ui.displayArea2TreeRoot.getFirstChild();
                    DefaultMutableTreeNode tmp2;
                    while (tmp1 != null) {
                        tmp2 = tmp1.getNextSibling();
                        ui.displayArea2TreeModel.removeNodeFromParent(tmp1);
                        tmp1 = tmp2;
                    }
                }

                ui.displayArea3Text.setText("");

                SetComponentEnable(false);
            }
            
        });

        // 捕获文件保存事件
        ui.mainToolBarSaveFileBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ui.mainMenuFileSave.doClick();
            }
            
        });

        // 选中表格中第一项
        ui.mainToolBarFirstItemBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(ui.displayArea1Table.getRowCount() != 0) {
                    ui.displayArea1Bar.setValue(0);
                    ui.displayArea1Table.setRowSelectionInterval(0, 0);
                }
            }

        });

        // 选中表格中最后一项
        ui.mainToolBarLastItemBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int cnt = ui.displayArea1Table.getRowCount();
                if(cnt != 0) {
                    int maxValue = ui.displayArea1Bar.getMaximum();
                    int winSize = ui.displayArea1Bar.getVisibleAmount();
                    ui.displayArea1Bar.setValue(maxValue - winSize);
                    ui.displayArea1Table.setRowSelectionInterval(cnt - 1, cnt - 1);
                }
            }
            
        });

        // 选中表格中当前选中的上一项
        ui.mainToolBarPreviousItemBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int cnt = ui.displayArea1Table.getRowCount();
                if(cnt != 0) {
                    int curSelectedRow = ui.displayArea1Table.getSelectedRow();
                    if(curSelectedRow != -1 && curSelectedRow > 0) {
                        ui.displayArea1Table.setRowSelectionInterval(curSelectedRow - 1, curSelectedRow - 1);
                    }
                }
            }
            
        });

        // 选中表格中当前选中的下一项
        ui.mainToolBarNextItemBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int cnt = ui.displayArea1Table.getRowCount();
                if(cnt != 0) {
                    int curSelectedRow = ui.displayArea1Table.getSelectedRow();
                    if(curSelectedRow != -1 && curSelectedRow < cnt - 1) {
                        ui.displayArea1Table.setRowSelectionInterval(curSelectedRow + 1, curSelectedRow + 1);
                    }
                }
            }
            
        });

        // 缩放字体
        ui.mainToolBarShrinkFontBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(fontSize >= 12){
                    fontSize--;
                    ui.displayArea1Table.setFont(new Font("宋体", Font.PLAIN, fontSize));
                    ui.displayArea2Tree.setFont(new Font("宋体", Font.PLAIN, fontSize));
                    ui.displayArea3Text.setFont(new Font("宋体", Font.PLAIN, fontSize));
                }
            }
            
        });

        // 放大字体
        ui.mainToolBarEnlargeFontBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(fontSize <= 17){
                    fontSize++;
                    ui.displayArea1Table.setFont(new Font("宋体", Font.PLAIN, fontSize));
                    ui.displayArea2Tree.setFont(new Font("宋体", Font.PLAIN, fontSize));
                    ui.displayArea3Text.setFont(new Font("宋体", Font.PLAIN, fontSize));
                }
            }
            
        });
    }

    // 添加搜索栏按钮事件
    private void MainUISearchBarAddEvent() {
        // 执行搜索事件
        ui.searchBarExeBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String textContent = ui.searchBarInputArea.getText();

                int cnt = ui.displayArea1Table.getRowCount();
                for(int i = 0; i < cnt; i++)
                    ui.displayArea1TableModel.removeRow(0);
                String []tmp;
                if(textContent.equals("")) {
                    int index = 0;
                    for(int i = 0; i < curPacketsCnt; i++)
                    {
                        tmp = PacketParseData(packetsList[i], index, index);
                        index++;
                        ui.displayArea1TableModel.addRow(tmp);
                    }
                }
                else {
                    int index = 0;
                    for(int i = 0; i < curPacketsCnt; i++)
                    {
                        tmp = PacketParseData(packetsList[i], index, i);
                        if(Pattern.matches(textContent, tmp[4])) {
                            ui.displayArea1TableModel.addRow(tmp);
                            index++;
                        }
                        // else if()
                        // if(packetsList[i] instanceof TCPPacket) {
                        //     TCPPacket tmpPacket = (TCPPacket)(packetsList[i]);
                        //     String src_port = Integer.toString(tmpPacket.src_port);
                        //     if(textContent.equals(src_port)) {
                        //         ui.displayArea1TableModel.addRow(tmp);
                        //     }
                        // }
                        // else if(packetsList[i] instanceof UDPPacket) {
                        //     UDPPacket tmpPacket = (UDPPacket)packetsList[i];
                        //     String src_port = Integer.toString(tmpPacket.src_port);
                        //     if(textContent.equals(src_port)) {
                        //         ui.displayArea1TableModel.addRow(tmp);
                        //     }
                        // }
                    }
                }
            }
            
        });
        // 清空搜索栏事件
        ui.searchBarClearBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ui.searchBarInputArea.setText("");
            }

        });
    }

    // 添加表格事件
    private void MainUIDisplayArea1TableAddEvent() {
        ui.displayArea1Table.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // System.out.println(displayArea1Table.getSelectedRow());
                if (ui.displayArea1Table.getSelectedRowCount() != 0) {
                    int packetRowIndex = ui.displayArea1Table.getSelectedRow();
                    String tmp = (String) ui.displayArea1TableModel.getValueAt(packetRowIndex, 0);
                    int packetIndex = Integer.parseInt(tmp);
                    PacketParseDetailData(packetsList[packetIndex], ui.displayArea2TreeRoot, packetIndex);
                    ui.displayArea2Tree.expandPath(new TreePath(ui.displayArea2TreeRoot.getPath()));
                    PacketParseBinary(packetsList[packetIndex], ui.displayArea3Text);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // System.out.println("按下");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // System.out.println("抬起");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // System.out.println("Entered");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // System.out.println("退出");
            }

        });
    }

    public static void main(String[] args) throws Exception {
        new App();
    }

    // private void CapExecution(boolean isSetting, int selectedInterfaceIndex, int snaplen, boolean isMixModes, String filterString, int maxCapPacketsCnt) {
    //     CapThread capThread = null;
    //     if(isSetting) {
    //         capThread = new CapThread(filterString, isMixModes, snaplen, selectedInterfaceIndex, maxCapPacketsCnt);
    //     }
    //     else {
    //         capThread = new CapThread();
    //     }
    //     capThread.start();
    // }

    // 将数据包转换成表格数据
    private String[] PacketParseData(Packet packet, int index, int realIndex) {
        String[] tmpRow = new String[7];
        tmpRow[0] = realIndex + "";
        if (index == 0)
            FirstSec = packet.sec;
        tmpRow[1] = (packet.sec - FirstSec) + "." + packet.usec;
        tmpRow[5] = packet.caplen + "";
        if (packet instanceof ARPPacket) {
            packetsTypeList[index] = "ARP";
            ARPPacket tmpPacket = (ARPPacket) packet;
            String senderHardAddress = (String) tmpPacket.getSenderHardwareAddress();
            String targetHardAddress = (String) tmpPacket.getTargetHardwareAddress();
            String senderProtocolAddress = ((Inet4Address) tmpPacket.getSenderProtocolAddress()).toString();
            String targetProtocolAddress = ((Inet4Address) tmpPacket.getTargetProtocolAddress()).toString();
            tmpRow[2] = senderHardAddress;
            tmpRow[3] = targetHardAddress;
            tmpRow[4] = packetsTypeList[index];
            if (tmpPacket.operation == ARPPacket.ARP_REPLY) {
                tmpRow[6] = "应答: " + senderProtocolAddress + "的MAC地址是 " + senderHardAddress;
            } else if (tmpPacket.operation == ARPPacket.ARP_REQUEST) {
                // String sourcesAddress =
                // ((EthernetPacket)(tmpPacket.datalink)).getSourceAddress();
                if (senderProtocolAddress.equals(targetProtocolAddress))
                    tmpRow[6] = "免费的ARP请求: 我的IP地址是" + targetProtocolAddress;
                else
                    tmpRow[6] = "请求: 谁有IP" + targetProtocolAddress + " 请告知IP" + senderProtocolAddress;
            } else {
                tmpRow[6] = "";
            }
        } else if (packet instanceof IPPacket) {
            IPPacket tmpPacket = (IPPacket) packet;
            tmpRow[2] = tmpPacket.src_ip.toString();
            tmpRow[3] = tmpPacket.dst_ip.toString();
            switch (tmpPacket.protocol) {
                case IPPacket.IPPROTO_ICMP:
                    packetsTypeList[index] = "ICMP";
                    tmpRow[4] = "ICMP";
                    switch (((ICMPPacket) tmpPacket).type) {
                        case ICMPPacket.ICMP_UNREACH:
                            tmpRow[6] = "差错报告报文: 终点不可达";
                            break;
                        case ICMPPacket.ICMP_TIMXCEED:
                            tmpRow[6] = "差错报告报文: 时间超过";
                            break;
                        case ICMPPacket.ICMP_PARAMPROB:
                            tmpRow[6] = "IP首部出错";
                            break;
                        case ICMPPacket.ICMP_REDIRECT:
                            tmpRow[6] = "重定向";
                            break;
                        case ICMPPacket.ICMP_ECHO:
                            tmpRow[6] = "请求回显";
                            break;
                        case ICMPPacket.ICMP_ECHOREPLY:
                            tmpRow[6] = "回显应答";
                            break;
                        case ICMPPacket.ICMP_TSTAMP:
                            tmpRow[6] = "时间戳请求";
                            break;
                        case ICMPPacket.ICMP_TSTAMPREPLY:
                            tmpRow[6] = "时间戳回答";
                            break;
                        default:
                            tmpRow[6] = "";
                            break;
                    }
                    break;
                case IPPacket.IPPROTO_TCP:
                    TCPPacket tmp1Packet = (TCPPacket) tmpPacket;
                    packetsTypeList[index] = "TCP";
                    tmpRow[4] = "TCP";
                    tmpRow[6] = "由端口" + tmp1Packet.src_port + "发往端口" + tmp1Packet.dst_port + " ";
                    if (tmp1Packet.ack)
                        tmpRow[6] = tmpRow[6] + "[ACK]";
                    if (tmp1Packet.syn)
                        tmpRow[6] = tmpRow[6] + "[SYN]";
                    if (tmp1Packet.fin)
                        tmpRow[6] = tmpRow[6] + "[FIN]";
                    if (tmp1Packet.psh)
                        tmpRow[6] = tmpRow[6] + "[PSH]";
                    if (tmp1Packet.urg)
                        tmpRow[6] = tmpRow[6] + "[URG]";
                    if (tmp1Packet.rst)
                        tmpRow[6] = tmpRow[6] + "[RST]";
                    tmpRow[6] = tmpRow[6] + ",Seq = " + tmp1Packet.sequence + ",Ack = " + tmp1Packet.ack_num + ",Win = "
                            + tmp1Packet.window;
                    break;
                case IPPacket.IPPROTO_UDP:
                    packetsTypeList[index] = "UDP";
                    tmpRow[4] = "UDP";
                    tmpRow[6] = "由端口" + ((UDPPacket) tmpPacket).src_port + "发往端口" + ((UDPPacket) tmpPacket).dst_port
                            + ", 数据长度为 " + ((UDPPacket) tmpPacket).length;
                    break;
                // 待续
                default:
                    packetsTypeList[index] = "Unknown";
                    tmpRow[4] = "IP";
                    tmpRow[6] = "未知";
                    break;
            }
        } else {
            packetsTypeList[index] = "Unknown";
            tmpRow[2] = "Unknown";
            tmpRow[3] = "Unknown";
            tmpRow[4] = "Unknown";
            tmpRow[6] = "Unknown";
        }
        return tmpRow;
    }

    // 将数据包中的数据转化成二进制并放入JTextArea组件
    private void PacketParseBinary(Packet packet, JTextArea textArea) {
        if (packet == null || textArea == null)
            return;
        textArea.setText("");
        byte[] packetData = new byte[packet.header.length + packet.data.length];
        System.arraycopy(packet.header, 0, packetData, 0, packet.header.length);
        System.arraycopy(packet.data, 0, packetData, packet.header.length, packet.data.length);
        // if(packetData != null){
        // System.out.println("buxin" + packetData.length);
        // return ;
        // }
        int rowCnt = Math.min(packetData.length / 16, 128);
        int rowIndex = 0x0;
        StringBuilder displayString = new StringBuilder("");
        for (int i = 0; i < rowCnt; i++) {
            // displayString += "0x" + Integer.toHexString(rowIndex) + " ";
            displayString.append(String.format("0x%04X", rowIndex));
            for (int j = 0; j < 16; j++) {
                // displayString += " " + Integer.toHexString(packetData[i * 16 + j] & 0xff);
                if (i * 16 + j < packetData.length)
                    displayString.append(String.format("  %02X", (packetData[i * 16 + j] & 0xff)));
                else
                    displayString.append("    ");
            }
            displayString.append("        ");
            for (int j = 0; j < 16; j++) {
                // displayString += " " + packetData[i * 16 + j];
                if (i * 16 + j < packetData.length) {
                    int t = packetData[i * 16 + j] & 0xff;
                    if (t > 31 && t < 127)
                        displayString.append(String.format("%c", t));
                    else
                        displayString.append(" ");
                } else
                    displayString.append(" ");
            }
            // displayString += "\n";
            displayString.append(String.format("%n"));
            rowIndex += 16;
        }
        textArea.setText(displayString.toString());
    }

    // 将包解析成详细信息，并展示在displayArea2区
    private void PacketParseDetailData(Packet packet, DefaultMutableTreeNode treeRoot, int tableRowIndex) {
        if (packet == null || treeRoot == null)
            return;
        if (treeRoot.getChildCount() != 0) {
            DefaultMutableTreeNode tmp1 = (DefaultMutableTreeNode) treeRoot.getFirstChild();
            DefaultMutableTreeNode tmp2;
            while (tmp1 != null) {
                tmp2 = tmp1.getNextSibling();
                ui.displayArea2TreeModel.removeNodeFromParent(tmp1);
                tmp1 = tmp2;
            }
        }

        // treeRoot.add(PacketParseTreeFirstChildNode(packet, tableRowIndex));
        ui.displayArea2TreeModel.insertNodeInto(PacketParseTreeFirstChildNode(packet, tableRowIndex), treeRoot, 0);
        ui.displayArea2TreeModel.insertNodeInto(PacketParseTreeSecondChildNode(packet), treeRoot, 1);
        if (packet instanceof ARPPacket) {
            ui.displayArea2TreeModel.insertNodeInto(PacketParseTreeThirdChildARPNode(packet), treeRoot, 2);
        } else if (packet instanceof IPPacket) {
            IPPacket ipPacket = (IPPacket) packet;
            ui.displayArea2TreeModel.insertNodeInto(PacketParseTreeThirdChildIPNode(ipPacket), treeRoot, 2);
            switch (ipPacket.protocol) {
                case IPPacket.IPPROTO_ICMP:
                    ui.displayArea2TreeModel.insertNodeInto(
                        PacketParseTreeFourthChildICMPNode((ICMPPacket)ipPacket), treeRoot, 3);
                    break;
                case IPPacket.IPPROTO_TCP:
                    ui.displayArea2TreeModel.insertNodeInto(
                        PacketParseTreeFourthChildTCPNode((TCPPacket)ipPacket), treeRoot, 3);
                    break;
                case IPPacket.IPPROTO_UDP:
                    ui.displayArea2TreeModel.insertNodeInto(
                        PacketParseTreeFourthChildUDPNode((UDPPacket)ipPacket), treeRoot, 3);
                    break;
                default:
                    break;
            }
        } else {

        }
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH:mm:ss.");

    // 将包转换为树状列表的第一层信息，并添加到树的节点上
    private DefaultMutableTreeNode PacketParseTreeFirstChildNode(Packet packet, int rowIndex) {
        sBuffer.setLength(0);
        sBuffer.append("帧 ").append(rowIndex).append(" :").append("数据包长度: ").append(packet.len).append("字节(")
                .append(packet.len * 8).append(" 比特) 捕获长度: ").append(packet.caplen).append("字节(")
                .append(packet.caplen * 8).append(" 比特)");
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(sBuffer.toString());
        treeNode.add(new DefaultMutableTreeNode("封装类型: 以太网帧"));

        sBuffer.setLength(0);
        sBuffer.append("到达时间: ").append(dateFormat.format(new Date(packet.sec * 1000))).append(packet.usec)
                .append("  本地时间");
        treeNode.add(new DefaultMutableTreeNode(sBuffer.toString()));

        sBuffer.setLength(0);
        sBuffer.append("时间戳: ").append(packet.sec).append(".").append(packet.usec).append(" 秒");
        treeNode.add(new DefaultMutableTreeNode(sBuffer.toString()));
        treeNode.add(new DefaultMutableTreeNode("帧数: " + rowIndex));
        return treeNode;
    }

    // 展示包的链路层信息
    private DefaultMutableTreeNode PacketParseTreeSecondChildNode(Packet packet) {
        EthernetPacket ethernetPacket = (EthernetPacket) packet.datalink;
        sBuffer.setLength(0);
        sBuffer.append("以太网v2, 源MAC地址: ").append(ethernetPacket.getSourceAddress()).append(", 目标MAC地址: ")
                .append(ethernetPacket.getDestinationAddress());
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(sBuffer.toString());
        treeNode.add(new DefaultMutableTreeNode("目标MAC地址: " + ethernetPacket.getDestinationAddress()));
        treeNode.add(new DefaultMutableTreeNode("源MAC地址: " + ethernetPacket.getSourceAddress()));
        sBuffer.setLength(0);

        sBuffer.append("上层协议类型: ");
        switch (ethernetPacket.frametype) {
            case EthernetPacket.ETHERTYPE_ARP:
                sBuffer.append("ARP (0x");
                break;
            case EthernetPacket.ETHERTYPE_IP:
                sBuffer.append("IPv4 (0x");
                break;
            case EthernetPacket.ETHERTYPE_IPV6:
                sBuffer.append("IPv6 (0x");
                break;
            case EthernetPacket.ETHERTYPE_LOOPBACK:
                sBuffer.append("LoopBack (0x");
                break;
            case EthernetPacket.ETHERTYPE_VLAN:
                sBuffer.append("Vlan (0x");
                break;
            case EthernetPacket.ETHERTYPE_PUP:
                sBuffer.append("Pup (0x");
                break;
            case EthernetPacket.ETHERTYPE_REVARP:
                sBuffer.append("Revarp (0x");
                break;
            default:
                sBuffer.append("Unknown");
        }
        sBuffer.append(String.format("%04X", ethernetPacket.frametype & 0xffff)).append(")");
        treeNode.add(new DefaultMutableTreeNode(sBuffer.toString()));

        if (packet instanceof ARPPacket) {
            sBuffer.setLength(0);
            byte[] b = packet.header;
            sBuffer.append("帧检验序列: 0x");
            for (int i = b.length - 4; i < b.length; i++)
                sBuffer.append(String.format("%02X", (b[i] & 0xff)));
            sBuffer.append("  [未检验]");
            treeNode.add(new DefaultMutableTreeNode(sBuffer.toString()));
        }

        return treeNode;
    }

    // 展示ARP的报文信息
    private DefaultMutableTreeNode PacketParseTreeThirdChildARPNode(Packet packet) {
        ARPPacket arpPacket = (ARPPacket) packet;
        String senderHardAddress = (String) arpPacket.getSenderHardwareAddress();
        String targetHardAddress = (String) arpPacket.getTargetHardwareAddress();
        String senderProtocolAddress = ((Inet4Address) arpPacket.getSenderProtocolAddress()).toString();
        String targetProtocolAddress = ((Inet4Address) arpPacket.getTargetProtocolAddress()).toString();

        sBuffer.setLength(0);
        sBuffer.append("ARP 地址解析协议");
        String s;
        if (senderProtocolAddress.equals(targetProtocolAddress)) {
            sBuffer.append(" ARP通告");
            s = " 请求 (1)";
        } else {
            if (arpPacket.operation == ARPPacket.ARP_REPLY) {
                sBuffer.append(" 应答");
                s = " 应答 (2)";
            } else {
                sBuffer.append(" 请求");
                s = " 请求 (1)";
            }
        }
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(sBuffer.toString());

        sBuffer.setLength(0);
        sBuffer.append("硬件类型: ");
        switch (arpPacket.hardtype) {
            case ARPPacket.HARDTYPE_ETHER:
                sBuffer.append("以太网 (");
                break;
            case ARPPacket.HARDTYPE_FRAMERELAY:
                sBuffer.append("帧中继 (");
                break;
            case ARPPacket.HARDTYPE_IEEE802:
                sBuffer.append("令牌环 (");
                break;
            default:
                sBuffer.append("Unknown");
                break;
        }
        sBuffer.append(arpPacket.hardtype).append(")");
        treeNode.add(new DefaultMutableTreeNode(sBuffer.toString()));
        // treeNode.add(new DefaultMutableTreeNode("IPv4 (0x0800)"));
        treeNode.add(new DefaultMutableTreeNode("硬件地址长度: " + arpPacket.hlen));
        treeNode.add(new DefaultMutableTreeNode("协议地址长度: " + arpPacket.plen));
        treeNode.add(new DefaultMutableTreeNode("操作码: " + s));
        treeNode.add(new DefaultMutableTreeNode("发送端 MAC 地址: " + senderHardAddress));
        treeNode.add(new DefaultMutableTreeNode("发送端 IP 地址: " + senderProtocolAddress));
        treeNode.add(new DefaultMutableTreeNode("目标端 MAC 地址: " + targetHardAddress));
        treeNode.add(new DefaultMutableTreeNode("目标端 IP 地址: " + targetProtocolAddress));
        return treeNode;
    }

    
    // 展示网络层的报文信息
    DecimalFormat df = new DecimalFormat("0000");
    DecimalFormat df8 = new DecimalFormat("00000000");
    private DefaultMutableTreeNode PacketParseTreeThirdChildIPNode(IPPacket packet) {
        sBuffer.setLength(0);
        String srcIP = packet.src_ip.toString();
        String dstIP = packet.dst_ip.toString();
        sBuffer.append("网络协议版本 ").append(packet.version).append(", 源IP地址: ").append(srcIP).append(", 目标IP地址: ")
                .append(dstIP);
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(sBuffer.toString());

        String prio = df8.format(Integer.valueOf(Integer.toBinaryString(packet.priority)));

        int verAndLen = packet.header[14];
        treeNode.add(
                new DefaultMutableTreeNode(df.format(Integer.valueOf(Integer.toBinaryString((verAndLen >> 4) & 0x0f)))
                        + ".... = 版本 " + ((verAndLen >> 4) & 0xf)));
        if (packet.version == 4) {
            treeNode.add(new DefaultMutableTreeNode(
                    "...." + df.format(Integer.valueOf(Integer.toBinaryString(verAndLen & 0x0f))) + " = 头部长度 "
                            + (verAndLen & 0x0f)));
            treeNode.add(new DefaultMutableTreeNode("区分服务: " + prio));
            treeNode.add(new DefaultMutableTreeNode("IP数据包总长度: " + packet.length + "字节"));
            treeNode.add(
                    new DefaultMutableTreeNode("标识: 0x" + String.format("%04X (", packet.ident) + packet.ident + ")"));

            DefaultMutableTreeNode treeChildNode = new DefaultMutableTreeNode("标志");
            sBuffer.setLength(0);
            int bit = 0;
            String s = "未设置";
            if (packet.dont_frag) {
                bit = 1;
                s = "设置为真";
            }
            sBuffer.append(".").append(bit).append(".. .... = 不能分片: ").append(s);
            treeChildNode.add(new DefaultMutableTreeNode(sBuffer.toString()));
            sBuffer.setLength(0);
            bit = 0;
            s = "未设置";
            if (packet.more_frag) {
                bit = 1;
                s = "设置为真";
            }
            sBuffer.append("..").append(bit).append(". .... = 还有分片: ").append(s);
            treeChildNode.add(new DefaultMutableTreeNode(sBuffer.toString()));
            treeNode.add(treeChildNode);

            treeNode.add(new DefaultMutableTreeNode("片偏移: " + packet.offset));
        } else if (packet.version == 6) {
            treeNode.add(new DefaultMutableTreeNode("流标号: 0x" + String.format("%05X", packet.flow_label)));
            treeNode.add(new DefaultMutableTreeNode("通信量类: " + prio));
            treeNode.add(new DefaultMutableTreeNode("有效载荷长度: " + packet.length + " 字节"));
        }
        treeNode.add(new DefaultMutableTreeNode("生存时间: " + packet.hop_limit));

        sBuffer.setLength(0);
        sBuffer.append("上层协议类型: ");
        switch (packet.protocol) {
            case IPPacket.IPPROTO_ICMP:
                sBuffer.append("ICMP (");
                break;
            case IPPacket.IPPROTO_IGMP:
                sBuffer.append("IGMP (");
                break;
            case IPPacket.IPPROTO_IP:
                sBuffer.append("IP (");
                break;
            case IPPacket.IPPROTO_IPv6:
                sBuffer.append("IPv6 (");
                break;
            case IPPacket.IPPROTO_TCP:
                sBuffer.append("TCP (");
                break;
            case IPPacket.IPPROTO_UDP:
                sBuffer.append("UDP (");
                break;
            default:
                sBuffer.append("Unknown (");
                break;
        }
        sBuffer.append(packet.protocol).append(")");
        treeNode.add(new DefaultMutableTreeNode(sBuffer.toString()));

        if (packet.version == 4) {
            int b10 = packet.header[10];
            int b11 = packet.header[11];
            int checkSum = (b10 << 8) + b11;
            treeNode.add(new DefaultMutableTreeNode("首部校验和: " + (checkSum & 0xffff) + " [未检验]"));
        }

        treeNode.add(new DefaultMutableTreeNode("源IP地址: " + srcIP));
        treeNode.add(new DefaultMutableTreeNode("目标IP地址: " + dstIP));
        return treeNode;
    }

    // 展示ICMP报文协议信息
    private DefaultMutableTreeNode PacketParseTreeFourthChildICMPNode(ICMPPacket packet) {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("网际控制协议ICMP");

        sBuffer.setLength(0);
        sBuffer.append("类型(Type): ").append(packet.type);
        switch (packet.type) {
            case ICMPPacket.ICMP_ECHO:
                sBuffer.append("(Echo 请求)");
                break;
            case ICMPPacket.ICMP_ECHOREPLY:
                sBuffer.append("(Echo 应答)");
                break;
            case ICMPPacket.ICMP_UNREACH:
                sBuffer.append("(差错报告报文: 终点不可达)");
                break;
            case ICMPPacket.ICMP_TIMXCEED:
                sBuffer.append("(差错报告报文: 时间超过)");
                break;
            case ICMPPacket.ICMP_PARAMPROB:
                sBuffer.append("(IP首部出错)");
                break;
            case ICMPPacket.ICMP_REDIRECT:
                sBuffer.append("(重定向)");
                break;
            case ICMPPacket.ICMP_TSTAMP:
                sBuffer.append("(时间戳请求)");
                break;
            case ICMPPacket.ICMP_TSTAMPREPLY:
                sBuffer.append("(时间戳回答)");
                break;
            default:
                sBuffer.append("(Unknown)");
                break;
        }
        treeNode.add(new DefaultMutableTreeNode(sBuffer.toString()));

        treeNode.add(new DefaultMutableTreeNode("代码(Code): " + packet.code));
        treeNode.add(new DefaultMutableTreeNode("检验和: " + packet.checksum));
        
        sBuffer.setLength(0);
        sBuffer.append("数据: ");
        byte[] data = packet.data;
        for(int i = 0; i < data.length; i++)
            sBuffer.append(Integer.toHexString((data[i] & 0xff)));
        sBuffer.append(" ( ").append(data.length).append(" 字节)");
        treeNode.add(new DefaultMutableTreeNode(sBuffer.toString()));
        return treeNode;
    }

    // 展示TCP报文协议信息
    private DefaultMutableTreeNode PacketParseTreeFourthChildTCPNode(TCPPacket packet) {
        sBuffer.setLength(0);
        sBuffer.append("传输控制协议TCP, 源端口: ").append(packet.src_port).append(", 目标端口: ")
        .append(packet.dst_port).append(", 序号(Seq): ").append(packet.sequence).append(", 确认号(Ack): ")
        .append(packet.ack_num);
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(sBuffer.toString());

        treeNode.add(new DefaultMutableTreeNode("源端口: " + packet.src_port));
        treeNode.add(new DefaultMutableTreeNode("目的端口: " + packet.dst_port));

        treeNode.add(new DefaultMutableTreeNode("序号(Sequence Number): " + packet.sequence));
        treeNode.add(new DefaultMutableTreeNode("确认号(Acknowledgment): " + packet.ack_num));
        
        int ipHeaderLen = (packet.header[14] & 0x0f) * 4;
        int tcpHeaderIndex = 14 + ipHeaderLen;
        int dataOffsetIndex = tcpHeaderIndex + 12;
        int dataOffset = (packet.header[dataOffsetIndex] >> 4) & 0x0f;
        treeNode.add(new DefaultMutableTreeNode("数据偏移(TCP首部长度): " + dataOffset * 4 + " 字节"));

        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode("控制位 (Flags)");
        childNode.add(new DefaultMutableTreeNode("紧急URG: " + (packet.urg? 1: 0)));
        childNode.add(new DefaultMutableTreeNode("确认ACK: " + (packet.ack? 1: 0)));
        childNode.add(new DefaultMutableTreeNode("推送PSH: " + (packet.psh? 1: 0)));
        childNode.add(new DefaultMutableTreeNode("复位RST: " + (packet.rst? 1: 0)));
        childNode.add(new DefaultMutableTreeNode("同步SYN: " + (packet.syn? 1: 0)));
        childNode.add(new DefaultMutableTreeNode("终止FIN: " + (packet.fin? 1: 0)));
        treeNode.add(childNode);

        treeNode.add(new DefaultMutableTreeNode("窗口: " + packet.window + " 字节"));

        int checkSumIndex = tcpHeaderIndex + 16;
        short checkSum = (short) ((packet.header[checkSumIndex] << 8) + (packet.header[checkSumIndex + 1] & 0x00ff));
        treeNode.add(new DefaultMutableTreeNode("检验和: " + String.format("0x%04X", checkSum) + "  [未检验]"));

        treeNode.add(new DefaultMutableTreeNode("紧急指针: " + packet.urgent_pointer));

        return treeNode;
    }

    // 展示UDP报文协议信息
    private DefaultMutableTreeNode PacketParseTreeFourthChildUDPNode(UDPPacket packet) {
        sBuffer.setLength(0);
        sBuffer.append("用户数据报协议UDP, 源端口: ").append(packet.src_port).append(", 目标端口: ")
        .append(packet.dst_port);
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(sBuffer.toString());

        treeNode.add(new DefaultMutableTreeNode("源端口号: " + packet.src_port));
        treeNode.add(new DefaultMutableTreeNode("目的端口号: " + packet.dst_port));
        
        treeNode.add(new DefaultMutableTreeNode("数据报长度: " + (packet.length + 8) + " 字节"));

        int ipHeaderLen = (packet.header[14] & 0x0f) * 4;
        int udpHeaderIndex = 14 + ipHeaderLen;
        int checkSumIndex = udpHeaderIndex + 6;
        short checkSum = (short) ((packet.header[checkSumIndex] << 8) + (packet.header[checkSumIndex + 1] & 0x00ff));
        treeNode.add(new DefaultMutableTreeNode("检验和: " + String.format("0x%04X", checkSum) + "  [未检验]"));

        sBuffer.setLength(0);
        sBuffer.append("数据部分: ");
        // int dataIndex = udpHeaderIndex + 8;
        for(int i = 0; i < packet.data.length && i < 32; i++)
            sBuffer.append(Integer.toHexString(packet.data[i]));
        sBuffer.append("... (").append(packet.length).append(" 字节)");
        treeNode.add(new DefaultMutableTreeNode(sBuffer.toString()));
        return treeNode;
    }

    // 重新初始化
    private void RestartCap() {
        if(curPacketsCnt != 0) {
            MyJDialog md = new MyJDialog(ui, "当前捕获的信息是否保存", "提醒");
            boolean state = md.GetState();
            md.dispose();

            if(state) {
                // ui.mainMenuFileSave.doClick();
                JFileChooser fileChooser = new JFileChooser();
                int st = fileChooser.showSaveDialog(ui);
                if(st == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        JpcapWriter jpcapWriter = JpcapWriter.openDumpFile(jpcapCaptor, file.getAbsolutePath());
                        for(int i = 0; i < curPacketsCnt; i++)
                            jpcapWriter.writePacket(packetsList[i]);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    
                }
            }
            int cnt = ui.displayArea1Table.getRowCount();
            for(int i = 0; i < cnt; i++)
                ui.displayArea1TableModel.removeRow(0);

            curPacketsCnt = 0;
        }
    }

    // 设置按钮是否可使用
    private void SetComponentEnable(boolean yes) {
        ui.mainMenuFileSave.setEnabled(yes);

        ui.mainToolBarCloseFileBtn.setEnabled(yes);
        ui.mainToolBarEnlargeFontBtn.setEnabled(yes);
        ui.mainToolBarFirstItemBtn.setEnabled(yes);
        ui.mainToolBarLastItemBtn.setEnabled(yes);
        ui.mainToolBarNextItemBtn.setEnabled(yes);
        ui.mainToolBarOpenFileBtn.setEnabled(yes);
        ui.mainToolBarPreviousItemBtn.setEnabled(yes);
        ui.mainToolBarSaveFileBtn.setEnabled(yes);
        ui.mainToolBarShrinkFontBtn.setEnabled(yes);
        // ui.mainToolBar.setEnabled(yes);
        ui.mainToolBarSaveFileBtn.setEnabled(yes);

        ui.searchBar.setEnabled(yes);
    }
    // private static String byteArrayParseIPString(byte[] arr) {
    // StringBuffer ipStringBuffer = new StringBuffer();
    // if(arr.length == 4) {
    // ipStringBuffer.append(arr[0]);
    // for(int i = 1; i < 4; i++)
    // ipStringBuffer.append(".").append(arr[i]);
    // }
    // return ipStringBuffer.toString();
    // }

    // 抓包线程内部类
    // final static int maxCapThreadCnt = 20;
    
    // 抓包线程内部类
    class CapThread extends Thread {
        // String filterString;
        // boolean isMixMode;
        // int snaplen;
        // int selectedInterfaceIndex;
        // JpcapCaptor jpcapCaptor = null;
        int maxCapPacketsCnt;

        public CapThread(String filterString, boolean isMixModes, int snaplen, int selectedInterfaceIndex,
                int maxCapPacketsCnt) {
            super();
            this.maxCapPacketsCnt = maxCapPacketsCnt;
            try {
                jpcapCaptor = JpcapCaptor.openDevice(networkInterfacesList[selectedInterfaceIndex], snaplen, isMixModes,
                        3000);
                if (isMixModes == false) {
                    jpcapCaptor.setFilter(filterString, true);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("打开网络接口" + selectedInterfaceIndex + "失败!");
            }
        }

        public CapThread() {
            this.maxCapPacketsCnt = 200;
            try {
                jpcapCaptor = JpcapCaptor.openDevice(networkInterfacesList[0], 1024, false, 3000);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("打开默认网络接口0失败!");
            }
        }

        private synchronized boolean GetPacket() {
            if (curPacketsCnt >= packetsListMaxLen)
                return false;
            Packet packet = jpcapCaptor.getPacket();
            if (packet == null)
                return true; // 会出现空包的情况
            packetsList[curPacketsCnt] = packet;
            String[] tmpRow = PacketParseData(packet, curPacketsCnt, curPacketsCnt);
            curPacketsCnt++;
            ui.displayArea1TableModel.addRow(tmpRow);

            // System.out.println(":" + displayArea1Bar.getValue() + " " +
            // displayArea1Bar.getMaximum() + " " + displayArea1Bar.getVisibleAmount());
            return true;
        }

        @Override
        public void run() {
            for (int i = 0; i < maxCapPacketsCnt; i++)
                if (!GetPacket() || capThreadIsStopped)
                    break;
        }
    }

    // 保存文件线程内部类
    class SavePacketThread extends Thread {
        private JpcapWriter jpcapWriter = null;
        private Packet[] packetsList;
        private int curPacketsCnt;
        @Override
        public void run() {
            for(int i = 0; i < curPacketsCnt; i++)
                jpcapWriter.writePacket(packetsList[i]);
            // System.out.println(curPacketsCnt);
            // jpcapWriter.close();
        }
        public SavePacketThread(JpcapCaptor jpcapCaptor, String path, Packet[] packetsList, int curPacketsCnt) {
            try {
                jpcapWriter = JpcapWriter.openDumpFile(jpcapCaptor, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.packetsList = packetsList;
            this.curPacketsCnt = curPacketsCnt;
        }
    }
}
