import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/HomeView.vue'
import Register from '../views/RegisterView.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/register', name: 'Register', component: Register },
  { path: '/auth/kakao/callback', name: 'KakaoCallback', component: Home }// 카카오 로그인 후 홈으로 리디렉트
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
