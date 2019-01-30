package com.linda.o2o.util;

import com.linda.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);



    /**
     * 将CommonsMultipartFile转换成File
     * @param cFile
     * @return
     */
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile){

        File newFile = new File(cFile.getOriginalFilename());
        try {
            cFile.transferTo(newFile);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return  newFile;
    }
    /**
     *处理缩略图，并返回新生成图片的相对值路径
     * @param targetAddr
     * @return
     */
    public static String genarateThumbnail(ImageHolder thumbnail,String targetAddr) throws UnsupportedEncodingException {
        String newbasePath=java.net.URLDecoder.decode(basePath,"utf-8");
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is : "+relativeAddr);

        System.out.println("****************"+newbasePath);

        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete Addr is : "+PathUtil.getImgBasePath()+relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(newbasePath + "watermark.jpg")), 0.25f).
                    outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }

        return relativeAddr;
    }

    /**
     * 创建目标路径所涉及到的目录，即/home/work/xiangzai/xxx.jpg
     * @param targetAddr
     */

    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath()+targetAddr;
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     * @return
     */
    public static String getRandomFileName(){
        //获取随机的五位数
        int rannum=r.nextInt(89999)+10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr+rannum;
    }

    /**
     * 获取输入文件流的扩展名
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName){

        return  fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 判断storePath是文件路径还是目录路径
     * 如果是文件，删除
     * 如果是目录，删除该目录下所有文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File files[] = fileOrPath.listFiles();
                for(int i=0;i<files.length;i++){
                    files[i].delete();
                }

            }
            fileOrPath.delete();
        }
    }


    public static String genarateNormalImg(ImageHolder thumbnail,String targetAddr) throws UnsupportedEncodingException {
        String newbasePath=java.net.URLDecoder.decode(basePath,"utf-8");
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is : "+relativeAddr);

        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete Addr is : "+PathUtil.getImgBasePath()+relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(337, 640)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(newbasePath + "watermark.jpg")), 0.25f).
                    outputQuality(0.9f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }

        return relativeAddr;
    }


}
