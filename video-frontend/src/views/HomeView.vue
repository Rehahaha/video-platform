<script setup>
// 导入依赖
import { onMounted, ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
// 业务API
import { fetchVideos } from '../api/video'

// 常量定义（集中管理，便于修改）
const MEDIA_BASE_URL = 'http://localhost:8080'
const DEFAULT_PAGE_SIZE = 12 // 默认每页加载视频数量
const DEFAULT_PAGE_NUM = 0 // 默认页码
const PLACEHOLDER_COVER = 'https://via.placeholder.com/400x225?text=No+Cover' // 默认封面图

// 响应式状态
const videos = ref([]) // 视频列表数据
const loading = ref(false) // 加载状态

// 路由实例（替代模板中的$router，更规范）
const router = useRouter()

/**
 * 加载视频列表数据
 * @param {number} page - 页码
 * @param {number} size - 每页数量
 */
const loadVideos = async (page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE) => {
  loading.value = true
  try {
    // 调用接口获取视频列表
    const { data } = await fetchVideos({ page, size })
    // 确保数据结构正确，避免空值
    videos.value = data.content || []
  } catch (error) {
    // 打印错误信息，便于开发调试
    console.error('视频列表加载失败：', error)
    ElMessage.error('加载视频列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

/**
 * 跳转到视频详情页
 * @param {number} videoId - 视频ID
 */
const goToVideoDetail = (videoId) => {
  router.push(`/video/${videoId}`)
}

// 组件挂载后加载数据
onMounted(() => {
  loadVideos()
})

// 计算属性：判断是否为空数据（简化模板逻辑）
const isVideoListEmpty = computed(() => !loading.value && videos.value.length === 0)
</script>

<template>
  <div class="home">
    <!-- 首页头部 -->
    <div class="home-header">
      <h2>推荐视频</h2>
      <span class="sub-title">已接入后端接口，展示最新上传的视频</span>
    </div>

    <!-- 骨架屏：加载中占位 -->
    <el-skeleton :loading="loading" animated :rows="4">
      <template #template>
        <div class="video-grid">
          <div v-for="i in 8" :key="i" class="video-card skeleton-card">
            <div class="cover-wrapper skeleton-block"></div>
            <div class="info">
              <div class="skeleton-line short"></div>
              <div class="skeleton-line long"></div>
            </div>
          </div>
        </div>
      </template>

      <!-- 视频列表：加载完成后展示 -->
      <template #default>
        <div class="video-grid">
          <!-- 视频卡片 -->
          <div
              v-for="video in videos"
              :key="video.id"
              class="video-card"
              @click="goToVideoDetail(video.id)"
          >
            <div class="cover-wrapper">
              <!-- 视频封面图：优先使用接口返回的封面，否则用占位图 -->
              <img
                  :src="video.coverUrl ? `${MEDIA_BASE_URL}${video.coverUrl}` : PLACEHOLDER_COVER"
                  :alt="video.title"
                  loading="lazy"
              />
            </div>
            <div class="info">
              <div class="title" :title="video.title">{{ video.title }}</div>
              <div class="meta">
                <span class="author">用户 {{ video.userId }}</span>
                <span class="views">{{ video.viewCount ?? 0 }} 次观看</span>
              </div>
            </div>
          </div>

          <!-- 空数据提示 -->
          <div v-if="isVideoListEmpty" class="empty-tip">
            还没有视频，快去上传一个吧～
          </div>
        </div>
      </template>
    </el-skeleton>
  </div>
</template>

<style scoped>
/* 首页容器 */
.home {
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

/* 首页头部 */
.home-header {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-bottom: 16px;
}

.home-header h2 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.sub-title {
  font-size: 13px;
  color: #909399;
}

/* 视频网格布局 */
.video-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
  width: 100%;
}

/* 视频卡片 */
.video-card {
  background-color: #ffffff;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  display: flex;
  flex-direction: column;
}

/* 卡片悬浮效果 */
.video-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
}

/* 骨架屏卡片样式（去除阴影，避免干扰） */
.skeleton-card {
  box-shadow: none;
}

/* 骨架屏块样式 */
.skeleton-block {
  background-color: #f2f2f2;
  width: 100%;
  height: 100%;
}

/* 骨架屏行样式 */
.skeleton-line {
  height: 10px;
  background-color: #f2f2f2;
  border-radius: 4px;
  margin-bottom: 6px;
}

.skeleton-line.short {
  width: 60%;
}

.skeleton-line.long {
  width: 80%;
}

/* 视频封面容器（16:9比例） */
.cover-wrapper {
  width: 100%;
  padding-top: 56.25%;
  position: relative;
  overflow: hidden;
}

/* 视频封面图 */
.cover-wrapper img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.2s ease;
}

/* 封面悬浮放大效果 */
.video-card:hover .cover-wrapper img {
  transform: scale(1.05);
}

/* 视频信息区域 */
.info {
  padding: 10px 12px 12px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 视频标题 */
.title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 视频元信息（作者/观看数） */
.meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
  margin-top: auto;
}

.author {
  max-width: 70%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 空数据提示 */
.empty-tip {
  grid-column: 1 / -1;
  text-align: center;
  font-size: 13px;
  color: #909399;
  margin-top: 20px;
  padding: 20px 0;
}

/* 响应式优化：小屏幕适配 */
@media (max-width: 768px) {
  .video-grid {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
    gap: 12px;
  }

  .home {
    padding: 0 10px;
  }
}

@media (max-width: 480px) {
  .video-grid {
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
    gap: 8px;
  }
}
</style>