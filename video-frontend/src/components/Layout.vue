<template>
  <div class="layout">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="header-left">
        <!-- Logo 区域 -->
        <div class="logo" @click="navigateToHome">VideoSite</div>
        <!-- 导航菜单 -->
        <nav class="nav">
          <a
              @click.prevent="navigateToHome"
              :class="{ active: route.path === '/' }"
          >
            首页
          </a>
          <a
              @click.prevent="navigateToUpload"
              :class="{ active: route.path === '/upload' }"
          >
            上传视频
          </a>
        </nav>
      </div>

      <div class="header-right">
        <!-- 已登录用户 -->
        <template v-if="isLoggedIn">
          <el-dropdown trigger="click">
            <span class="user-trigger">
              <span class="avatar-small">
                {{ getAvatarText }}
              </span>
              <span class="nickname">{{ getUserName }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="navigateToProfile">个人中心</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>

        <!-- 未登录用户 -->
        <template v-else>
          <el-button type="primary" @click="navigateToLogin">登录 / 注册</el-button>
        </template>
      </div>
    </header>

    <!-- 主内容区：渲染子路由页面 -->
    <main class="main">
      <router-view />
    </main>

    <!-- 底部 -->
    <footer class="footer">
      <span>© 2025 My VideoSite · 用于个人简历展示</span>
    </footer>
  </div>
</template>

<script setup>
// 1. 导入依赖
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../store/auth'

// 2. 初始化实例
const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const { currentUser, clearAuth } = authStore

// 3. 计算属性
const isLoggedIn = computed(() => !!currentUser.value)

// 头像显示的文字
const getAvatarText = computed(() => {
  if (!currentUser.value) return 'U'
  return currentUser.value.nickname?.[0] || currentUser.value.username?.[0] || 'U'
})

// 用户名显示
const getUserName = computed(() => {
  if (!currentUser.value) return ''
  return currentUser.value.nickname || currentUser.value.username
})

// 4. 导航方法
const navigateToHome = () => router.push('/')
const navigateToUpload = () => router.push('/upload')
const navigateToLogin = () => router.push('/login')
const navigateToProfile = () => router.push('/profile')

// 5. 退出登录方法
const handleLogout = () => {
  clearAuth()
  navigateToLogin()
}
</script>

<style scoped>
/* 全局布局样式 */
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

/* 顶部导航栏 */
.header {
  height: 64px;
  padding: 0 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  background-color: #ffffff;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 24px;
}

.logo {
  font-size: 20px;
  font-weight: 700;
  color: #409eff;
  cursor: pointer;
}

.nav {
  display: flex;
  gap: 16px;
}

.nav a {
  cursor: pointer;
  color: #606266;
  font-size: 14px;
  text-decoration: none;
  padding-bottom: 4px;
  transition: color 0.2s ease;
}

.nav a.active {
  color: #409eff;
  border-bottom: 2px solid #409eff;
}

/* 右侧用户区域 */
.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-trigger {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.avatar-small {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, #66b1ff, #409eff);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 14px;
  font-weight: 600;
}

.nickname {
  font-size: 14px;
  color: #303133;
}

/* 主内容区 */
.main {
  flex: 1;
  padding: 24px 40px 40px;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
}

/* 底部 */
.footer {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #909399;
  background-color: #ffffff;
  border-top: 1px solid #ebeef5;
}

/* 响应式优化 */
@media (max-width: 768px) {
  .header {
    padding: 0 20px;
  }

  .nav {
    gap: 12px;
  }

  .nickname {
    display: none;
  }

  .main {
    padding: 24px 20px 40px;
  }
}

@media (max-width: 480px) {
  .header {
    padding: 0 12px;
  }

  .logo {
    font-size: 16px;
  }

  .nav {
    gap: 8px;
  }
}
</style>