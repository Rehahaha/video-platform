## 项目概述：简历用视频网站（Vue + Spring Boot + MySQL）

这是一个为应届软件工程毕业生准备的 **个人项目级视频网站**，用于在简历和面试中展示你的工程实践能力。  
技术栈选型：
- **前端**：Vue 3（基于 Vite 构建）
- **后端**：Spring Boot 3（JDK 21）
- **数据库**：MySQL 5.7
- **构建与运行环境**：
  - Java：`21.0.6`
  - Node.js：`v22.16.0`

目标是实现一个从 0 到 1 的完整 Web 项目，涵盖：需求分析、数据库设计、接口设计、前后端联调、基础测试与文档。

---

## 一、功能需求设计（MVP 版本）

只实现简历和演示需要的 **最小可用产品（MVP）** 功能，保持简单但完整。

- **1. 用户模块**
  - 用户注册（邮箱/用户名 + 密码）
  - 用户登录 / 退出登录（JWT 或 Session）
  - 查看并编辑个人资料（昵称、头像占位、简介）

- **2. 视频模块**
  - 视频列表页：展示视频卡片（封面、标题、作者、播放量）
  - 视频详情页：视频播放（先用本地静态文件或占位资源）、视频简介、上传者信息
  - 视频上传：
    - 支持选择本地视频文件上传（先不做大文件分片）
    - 支持上传封面图（可选）
    - 填写标题、简介、标签（简单字符串）

- **3. 互动模块（可选，作为加分项）**
  - 点赞视频
  - 视频评论（基础增删查）

- **4. 管理/运维（简化）**
  - 仅实现简单的管理接口（如删除视频 / 删除评论），不做完整后台界面

---

## 二、整体架构设计

- **前端（video-frontend）**
  - 技术：Vue 3 + Vite + TypeScript（可选）+ Vue Router + Axios
  - UI：Element Plus 或 Naive UI（二选一），配合自定义样式
  - 功能：路由、登录态管理、请求封装、基础组件（导航栏、视频卡片等）

- **后端（video-backend）**
  - 技术：Spring Boot 3 + Spring Web + Spring Data JPA / MyBatis + Spring Security（简化使用）或自实现过滤器 + JWT
  - 功能：RESTful API、登录注册、视频上传、视频信息管理、简单权限控制

- **数据库（MySQL 5.7）**
  - 用户表：`user`
  - 视频表：`video`
  - 评论表（可选）：`comment`
  - 点赞表（可选）：`video_like`

---

## 三、数据库初步设计

- **user 表**
  - `id` (PK, bigint)
  - `username` (varchar, 唯一)
  - `email` (varchar, 唯一)
  - `password_hash` (varchar, 加密后保存)
  - `nickname` (varchar)
  - `avatar_url` (varchar, 可为空)
  - `bio` (varchar, 可为空)
  - `created_at` (datetime)

- **video 表**
  - `id` (PK, bigint)
  - `user_id` (FK -> user.id)
  - `title` (varchar)
  - `description` (text)
  - `cover_url` (varchar)
  - `video_url` (varchar)
  - `view_count` (int)
  - `created_at` (datetime)

- **comment 表（可选）**
  - `id` (PK, bigint)
  - `video_id` (FK -> video.id)
  - `user_id` (FK -> user.id)
  - `content` (text)
  - `created_at` (datetime)

- **video_like 表（可选）**
  - `id` (PK, bigint)
  - `video_id` (FK -> video.id)
  - `user_id` (FK -> user.id)
  - `created_at` (datetime)

> 后续实现时，可以根据实际需要精简或扩展字段。

---

## 四、接口设计（概要）

后端将提供一组 RESTful API，前端通过 Axios 调用。

- **认证相关**
  - `POST /api/auth/register`：注册
  - `POST /api/auth/login`：登录，返回 JWT
  - `GET /api/auth/profile`：获取当前用户信息（需登录）
  - `PUT /api/auth/profile`：更新个人资料（需登录）

- **视频相关**
  - `GET /api/videos`：分页获取视频列表（按时间或热度排序）
  - `GET /api/videos/{id}`：获取视频详情
  - `POST /api/videos`：上传视频信息 + 文件（需登录）
  - `DELETE /api/videos/{id}`：删除视频（仅限作者或管理员）

- **评论 & 点赞（可选）**
  - `GET /api/videos/{id}/comments`
  - `POST /api/videos/{id}/comments`
  - `POST /api/videos/{id}/like`
  - `DELETE /api/videos/{id}/like`

接口的详细请求/响应字段，会在后端实现阶段细化。

---

## 五、开发计划（分阶段，从 0 到 1）

### 阶段 0：项目初始化

- **后端**
  - 使用 Spring Initializr 创建 `video-backend` 项目
  - 引入依赖：Spring Web、Spring Data JPA 或 MyBatis、MySQL Driver、Spring Security（或 JWT 相关依赖）
  - 配置基础的 application 配置（端口、数据库连接）

