import { createRouter, createWebHistory } from 'vue-router'
import { inject} from 'vue'
import type { Ref } from 'vue'
import {
  HomeView,
  LoginView,
  DashboardView,
  ApplyView,
  ConfirmationView,
  VerifyView,
  ProfileView,
  StudentApplicationsView,
  AccessDeniedView,
  MyApplicationsView,
  MyCoursesView,
  MyModulesView,
  ApplyForCourseView,
  CourseView,
  AllCoursesView,
  NotFoundView,
  CreateCourseView,
  UnderConstructionView,
  ModerateCourseView,
  CreateModuleView,
  ForgotPasswordView,
  ResetPasswordView,
  ModerateModuleView
} from '@/views'
import { AuthStore } from '@/stores'
import { Role } from '@/types'

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
          component: UnderConstructionView
        },
        {
          path: 'profile',
          name: 'profile',
          component: ProfileView
        },
        {
          path: 'studentApplications',
          name: 'studentApplications',
          component: StudentApplicationsView,
          meta: { roles: [Role.ROLE_ADMIN] }
        },
        {
          path: 'allCourses',
          name: 'allCourses',
          component: AllCoursesView,
          meta: { roles: [Role.ROLE_ADMIN] }
        },
        {
          path: 'myApplications',
          name: 'myApplications',
          component: MyApplicationsView,
          meta: { roles: [Role.ROLE_STUDENT] }
        },
        {
          path: 'myCourses',
          name: 'myCourses',
          component: MyCoursesView,
          meta: { roles: [Role.ROLE_STUDENT] }
        },
        {
          path: 'applyCourse',
          name: 'applyCourse',
          component: ApplyForCourseView,
          meta: { roles: [Role.ROLE_STUDENT] }
        },
        {
          path: 'createCourse',
          name: 'createCourse',
          component: CreateCourseView,
          meta: { roles: [Role.ROLE_ADMIN, Role.ROLE_COURSE_MODERATOR] }
        },
        {
          path: 'myModules',
          name: 'myModules',
          component: MyModulesView,
          meta: { roles: [Role.ROLE_STUDENT] }
        },
        {
          path: 'course/:courseId',
          name: 'studentCourse',
          component: CourseView
        },
        {
          path: 'course/:courseId/edit',
          name: 'moderateCourse',
          component: ModerateCourseView,
          meta: { roles: [Role.ROLE_COURSE_MODERATOR,Role.ROLE_ADMIN] }
        },
        {
          path: 'course/:courseId/createModule',
          name: 'createModule',
          component: CreateModuleView
        },
        {
          path: 'module/:moduleId',
          name: 'studentModule',
          component: UnderConstructionView
        },
        {
          path: 'module/:moduleId/edit',
          name: 'moderateModule',
          component: ModerateModuleView,
          meta: { roles: [Role.ROLE_COURSE_MODERATOR,Role.ROLE_ADMIN] }
        },
        {
          path: '',
          name: 'default',
          redirect: { name: 'dashboard' }
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
      path: '/forgot',
      name: 'forgot',
      component: ForgotPasswordView
    },
    {
      path: '/reset',
      name: 'reset',
      component: ResetPasswordView
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
    },
    {
      path: '/accessDenied',
      name: 'accessDenied',
      component: AccessDeniedView
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: NotFoundView
    }
  ]
})

router.beforeEach(async (to) => {
  const isLoading = inject<Ref<boolean>>('isLoading')
  if (isLoading === undefined) throw new Error('isLoading not provided')
  isLoading.value = true
  const publicPages = [
    '/login',
    '/apply',
    '/confirm',
    '/verifyEmail',
    '/forgot',
    '/reset',
  ]
  const authRequired = !publicPages.includes(to.path)
  const auth = AuthStore()

  if (authRequired && !(await auth.loggedIn())) {
    auth.returnUrl = to.fullPath
    isLoading.value = false
    return '/login'
  }
  isLoading.value = false
})

router.beforeEach(async (to, from, next) => {
  const auth = AuthStore()
  const loggedIn = await auth.loggedIn()

  const guardedPaths = ['/login', '/apply', '/forgot', '/reset', '/confirm', '/verifyEmail']

  if (loggedIn && guardedPaths.includes(to.path)) {
    next('/dashboard')
  } else {
    next() // Otherwise, proceed as normal
  }
})

router.beforeEach(async (to, from, next) => {
  const isLoading = inject<Ref<boolean>>('isLoading')
  if (isLoading === undefined) throw new Error('isLoading not provided')
  const auth = AuthStore()
  const allowedRoles = to.meta.roles as Role[]
  if (allowedRoles && !(await auth.isRoleIncluded(allowedRoles))) {
    auth.returnUrl = to.fullPath
    isLoading.value = false
    console.log('Access Denied')
    next('/accessDenied')
  } else {
    isLoading.value = false
    next()
  }
})

export default router
