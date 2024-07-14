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
        <input type="password" v-model="password" placeholder="Password" required>
      </div>
      <p>
        New here? <router-link to="/register">Sign up here!</router-link>
      </p>
      <button type="submit">Login</button>
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
      successMessage: ''
    }
  },
  methods: {
    ...mapActions(['login']),
    async performLogin () {
      this.errorMessage = ''
      try {
        await this.login({ email: this.email, password: this.password })
        // this.$router.push('/')
        location.reload()
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
