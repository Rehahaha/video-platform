import http from './http'

export function loginApi(payload) {
  return http.post('/api/auth/login', payload)
}

export function registerApi(payload) {
  return http.post('/api/auth/register', payload)
}

export function getProfileApi(userId) {
  // 当前后端是通过 query 参数 userId 获取用户信息
  return http.get('/api/auth/profile', {
    params: { userId },
  })
}



