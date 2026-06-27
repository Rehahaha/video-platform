<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { uploadVideoApi } from '../api/video'
import { useAuthStore } from '../store/auth'

const router = useRouter()
const { currentUser } = useAuthStore()

const form = ref({
  title: '',
  description: '',
  tags: '',
  coverFile: null,
  videoFile: null,
})

const uploading = ref(false)

const handleCoverChange = (file) => {
  form.value.coverFile = file.raw
}

const handleVideoChange = (file) => {
  form.value.videoFile = file.raw
}

const handleSubmit = async () => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录后再上传视频')
    router.push('/login')
    return
  }
  if (!form.value.title) {
    ElMessage.warning('请输入视频标题')
    return
  }
  if (!form.value.videoFile) {
    ElMessage.warning('请选择要上传的视频文件')
    return
  }

  const fd = new FormData()
  fd.append('userId', currentUser.value.id)
  fd.append('title', form.value.title)
  if (form.value.description) fd.append('description', form.value.description)
  if (form.value.coverFile) fd.append('coverFile', form.value.coverFile)
  fd.append('videoFile', form.value.videoFile)

  uploading.value = true
  try {
    const { data } = await uploadVideoApi(fd)
    ElMessage.success('上传成功！')
    router.push(`/video/${data.id}`)
  } catch (e) {
    ElMessage.error('上传失败，请稍后重试')
  } finally {
    uploading.value = false
  }
}
</script>

<template>
  <div class="upload-page">
    <div class="upload-card">
      <h2 class="title">上传视频</h2>
      <p class="subtitle">已接入后端接口，当前登录用户将作为视频的上传者</p>

      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入视频标题" />
        </el-form-item>

        <el-form-item label="简介">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="简单介绍一下你的视频内容"
          />
        </el-form-item>

        <el-form-item label="标签">
          <el-input v-model="form.tags" placeholder="例如：前端, Java, MySQL" />
        </el-form-item>

        <el-form-item label="封面图">
          <el-upload
            class="upload-block"
            :auto-upload="false"
            :on-change="handleCoverChange"
            accept="image/*"
          >
            <el-button type="primary">选择封面图片</el-button>
            <template #tip>
              <div class="el-upload__tip">建议 16:9 比例的图片，JPG/PNG 格式</div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item label="视频文件">
          <el-upload
            class="upload-block"
            :auto-upload="false"
            :on-change="handleVideoChange"
            accept="video/*"
          >
            <el-button type="primary">选择视频文件</el-button>
            <template #tip>
              <div class="el-upload__tip">支持常见视频格式，后续会加入大小和时长限制</div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="uploading" @click="handleSubmit">
            提交上传
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.upload-page {
  max-width: 900px;
  margin: 0 auto;
}

.upload-card {
  background-color: #ffffff;
  padding: 24px 28px 20px;
  border-radius: 10px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
}

.title {
  margin: 0;
  font-size: 20px;
}

.subtitle {
  margin: 6px 0 18px;
  font-size: 13px;
  color: #909399;
}

.upload-block {
  width: 100%;
}
</style>


