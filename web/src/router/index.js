import { createRouter, createWebHistory } from 'vue-router'
import PKindexView from '../views/pk/PKindexView'
import RecordindexView from '../views/record/RecordindexView'
import RanklistindexView from '../views/ranklist/RanklistindexView'
import UserBotindexView from '../views/user/bot/UserBotindexView'
import NotFound from '../views/error/NotFound'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'


const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/"
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PKindexView,
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordindexView,
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RanklistindexView,
  },
  {
    path: "/user/bot",
    name: "user_bot_index",
    component: UserBotindexView,
  },

  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
  },
 

  {
    path: "/404/",
    name: "404_index",
    component: NotFound,
  },
  // 遵循从上往下匹配，当输入非法路径时候 返回404
  
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
