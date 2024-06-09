package com.yxhxianyu.tiktok.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxhxianyu.tiktok.dao.UserDao;
import com.yxhxianyu.tiktok.dao.VideoDao;
import com.yxhxianyu.tiktok.pojo.UserPojo;
import com.yxhxianyu.tiktok.pojo.VideoPojo;
import com.yxhxianyu.tiktok.utils.Result;
import com.yxhxianyu.tiktok.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * @author YXH_XianYu
 **/
@Service
public class VideoService {

    @Autowired
    VideoDao videoDao;

    public Result<String> createVideo(MultipartFile file, String title, String userUuid) {

        try {
            String uuid = UUID.randomUUID().toString();
            String filepath = saveVideo(file, title, uuid);
            videoDao.insert(new VideoPojo(uuid, title, filepath, 0, userUuid));
            return new Result<>(null, uuid);
        } catch (DuplicateKeyException e) {
            System.out.println("Insert failed: duplicate username");
            return new Result<>("ERROR: Insert failed: duplicate username", null);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Insert failed: data integrity violation (maybe data is too long)");
            return new Result<>("ERROR: Insert failed, data integrity violation", null);
        } catch (IOException e) {
            System.out.println("Saving file failed");
            return new Result<>("ERROR: Saving file failed", null);
        }
    }

    public Result<VideoPojo> getVideoByUUID(String uuid) {
        VideoPojo video = videoDao.selectById(uuid);
        if (video == null) {
            return new Result<>("Not Found", null);
        } else {
            return new Result<>(null, video);
        }
    }

    public Result<VideoPojo> getVideoByTitle(String title) {
        VideoPojo video = videoDao.selectOne(new QueryWrapper<VideoPojo>().eq("title", title));
        if (video == null) {
            return new Result<>("Not Found", null);
        } else {
            return new Result<>(null, video);
        }
    }

    public Result<VideoPojo> getVideoByFilepath(String filepath) {
        VideoPojo video = videoDao.selectOne(new QueryWrapper<VideoPojo>().eq("filepath", filepath));
        if (video == null) {
            return new Result<>("Not Found", null);
        } else {
            return new Result<>(null, video);
        }
    }

    public Result<List<VideoPojo>> getAllVideos() {
        List<VideoPojo> videoList = videoDao.selectList(null);
        if (videoList == null) {
            return new Result<>("Not Found", null);
        } else {
            return new Result<>(null, videoList);
        }
    }

    public void deleteVideoByUUID(String uuid) {
        videoDao.deleteById(uuid);
        // TODO: delete file
    }

    public void deleteVideoByTitle(String title) {
        videoDao.delete(new QueryWrapper<VideoPojo>().eq("title", title));
        // TODO: delete file
    }

    @Value("${video.upload-dir}")
    private String uploadDir;

    private String saveVideo(MultipartFile file, String title, String uuid) throws IOException {
        String fileName = uuid + ".mp4";
        Path path = Paths.get(uploadDir, fileName);
        System.out.println("Prepare to save video <" + fileName + "> in: " + path);
        InputStream inputStream = file.getInputStream();
        Files.copy(inputStream, path);
        inputStream.close();
        System.out.println("Save video <" + fileName + "> in: " + path);
        return path.toString();
    }
}
