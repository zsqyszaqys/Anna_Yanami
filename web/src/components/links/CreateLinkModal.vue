<template>
  <div v-if="modelValue" class="modal-mask" @click.self="$emit('update:modelValue', false)">
    <div class="modal">
      <h3>{{ form.id ? '编辑链接' : '添加链接' }}</h3>
      <div class="form">
        <label>分组</label>
        <select v-model="form.groupId">
          <option v-for="g in groups" :key="g.id" :value="g.id">{{ g.name }}</option>
        </select>

        <label>标题</label>
        <input v-model.trim="form.title" placeholder="例如：GitHub"/>

        <label>URL</label>
        <input v-model.trim="form.url" placeholder="https://..."/>

        <label>描述</label>
        <textarea v-model.trim="form.description" rows="3"/>
      </div>
      <div class="modal-actions">
        <button class="btn ghost" @click="$emit('update:modelValue', false)">取消</button>
        <button class="btn primary" @click="onSubmit">保存</button>
      </div>
    </div>
  </div>
</template>

<script>
import {reactive, watch} from 'vue';

export default {
  name: 'CreateLinkModal',
  props: {
    modelValue: {type: Boolean, default: false},
    groups: {type: Array, default: () => []},
    initial: {type: Object, default: null}
  },
  emits: ['update:modelValue', 'submit'],
  setup(props, {emit}) {
    const form = reactive({
      id: undefined,
      groupId: undefined,
      title: '',
      url: '',
      description: ''
    });

    function reset(val) {
      form.id = val && val.id != null ? val.id : undefined;
      form.groupId = val && val.groupId != null ? val.groupId : undefined;
      form.title = (val && val.title) || '';
      form.url = (val && val.url) || '';
      form.description = (val && val.description) || '';
    }

    watch(() => props.initial, (val) => reset(val), {immediate: true});

    function onSubmit() {
      if (!form.groupId || !form.title || !form.url) {
        alert('请填写分组、标题与URL');
        return;
      }
      emit('submit', {...form});
    }

    return {form, onSubmit};
  }
}
</script>

<style scoped>
.modal-mask {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, .25);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 50;
}

.modal {
  width: 520px;
  max-width: calc(100vw - 40px);
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, .2);
  padding: 16px;
}

.modal h3 {
  margin: 6px 2px 12px;
  font-size: 18px;
  font-weight: 600;
}

.form {
  display: grid;
  gap: 8px;
}

.form input, .form textarea, .form select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  outline: none;
  background: #fff;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 12px;
}

.btn {
  padding: 8px 12px;
  border-radius: 10px;
  border: 1px solid #e5e7eb;
  background: #fff;
  cursor: pointer;
}

.btn.primary {
  background: #111827;
  color: #fff;
  border-color: #111827;
}

.btn.ghost {
  background: #fff;
}
</style>