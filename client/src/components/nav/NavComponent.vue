<template>
  <div class="hidden md:flex flex-col w-64 bg-appSecondary-light dark:bg-appSecondary-dark shadow-[13px_0px_20px_0px_#00000024] z-10">
    <div class="flex items-center justify-center h-16 shadow-xl">
      <div class="flex flex-row pt-6 items-center justify-evenly text-2xl mb-8 font-bold text-appText-light dark:text-appText-dark">
        <LogoImg :size="2.5" :mr="0.5"/>
        <h1>Portal</h1>
      </div>
    </div>
    <div class="flex flex-col flex-1 overflow-y-auto">
      <nav class="flex-1 flex flex-col justify-between">
        <div>
          <RouterLink :to="{name: 'dashboard'}">
            <a class="flex w-full items-center px-6 py-5 text-appText-light dark:text-appText-dark hover:bg-appHover-light dark:hover:bg-appHover-dark">
              <i class="fa-solid fa-bars text-primaryButton-500"></i>
              <span class="ml-4">Dashboard</span>
            </a>
          </RouterLink>
          <RouterLink :to="{name: 'myModules'}">
            <a class="flex w-full items-center px-6 py-5 text-appText-light dark:text-appText-dark hover:bg-appHover-light dark:hover:bg-appHover-dark">
              <i class="fa-solid fa-book text-primaryButton-500"></i>
              <span class="ml-4">My Modules</span>
            </a>
          </RouterLink>
          <RouterLink :to="{name: 'myCourses'}">
            <a class="flex w-full items-center px-6 py-5 text-appText-light dark:text-appText-dark hover:bg-appHover-light dark:hover:bg-appHover-dark">
              <i class="fa-solid fa-graduation-cap text-primaryButton-500"></i>
              <span class="ml-3">My Courses</span>
            </a>
          </RouterLink>
          <RouterLink v-show="isAdmin" :to="{name: 'studentApplications'}">
            <a class="flex w-full items-center px-6 py-5 text-appText-light dark:text-appText-dark hover:bg-appHover-light dark:hover:bg-appHover-dark">
              <i class="fa-solid fa-user-graduate text-primaryButton-500"></i>
              <span class="ml-4">Student Applications</span>
            </a>
          </RouterLink>
        </div>
        <div>
          <RouterLink :to="{name: 'profile'}">
            <a class="flex w-full items-center px-6 py-5 text-appText-light dark:text-appText-dark hover:bg-appHover-light dark:hover:bg-appHover-dark">
              <i class="fa-regular fa-user text-primaryButton-500"></i>
              <span class="ml-4">Profile</span>
            </a>
          </RouterLink>
          <button class="flex w-full items-center px-6 py-5 text-appText-light dark:text-appText-dark hover:bg-appHover-light dark:hover:bg-appHover-dark" @click="logout">
            <i class="fa-solid fa-right-from-bracket text-primaryButton-500"></i>
            <span class="ml-4">Logout</span>
          </button>
        </div>
      </nav>
    </div>
  </div>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router';
import { LogoImg } from '@/components'
import { AuthStore} from '@/stores'
import { onMounted, ref } from 'vue'
const authStore = AuthStore();
const logout = () => {
  authStore.Logout();
}
const isAdmin = ref(false);
onMounted(() => {
  isAdmin.value = authStore.isAdmin();
})
</script>