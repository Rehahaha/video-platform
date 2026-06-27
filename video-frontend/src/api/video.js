import http from './http'

export function fetchVideos(params) {
  return http.get('/api/videos', { params })
}

export function fetchVideoDetail(id) {
  return http.get(`/api/videos/${id}`)
}

export function uploadVideoApi(formData) {
  return http.post('/api/videos', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}



