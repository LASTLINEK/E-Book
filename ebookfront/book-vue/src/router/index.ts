import Vue from 'vue'
import VueRouter, { RawLocation }  from 'vue-router'
import Login from '../views/Login/index.vue'
import Register from '../views/Register/index.vue'
import store from '@/store'
import { generateRoutes } from './permission'

/**
 * 重写路由的push方法
 */
const routerPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location: RawLocation) {
  return (<any>(routerPush.call(this, location))).catch((error: any)=> error)
}

Vue.use(VueRouter)

// 公共路由，全部角色均具有该部分路由权限
const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    hidden: true,
    component: Login
  },
  {
    path: '/register',
    name: 'register',
    hidden: true,
    component: Register
  },
  {
    path: '/books',
    name: 'books',
    hidden: true,
    component: ()=>import('../views/BookManagement/index.vue')
  },
  {
    path: '/books/:id',
    name: 'books',
    hidden: true,
    component: ()=>import('../views/Book/index.vue')
  }
]

const asyncRoutes = []

const asyncRoutes_project = [
]

const router = new VueRouter({
  // mode: 'history',
  base: process.env.BASE_URL,
  routes
})


// var flag = true  // 页面刷新标志
// router.beforeEach((to, from, next) => {
//   if (to.path == '/login' || to.path == '/register') {
//     sessionStorage.removeItem('name');
//     sessionStorage.removeItem('pass');
//     store.dispatch('app/ResetRouter');  // 全局路由tag重置
//     flag = true
//   }
//   let admin = <String>sessionStorage.getItem('name');
//   if (!admin && to.path !== '/login') {
//     if (to.path == '/register') {
//       return next()
//     }
//     next({ path: '/login' })
//   } else {
//     // 该处进行动态路由生成（当页面刷新时或首次进入该系统时，生成路由）
//     if (flag && to.path !== '/login') {
//       flag = false
//       router['options'].routes = routes
//       //分配路由
//       let generateAsyncRoutes = generateRoutes(asyncRoutes, <string>sessionStorage.getItem('role'))  // 根据登录角色生成动态路由
//       router.addRoutes(generateAsyncRoutes)
//       router['options'].routes = router['options'].routes.concat(generateAsyncRoutes)
//
//       let generateAsyncRoutes_project = generateRoutes(asyncRoutes_project, <string>sessionStorage.getItem('role'))  // 根据登录角色生成动态路由
//       router.addRoutes(generateAsyncRoutes_project)
//       router['options'].routes = router['options'].routes.concat(generateAsyncRoutes_project)
//       next({ ...to, replace: true })  // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
//     } else {
//       // 以下部分为向全局变量中添加路由历史，形成tag标签
//       if (to.path !== '/login') {
//         let newPath = {
//           name: to.name,
//           path: to.fullPath
//         }
//         store.dispatch("app/AddRouter", newPath)  // 向全局变量中添加路由
//       }
//       next()
//     }
//   }
// });

export default router
