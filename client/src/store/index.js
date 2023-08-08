import {createStore} from 'vuex'
import jwt from '../jwt'
import api from '../api'

const store = createStore({
    state () {
        return {
            user: {},
            groupsMenuItems: [],
            subjectsMenuItems: [],
            breadcrumbMenuItems: [],
            firstLvlStatements: [], 
        }
    },
    getters: {
        getUser(state) {
            return state.user
        },
        getNotArchivedGroupsMenuItems(state) {
            return state.groupsMenuItems.filter((group) => !group.isArchived)
        },
        getArchivedGroupsMenuItems(state) {
            return state.groupsMenuItems.filter((group) => group.isArchived)
        },
        getGroupsMenuItems(state) {
            return state.groupsMenuItems
        },
        getSubjectsMenuItems(state) {
            return state.subjectsMenuItems
        },
        getBreadcrumbMenuItems(state) {
            return state.breadcrumbMenuItems
        },
        getStatements(state) {
            return state.firstLvlStatements
        },
        getNotArchivedStatements(state) {
            return state.firstLvlStatements.filter((statement) => !statement.group.isArchived )
        },
        getArchivedStatements(state) {
            return state.firstLvlStatements.filter((statement) => statement.group.isArchived )
        },
        getSubjects(state) {
            const subjects = []
            for (let i = 0; i < state.subjectsMenuItems.length; i += 2) {
                subjects.push(state.subjectsMenuItems[i])
            }
            return subjects
        },
        getSecondLvlStatements(state) {
            const secondLvlStatements = []
            
            state.firstLvlStatements.forEach(firstLvlStatement => {
                firstLvlStatement.secondLvlStatements.forEach(secondLvlStatement => secondLvlStatements.push(secondLvlStatement))
            })

            return secondLvlStatements
        },
    },
    mutations: {
        SAVE_PROFILE_CHANGES(state, payload) {
            api.saveProfileChanges({ username: payload[0], fullName: payload[1] })
        },
        CLEAR_BREADCRUMB_MENU(state) {
            state.breadcrumbMenuItems = []
        },
        SET_BREADCRUMB_MENU_GROUP(state, group) {
            if (state.breadcrumbMenuItems.length === 1) {
                state.breadcrumbMenuItems.shift()
            }

            state.breadcrumbMenuItems.push({ label: group.label, to: `/groups/${group.key}` })
        },
        DELETE_STUDENT(state, payload) {
            const deletableStudent = payload[0]
           
            api.deleteStudent(deletableStudent.key)
                .then(() => {
                    const deletableStudentGroup = payload[1]
                    const deletableStudentIndex = deletableStudentGroup.items[0].items.indexOf(deletableStudent)
                    deletableStudentGroup.items[0].items.splice(deletableStudentIndex, 1)
                })
                .catch(err => console.log(err))
        },
        RENAME_STUDENT(state, payload) {
            const student = payload[0]
            
            api.renameStudent(student.key, payload[1])
                .then(() => student.label = payload[1])
                .catch(err => console.log(err))
        },
        SAVE_SUBJECT(state, subjectName) {
            if (state.subjectsMenuItems.length > 0) {
                state.subjectsMenuItems.push({ separator: true })
            }

            api.saveSubject(subjectName)
                .then(response => state.subjectsMenuItems.push({
                    key: response.data.id, 
                    label: subjectName, 
                    icon: 'pi pi-book'
                }))
                .catch(err => {
                    console.log(err)
                    state.subjectsMenuItems.pop()
                })
        },
        RENAME_SUBJECT(state, payload) {
            const subject = payload[0]
            const newSubjectName = payload[1]

            api.renameSubject(subject.key, newSubjectName)
                .then(() => subject.label = newSubjectName)
                .catch(err => console.log(err))
        },
        DELETE_SUBJECT(state, payload) {
            const subject = payload[0]
            let result = payload[1]

            for (let i = 0; i < state.firstLvlStatements.length; i++) {
                const statement = state.firstLvlStatements[i]
                const statementSubjects = []

                statement.secondLvlStatements.forEach(secondLvlStatement => 
                    statementSubjects.push(secondLvlStatement.subject))
                
                if (statementSubjects.indexOf(subject) !== -1 && statementSubjects.length === 2) {
                    result.out = false
                    return
                }
            }

            const deletableIndex = state.subjectsMenuItems.indexOf(subject)

            if (deletableIndex !== state.subjectsMenuItems.length - 1) {
                state.subjectsMenuItems.splice(deletableIndex, 2)
                
                api.deleteSubject(subject.key)
                    .then(() => {})
                    .catch(err => console.log(err))
            } else {
                state.subjectsMenuItems.splice(deletableIndex -1, 2)

                api.deleteSubject(subject.key)
                    .then(() => {})
                    .catch(err => console.log(err))
            }

            state.firstLvlStatements.forEach((statement) => {
                statement.secondLvlStatements.forEach(secondLvlStatement => {
                    
                    if (secondLvlStatement.subject === subject) {
                        const deletableStatementIndex = statement.secondLvlStatements.indexOf(secondLvlStatement)
                        statement.secondLvlStatements.splice(deletableStatementIndex, 1)
                    }  
                })
            })

            result.out = true
        },
        RENAME_GROUP(state, payload) {
            const group = payload[0]
            const newGroupName = payload[1]

            api.renameGroup(group.key, newGroupName)
                .then(() => {
                    group.name = newGroupName
                    group.label = `${group.name} (${group.year})`
                })
                .catch(err => console.log(err))
        },
        SAVE_GROUP(state, payload) {
            const groupName = payload[0]
            const groupYear = payload[1]
            const openCommand = payload[2]
            const renameCommand = payload[3]
            const addStudentCommand = payload[4]

            api.saveGroup(groupName, groupYear)
                .then(response => {
                    state.groupsMenuItems.push({
                        key: `${response.data.id}`, 
                        label: `${groupName} (${groupYear})`, 
                        name: groupName, 
                        icon: 'pi pi-folder', 
                        year: groupYear, 
                        createdAt: new Date(),
                        isArchived: false,
                        items: [
                            { key: `${response.data.id}_students`, label: 'Учащиеся', icon: 'pi pi-users', items: [] },
                            { key: `${response.data.id}_add_student`, label: 'Добавить учащегося', icon: 'pi pi-plus', command: addStudentCommand},
                            { key: `${response.data.id}_open`, label: 'Открыть', icon: 'pi pi-folder-open', command: openCommand},
                            { key: `${response.data.id}_rename`, label: 'Переименовать', icon: 'pi pi-pencil', command: renameCommand}
                        ]
                    })
                })
                .catch(err => console.log(err))
        },
        DELETE_STATEMENT(state, deletableStatement) {    
            api.deleteFirstLvlStatement(deletableStatement.key)
                .then(() => {
                    const deletableIndex = state.firstLvlStatements.indexOf(deletableStatement)
                    state.firstLvlStatements.splice(deletableIndex, 1)
                })
                .catch(err => console.log(err))
        },
        SAVE_STATEMENT(state, payload) {
            const statementName = payload[0]
            const statementSubjects = payload[1]
            const statementGroup = payload[2]

            api.saveFirstLvlStatement(statementName, statementSubjects, statementGroup)
                .then(response => {
                    const entity = {
                        key: response.data.id,
                        label: statementName,
                        group: statementGroup,
                        secondLvlStatements: []
                    }

                    response.data.secondLvlStatements.forEach(secondLvlStatement => {
                        const secondLvlStatementSubject = statementSubjects.find(subject => subject.key === secondLvlStatement.subjectId)

                        entity.secondLvlStatements.push({
                            key: secondLvlStatement.id,
                            subject: secondLvlStatementSubject
                        })
                    })

                    state.firstLvlStatements.push(entity)
                })
                .catch(err => console.log(err))
        },
        EDIT_STATEMENT(state, payload) {
            const editableStatement = payload[0]
            const newStatementName = payload[1]

            api.renameStatement(editableStatement.key, newStatementName)
                .then(() => {
                    state.firstLvlStatements.find((statement) => {
                        if (statement === editableStatement) {
                            statement.label = newStatementName
                        }
                    })
                })
                .catch(err => console.log(err))
        },
        SAVE_STUDENT(state, payload) {
            const studentName = payload[0]
            const studentGroup = payload[1]

            api.saveStudent(studentGroup.key, studentName)
                .then(response => {
                    studentGroup.items[0].items.push({
                        key: response.data.id,
                        label: studentName,
                        icon: 'pi pi-user',
                        marks: []
                    })
        
                    studentGroup.items[0].items.sort((student1, student2) => student1.label.localeCompare(student2.label))
                })
                .catch(err => console.log(err))
        },
        ARCHIVE_GROUP(state, group) {
            api.archiveGroup(group.key)
                .then(() => group.isArchived = true)
                .catch(err => console.log(err))
        },
        DEARCHIVE_GROUP(state, group) {
            api.archiveGroup(group.key)
                .then(() => group.isArchived = false)
                .catch(err => console.log(err))
        },
        DELETE_GROUP(state, deletableGroup) {
            api.deleteGroup(deletableGroup.key)
                .then(() => {
                    const deletableIndex = state.groupsMenuItems.indexOf(deletableGroup)
                    state.firstLvlStatements = state.firstLvlStatements.filter((statement) => statement.group !== deletableGroup)
                    state.groupsMenuItems.splice(deletableIndex, 1)
                })
                .catch(err => console.log(err))
        },
        CLEAR_ARCHIVE(state) {
            const archivedGroups = state.groupsMenuItems.filter((group) => group.isArchived)

            while (archivedGroups.length !== 0) {
                const deletableGroup = archivedGroups.shift()
                
                api.deleteGroup(deletableGroup.key)
                    .then(() => {
                        const deletableIndex = state.groupsMenuItems.indexOf(deletableGroup)
                        state.firstLvlStatements = state.firstLvlStatements.filter((statement) => statement.group !== deletableGroup)
                        state.groupsMenuItems.splice(deletableIndex, 1)
                    })
                    .catch(err => console.log(err))   
            }
        },
        SAVE_MARK(state, payload) {
            const student = payload[0]
            const mark = payload[1]

            student.marks.push(mark)
            
            api.saveMark(student, mark)
                .then(response => mark.key = response.data.id)
                .catch(err => {
                    student.marks.pop()
                    console.log(err)
                })
        },
        EDIT_MARK(state, payload) {
            const editedMark = payload[0]
            const oldMark = payload[1]

            api.editMark(editedMark.key, editedMark.value)
                .catch(err => {
                    editedMark.value = oldMark.value
                    console.log(err)
                })
        },
        SAVE_USER_INFO(state, payload) {
            state.user = payload
        },
        SAVE_GROUP_INFO(state, payload) { 
            state.groupsMenuItems = payload
        },
        SAVE_SUBJECTS_INFO(state, payload) {
            state.subjectsMenuItems = payload
        },
        SAVE_STATEMENTS_INFO(state, payload) {
            const statements = payload
            const subjects = []

            for (let i = 0; i < state.subjectsMenuItems.length; i += 2) {
                subjects.push(state.subjectsMenuItems[i])
            }

            statements.forEach(firstLvlStatement => {
                firstLvlStatement.group = state.groupsMenuItems
                    .find(group => group.key === firstLvlStatement.group.key)
                
                firstLvlStatement.secondLvlStatements
                    .forEach(secondLvlStatement => secondLvlStatement.subject = subjects
                        .find(subject => subject.key === secondLvlStatement.subject.key))
            })

            state.firstLvlStatements = statements
        }
    },
    actions: {
        loadUser({ commit }) {
            commit('SAVE_USER_INFO', jwt.getInMemoryUser)
        },
        async loadGroups({ commit }, commands) {
            return await api.getGroups(commands)
                .then(groups => commit('SAVE_GROUP_INFO', groups))
        },
        async loadSubjects({ commit }) {
            return await api.getSubjects()
                .then(subjects => commit('SAVE_SUBJECTS_INFO', subjects))
        },
        async loadFirstLvlStatements({ commit }) {
            return await api.getFirstLvlStatements()
                .then(firstLvlStatements => commit('SAVE_STATEMENTS_INFO', firstLvlStatements))
        },
    }
})

export default store