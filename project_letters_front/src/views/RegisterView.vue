<template>
  <div class="container">
    <h1>Register</h1>
    <form @submit.prevent="register">

      <div v-if="errorMessage" class="error">
        {{ errorMessage }}
      </div>
      <div v-if="successMessage" class="success">
        {{ successMessage }}
      </div>

      <div class="form-group">
        <label for="name">Name</label>
        <input type="text" v-model="name" required>
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <input type="email" v-model="email" required>
      </div>
      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" v-model="password" required>
      </div>
      <div>
        <label for="confirmPassword">Confirm Password</label>
        <input type="password" v-model="confirmPassword" required>
      </div>
      <div class="form-group">
        <label for="gender">Gender</label>
        <select v-model="gender" required>
          <option value="male">Male</option>
          <option value="female">Female</option>
          <option value="other">Other</option>
        </select>
      </div>
      <div class="form-group">
        <label for="age">Age</label>
        <input type="number" v-model="age" required>
      </div>
      <button type="submit">Register</button>
    </form>
  </div>
</template>

<script>
import '../assets/css/style.css'
import axios from 'axios'

export default {
  data () {
    return {
      name: '',
      email: '',
      password: '',
      confirmPassword: '',
      gender: 'female',
      age: null,
      errorMessage: '',
      successMessage: ''
    }
  },
  methods: {
    async register () {
      this.errorMessage = ''
      this.successMessage = ''
      const userDTO = {
        name: this.name,
        email: this.email,
        password: this.password,
        confirmPassword: this.confirmPassword,
        gender: this.gender,
        age: this.age
      }
      try {
        const response = await axios.post('/api/register', userDTO)

        this.successMessage = response.data
        // 회원가입 성공 시 로그인 페이지로 리다이렉션
        setTimeout(() => {
          this.$router.push('/')
        }, 2000) // 2초 후 리다이렉션
      } catch (error) {
        if (error.response) {
          this.errorMessage = error.response.data
        } else {
          this.errorMessage = '회원가입 실패. 관리자에게 문의하세요.'
        }
      }
    }
  }
}
</script>
