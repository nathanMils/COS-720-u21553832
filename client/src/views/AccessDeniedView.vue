<script setup lang="ts">
import { LogoImg } from '@/components'
</script>

<template>
  <section class="flex items-center justify-center h-screen bg-appPrimary-light dark:bg-appPrimary-dark">
    <div class="w-full max-w-md overflow-hidden mx-3">
      <div class="px-4 py-2 mt-2">
        <div class="flex flex-row items-center justify-center text-7xl mb-8 font-bold text-appText-light dark:text-appText-dark">
          <LogoImg :size="6" :mr="1"/>
          <h1>Portal</h1>
        </div>
        <div class="flex items-center justify-center">
          <i class="fa-solid fa-triangle-exclamation text-green-500 text-7xl"></i>
        </div>
        <h2 class="text-2xl font-semibold text-center text-appText-light dark:text-appText-dark mt-8">ACCESS DENIED</h2>
        <p class="text-sm text-center text-gray-500 mt-2">
          Sorry but you do not have permission to access this page. Please contact your administrator.
        </p>
      </div>
    </div>
  </section>
</template>