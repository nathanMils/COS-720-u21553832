import { createRouter, createWebHistory } from 'vue-router'
import { HomeView, LoginView, WelcomeView, DashboardView } from '@/views'
import { AuthStore } from '@/stores'
import ApplyView from '@/views/ApplyView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      children: [
        {
          path: '/dashboard',
          name: 'dashboard',
          component: DashboardView
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/apply',
      name: 'apply',
      component: ApplyView
    },
    {
      path: '/welcome',
      name: '/login',
      component: WelcomeView
    }
  ]
})

router.beforeEach(async (to) => {
  const publicPages = ['/login', '/welcome']
  const authRequired = !publicPages.includes(to.path)
  const auth = AuthStore()

  if (authRequired && !auth.user) {
    auth.returnUrl = to.fullPath
    return '/welcome'
  }
})

export default router
