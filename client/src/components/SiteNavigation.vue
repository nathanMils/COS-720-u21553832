<template>
  <header class="sticky top-0 bg-app-primary shadow-lg">
    <nav class="text-white py-3.5 px-5 shadow md:flex justify-between items-center">
      <div class="flex items-center gap-6 flex-1">
        <i
          class="fa-solid fa-bars text-2xl hover:text-app-secondary duration-150 cursor-pointer"
        ></i>
        <RouterLink :to="{ name: 'home' }">
          <p class="text-2xl hover:text-app-secondary duration-150 cursor-pointer">Portal</p>
        </RouterLink>
      </div>

      <div class="flex gap-3 flex-1 justify-end">
        <i
          class="fa-solid fa-circle-info text-xl hover:text-app-secondary duration-150 cursor-pointer"
        ></i>
        <i
          class="fa-solid fa-user text-xl hover:text-app-secondary duration-150 cursor-pointer"
        ></i>
      </div>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>
