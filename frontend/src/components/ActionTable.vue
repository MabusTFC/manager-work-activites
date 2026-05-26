<template>
  <section class="card">
    <h2>Список интервалов</h2>

    <div v-if="loading" class="muted">Загрузка…</div>
    <div v-else-if="loadError" class="notice notice--error">{{ loadError }}</div>
    <div v-else-if="actions.length === 0" class="muted">Нет добавленных интервалов</div>

    <table v-else class="table">
      <thead>
        <tr>
          <th>Тип</th>
          <th>Начало (с)</th>
          <th>Конец (с)</th>
          <th>Длительность (с)</th>
          <th>Описание</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="a in sortedActions" :key="a.id">
          <td>
            <span class="badge" :class="`badge--${a.type.toLowerCase()}`">
              {{ a.type === 'WORK' ? 'Работа' : 'Перерыв' }}
            </span>
          </td>
          <td>{{ a.startTime }}</td>
          <td>{{ a.endTime }}</td>
          <td>{{ a.endTime - a.startTime }}</td>
          <td>{{ a.description }}</td>
        </tr>
      </tbody>
    </table>
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { actionService } from '../services/actionService';

const actions = ref([]);
const loading = ref(false);
const loadError = ref(null);

// Сортируем по времени начала, чтобы список читался как расписание
const sortedActions = computed(() =>
  [...actions.value].sort((a, b) => a.startTime - b.startTime)
);

const load = async () => {
  loading.value = true;
  loadError.value = null;
  try {
    actions.value = await actionService.getAllActions();
  } catch (err) {
    loadError.value = err.message;
  } finally {
    loading.value = false;
  }
};

onMounted(load);

// Родитель вызывает load() после успешного добавления
defineExpose({ load });
</script>
