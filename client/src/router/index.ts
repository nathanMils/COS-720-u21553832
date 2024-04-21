import { createRouter, createWebHistory } from 'vue-router'
import {
  HomeView,
  LoginView,
  DashboardView,
  ApplyView,
  ConfirmationView,
  VerifyView,
  ProfileView,
  StudentApplicationsView, AccessDeniedView, MyApplicationsView, MyCoursesView, MyModulesView
} from '@/views'
import { AuthStore } from '@/stores'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: DashboardView
        },
        {
          path: 'profile',
          name: 'profile',
          component: ProfileView
        },
        {
          path: 'studentApplications',
          name: 'studentApplications',
          component: StudentApplicationsView
        },
        {
          path: 'accessDenied',
          name: 'accessDenied',
          component: AccessDeniedView
        },
        {
          path: 'myApplications',
          name: 'myApplications',
          component: MyApplicationsView
        },
        {
          path: 'myCourses',
          name: 'myCourses',
          component: MyCoursesView
        },
        {
          path: 'myModules',
          name: 'myModules',
          component: MyModulesView
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
      path: '/confirm',
      name: 'confirm',
      component: ConfirmationView
    },
    {
      path: '/verifyEmail',
      name: 'verify',
      component: VerifyView
    }
  ]
})

router.beforeEach(async (to) => {
  const publicPages = [
    '/login',
    '/apply',
    '/confirm',
    '/verifyEmail'
  ]
  const authRequired = !publicPages.includes(to.path)
  const auth = AuthStore()

  if (authRequired && !auth.user) {
    auth.returnUrl = to.fullPath
    return '/login'
  }
})

// Check Role Requirements
router.beforeEach(async (to) => {
  const adminPages = [
    '/studentApplications',
  ]
  const authRequired = adminPages.includes(to.path);
  const auth = AuthStore()

  if (authRequired && auth.user?.role !== "ROLE_ADMIN") {
    auth.returnUrl = to.fullPath
    return '/accessDenied'
  }
})

export default router
