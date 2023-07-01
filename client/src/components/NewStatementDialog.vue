<template>
    <Dialog :breakpoints="{ '960px': '75vw', '641px': '100vw' }" :style="{ width: '40rem' }" header="Создать ведомость" modal>
        <br>
        <span class="p-float-label">
            <InputText v-tooltip="'Длинна названия ведомости должна быть больше 4 и меньше 50. Название ведомости должно быть уникальным и не должно содержать знаков (, / . *).'"
                       id="statement-name" v-model="newStatementName" :style="{ width: '100%' }" />
            <label for="statement-name">Название ведомости</label>
        </span>
        <br>
        <span v-tooltip.left="'Ведомость должна включать минимум 2 предмета.'" class="p-float-label" :style="{ width: '100%' }">
            <MultiSelect id="statement-subjects" v-model="newStatementSubjects" :options="getSubjects" display="chip" option-label="label"
                         class="w-full md:w-20rem" :style="{ height: '52px', width: '100%', maxWidth: '100%' }" empty-message="Пусто" />
            <label for="statement-subjects">Предметы</label>
        </span>
        <br>
        <div v-show="params.currentGroup === undefined || params.currentGroup === null">
            <span class="p-float-label">
                <Dropdown id="statement-group" v-model="newStatementGroup" :options="getNotArchivedGroupsMenuItems" optionLabel="name" placeholder="Выберите группу" :style="{ minWidth: '170px' }">
                    <template #value="slotProps">
                        <Tag :value="(slotProps.value === null) ? null : slotProps.value.label"></Tag>
                    </template>
                    <template #option="slotProps">
                        <Tag :value="slotProps.option.label"></Tag>
                    </template>
                </Dropdown>
                <label for="statement-group">Группа</label>
            </span>
            <br>
        </div>
        <Button label="Сохранить ведомость" icon="pi pi-check" @click="createStatement" />
    </Dialog>
</template>

<script>
import { mapGetters } from 'vuex'
import Dialog from 'primevue/dialog'
import InputText from 'primevue/inputtext'
import MultiSelect from 'primevue/multiselect'
import Dropdown from 'primevue/dropdown'
import Tag from 'primevue/tag'
import Button from 'primevue/button'

export default {
    name: 'new-statement-dialog',
    props: ['params'],
    data() {
        return {
            newStatementName: '',
            newStatementGroup: null,
            newStatementSubjects: [],
        }
    },
    methods: {
        createStatement() {
            const statementNameInput = document.getElementById('statement-name')
            const statementGroupDropdown = document.getElementById('statement-group')
            const statementSubjectsSelect = document.getElementById('statement-subjects')
            this.newStatementGroup = (this.params.currentGroup === null || this.params.currentGroup === undefined) ? this.newStatementGroup : this.params.currentGroup

            if (this.newStatementName.length < 4 || this.newStatementName.length > 50 || /[,.*\/]/.test(this.newStatementName)) {
                statementNameInput.classList.add('p-invalid')
                return;
            }

            if (this.newStatementSubjects.length < 2) {
                statementSubjectsSelect.classList.add('p-invalid')
                return;
            }

            if (this.newStatementGroup === null || this.newStatementGroup.isArchived) {
                statementGroupDropdown.classList.add('p-invalid')
                return;
            }

            const foundStatement = this.getStatements.filter((statement) => {
                return statement.label === this.newStatementName
            })

            if (foundStatement.length !== 0) {
                statementNameInput.classList.add('p-invalid')
                return
            }

            this.$store.commit('SAVE_STATEMENT', [this.newStatementName, this.newStatementSubjects, this.newStatementGroup])

            this.newStatementName = ''
            this.newStatementGroup = null
            this.newStatementSubjects = []
            this.params.visible = false
        },
    },
    computed: {
        ...mapGetters(['getSubjects', 'getGroupsMenuItems', 'getStatements', 'getNotArchivedGroupsMenuItems'])
    },
    components: {
        Dialog,
        InputText,
        MultiSelect,
        Dropdown,
        Tag,
        Button
    }
}
</script>