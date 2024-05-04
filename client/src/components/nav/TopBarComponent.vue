<script setup lang="ts">
import { useRouter } from 'vue-router'
import { DarkMode } from '@/components'

const router = useRouter()

const goBack = () => {
  router.go(-1)
}
</script>

<template>
  <div class="flex items-center justify-end h-16 bg-appSecondary-light dark:bg-appSecondary-dark border-b-appBorder-light dark:border-appBorder-dark shadow-xl z-10">
    <button @click="goBack" class="pl-4">
      <i class="fa fa-arrow-left text-primaryButton-500 dark:text-primaryButton-800 flex items-center justify-center cursor-pointer w-8 h-8 bg-slate-100 hover:bg-slate-200 dark:bg-slate-700 dark:hover:bg-slate-600/80 rounded-full"></i>
    </button>
    <div class="flex items-center justify-end flex-1 pr-4">
      <DarkMode />
    </div>
  </div>
</template>