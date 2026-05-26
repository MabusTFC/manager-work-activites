<template>
  <section class="card">
    <h2>Добавить интервал</h2>

    <div
      v-if="notice"
      class="notice"
      :class="{ 'notice--error': notice.type === 'error' }"
    >
      {{ notice.text }}
    </div>

    <form class="form" @submit.prevent="submit">
      <div class="form__row">
        <label class="form__field">
          <span>Начало (сек)</span>
          <input
            v-model.number="form.startTime"
            type="number"
            min="0"
            max="86400"
            required
          />
        </label>

        <label class="form__field">
          <span>Конец (сек)</span>
          <input
            v-model.number="form.endTime"
            type="number"
            min="0"
            max="86400"
            required
          />
        </label>
      </div>

      <label class="form__field">
        <span>Тип активности</span>
        <select v-model="form.type" required>
          <option value="WORK">Работа</option>
          <option value="BREAK">Перерыв</option>
        </select>
      </label>

      <label class="form__field">
        <span>Описание</span>
        <input v-model="form.description" type="text" required />
      </label>

      <button type="submit" :disabled="loading">
        {{ loading ? 'Добавление…' : 'Добавить' }}
      </button>
    </form>
  </section>
</template>

<script setup>
import { ref } from 'vue';
import { actionService } from '../services/actionService';

const emit = defineEmits(['action-added']);

const emptyForm = () => ({
  startTime: null,
  endTime: null,
  type: 'WORK',
  description: '',
});

const form = ref(emptyForm());
const loading = ref(false);
const notice = ref(null);

// Локальная проверка чтобы не дёргать сервер на очевидных ошибках
const validate = () => {
  const { startTime, endTime } = form.value;
  if (startTime === null || endTime === null) {
    return 'Заполните начало и конец интервала';
  }
  if (startTime < 0 || endTime < 0 || startTime > 86400 || endTime > 86400) {
    return 'Значения должны быть в диапазоне 0–86400 секунд';
  }
  if (startTime >= endTime) {
    return 'Начало должно быть меньше конца';
  }
  return null;
};

const submit = async () => {
  notice.value = null;

  const localError = validate();
  if (localError) {
    notice.value = { type: 'error', text: localError };
    return;
  }

  loading.value = true;
  try {
    await actionService.createAction(form.value);
    form.value = emptyForm();
    emit('action-added');
  } catch (err) {
    notice.value = {
      type: 'error',
      text: err.isOverlap
        ? `Пересечение интервалов: ${err.message}`
        : err.message,
    };
  } finally {
    loading.value = false;
  }
};
</script>
