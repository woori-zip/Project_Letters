// HomeView.vue
<template>
<div class="home">
    <h1>📬</h1>
    <div v-if="isAuthenticated">
      <p>Welcome!</p>
      <button @click="logout">Logout</button>
    </div>
    <div v-else>
      <Login />
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import Login from '../components/LoginView.vue'

export default {
  name: 'HomeView',
  components: {
    Login
  },
  computed: {
    ...mapGetters(['isAuthenticated'])
  },
  methods: {
    ...mapActions(['logout'])
  }
}
</script>
