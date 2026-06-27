import axios from 'axios'

const http = axios.create({
  baseURL: 'http://localhost:8080', // 如果后端端口变化，这里一起改
  timeout: 10000,
})

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('auth_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

http.interceptors.response.use(
  (response) => response,
  (error) => {
    // 这里可以根据实际需求统一处理错误，例如 401 时跳转登录等
    return Promise.reject(error)
  },
)

export default http




