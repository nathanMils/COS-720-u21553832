import { createRouter, createWebHistory } from 'vue-router'
import { HomeView, LoginView, WelcomeView, DashboardView } from '@/views'
import { AuthStore } from '@/stores'
import ApplyView from '@/views/ApplyView.vue'
import ConfirmationView from '@/views/ConfirmationView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/home',
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
      name: 'welcome',
      component: WelcomeView
    },
    {
      path: '/confirm',
      name: 'confirm',
      component: ConfirmationView
    }
  ]
})

router.beforeEach(async (to) => {
  const publicPages = ['/login', '/welcome', '/apply', '/confirm']
  const authRequired = !publicPages.includes(to.path)
  const auth = AuthStore()

  if (authRequired && !auth.user) {
    console.log('Here')
    auth.returnUrl = to.fullPath
    return '/login'
  }
})

export default router
