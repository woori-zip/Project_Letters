<template>
  <div class="container">
    <h2>Login</h2>

    <div v-if="errorMessage" class="error">
        {{ errorMessage }}
      </div>
      <div v-if="successMessage" class="success">
        {{ successMessage }}
      </div>

    <form @submit.prevent="performLogin">
      <div class="form-group">
        <input type="email" v-model="email" placeholder="Email" required>
      </div>
      <div class="form-group">
        <input type="password" v-model="password" placeholder="password" required>
      </div>
      <p>
        New here? <router-link to="/register">Sign up here!</router-link>
      </p>
      <div class="btn-container">
        <button type="submit">Login</button>
        <img src="../assets/kakao_login_medium_wide.png" :href="kakaoLoginUrl">
      </div>
    </form>

  </div>
</template>

<script>
import '../assets/css/style.css'
// import axios from 'axios'
import { mapActions } from 'vuex'

export default {
  data () {
    return {
      email: '',
      password: '',
      errorMessage: '',
      successMessage: '',
      kakaoLoginUrl: ''
    }
  },
  created () {
    // 카카오 로그인 URL 설정
    const clientId = 'fb67f53f9623c671f4ca405959464c76'
    const redirectUri = 'http://localhost:9095/auth/kakao/callback'
    this.kakaoLoginUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code&scope=profile_nickname`
  },
  methods: {
    ...mapActions(['login']),
    async performLogin () {
      this.errorMessage = ''
      try {
        await this.login({ email: this.email, password: this.password })
        this.$router.push('/') // 로그인 성공 시 홈 페이지로 리다이렉트
      } catch (error) {
        if (error.response) {
          this.errorMessage = error.response.data
        } else {
          this.errorMessage = '로그인 실패. 관리자에게 문의하세요.'
        }
      }
    }
  }
}
</script>

<style scoped>
.btn-container {
  width: 60%;
  margin:auto;
}
.btn-container button {
  width: 100%;
}

.btn-container img {
  width: 100%;
  margin: 5px auto 0 auto;
}
</style>
