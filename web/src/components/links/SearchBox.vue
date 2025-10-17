<template>
  <div class="serch-box-shell">
    <input type="checkbox" class="toggle" v-model="open"/>
    <label :for="cid"></label>
    <input
        ref="inputEl"
        type="text"
        class="input"
        :id="cid + '-input'"
        :value="modelValue"
        @input="onInput"
        @keyup.enter="onEnter"
        placeholder="我是一个高冷的搜索框"
    />
    <div class="fonts">
      <span style="--i:1">S</span>
      <span style="--i:2">e</span>
      <span style="--i:3">a</span>
      <span style="--i:4">r</span>
      <span style="--i:5">c</span>
      <span style="--i:6">h</span>
      <span style="--i:7">.</span>
      <span style="--i:8">.</span>
      <span style="--i:9">.</span>
      <span style="--i:10">.</span>
      <span style="--i:11">.</span>
      <span style="--i:12">.</span>
    </div>
  </div>
</template>

<script>
import {ref, onMounted, defineExpose} from 'vue';

export default {
  name: 'Serchbox',
  props: {//父组件传入
    modelValue: {type: String, default: ''}, // v-model
    autoOpen: {type: Boolean, default: false} // 是否默认展开
  },
  emits: ['update:modelValue', 'search', 'focus', 'blur'],

  setup(props, {emit}) {
    const open = ref(!!props.autoOpen);//默认不展开
    const inputEl = ref(null);
    const cid = 'sb-' + Math.random().toString(36).slice(2, 8); // 动态 id

    const onInput = (e) => emit('update:modelValue', e.target.value);
    const onEnter = () => emit('search');

    // 对外暴露 focus 方法，供父组件快捷键调用
    focus = () => {
      open.value = true;

      // 让 label 的 for 生效（点击展开）同时聚焦
      setTimeout(() => inputEl.value && inputEl.value.focus(), 0);
      emit('focus');
    }
    defineExpose({focus});

    onMounted(() => {
      if (props.autoOpen) focus();
    });

    return {
      open,
      inputEl,
      cid,
      onInput,
      onEnter,
      focus
    };
  }
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  background-size: cover;
}

.serch-box-shell {
  position: relative;
}

.input {
  padding: 10px;
  width: 80px;
  height: 80px;
  background: none;
  border: 4px solid rgb(255, 210, 45);
  border-radius: 20px;
  box-sizing: border-box;
  font: 400 26px Comic Sans Ms;
  color: rgba(255, 210, 45, 0.2);
  outline: none;
  transition: 0.5s;
}

/* 用 class 而非固定 id，避免多实例冲突 */
.toggle {
  display: none;
}

/* label 绑定到动态 id，让点击图标也能展开 */
label[for] {
  display: block;
  width: 70px;
  height: 70px;
  border-radius: 20px;
  position: absolute;
  right: 0;
  margin: 4px;
  transition: 0.4s;
  background-image: url('~@/assets/AboutMe/links/serchfield.png');
  background-size: cover;
}

/* 展开态样式：用 :checked 选择器 + 相邻兄弟组合 */
.toggle:checked ~ label[for] {
  border-radius: 10px;
  background-image: url('~@/assets/AboutMe/links/cross.png');
  border: 4px solid rgb(255, 210, 45);
  margin: 0 -5px;
}

.toggle:checked ~ .input {
  width: 400px;
  border-radius: 10px;
  margin-right: 80px;
  background-color: #272133;
  color: rgb(255, 210, 45);
}

.fonts {
  position: absolute;
  top: 0;
  left: 10px;
  color: rgb(255, 210, 45);
  letter-spacing: 5px;
  font: 400 26px Comic Sans Ms;
  display: flex;
}

.fonts span {
  transition: 0.3s;
  opacity: 0;
}

.toggle:checked ~ .fonts span {
  transition: calc(var(--i) * 0.1s) 0.3s;
  transform: translateY(-40px);
  opacity: 1;
}
</style>