<template>
  <div class="layout">
    <header class="layout__header">
      <h1>Интервалы активности</h1>
    </header>

    <main class="layout__main">
      <ActionForm @action-added="onActionAdded" />
      <ActionTable ref="tableRef" />
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import ActionForm from './components/ActionForm.vue';
import ActionTable from './components/ActionTable.vue';

const tableRef = ref(null);

const onActionAdded = () => {
  tableRef.value?.load();
};
</script>
