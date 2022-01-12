package cn.canerme.httpproxy.util;

import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.ReflectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * UTF-8
 * Created by czy  Time : 2022/1/12 9:30
 *
 * @version 1.0
 */
public class CompressUtil {

    public static <T> byte [] unCompress(HttpResponse<byte[]> response){
        List<String> encodings = response.headers().allValues("Content-Encoding");
        byte[] bytes = response.body();
        if (!Objects.isNull(encodings)&& encodings.size()>=1) {
            for (String encoding : encodings) {
                if (encoding.contains("gzip")){
                    return unCompressGZIP(bytes);
                }else if (encoding.contains("br")){
                    break;
                }else if (encoding.contains("deflate")){
                    return unCompressDeflate(bytes);
                }
            }
        }

        return bytes;
    }

    protected static byte [] unCompressGZIP(byte[] bytes)  {
        byte[] content = new byte[1024];
        int readLen = 0;
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes); ByteArrayOutputStream byteOut = new ByteArrayOutputStream(); GZIPInputStream gzipIn = new GZIPInputStream(byteIn)) {
            while ((readLen = gzipIn.read(content)) != -1) {
                byteOut.write(content, 0, readLen);
            }
            return byteOut.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static byte[] unCompressDeflate(byte[] src){
        Inflater inf = new Inflater(true);
        byte[] content = new byte[1024];
        int readLen = 0;
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(src); ByteArrayOutputStream byteOut = new ByteArrayOutputStream(); InflaterInputStream gzipIn = new InflaterInputStream(byteIn, inf)) {
            //这个地方，直接new InflaterInputStream(in)就会出错
            while ((readLen = gzipIn.read(content)) != -1) {
                byteOut.write(content, 0, readLen);
            }
            return byteOut.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
