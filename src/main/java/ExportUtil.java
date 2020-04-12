import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class ExportUtil {

    public static void export(List rows,String path) {
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(path);
        Sheet sheet = writer.getSheet();
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 50 * 256);
        sheet.setColumnWidth(2, 50 * 256);
        sheet.setColumnWidth(3, 50 * 85);
        sheet.setColumnWidth(4, 50 * 200);
        //自定义标题别名
        writer.addHeaderAlias("title", "标题");
        writer.addHeaderAlias("publish", "期刊");
        writer.addHeaderAlias("author", "作者");
        writer.addHeaderAlias("year", "年份");
        writer.addHeaderAlias("httpUrl", "网页地址");
        writer.addHeaderAlias("source", "来源");
        writer.addHeaderAlias("pdf", "pdf文件");
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        // 关闭writer，释放内存
        writer.close();
    }
}
