import { ref } from 'vue'

const currentUser = ref(null)
const token = ref(null)

function loadAuthFromStorage() {
  const savedToken = localStorage.getItem('auth_token')
  const savedUser = localStorage.getItem('auth_user')
  if (savedToken && savedUser) {
    token.value = savedToken
    try {
      currentUser.value = JSON.parse(savedUser)
    } catch {
      currentUser.value = null
    }
  }
}

export function useAuthStore() {
  if (!token.value && !currentUser.value) {
    loadAuthFromStorage()
  }

  const setAuth = (newToken, user) => {
    token.value = newToken
    currentUser.value = user
    localStorage.setItem('auth_token', newToken)
    localStorage.setItem('auth_user', JSON.stringify(user))
  }

  const clearAuth = () => {
    token.value = null
    currentUser.value = null
    localStorage.removeItem('auth_token')
    localStorage.removeItem('auth_user')
  }

  return {
    token,
    currentUser,
    setAuth,
    clearAuth,
  }
}



