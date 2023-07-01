<template>
    <Dialog :breakpoints="{ '960px': '75vw', '641px': '100vw' }"
        :style="{ width: '40rem' }" header="Переименовать ведомость" modal>
        <br>
        <span class="p-float-label">
            <InputText
                v-tooltip="'Длинна названия ведомости должна быть больше 4 и меньше 50. Название ведомости должно быть уникальным и не должно содержать знаков (, / . *).'"
                id="edit-statement-name" v-model="params.editableName" :style="{ width: '100%' }" />
            <label for="edit-statement-name">Название ведомости</label>
        </span>
        <Button label="Сохранить иземения" icon="pi pi-check" :style="{ marginTop: '10px', width: '100%' }" @click="editStatement" />
    </Dialog>

</template>

<script>
import { mapGetters } from 'vuex'
import Dialog from 'primevue/dialog'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'

export default {
    name: 'edit-statement-dialog',
    props: ['params'],
    methods: {
        editStatement() {
            const statementNameInput = document.getElementById('edit-statement-name')

            if (this.params.editableName.length < 4 || this.params.editableName.length > 50 || /[,.*\/]/.test(this.params.editableName)) {
                statementNameInput.classList.add('p-invalid')
                return;
            }

            const foundStatement = this.getStatements.filter((statement) => {
                return statement.label === this.params.editableName
            })

            if (foundStatement.length !== 0) {
                statementNameInput.classList.add('p-invalid')
                return
            }

            this.$confirm.require({
                message: `Вы действительно хотите переименовать ведомость в ${this.params.editableName}?`,
                header: 'Подтверждение переименования',
                icon: 'pi pi-info-circle',
                accept: () => {
                    this.params.visible = false
                    this.$store.commit('EDIT_STATEMENT', [this.params.statement, this.params.editableName])
                    this.$toast.add({ severity: 'info', summary: 'Изменено', detail: `Ведомость \"${this.params.statement.label}\" переименована`, life: 3000 })
                    this.params.editableName = ''
                },
                reject: () => { }
            })
        },
    },
    computed: {
        ...mapGetters(['getStatements'])
    },
    components: {
        Dialog,
        InputText,
        Button
    }
}
</script>