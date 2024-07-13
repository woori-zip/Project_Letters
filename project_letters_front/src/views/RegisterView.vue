<template>
  <div class="container">
    <h1>Register</h1>
    <form @submit.prevent="register">
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
      gender: 'male',
      age: ''
    }
  },
  methods: {
    async register () {
      try {
        const response = await axios.post('/api/register', {
          name: this.name,
          email: this.email,
          password: this.password,
          confirmPassword: this.confirmPassword,
          gender: this.gender,
          age: this.age
        })
        console.log(response.data)
        alert(response.data)
      } catch (error) {
        console.error('Registration failed', error)
        alert('Registration failed')
      }
    }
  }
}
</script>
