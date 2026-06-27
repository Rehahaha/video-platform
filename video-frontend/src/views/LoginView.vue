<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { loginApi, registerApi } from '../api/auth'
import { useAuthStore } from '../store/auth'

const router = useRouter()
const { setAuth } = useAuthStore()

const activeTab = ref('login')

const loginForm = ref({
  email: '',
  password: '',
})

const registerForm = ref({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
})

const loadingLogin = ref(false)
const loadingRegister = ref(false)

const handleLogin = async () => {
  if (!loginForm.value.email || !loginForm.value.password) {
    ElMessage.warning('请输入邮箱和密码')
    return
  }
  loadingLogin.value = true
  try {
    const { data } = await loginApi({
      email: loginForm.value.email,
      password: loginForm.value.password,
    })
    setAuth(data.token, data.user)
    ElMessage.success('登录成功')
    router.push('/')
  } catch (e) {
    const msg = e?.response?.data?.message || '登录失败，请检查邮箱和密码'
    ElMessage.error(msg)
  } finally {
    loadingLogin.value = false
  }
}

const handleRegister = async () => {
  if (!registerForm.value.username || !registerForm.value.email || !registerForm.value.password) {
    ElMessage.warning('请完整填写用户名、邮箱和密码')
    return
  }
  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }
  loadingRegister.value = true
  try {
    await registerApi({
      username: registerForm.value.username,
      email: registerForm.value.email,
      password: registerForm.value.password,
    })
    ElMessage.success('注册成功，请使用邮箱和密码登录')
    activeTab.value = 'login'
  } catch (e) {
    const msg = e?.response?.data?.message || '注册失败，请稍后重试'
    ElMessage.error(msg)
  } finally {
    loadingRegister.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <h2 class="title">欢迎来到 VideoSite</h2>
      <p class="subtitle">请先完成登录/注册，后续会接入真实后端接口</p>

      <el-tabs v-model="activeTab" class="login-tabs">
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" label-width="80px">
            <el-form-item label="邮箱">
              <el-input v-model="loginForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="loginForm.password" show-password placeholder="请输入密码" />
            </el-form-item>
            <el-form-item>
              <el-button
                  type="primary"
                  class="full-width"
                  :loading="loadingLogin"
                  @click="handleLogin"
              >
                登录
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="注册" name="register">
          <el-form :model="registerForm" label-width="80px">
            <el-form-item label="用户名">
              <el-input v-model="registerForm.username" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="registerForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="registerForm.password" show-password placeholder="请输入密码" />
            </el-form-item>
            <el-form-item label="确认密码">
              <el-input
                  v-model="registerForm.confirmPassword"
                  show-password
                  placeholder="请再次输入密码"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                  type="primary"
                  class="full-width"
                  :loading="loadingRegister"
                  @click="handleRegister"
              >
                注册
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: calc(100vh - 64px - 56px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-card {
  width: 420px;
  background-color: #ffffff;
  padding: 32px 36px 28px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.title {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  text-align: center;
}

.subtitle {
  margin: 8px 0 24px;
  font-size: 13px;
  color: #909399;
  text-align: center;
}

.login-tabs {
  margin-top: 8px;
}

.full-width {
  width: 100%;
}
</style>


