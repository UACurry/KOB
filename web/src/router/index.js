import { createRouter, createWebHistory } from 'vue-router'
import PKindexView from '../views/pk/PKindexView'
import RecordindexView from '../views/record/RecordindexView'
import RecordContentView from '../views/record/RecordContentView'
import RanklistindexView from '../views/ranklist/RanklistindexView'
import UserBotindexView from '../views/user/bot/UserBotindexView'
import NotFound from '../views/error/NotFound'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'
import CakeView from '../views/birthday/CakeView'

import store from '../store/index'

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/",
    // 修改前端是否需要授权
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PKindexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordindexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/:recordId/",
    name: "record_content",
    component: RecordContentView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RanklistindexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/bot",
    name: "user_bot_index",
    component: UserBotindexView,
    meta: {
      requestAuth: true,
    }
  },

  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,
    }
  },

  {
    path: "/lwy/goose/birthday/",
    name: "cake",
    component: CakeView,
    meta: {
      requestAuth: true,
    }
  },
 

  {
    path: "/404/",
    name: "404_index",
    component: NotFound,
    meta: {
      requestAuth: false,
    }
  },
  // 遵循从上往下匹配，当输入非法路径时候 返回404
  
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})



// 调用router之前执行 to 表示当前要去的页面
router.beforeEach((to, from, next) => {
  // 要去的前端页面 进行是否有权限判断
  if (to.meta.requestAuth && !store.state.user.is_login) {
    next({name: "user_account_login"});
  } else {
    next();
  }
})


export default router
