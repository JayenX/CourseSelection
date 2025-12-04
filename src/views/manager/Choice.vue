<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="Please enter a name to search"></el-input>
      <el-button type="primary" @click="load">Search</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">Reset</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe>
        <el-table-column label="Course name" prop="name"></el-table-column>
        <el-table-column label="Teacher" prop="teacherName"></el-table-column>
        <el-table-column label="Student" prop="studentName"></el-table-column>
        <el-table-column label="Operation" align="center" width="160" v-if="data.user.role === 'STUDENT'">
          <template #default="scope">
            <el-button type="danger" @click="handleDelete(scope.row.id)">Cancel enrollment</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination background layout="prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" @current-change="load"/>
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import {reactive} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";


const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  pageNum: 1,
  pageSize: 5,
  total: 0,
  form: {},
  tableData: [],
  name: null
})

// Paginated queries
const load = () => {
  let teacherId = null
  let studentId = null
  if (data.user.role === 'TEACHER'){
    teacherId = data.user.id
  }
  if (data.user.role === 'STUDENT'){
    studentId = data.user.id
  }
  request.get('/choice/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      teacherId: teacherId,
      studentId: studentId
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}

// Delete
const handleDelete = (id) => {
  ElMessageBox.confirm('If you cancel your course enrollment, you may not be able to choose this course again, do you cancel it?', 'Cancel course enrollment', { type: 'warning' }).then(res => {
    request.delete('/choice/deleteById/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('The operation was successful')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

// Reset
const reset = () => {
  data.name = null
  load()
}


load()
</script>