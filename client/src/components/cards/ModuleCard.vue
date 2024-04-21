<script setup lang="ts">
import type {ModuleDTO} from '@/types'

const props = defineProps<{
  module: ModuleDTO
}>()
</script>

<template>
  <h2 class="text-lg font-semibold">{{module.name}}</h2>
  <p class="mt-2 text-gray-600">{{module.description}}</p>
</template>