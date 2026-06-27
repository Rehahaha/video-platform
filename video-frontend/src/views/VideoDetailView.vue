<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { fetchVideoDetail } from '../api/video'

const route = useRoute()
const videoId = route.params.id

const MEDIA_BASE_URL = 'http://localhost:8080'

const video = ref(null)
const loading = ref(false)

const loadDetail = async () => {
  loading.value = true
  try {
    const { data } = await fetchVideoDetail(videoId)
    video.value = data
  } catch (e) {
    ElMessage.error('获取视频详情失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadDetail()
})
</script>

<template>
  <div class="detail-page">
    <el-skeleton :loading="loading || !video" animated :rows="4" style="width: 100%">
      <template #template>
        <div class="player-section">
          <div class="video-player skeleton-block"></div>
          <div class="skeleton-line long"></div>
          <div class="skeleton-line short"></div>
          <div class="skeleton-line long"></div>
        </div>
      </template>
      <template #default>
        <div class="player-section" v-if="video">
          <video
            class="video-player"
            controls
            :src="video.videoUrl ? MEDIA_BASE_URL + video.videoUrl : ''"
          ></video>
          <h2 class="title">{{ video.title }}</h2>
          <div class="meta">
            <span class="author">用户 {{ video.userId }}</span>
            <span class="dot">·</span>
            <span>{{ video.viewCount ?? 0 }} 次观看</span>
            <span class="dot">·</span>
            <span>{{ video.createdAt }}</span>
          </div>
          <p class="description">
            {{ video.description || '这个视频暂时还没有简介。' }}
          </p>
        </div>
      </template>
    </el-skeleton>

    <aside class="side-section">
      <h3>推荐视频（静态占位）</h3>
      <ul class="recommend-list">
        <li v-for="i in 5" :key="i" class="recommend-item">
          <div class="thumb"></div>
          <div class="info">
            <div class="name">示例推荐视频 {{ i }}</div>
            <div class="meta">1234 次观看</div>
          </div>
        </li>
      </ul>
    </aside>
  </div>
</template>

<style scoped>
.detail-page {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 3fr 1.3fr;
  gap: 24px;
}

.player-section {
  background-color: #ffffff;
  padding: 16px 16px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.video-player {
  width: 100%;
  border-radius: 8px;
  background-color: #000;
}

.title {
  margin: 12px 0 4px;
}

.meta {
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 6px;
}

.author {
  font-weight: 500;
  color: #606266;
}

.dot {
  font-size: 10px;
}

.description {
  margin-top: 12px;
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
}

.side-section {
  align-self: flex-start;
}

.side-section h3 {
  margin: 0 0 12px;
  font-size: 15px;
}

.recommend-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.recommend-item {
  display: flex;
  gap: 8px;
  cursor: pointer;
}

.thumb {
  width: 120px;
  height: 70px;
  border-radius: 6px;
  background: linear-gradient(135deg, #a5d6ff, #409eff);
}

.info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.name {
  font-size: 13px;
  font-weight: 500;
  margin-bottom: 4px;
}

.meta {
  font-size: 12px;
  color: #a0a0a0;
}

@media (max-width: 960px) {
  .detail-page {
    grid-template-columns: 1fr;
  }
}
</style>


