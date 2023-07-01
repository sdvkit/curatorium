<template>
    <Dialog :breakpoints="{ '960px': '75vw', '641px': '100vw' }" modal maximizable :style="{ width: '75vw' }" 
        :header="(params.subject !== null) ? params.subject.label : '' " :contentStyle="{ minHeight: '580px' }">
        <DataTable :value="params.students" editMode="cell" tableClass="editable-cells-table"
            contextMenu showGridlines scrollable scrollHeight="560px" responsiveLayout="stack" breakpoint="960px">
            <template #empty>Пусто</template>
            <ColumnGroup type="header">
                <Row>
                    <Column header="Учащиеся"/>
                    <Column :colspan="getMaxMarksCount">
                        <template #header>
                            <span>Отметки</span>
                            <Button icon="pi pi-plus" text @click="toggleAddMarkMenu" :style="{ position: 'absolute', right: '5px' }" aria-haspopup="true" aria-controls="overlay_menu" />
                            <Menu ref="menu" id="overlay_menu" :model="addMarkItems" :popup="true" :style="{ width: '200px' }">
                                <template #itemicon="slotProps">
                                    <Tag v-if="slotProps.item.key === 1" value="" :style="{ width: '15px', height: '15px', marginRight: '10px', borderRadius: '180px', background: 'none' }" />
                                    <Tag v-if="slotProps.item.key === 2" value="" :style="{ width: '15px', height: '15px', marginRight: '10px', borderRadius: '180px' }" />
                                    <Tag v-if="slotProps.item.key === 3" severity="warning" value="" :style="{ width: '15px', height: '15px', marginRight: '10px', borderRadius: '180px' }" />
                                </template>    
                            </Menu>
                        </template>
                    </Column>
                </Row>
            </ColumnGroup>
            <Column header="" field="label" frozen/>

            <Column v-if="getMaxMarksCount > 0" header="" v-for="(temp, index) in [...Array(getMaxMarksCount).keys()]">
                <template #body="slotProps">
                    <div v-if="slotProps.data[`mark_${index + 1}`] !== undefined">
                        <span v-if="slotProps.data[`mark_${index + 1}`].typeId === 1">
                            {{ slotProps.data[`mark_${index + 1}`].value }}
                        </span>
                        <span v-if="slotProps.data[`mark_${index + 1}`].typeId === 2" :style="{ color: 'var(--primary-color)' }">
                            {{ slotProps.data[`mark_${index + 1}`].value }}
                        </span>
                        <span v-if="slotProps.data[`mark_${index + 1}`].typeId === 3" :style="{ color: '#fbc129' }">
                            {{ slotProps.data[`mark_${index + 1}`].value }}
                        </span>
                    </div>
                    <div v-else>
                       {{ createAndReturnMark(slotProps.data, `mark_${index + 1}`) }}
                    </div>
                </template>
                <template #editor="slotProps">
                    <InputNumber v-model="slotProps.data[`mark_${index + 1}`].value" autofocus inputId="integeronly" />
                </template>
            </Column>

            <Column v-else header="">
                <template #body="slotProps">
                    {{ createAndReturnMark(slotProps.data, `mark_1`) }}
                </template>
            </Column>

        </DataTable>
    </Dialog>
</template>

<script>
import { mapGetters } from 'vuex'
import Dialog from 'primevue/dialog'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import ColumnGroup from 'primevue/columngroup'
import Row from 'primevue/row'
import InputNumber from 'primevue/inputnumber'
import Button from 'primevue/button'
import Menu from 'primevue/menu'
import Tag from 'primevue/tag'

