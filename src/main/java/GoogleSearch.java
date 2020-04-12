import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;

public class GoogleSearch {
    /**
     * 时间开始
     */
    private String as_ylo;
    /**
     * 时间结束
     */
    private String as_yhi;
    private String hl = "zh-CN";
    /**
     * 搜索关键字
     */
    private String q;
    /**
     * 页数
     */
    private int start = 0;

    private int limit;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public GoogleSearch nextPage() {
        this.start += 10;
        return this;
    }

    public GoogleSearch(String q) {
        this.q = q;
    }

    public String getAs_ylo() {
        return as_ylo;
    }

    public GoogleSearch setAs_ylo(String as_ylo) {
        this.as_ylo = as_ylo;
        return this;
    }

    public String getAs_yhi() {
        return as_yhi;
    }

    public GoogleSearch setAs_yhi(String as_yhi) {
        this.as_yhi = as_yhi;
        return this;
    }

    public String getHl() {
        return hl;
    }

    public GoogleSearch setHl(String hl) {
        this.hl = hl;
        return this;
    }

    public String getQ() {
        return q;
    }

    public GoogleSearch setQ(String q) {
        this.q = q.trim();
        return this;
    }

    public int getStart() {
        return start;
    }

    public GoogleSearch setStart(int start) {
        this.start = start;
        return this;
    }

    public String toUrl(){
        String params= HttpUtil.toParams(BeanUtil.beanToMap(this));
        String google = "https://scholar.google.com/scholar?"+params;
        return google;
    }
}
