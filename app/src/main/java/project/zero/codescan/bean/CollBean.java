package project.zero.codescan.bean;

/**
 * Created by ZeroProject on 2016/5/26 14:39
 */
public class CollBean {
    private String url;
    private String name;

    @Override
    public String toString() {
        return "CollBean{" +
            "url='" + url + '\'' +
            ", name='" + name + '\'' +
            '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
