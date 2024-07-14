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
    ...mapActions(['logout', 'kakaoLogin']),
    async checkToken () {
      const urlParams = new URLSearchParams(window.location.search)
      const token = urlParams.get('token')
      const externalId = urlParams.get('externalId')
      if (token) {
        this.$store.commit('setToken', { token, externalId })
        window.history.replaceState({}, document.title, '/') // URL에서 토큰 제거
        this.$router.replace({ path: '/' }) // 홈 페이지로 리디렉트
      } else {
        console.log('No token found')
      }
    }
  },
  mounted () {
    this.checkToken()
  }
}
</script>