- **前端**
  - 使用 `npm create vite@latest` 创建 `video-frontend`
  - 选择 Vue 模板（可选 TypeScript）
  - 配置基础目录结构：`components/`, `views/`, `router/`, `api/`
  - 安装 UI 库（如 Element Plus）和 Axios

> 当前优先从 **前端界面** 开始实现，后端先占位一个简单的健康检查接口。

---

### 阶段 1：前端界面开发（无后端联通，先做静态页面）

- **1.1 全局布局**
  - 顶部导航栏：Logo、导航菜单（首页、上传、登录/用户头像）
  - 主内容区：根据路由展示不同页面
  - 底部简单版权信息

- **1.2 路由设计**
  - `/`：首页视频列表页
  - `/login`：登录/注册页（Tab 或切换）
  - `/video/:id`：视频详情页
  - `/upload`：上传视频页（需登录，前期可不做权限拦截）
  - `/profile`：个人中心页（查看/编辑资料）

- **1.3 页面原型**
  - 首页：网格/瀑布流视频卡片（封面+标题+作者+播放量）
  - 登录/注册：表单 + 基础校验
  - 视频详情页：视频播放器（可先用 HTML5 video 标签 + 本地静态资源），右侧/下方显示简介、作者、发布时间
  - 上传页：表单（标题、简介、标签、视频文件、封面文件）
  - 个人中心：展示个人信息，简单表单编辑

> 阶段 1 的目标：在没有真实后端的情况下，先完成所有页面的 UI 和基础交互（用 mock 数据）。

---

### 阶段 2：后端接口开发

- **2.1 数据库 & 实体**
  - 创建数据库和表（按上面设计）
  - 创建实体类 / Mapper、Repository

- **2.2 认证模块**
  - 注册接口：保存用户，密码加密（BCrypt）
  - 登录接口：校验用户，生成 JWT
  - 登录状态校验：拦截器/过滤器验证 JWT

- **2.3 视频模块**
  - 视频的增删查接口
  - 视频文件上传接口（文件先存本地磁盘，路径存数据库）
  - 简单的权限控制（作者才可删）

- **2.4 评论和点赞（时间允许时做）**

> 阶段 2 的目标：保证所有前端已设计的核心功能都有对应可用的后端接口。

---

### 阶段 3：前后端联调与登录态管理

- 使用 Axios 封装请求，统一处理：
  - Base URL、超时时间
  - 请求头中携带 JWT（从 `localStorage` 或 `sessionStorage` 中读取）
  - 统一错误处理和登录失效跳转

- 在前端完成：
  - 登录成功后保存 Token 和用户信息
  - 导航栏根据登录状态显示“登录/注册”或“用户头像 + 下拉菜单”
  - 上传页、个人中心页添加简单的登录权限拦截

> 阶段 3 的目标：让项目“真正可用”，完成端到端的用户登录、上传视频、浏览视频流程。

---

### 阶段 4：基础测试与优化

- **后端测试**
  - 使用 Postman / Thunder Client / curl 测试主要接口
  - 编写少量单元测试 / 集成测试（可选）

- **前端测试**
  - 手动测试核心用户流程
  - 修复明显 UI 问题和交互 bug

- **性能 & 安全（简单处理）**
  - 密码加密、Token 过期处理
  - 简单的跨域配置（CORS）
  - 上传文件大小限制

---

## 六、项目运行方式（规划）

> 实际内容会在项目实现后逐步补全，这里先约定规范。

- **后端运行**
  - 配置 `application.yml` 中的数据库连接
  - 通过 IDE（IntelliJ IDEA）或命令 `mvn spring-boot:run` 启动后端
  - 默认端口：`8080`

- **前端运行**
  - 进入 `video-frontend` 目录
  - 安装依赖：`npm install`
  - 启动开发服务器：`npm run dev`
  - 访问：`http://localhost:5173`（或 Vite 输出的端口）

---

## 七、简历与面试展示亮点（撰写时可用）

- **完整技术闭环**：从需求分析、数据库设计、接口设计到前后端实现与联调。
- **技术栈贴合企业环境**：Vue + Spring Boot + MySQL 是大量公司在用的组合。
- **项目亮点示例**：
  - 自定义 JWT 登录鉴权和前端登录态管理
  - 视频上传与静态资源访问的简单实现
  - 使用 UI 组件库打造了接近真实产品的界面

在项目开发完成后，可以在此 README 中补充：
- 项目截图
- 核心模块说明
- 可以展示给面试官的 Demo 视频链接或在线部署地址。

---

## 下一步开发指引（从这里开始）

1. 在工程根目录下创建前端项目 `video-frontend`（Vite + Vue 3）。  
2. 在前端项目中搭建基础路由和布局（导航栏 + 主内容区）。  
3. 实现首页视频列表、登录/注册页、视频详情页等静态页面。  
4. 等前端静态界面完成后，再开始搭建 Spring Boot 后端工程。


