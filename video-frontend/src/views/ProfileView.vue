<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../store/auth'
import { getProfileApi } from '../api/auth'

const router = useRouter()
const { currentUser } = useAuthStore()

const user = ref({
  id: null,
  username: '',
  nickname: '',
  email: '',
  avatarUrl: '',
  bio: '',
})

const loading = ref(false)

onMounted(async () => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  loading.value = true
  try {
    const { data } = await getProfileApi(currentUser.value.id)
    user.value = {
      id: data.id,
      username: data.username,
      nickname: data.nickname || data.username,
      email: data.email,
      avatarUrl: data.avatarUrl || '',
      bio: data.bio || '',
    }
  } catch (e) {
    ElMessage.error('获取个人信息失败，请稍后重试')
  } finally {
    loading.value = false
  }
})

const handleSave = () => {
  // 暂时只在前端修改并打印，后续接入后端更新接口
  console.log('save user profile (todo: 调用后端接口)', user.value)
  ElMessage.success('前端已更新，保存到后端功能稍后实现')
}
</script>

<template>
  <div class="profile-page">
    <div class="profile-card">
      <h2 class="title">个人中心</h2>
      <p class="subtitle">当前已接入后端获取用户信息，后续会支持修改并保存到后端</p>

      <div class="content">
        <div class="avatar-area">
          <div class="avatar">
            {{ user.nickname?.[0] || 'U' }}
          </div>
          <div class="avatar-tip">后续可以扩展为上传头像功能</div>
        </div>

        <div class="form-area">
          <el-form :model="user" label-width="80px" :disabled="loading">
            <el-form-item label="用户名">
              <el-input v-model="user.username" disabled />
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="user.nickname" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="user.email" />
            </el-form-item>
            <el-form-item label="简介">
              <el-input
                v-model="user.bio"
                type="textarea"
                :rows="4"
                placeholder="写一点你的学习方向、技术栈等"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave">保存（暂未接入后端）</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-page {
  max-width: 900px;
  margin: 0 auto;
}

.profile-card {
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

.content {
  display: grid;
  grid-template-columns: 1.1fr 2fr;
  gap: 24px;
}

.avatar-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 96px;
  height: 96px;
  border-radius: 50%;
  background: linear-gradient(135deg, #66b1ff, #409eff);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  color: #ffffff;
  font-weight: 700;
}

.avatar-tip {
  font-size: 12px;
  color: #909399;
}

@media (max-width: 768px) {
  .content {
    grid-template-columns: 1fr;
  }
}
</style>


