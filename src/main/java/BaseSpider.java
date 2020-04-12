import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

public class BaseSpider {

    private final String ip="127.0.0.1";
    private final int port =10809;
    private final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
    private final String SCI_HUB_URL = "https://sci-hub.tw/";
    private final Map<String,String> baseHeader = new HashMap<String, String>(){{
        put(Header.USER_AGENT.toString(), "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
    }};

    /**
     * 获取最近的scihub地址
     * @return
     */
    private String getNewestSciHub(){
        return "sad";
    }


    /**
     * Sci-Hub pdf链接生成
     * @param url 请求地址
     * @return 返回 sci-hub 新地址
     */
    private String sciHubUrl(String url){
        Assert.notBlank(url);
        return SCI_HUB_URL + url;
    }

    /**
     * 普通下载
     * 判断如果链接返回类型为 pdf 则进行读写生成 pdf文件
     * text/html 则 通过生成 sci-hub 链接 搜索 下载地址
     * @param url 下载地址
     * @param filePath 文件绝对路径
     * @return 下载是否成功
     */
    private boolean downLoad(String url,String filePath){
        final HttpResponse response = HttpRequest.get(url)
                .headerMap(baseHeader,false)
//                .setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)))
                .setProxy(proxy)
                .timeout(-1).execute();
        if(!response.isOk()){
            return false;
        }
        String contentType = response.header(Header.CONTENT_TYPE);
        if(contentType.contains("application/pdf")){
            response.writeBody(FileUtil.file(filePath));
            return true;
        }
        if(contentType.contains("text/html")){
            return downLoadBySciHub(url, filePath);
        }
        return false;
    }


    public boolean downLoad(String url,String filePath,boolean sciFlag){
        if(sciFlag){
            return downLoadBySciHub(url, filePath);
        }else {
            return downLoad(url, filePath);
        }
    }
    /**
     * 通过 sci-hub 进行下载
     * @param url 下载地址
     * @param filePath 文件绝对路径
     * @return 下载是否成功
     */
    private boolean downLoadBySciHub(String url,String filePath){
        String sciHubUrl = sciHubUrl(url);
        String downloadUrl = convertSciHubDowLoadUrl(sciHubUrl);
        if(StrUtil.isEmpty(downloadUrl)){
            System.out.println(filePath+"   "+url+ " 无法搜索sci-hub 地址");
            return false;
        }
        System.out.println(downloadUrl);
        final HttpResponse response = HttpRequest.get(downloadUrl)
                .headerMap(baseHeader,false)
//                .setProxy(proxy)
                .timeout(-1).executeAsync();
        if (response.isOk()) {
            response.writeBody(FileUtil.file(filePath));
            return true;
        }
        return false;
    }

    /**
     * 谷歌学术普通链接生成并解析新下载地址
     * @param sciHubUrl sci-hub提供的新下载地址
     * @return 解析新地址
     */
    public String convertSciHubDowLoadUrl(String sciHubUrl){
        String body = httpStr(sciHubUrl);
        Document document = Jsoup.parse(body);
        String onclickStr=document.getElementsByTag("a").stream()
                .filter(element -> element.text().contains("save"))
                .findFirst()
                .map(element -> element.attr("onclick"))
                .orElse(null);
        if(onclickStr!=null){
            String match = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\\\+&amp;%\\$#_]*)?";
            return ReUtil.get(match, onclickStr, 0);
        }
        return "";
    }

    public String httpStr(String url){
        return httpStr(url, null,null);
    }

    public String httpStr(String url,Map<String,String> headerList){
        return httpStr(url, headerList, null);
    }

    public String httpStrProxy(String url,Map<String,String> headerList){
        return httpStr(url, headerList, proxy);
    }


    public String httpStr(String url,Map<String,String> headerList,Proxy proxy){
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpRequest.
                    get("url")
                    .setUrl(url)
                    .headerMap(baseHeader,false)
                    .headerMap(headerList,true)
                    .timeout(10*1000)
                    .setProxy(proxy)
                    .execute();
        } catch (IORuntimeException e) {
            System.out.println("请求超时");
            return "";
        }
        if(httpResponse.isOk()){
            return httpResponse.body();
        }
        return "";
    }


}
