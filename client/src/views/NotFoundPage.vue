<template>
  <div class="wrapper">
      <div class="error-label">
          <h1>Что-то пошло не так...</h1>
          <span class="path-error-label">
              Возникла непредвиденная ошибка
          </span>
      </div>
  </div>
</template>

<script>
export default {
    name: 'not-found-page'
}
</script>

<style scoped>
  .error-label {
      position: absolute;
      top: 40%;
      left: 50%;
      transform: translate(-50%, -50%);
      text-align: center;
  }

  .path-error-label {
      color: var(--primary-color);
  }
</style>