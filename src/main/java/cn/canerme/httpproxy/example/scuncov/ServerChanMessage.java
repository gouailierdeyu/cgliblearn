package cn.canerme.httpproxy.example.scuncov;

import java.io.Serializable;

/**
 * UTF-8
 * Created by czy  Time : 2022/1/3 15:23
 *
 * @version 1.0
 */
public class ServerChanMessage implements Serializable {
    public String title;
    public String desp;

    public ServerChanMessage(String title, String desp) {
        this.title = title;
        this.desp = desp;
    }

    @Override
    public String toString() {
        return "title=" + title + "&desp=" + desp;
    }
}
