import { createStore } from 'vuex'
import axios from 'axios'

export default createStore({
  state: {
    token: localStorage.getItem('token') || null,
    isAuthenticated: !!localStorage.getItem('token'),
    externalId: localStorage.getItem('externalId') || null
  },
  getters: {
    isAuthenticated: state => state.isAuthenticated
  },
  mutations: {
    setToken (state, { token, externalId }) {
      state.token = token
      state.isAuthenticated = !!token
      state.externalId = externalId
      if (token) {
        localStorage.setItem('token', token)
        localStorage.setItem('externalId', externalId)
      } else {
        localStorage.removeItem('token')
        localStorage.removeItem('externalId')
        console.log('Token removed from localStorage')
      }
    }
  },
  actions: {
    async login ({ commit }, credentials) {
      try {
        const response = await axios.post('/api/login', credentials)
        commit('setToken', { token: response.data.token, externalId: response.data.externalId })
        location.reload()
      } catch (error) {
        commit('setToken', { token: null, externalId: null })
        throw error
      }
    },
    async kakaoLogin ({ commit }, code) {
      try {
        const response = await axios.get(`/auth/kakao/callback?code=${code}`)
        commit('setToken', { token: response.data.token, externalId: response.data.externalId })
        console.log('Token stored:', response.data.token) // 토큰 저장 확인 로그
        console.log('externalId stored:', response.data.externalId) // 외부 id 확인 로그
        return true // 성공 시 true 반환
      } catch (error) {
        commit('setToken', { token: null, externalId: null })
        commit('setToken', { token: null, externalId: null })
        location.reload()
      }
    },
    async logout ({ commit, state }) {
      if (state.externalId.includes('@kakao')) {
        console.log('카카오 로그인 사용자:', state.externalId)
        try {
          const response = await axios.get(`/auth/kakao/logout?token=${state.token}`)
          console.log(response.data)
        } catch (error) {
          console.error('Kakao logout failed:', error)
        }
      }
      commit('setToken', { token: null, externalId: null })
      location.reload()
    }
  },
  modules: {
  }
})
