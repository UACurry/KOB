import { createStore } from 'vuex'
import ModuleUser from './user'

// Vuex持久化插件(vuex-persistedstate)解决刷新数据消失的问题
import createPersistedState from "vuex-persistedstate"

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
  },


  // / Vuex持久化插件(vuex-persistedstate)解决刷新数据消失的问题
  // 下面的写法，会默认持久化所有state
  plugins: [createPersistedState({
    // 存储在 localStorage 的数据可以长期保留
    // 当页面被关闭时，存储在 sessionStorage 的数据会被清除
    storage: window.sessionStorage,
    // 只想存储指定的 state
    reducer(val) {
      return {
        user: val.user,
      }
    }

  })]
})
