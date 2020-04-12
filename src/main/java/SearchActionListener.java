import cn.hutool.core.util.StrUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchActionListener implements ActionListener {
    private MainUI mainUI;

    public SearchActionListener(MainUI mainUI) {
        this.mainUI = mainUI;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(mainUI.check()){
            GoogleSpider.create(mainUI.getGoogleSearch()).exportExcel();
        }
    }
}
