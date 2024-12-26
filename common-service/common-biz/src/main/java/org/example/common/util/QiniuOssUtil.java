package org.example.common.util;

import cn.hutool.core.net.url.UrlBuilder;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
@Builder
public class QiniuOssUtil {

    private String accessKey;
    private String secretKey;
    private String bucket; //空间名称
    private String cdn; //CDN测试域名

    /**
     * 本地文件上传
     *
     * @param localFilePath 本地文件路径
     * @param fileName      上传文件名
     * @return 上传文件访问路径
     */
    public String upload(String localFilePath, String fileName) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            //默认不指定fileName的情况下，以文件内容的hash值作为文件名
            Response response = uploadManager.put(localFilePath, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            ex.printStackTrace();
            if (ex.response != null) {
                log.error("[log] 文件上传失败 response: {}", ex.response);
                try {
                    String body = ex.response.toString();
                    log.error("[log] 文件上传失败 body: {}", body);
                } catch (Exception ignored) {
                }
            }
        }

        // http://CDN测试域名/fileName
        String uploadFileUrl = UrlBuilder.of()
                .setScheme("http")
                .setHost(cdn)
                .addPath(fileName)
                .build();
        log.info("[log] 文件已上传到七牛云 uploadFileUrl: {}", uploadFileUrl);

        return uploadFileUrl;
    }

    /**
     * 字节数组上传
     *
     * @param uploadBytes 上传文件的字节数组
     * @param fileName    上传文件名
     * @return 上传文件访问路径
     */
    public String upload(byte[] uploadBytes, String fileName) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            //默认不指定fileName的情况下，以文件内容的hash值作为文件名
            Response response = uploadManager.put(uploadBytes, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            ex.printStackTrace();
            if (ex.response != null) {
                log.error("[log] 文件上传失败 response: {}", ex.response);
                try {
                    String body = ex.response.toString();
                    log.error("[log] 文件上传失败 body: {}", body);
                } catch (Exception ignored) {
                }
            }
        }

        // http://CDN测试域名/fileName
        String uploadFileUrl = UrlBuilder.of()
                .setScheme("http")
                .setHost(cdn)
                .addPath(fileName)
                .build();
        log.info("[log] 文件已上传到七牛云 uploadFileUrl: {}", uploadFileUrl);

        return uploadFileUrl;
    }

    /**
     * 删除空间中的文件
     *
     * @param fileName 要删除的文件名
     */
    public void delete(String fileName) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释

        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, fileName);
            log.info("[log] 文件已成功删除 fileName: {}", fileName);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            log.error("[log] 文件删除失败 code: {}, response: {}", ex.code(), ex.response.toString());
        }

    }

}
