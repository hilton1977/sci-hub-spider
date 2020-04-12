import cn.hutool.core.util.StrUtil;

import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {

    private JTextField searchInput =new JTextField(15);
    private JPanel searchPanel = new JPanel();
    private JButton searchBtn = new JButton("下载");
    private JProgressBar downloadBar = new JProgressBar();
    private JTextField limit = new JTextField(2);
    private JTextField yearS = new JTextField(3);
    private JTextField yearE = new JTextField(3);


    public MainUI() throws HeadlessException {
        init();
    }

    public boolean check(){
        if(StrUtil.isEmpty(searchInput.getText())){
            this.showTip("请输入搜索内容");
            return false;
        }
        String searchLimit = limit.getText();
        if(StrUtil.isEmpty(searchLimit)){
            this.showTip("请输入下载条数");
            return false;
        }
        return true;
    }


    public GoogleSearch getGoogleSearch(){
        GoogleSearch googleSearch = new GoogleSearch(searchInput.getText());
        googleSearch.setAs_ylo(yearS.getText());
        googleSearch.setAs_yhi(yearE.getText());
        googleSearch.setLimit(Integer.valueOf(limit.getText()));
        initProgressBar(googleSearch.getLimit());
        return googleSearch;
    }

    public void disableBtn() {
        this.searchBtn.setEnabled(false);
    }

    public void enableBtn() {
        this.searchBtn.setEnabled(true);
    }

    public void initProgressBar(int max){
        this.downloadBar.setValue(0);
        this.downloadBar.setMaximum(max);
    }

    public synchronized void updateProgressBar(){
        int newValue = this.downloadBar.getValue()+1;
        this.downloadBar.setValue(newValue);
    }

    public void showTip(String msg) {
        JOptionPane
                .showMessageDialog(this,
                        msg,
                        "消息", JOptionPane.INFORMATION_MESSAGE);
    }


    public void init() {
        this.setTitle("谷歌爬虫");
        this.setSize(350, 140);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        searchPanel.add(new JLabel("下载条数"));
        searchPanel.add(limit);
        searchPanel.add(new JLabel("时间范围（单位:年）"));
        searchPanel.add(yearS);
        searchPanel.add(new JLabel("-"));
        searchPanel.add(yearE);
        searchBtn.addActionListener(new SearchActionListener(this));
        searchBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchInput.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(new JLabel("搜索关键字"));
        searchPanel.add(searchInput);
        searchPanel.add(searchBtn);
        downloadBar = new JProgressBar();
//        downloadBar.setBackground(Color.GREEN);
        downloadBar.setForeground(Color.GREEN);
        downloadBar.setMinimum(0);
        downloadBar.setStringPainted(true);
        downloadBar.setPreferredSize(new Dimension(234,25));
        searchPanel.add(new JLabel("下载进度"));
        searchPanel.add(downloadBar);
        this.setContentPane(searchPanel);
        this.setVisible(true);
    }
}
