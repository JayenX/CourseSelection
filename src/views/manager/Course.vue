<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="Please enter the course name to search"></el-input>
      <el-button type="primary" @click="load">Search</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">Reset</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px" v-if="data.user.role === 'ADMIN'">
        <el-button type="primary" @click="handleAdd">New</el-button>
      </div>
      <el-table :data="data.tableData" stripe>
        <el-table-column label="Name" prop="name" class-name="no-break-word"></el-table-column>
        <el-table-column label="Introduction" prop="content" show-overflow-tooltip></el-table-column>
        <el-table-column label="Credits" prop="score"></el-table-column>
        <el-table-column label="Teacher" prop="teacherName"></el-table-column>
        <el-table-column label="Class size" prop="num"></el-table-column>
        <el-table-column label="Class time" prop="time" class-name="no-break-word"></el-table-column>
        <el-table-column label="Location" prop="location"></el-table-column>
        <el-table-column label="Affiliated college" prop="collegeName" class-name="no-break-word"></el-table-column>
        <el-table-column label="Already selected" prop="alreadyNum"></el-table-column>
        <el-table-column label="Operation" align="center" width="160" v-if="data.user.role !== 'TEACHER'">
          <template #default="scope" v-if="data.user.role === 'ADMIN'">
            <el-button type="primary" @click="handleEdit(scope.row)">Edit</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">Delete</el-button>
          </template><template #default="scope" v-else>
          <el-button type="primary" @click="choiceCourse(scope.row)" :disabled="scope.row.num === scope.row.alreadyNum">Select</el-button>
        </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination background layout="prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" @current-change="load"/>
    </div>

    <el-dialog title="Course information" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="120px" style="padding-right: 50px">
        <el-form-item label="Name" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Introduction" prop="content">
          <el-input type="textarea" :rows="4" v-model="data.form.content" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Credits" prop="score">
          <el-input v-model="data.form.score" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Teacher" prop="teacherId">
          <el-select v-model="data.form.teacherId" placeholder="Please select a teacher">
            <el-option
                v-for="item in data.teacherData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Class size" prop="num">
          <el-input v-model="data.form.num" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Class time" prop="time">
          <el-input v-model="data.form.time" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Location" prop="location">
          <el-input v-model="data.form.location" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Affiliated college" prop="collegeName">
          <el-select v-model="data.form.collegeId" placeholder="Please select a college">
            <el-option
                v-for="item in data.collegeData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </span>
      </template>
    </el-dialog>

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
  formVisible: false,
  form: {},
  tableData: [],
  name: null,
  collegeData: [],
  teacherData: []
})

// Paginated queries
const load = () => {
  let teacherId = null
  if (data.user.role === 'TEACHER') {
    teacherId = data.user.id
  }
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      teacherId: teacherId,
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}

// Newly added
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

// Edit
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// Save what's new
const add = () => {
  request.post('/course/add', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('The operation was successful')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// Save edits
const update = () => {
  request.put('/course/update', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('The operation was successful')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// The pop-up window is saved
const save = () => {
  // If the data.form has an id, it is updated, and if it is not, it is added
  data.form.id ? update() : add()
}

// Delete
const handleDelete = (id) => {
  ElMessageBox.confirm('The data cannot be recovered after deletion, are you sure to delete it?', 'Delete confirmation', { type: 'warning' }).then(res => {
    request.delete('/course/deleteById/' + id).then(res => {
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

const loadCollege = () => {
  request.get('/college/selectAll').then(res => {
    if (res.code === '200') {
      data.collegeData = res.data
    }else {
      ElMessage.error(res.msg)
    }
  })
}

const loadTeacher = () => {
  request.get('/teacher/selectAll').then(res => {
    if (res.code === '200') {
      data.teacherData = res.data
    }else {
      ElMessage.error(res.msg)
    }
  })
}

const choiceCourse = (row) => {
  let courseData = JSON.parse(JSON.stringify(row))
  courseData.studentId = data.user.id
  request.post('/choice/add',courseData).then(res => {
    if (res.code === '200'){
      ElMessage.success('Course selection was successful')
      load()
    }else {
      ElMessage.error(res.msg)
    }
  })
}

load()
loadCollege()
loadTeacher()
</script>

<style>
.el-table .no-break-word .cell {
  white-space: normal;
  word-break: keep-all;
  overflow-wrap: break-word;
}
</style>
