import axios from 'axios'
import auth from '../auth'
import jwt from '../jwt'
import mapper from '../util/markTypeMapper'

const api = {
    baseUrl: 'http://localhost:8081/api/v1',
    isLoginError: false,
    isRegistrationError: false,

    async login(loginBody) {
        await axios.post(`${this.baseUrl}/auth/login`, loginBody)
            .then(response => auth.authenticate(response.data.value))
            .catch(() => { this.isLoginError = true })
    },
    async registration(registrationBody) {
        await axios.post(`${this.baseUrl}/auth/registration`, registrationBody)
            .then(response => {
                if (response.status === 201) {
                    this.login({ username: registrationBody.username, password: registrationBody.password })
                }
            })
            .catch(() => this.isRegistrationError = true)
    },
    async getGroups(commands) {
        const convertedGroups = []

        await axios({ 
            method: 'get', 
            url: `${this.baseUrl}/groups`, 
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` }
        })
        .then(response => response.data
            .forEach(group => {
                const convertedGroup = {
                    key: `${group.id}`,
                    label: `${group.name} (${group.groupYear})`,
                    name: group.name,
                    icon: 'pi pi-folder',
                    year: group.groupYear,
                    createdAt: new Date(group.createdAt),
                    isArchived: group.isArchived,
                    items: [ 
                        { key: `${group.id}_students`, label: 'Учащиеся', icon: 'pi pi-users', items: [] },
                        { key: `${group.id}_add_student`, label: 'Добавить учащегося', icon: 'pi pi-plus', command: commands[2] },
                        { key: `${group.id}_open`, label: 'Открыть', icon: 'pi pi-folder-open', command: commands[0] },
                        { key: `${group.id}_rename`, label: 'Переименовать', icon: 'pi pi-pencil', command: commands[1] }
                    ]
                }

                group.students.forEach(student => {
                    const convertedStudent = {
                        key: student.id,
                        icon: 'pi pi-user',
                        label: student.name,
                        marks: []
                    }

                    student.marks.forEach(mark => {
                        convertedStudent.marks.push({
                            key: mark.id,
                            value: mark.value,
                            typeId: parseInt(mapper.fromStringToInt(mark.markType)),
                            secondLvlStatementKey: mark.secondLvlStatementId
                        })
                    })

                   convertedStudent.marks.sort((mark1, mark2) => mark1.typeId - mark2.typeId)

                    convertedGroup.items[0].items.push(convertedStudent)
                })

                convertedGroups.push(convertedGroup)
            }))

        return convertedGroups
    },
    async getSubjects() {
        const convertedSubjects = []

        await axios({
            method: 'get',
            url: `${this.baseUrl}/subjects`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` }
        })
        .then(response => response.data
            .forEach(subject => {
                convertedSubjects.push({
                    key: subject.id,
                    label: subject.name,
                    icon: 'pi pi-book'
                })

                if (response.data.indexOf(subject) !== response.data.length - 1) {
                    convertedSubjects.push({ separator: true })
                }
            }
        ))

        return convertedSubjects
    },
    async getFirstLvlStatements() {
        const convertedFirstLvlStatements = []

        await axios({
            method: 'get',
            url: `${this.baseUrl}/fls`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` }
        })
        .then(response => response.data
            .forEach(firstLvlStatement => {
                const convertedFirstLvlStatement = {
                    key: firstLvlStatement.id,
                    label: firstLvlStatement.name,
                    group: { key: `${firstLvlStatement.groupId}` },
                    secondLvlStatements: []
                }

                firstLvlStatement.secondLvlStatements
                    .forEach(secondLvlStatement => {
                        const convertedSecondLvlStatement = {
                            key: secondLvlStatement.id,
                            subject: { key: secondLvlStatement.subjectId }
                        }

                        convertedFirstLvlStatement.secondLvlStatements.push(convertedSecondLvlStatement)
                    })

                convertedFirstLvlStatements.push(convertedFirstLvlStatement)
            }
        ))

        return convertedFirstLvlStatements
    },
    async saveProfileChanges(userRenameBody) {
        await axios({
            method: "patch",
            url: `${this.baseUrl}/users/${jwt.getInMemoryUser.username}`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` },
            data: userRenameBody
        })
        .then(response => auth.authenticate(response.data.value))
        .catch(err => console.log(err))
    },
    async deleteStudent(studentId) {
        return await axios({
            method: 'delete',
            url: `${this.baseUrl}/students/${studentId}`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` }
        })
    },
    async renameStudent(studentId, newStudentName) {
        await axios({
            method: 'patch',
            url: `${this.baseUrl}/students/${studentId}`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` },
            data: { name: newStudentName }
        })
    },
    async saveSubject(subjectName) {
        return await axios({
            method: 'post',
            url: `${this.baseUrl}/subjects`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` },
            data: { name: subjectName }
        })
    },
    async renameSubject(subjectId, newSubjectName) {
        return await axios({
            method: 'patch',
            url: `${this.baseUrl}/subjects/${subjectId}`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` },
            data: { name: newSubjectName }
        })
    },
    async deleteSubject(subjectId) {
        await axios({
            method: 'delete',
            url: `${this.baseUrl}/subjects/${subjectId}`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` }
        })
    },
    async renameGroup(groupId, newGroupName) {
        await axios({
            method: 'patch',
            url: `${this.baseUrl}/groups/${groupId}`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` },
            data: { name: newGroupName }
        })
    },
    async saveGroup(newGroupName, newGroupYear) {
        return await axios({
            method: 'post',
            url: `${this.baseUrl}/groups`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` },
            data: { 
                name: newGroupName,
                groupYear: newGroupYear
            }
        })
    },
    async deleteFirstLvlStatement(statementId) {
        await axios({
            method: 'delete',
            url: `${this.baseUrl}/fls/${statementId}`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` }
        })
    },
    async saveFirstLvlStatement(newStatementName, newStatementSubjects, newStatementGroup) {
        const flsCreationBody = {
            name: newStatementName,
            subjects: newStatementSubjects.map(subject => {
                return { id: subject.key }
            }),
            groupId: newStatementGroup.key
        }

        return await axios({
            method: 'post',
            url: `${this.baseUrl}/fls`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` },
            data: flsCreationBody
        })
    },
    async renameStatement(statementId, newStatementName) {
        await axios({
            method: 'patch',
            url: `${this.baseUrl}/fls/${statementId}`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` },
            data: { name: newStatementName }
        })
    },
    async saveStudent(newGroupId, newStudentName) {
        return await axios({
            method: 'post',
            url: `${this.baseUrl}/students`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` },
            data: { groupId: newGroupId, name: newStudentName }
        })
    },
    async archiveGroup(groupId) {
        await axios({
            method: 'patch',
            url: `${this.baseUrl}/groups/archive/${groupId}`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` }
        })
    },
    async deleteGroup(groupId) {
        await axios({
            method: 'delete',
            url: `${this.baseUrl}/groups/${groupId}`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` }
        })
    },
    async saveMark(student, mark) {
        return await axios({
            method: 'post',
            url: `${this.baseUrl}/marks`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` },
            data: { 
                studentId: student.key,
                secondLvlStatementId: mark.secondLvlStatementKey,
                value: mark.value,
                markType: mark.typeId
            }
        })
    },
    async editMark(markId, newValue) {
        return await axios({
            method: 'patch',
            url: `${this.baseUrl}/marks/${markId}`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` },
            data: { value: newValue }
        })
    },
    async logoutUser() {
        return await axios({
            method: 'post',
            url: `${this.baseUrl}/auth/logout`,
            headers: { 'Authorization': `Bearer ${jwt.getInMemoryToken}` }
        })
    }
}

export default api