import { createStore } from 'vuex'
import ModuleUser from './user'

export default createStore({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    // 存全局用户信息
    user: ModuleUser
  }
})
