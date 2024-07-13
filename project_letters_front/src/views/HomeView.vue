<template>
  <div class="home">
    <h1>📬</h1>
    <Login />
  </div>
</template>

<script>
// @ is an alias to /src
import Login from '../components/LoginView.vue'

export default {
  name: 'HomeView',
  components: {
    Login
  }
}
</script>
