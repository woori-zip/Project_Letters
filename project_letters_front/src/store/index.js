import { createStore } from 'vuex'
import axios from 'axios'

export default createStore({
  state: {
    token: localStorage.getItem('token') || null,
    isAuthenticated: !!localStorage.getItem('token')
  },
  getters: {
    isAuthenticated: state => state.isAuthenticated
  },
  mutations: {
    setToken (state, token) {
      state.token = token
      state.isAuthencicated = !!token
      if (token) {
        localStorage.setItem('token', token)
        console.log('isAuthenticated:', state.isAuthenticated)
      } else {
        localStorage.removeItem('token')
      }
    }
  },
  actions: {
    async login ({ commit }, credentials) {
      try {
        const response = await axios.post('/api/login', credentials)
        commit('setToken', response.data.token)
        location.reload()
      } catch (error) {
        commit('setToken', null)
        throw error
      }
    },
    logout ({ commit }) {
      commit('setToken', null)
      location.reload()
    }
  },
  modules: {
  }
})
