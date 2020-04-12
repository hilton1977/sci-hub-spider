import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public final class GoogleSpider extends BaseSpider {
    private static Map<String, String> googleHeaders = new HashMap<String, String>() {{
        put(Header.CACHE_CONTROL.toString(), "max-age=0");
        put(Header.COOKIE.toString(), "SID=uQfg9VWfk1SxHiKLaKkt9UMrDKTtO277vNfZ_grRJb5IMOI3cfRuQ9bclv3qXFje3zEgfQ.; __Secure-3PSID=uQfg9VWfk1SxHiKLaKkt9UMrDKTtO277vNfZ_grRJb5IMOI3W9mOlC2SO5Kq0QcoRjJHYw.; HSID=A8DaaDk2UfbY7aBZC; SSID=AJaVn_Q6KcvTH9Nrr; APISID=6XK7hfX7bZMABpdP/AGSXLIRlzWPrED9T7; SAPISID=4Ds58-NiStE0Qhs9/A5Q01udHCh4Iuhrfq; __Secure-HSID=A8DaaDk2UfbY7aBZC; __Secure-SSID=AJaVn_Q6KcvTH9Nrr; __Secure-APISID=6XK7hfX7bZMABpdP/AGSXLIRlzWPrED9T7; __Secure-3PAPISID=4Ds58-NiStE0Qhs9/A5Q01udHCh4Iuhrfq; CONSENT=YES+CN.zh-CN+20180311-09-0; GSP=LM=1584085704:S=pLL-Xu4DhGvL_4lQ; 1P_JAR=2020-3-18-16; NID=200=EVwxqEbHXWRv3UISD_qk_JSfZYCKXukk_WgMH9-ZqVoWMIKd8YC10uIbz7XIP4xHPWnwUFlAdLUHDExHfhx3h7PLPtxFz9yn3Ov9stbhLkry01CBGIIoGGJTTt6MBmDtnQQABpjJcweSggXoR7-fzOzLadwNPV5TKbZWRAo3Hy4; SIDCC=AJi4QfHbgcBWvCH5lGgltHKwPiQMzqxpp_iu39Ydd0JNN5u4a_-iUyR_OUlBQUTjPV49-d1nAA");
    }};

    private GoogleSearch googleSearch;

    private AtomicInteger download;

    public static GoogleSpider create(GoogleSearch googleSearch) {
        return new GoogleSpider(googleSearch);
    }

    public GoogleSpider(GoogleSearch googleSearch) {
        this.googleSearch = googleSearch;
        this.download = new AtomicInteger(googleSearch.getLimit());
    }

    private boolean checkLimit() {
        return 0 == 0;
    }


    /**
     * 下载成功
     * 更新下载数量和进度条
     */
    private void downLoadSuccess(){
        download.decrementAndGet();
        Config.getMainUI().updateProgressBar();
    }

    public void exportExcel() {
        while (checkLimit()) {
            downloadByPage();
            googleSearch.nextPage();
        }
    }

    private void downloadByPage() {
        String htmlStr = httpStrProxy(googleSearch.toUrl(), googleHeaders);
        if (StrUtil.isEmpty(htmlStr)) {
            return;
        }
        Document document = Jsoup.parse(htmlStr);
        List<Dissertation> dissertationList = new ArrayList<>();
        String fileLinkFormat = "HYPERLINK(\"" + "{}" + "\",\"" + "文件地址" + "\")";
        String downPath = "D://test//";
        List<CompletableFuture> completableFutureList = new ArrayList<>();
        for (Element element : document.getElementsByClass("gs_r gs_or gs_scl")) {
            if (!checkLimit()) {
                return;
            }
            String title = element.select(".gs_rt>a").text();
            String[] baseInfo = element.select(".gs_a").text().split("-");
            String author = baseInfo[0];
            String year = baseInfo[1];
            String source = baseInfo[2];
            Dissertation dissertation = new Dissertation();
            dissertation.setAuthor(author);
            dissertation.setSource(source);
            dissertation.setTitle(title);
            dissertation.setYear(year);
            dissertationList.add(dissertation);
            Elements docView = element.select(".gs_or_ggsm");
            if (docView.size() > 0) {
                String docType = docView.select(".gs_ctg2").text();
                String fileName = downPath + title + ".pdf";
                String url = docView.select("a").get(0).attr("href");
                if ("[PDF]".equals(docType)) {
                    downloadPdf(url,fileName,false);
                }
                if ("[HTML]".equals(docType) && docView.text().contains("Full View")) {
                    downloadPdf(url,fileName,true);
                }
            }
        }
    }


    /**
     * 根据地址下载pdf
     *
     * @param url      下载地址
     * @param filePath 存放路径
     */
    private void downloadPdf(String url, String filePath,boolean isSci) {
        CompletableFuture
                .runAsync(() -> super.downLoad(url, filePath,isSci), Config.threadPool())
                .whenComplete((aVoid, throwable) -> {
                    if(throwable==null){
                        downLoadSuccess();
                    }
                });
    }
}
