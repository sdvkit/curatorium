<template>
    <Dialog :breakpoints="{ '960px': '75vw', '641px': '100vw' }" :style="{ width: '20rem' }" header="Переименовать учащегося" modal>
        <span :style="{ marginTop: '5px' }" class="p-float-label">
            <InputText id="student-name" v-model="params.editableName" v-tooltip="'Длинна ФИО должна быть больше 4, меньше 50.'" :style="{ width: '100%' }"/>
            <label for="student-name">ФИО учащегося</label>
        </span>
        <Button :style="{ marginTop: '10px', width: '100%' }" icon="pi pi-check" label="Сохранить" @click="renameStudent"/>
    </Dialog>
</template>

<script>
import Dialog from 'primevue/dialog'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'

export default {
    name: 'edit-student-dialog',
    props: ['params'],
    methods: {
        renameStudent() {
            const studentNameInput = document.getElementById('student-name')

            if (this.params.editableName.length < 5 || this.params.editableName.length > 50) {
                studentNameInput.classList.add('p-invalid')
                return;
            }

            this.$confirm.require({
                message: `Вы действительно хотите переименовать студента \"${this.params.student.label}\" в \"${this.params.editableName}\"?`,
                header: 'Подтверждение переименования',
                icon: 'pi pi-info-circle',
                accept: () => {
                    this.params.visible = false
                    this.$store.commit('RENAME_STUDENT', [this.params.student, this.params.editableName])
                    this.$toast.add({ severity: 'info', summary: 'Изменено', detail: `Учащийся \"${this.params.student.label}\" переименован`, life: 3000 })
                    this.params.editableName = ''
                },
                reject: () => {}
            });
        },
    },
    components: {
        Dialog,
        InputText,
        Button
    }
}
</script>