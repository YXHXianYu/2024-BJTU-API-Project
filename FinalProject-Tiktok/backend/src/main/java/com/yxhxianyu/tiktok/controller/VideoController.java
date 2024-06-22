package com.yxhxianyu.tiktok.controller;

import com.yxhxianyu.tiktok.pojo.UserPojo;
import com.yxhxianyu.tiktok.pojo.VideoPojo;
import com.yxhxianyu.tiktok.service.UserService;
import com.yxhxianyu.tiktok.service.VideoService;
import com.yxhxianyu.tiktok.utils.Result;
import com.yxhxianyu.tiktok.utils.Util;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

import static com.yxhxianyu.tiktok.utils.Util.checkPermission;

/**
 * @author YXH_XianYu
 **/
@RestController
public class VideoController {

    @Autowired
    VideoService videoService;

    @Autowired
    UserService userService;

    @RequestMapping(
            value = "/api/v1/videos",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Object> createVideo(
            @RequestParam("file")MultipartFile file,
            @RequestParam("title")String title,
            @RequestParam("token")String token
    ) {
        Result<UserPojo> user = userService.getUserByToken(token);
        if (user.err != null) {
            return Util.getResponse(401, "Unauthorized");
        }

        Result<String> res = videoService.createVideo(file, title, user.val.getUuid());
        if (res.err != null) {
            return Util.getResponse(422, res.err);
        }
        return Util.getOkResponse("创建成功");
    }


    @RequestMapping(value = "/api/v1/videos", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllVideos(@RequestParam(defaultValue = "1") int page) {
        Result<List<VideoPojo>> allVideos = videoService.getAllVideos(page, 2);
        if (allVideos.err != null) {
            return Util.getResponse(404, "视频不存在");
        }
        return Util.getOkResponse("获取成功", allVideos.val);
    }

    @RequestMapping(value = "/api/v1/videos/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<Object> getVideo(@PathVariable String uuid) {
        Result<VideoPojo> video = videoService.getVideoByUUID(uuid);
        if (video.err != null) {
            return Util.getResponse(404, "视频不存在");
        }

        return Util.getOkResponse("获取成功", video.val);
    }

    @RequestMapping(value = "/api/v1/videos/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteVideo(@PathVariable String uuid, @RequestHeader String Authorization) {
        Result<UserPojo> user = userService.getUserByToken(Authorization);
        if (user.err != null) {
            return Util.getResponse(401, "Unauthorized");
        }

        Result<VideoPojo> video = videoService.getVideoByUUID(uuid);
        if (video.err != null) {
            return Util.getResponse(404, "视频不存在");
        }

        if (!video.val.getUserUuid().equals(user.val.getUuid())) {
            return Util.getResponse(403, "Forbidden");
        }

        videoService.deleteVideoByUUID(uuid);
        return Util.getOkResponse("删除成功");
    }

    @RequestMapping(value = "/api/v1/playable_videos/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<Object> getPlayableVideo(@PathVariable String uuid) {
        Result<VideoPojo> video = videoService.getVideoByUUID(uuid);
        if (video.err != null) {
            return Util.getResponse(404, "视频不存在");
        }

        return videoService.playVideo(video.val.getFilepath()); // hash
    }

    // Recommendation System
    @RequestMapping(value = "/api/v1/videos/{uuid}/likes", method = RequestMethod.POST)
    public ResponseEntity<Object> likeVideo(@PathVariable String uuid) {
        Result<VideoPojo> video = videoService.getVideoByUUID(uuid);
        if (video.err != null) {
            return Util.getResponse(404, "视频不存在");
        }
        videoService.likeVideo(uuid);
        return Util.getOkResponse("点赞成功");
    }

    @RequestMapping(value = "/api/v1/videos/{uuid}/likes", method = RequestMethod.GET)
    public ResponseEntity<Object> getLikes(@PathVariable String uuid) {
        Result<VideoPojo> video = videoService.getVideoByUUID(uuid);
        if (video.err != null) {
            return Util.getResponse(404, "视频不存在");
        }
        return Util.getOkResponse("获取成功", video.val.getLikes());
    }

    @RequestMapping(value = "/api/v1/videos/recommendations", method = RequestMethod.GET)
    public ResponseEntity<Object> getRecommendations(@RequestHeader String Authorization) {
        Result<UserPojo> user = userService.getUserByToken(Authorization);
        if (user.err != null) {
            return Util.getResponse(401, "Unauthorized");
        }
        Result<List<VideoPojo>> recommendations = videoService.getRecommendations();
        if (recommendations.err != null) {
            return Util.getResponse(404, "视频不存在");
        }
        return Util.getOkResponse("获取成功", recommendations.val);
    }
}