export default {
    name: 'second-lvl-statement-dialog',
    props: ['params'],
    data() {
        return {
            addMarkItems: [
                {
                    key: 1,
                    label: 'Стандартная отметка',
                    command: this.addMark
                },
                {
                    key: 2,
                    label: 'Отметка за ОКР',
                    command: this.addControlMark
                },
                {
                    key: 3,
                    label: 'Отметка за курсовую работу',
                    command: this.addCursMark
                }
            ]
        }
    },
    computed: {
        ...mapGetters(['getSubjects']),
        getMaxMarksCount() {
            let maxMarksCount = 0

            this.params.students.forEach(student => {
                const studentMarksCount = Object.keys(student).length - 2

                if (studentMarksCount > maxMarksCount) {
                    maxMarksCount = studentMarksCount
                }
            })

            return maxMarksCount
        }
    },
    methods: {
        toggleAddMarkMenu(event) {
            this.$refs.menu.toggle(event);
        },
        createAndReturnMark(data, field) {
            const originalStudents = this.params.group.items[0].items
            const student = originalStudents.find(originalStudent => originalStudent.label === data.label)
            const subject = this.getSubjects.find(subject => subject.key === this.params.subjectKey)
            const statement = this.params.firstLvlStatement.secondLvlStatements.find(firstLvlStatement => firstLvlStatement.subject.key === subject.key)

            const mark = { secondLvlStatementKey: statement.key, value: 0, typeId: 1 }
            data[field] = mark
            this.$store.commit('SAVE_MARK', [student, mark])
            return data[field]
        },
        addMark() {
            const originalStudents = this.params.group.items[0].items
            const subject = this.getSubjects.find(subject => subject.key === this.params.subjectKey)
            const statement = this.params.firstLvlStatement.secondLvlStatements.find(firstLvlStatement => firstLvlStatement.subject.key === subject.key)

            originalStudents.forEach(student => this.$store.commit('SAVE_MARK', [student, { secondLvlStatementKey: statement.key, value: 0, typeId: 1 }]))

            this.params.students = []
            originalStudents.forEach((student) => {
                let entitie = { label: student.label }
        
                const marks = student.marks.filter((mark) => {
                    const foundSecondLvlStatement = this.params.firstLvlStatement.secondLvlStatements.find(secondLvlStatement => secondLvlStatement.key === mark.secondLvlStatementKey)
                    return foundSecondLvlStatement !== undefined && foundSecondLvlStatement.subject.key === this.params.subjectKey
                })

                marks.forEach((mark) => entitie[`mark_${Object.keys(entitie).length}`] = mark)
                entitie['key'] = student.key
                this.params.students.push(entitie)
            })
        },
        addControlMark() {
            const originalStudents = this.params.group.items[0].items
            const subject = this.getSubjects.find(subject => subject.key === this.params.subjectKey)
            const statement = this.params.firstLvlStatement.secondLvlStatements.find(firstLvlStatement => firstLvlStatement.subject.key === subject.key)

            originalStudents.forEach(student => this.$store.commit('SAVE_MARK', [student, { secondLvlStatementKey: statement.key, value: 0, typeId: 2 }]))

            this.params.students = []
            originalStudents.forEach((student) => {
                let entitie = { label: student.label }
        
                const marks = student.marks.filter((mark) => {
                    const foundSecondLvlStatement = this.params.firstLvlStatement.secondLvlStatements.find(secondLvlStatement => secondLvlStatement.key === mark.secondLvlStatementKey)
                    return foundSecondLvlStatement !== undefined && foundSecondLvlStatement.subject.key === this.params.subjectKey
                })

                marks.forEach((mark) => entitie[`mark_${Object.keys(entitie).length}`] = mark)
                entitie['key'] = student.key
                this.params.students.push(entitie)
            })
        },
        addCursMark() {
            const originalStudents = this.params.group.items[0].items
            const subject = this.getSubjects.find(subject => subject.key === this.params.subjectKey)
            const statement = this.params.firstLvlStatement.secondLvlStatements.find(firstLvlStatement => firstLvlStatement.subject.key === subject.key)

            originalStudents.forEach(student => this.$store.commit('SAVE_MARK', [student, { secondLvlStatementKey: statement.key, value: 0, typeId: 3 }]))

            this.params.students = []
            originalStudents.forEach((student) => {
                let entitie = { label: student.label }
        
                const marks = student.marks.filter((mark) => {
                    const foundSecondLvlStatement = this.params.firstLvlStatement.secondLvlStatements.find(secondLvlStatement => secondLvlStatement.key === mark.secondLvlStatementKey)
                    return foundSecondLvlStatement !== undefined && foundSecondLvlStatement.subject.key === this.params.subjectKey
                })

                marks.forEach((mark) => entitie[`mark_${Object.keys(entitie).length}`] = mark)
                entitie['key'] = student.key
                this.params.students.push(entitie)
            })
        },
    },
    components: {
        Dialog,
        DataTable,
        Column,
        ColumnGroup,
        Row,
        InputNumber,
        Button,
        Menu,
        Tag
    }
}
</script>