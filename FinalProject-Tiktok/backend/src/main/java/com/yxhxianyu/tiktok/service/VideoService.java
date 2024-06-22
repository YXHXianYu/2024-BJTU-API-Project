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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
            String filepath = saveVideo(file, uuid);
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
    public Result<List<VideoPojo>> getMyVideos(String userUuid, int page, int size) {
        page = Math.max(1, page);

        QueryWrapper<VideoPojo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userUuid", userUuid).last("limit " + (page - 1) * size + ", " + size);

        List<VideoPojo> videoList = videoDao.selectList(queryWrapper);

        if (videoList == null) {
            return new Result<>("Not Found", null);
        } else {
            return new Result<>(null, videoList);
        }
    }

    public Result<List<VideoPojo>> getAllVideos(int page, int size) {
        page = Math.max(1, page);
        List<VideoPojo> videoList = videoDao.selectList(new QueryWrapper<VideoPojo>().last("limit " + (page - 1) * size + ", " + size));
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

    public String saveVideo(MultipartFile file, String uuid) throws IOException {
        String fileName = uuid + ".mp4";
        Path path = Paths.get(uploadDir, fileName);

        // 检查并创建目录
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }

        // 使用 try-with-resources 关闭 InputStream
        try (InputStream inputStream = file.getInputStream()) {
            System.out.println("Prepare to save video <" + fileName + "> in: " + path);
            Files.copy(inputStream, path);
            System.out.println("Save video <" + fileName + "> in: " + path);
        } catch (IOException e) {
            System.err.println("Error saving video <" + fileName + "> in: " + path);
            e.printStackTrace();
            throw e; // 或者抛出自定义异常
        }

        return path.toString();
    }

    public ResponseEntity<Object> playVideo(String uuid) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(uuid + ".mp4").normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, "video/mp4") // 确保 MIME 类型是 video/mp4
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public void likeVideo(String uuid) {
        VideoPojo video = videoDao.selectById(uuid);
        assert video != null;
        video.setLikes(video.getLikes() + 1);
        videoDao.updateById(video);
    }

    public Result<List<VideoPojo>> getRecommendations() {
        // sort by likes, return all
        List<VideoPojo> recommendations = videoDao.selectList(new QueryWrapper<VideoPojo>().orderByDesc("likes"));
        return new Result<>(null, recommendations);
    }
}
