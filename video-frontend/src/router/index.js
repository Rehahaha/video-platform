import { createRouter, createWebHistory } from 'vue-router'

// 懒加载页面组件（优化性能，推荐）
const Layout = () => import('../components/Layout.vue')
const HomeView = () => import('../views/HomeView.vue')
const LoginView = () => import('../views/LoginView.vue')
const VideoDetailView = () => import('../views/VideoDetailView.vue')
const UploadView = () => import('../views/UploadView.vue')
const ProfileView = () => import('../views/ProfileView.vue')

const routes = [
    // 不需要导航栏的页面：单独渲染
    {
        path: '/login',
        name: 'login',
        component: LoginView
    },
    // 需要导航栏的页面：以 Layout 为父组件（仅挂载一次）
    {
        path: '/',
        component: Layout,
        children: [
            { path: '', name: 'home', component: HomeView }, // 首页（默认子路由）
            { path: 'upload', name: 'upload', component: UploadView }, // 上传视频
            { path: 'video/:id', name: 'video-detail', component: VideoDetailView }, // 视频详情
            { path: 'profile', name: 'profile', component: ProfileView } // 个人中心
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior() {
        // 路由跳转时回到页面顶部
        return { top: 0 }
    }
})

// 可选：登录权限守卫（保护需要登录的页面）
import { useAuthStore } from '../store/auth'
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()
    // 白名单：无需登录的页面
    const whiteList = ['/login']
    if (whiteList.includes(to.path)) {
        next()
    } else {
        // 未登录则跳转到登录页
        if (!authStore.currentUser) {
            next('/login')
        } else {
            next()
        }
    }
})

export default router