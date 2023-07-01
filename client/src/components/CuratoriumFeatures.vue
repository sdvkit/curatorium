<template>
    <img src="../../public/logo.png" :style="{ height: '50px', width: '50px', marginBottom: '40px' }" />
    <br>
    <span :style="{ fontSize: '14pt' }">
        Добро пожаловать в <strong :style="{ color: 'var(--primary-color)' }">Curatorium</strong>
    </span>
    <br><br>
    <span :style="{ opacity: '0.6' }">
        Сервис, позволяющий быстро, легко и эффективно управлять ведомостями. 
    </span>
    <br><br>
    <ul class="features">
        <li>
            <Button icon="bx bxs-bolt" />
            <div class="description">
                <strong>Быстродействие</strong>
                <br>
                <span>Благодаря грамотному распределению рессурсов, сервис работает быстро и без зависаний.</span>
            </div>
        </li>
        <li>
            <Button icon="bx bx-cube-alt" />
            <div class="description">
                <strong>Понятность</strong>
                <br>
                <span>Несмотря на немалое количество функций, интерфейс сервиса остается простым и понятным.</span>
            </div>
        </li>
        <li>
            <Button icon="bx bx-shield-alt-2" />
            <div class="description">
                <strong>Безопасность</strong>
                <br>
                <span>Сервис не собирает "чувствительные" данные, а все пароли шифруются.</span>
            </div>
        </li>
    </ul>
</template>

<script>
import Button from 'primevue/button'

export default {
    name: 'curatorium-features',
    components: {
        Button
    }
}
</script>

<style scoped>
.features {
    position: absolute;
    left: 40px;
    list-style-type: none;
}

.features li {
    display: flex;
    align-items: start;
    margin-bottom: 20px;
}

.features li button {
    margin-right: 20px;
}

.features li .description {
    width: 35%;
}  

.features li .description span {
    opacity: 0.6;
}
</style>