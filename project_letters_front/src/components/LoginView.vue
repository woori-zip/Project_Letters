<template>
  <div class="container">
    <h2>Login</h2>
    <form @submit.prevent="login">
      <div class="form-group">
        <label for="email">Email</label>
        <input type="email" v-model="email" required>
      </div>
      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" v-model="password" required>
      </div>
      <p>
        New here? <router-link to="/register">Sign up here!</router-link>
      </p>
      <button type="submit">Login</button>
    </form>
    <div>
      <p style="background: #405D72; color: white;">#405D72</p>
      <p style="background: #758694; color: white;">#758694</p>
      <p style="background: #F7E7DC;">#F7E7DC</p>
      <p style="background: #FFF8F3;">#FFF8F3</p>
    </div>
  </div>
</template>

<script>
import '../assets/css/style.css'
import axios from 'axios'

export default {
  data () {
    return {
      email: '',
      password: ''
    }
  },
  methods: {
    async login () {
      try {
        const response = await axios.post('/api/login', {
          email: this.email,
          password: this.password
        })
        console.log('Login successful', response.data)
        // 로그인 성공 시 처리 (예: 토큰 저장, 페이지 이동 등)
      } catch (error) {
        console.error('Login failed', error)
      }
    }
  }
}
</script>
