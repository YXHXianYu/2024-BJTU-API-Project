# API FinalProject Tiktok

## 1. Task Division

* Docs
  * 需求方案 —— @ZY_MC
  * 技术设计文档 —— @YXHXianYu
  * 测试文档 —— @黎苏
* Frontend —— @YXHXianYu
* Backend
  * ~~Framework —— @YXHXianYu~~
  * ~~User Management —— @YXHXianYu~~
    * Login
    * Register
    * Logout
  * ~~Video Management —— @YXHXianYu~~
    * Create A Video
    * Query My Video (分页)
    * Delete My Video
    * Need Authority Check
  * ~~Video Recommendation —— @Fooliqi~~
    * Most Likes Video (Every video is only recommended once)
    * Last Video, Next Video
    * Like Button
  * ~~Database —— @YXHXianYu~~
    * Save Video
  * ~~Log —— @lovekdl~~
    * Every API's Response Time
    * Every API's Input & Output
  * ~~Security —— @YXHXianYu~~
  * Bonus —— @Fooliqi
    * 对视频内容取哈希值，在存视频时，若哈希值匹配，则不重新存视频，减小重复视频的开销
* 答辩 —— @YXHXianYu

## 2. Bonus

* 安全性
  * Authentication身份认证：提供了用户系统，并且在日志中添加了请求方的IP
  * Authorization授权：给用户提供了会话功能.....
  * AccessControl访问控制：API.......
  * Auditable可审计性：通过AOP和AspectJ实现了日志模块，会记录所有接收到的请求的时间、输入、处理接口、输出、耗时
  * AssetProtection资产保护：.....
* 哈希值存储文件，TODO