<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="Please enter the student's name to search"></el-input>
      <el-button type="primary" @click="load">Search</el-button>
      <el-button type="info" @click="reset">Reset</el-button>
    </div>
    <div class="card">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">New</el-button>
      </div>
      <el-table :data="data.tableData" stripe>
        <el-table-column label="Username" prop="username"></el-table-column>
        <el-table-column label="Name" prop="name"></el-table-column>
        <el-table-column label="Avatar" prop="avatar">
          <template v-slot="scope">
            <el-image :src="scope.row.avatar" style="width: 40px; height: 40px; border-radius: 50%"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="Gender" prop="sex"></el-table-column>
        <el-table-column label="Student ID" prop="code"></el-table-column>
        <el-table-column label="Credits" prop="score"></el-table-column>
        <el-table-column label="Affiliated college" prop="collegeName" class-name="no-break-word"></el-table-column>
        <el-table-column label="Role" prop="role"></el-table-column>
        <el-table-column label="Operation" align="center" width="160">
          <template #default="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">Edit</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination background layout="prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" @current-change="changePage"/>
    </div>

    <el-dialog title="Student information" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="120px" style="padding-right: 50px">
        <el-form-item label="Avatar" prop="avatar">
          <el-upload :action="uploadUrl" list-type="picture" :on-success="handleImgSuccess">
            <el-button type="primary">Upload an image</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="Username" prop="username">
          <el-input v-model="data.form.username" autocomplete="off" placeholder="Please enter the username"/>
        </el-form-item>
        <el-form-item label="Name" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="Please enter the name"/>
        </el-form-item>
        <el-form-item label="Gender" prop="sex">
          <el-select v-model="data.form.sex" placeholder="Please select a gender" style="width: 100%">
            <el-option label="Male" value="Male"></el-option>
            <el-option label="Female" value="Female"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Student ID" prop="code">
          <el-input v-model="data.form.code" autocomplete="off" placeholder="Please enter the student ID"/>
        </el-form-item>
        <el-form-item label="Affiliated college" prop="collegeId">
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
  import {reactive} from "vue";
  import request from "@/utils/request";
  import {ElMessage, ElMessageBox} from "element-plus";

  //The API for file upload
  const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'

  const data = reactive({
    formVisible: false,
    form: {},
    tableData: [],
    pageNum: 1,
    pageSize: 5,
    total: 0,
    name: null,
    collegeData: []
  })

  const load = () =>{
    request.get('/student/selectPage',{
      params:{
        pageNum: data.pageNum,
        pageSize: data.pageSize,
        name: data.name
      }
    }).then(res => {
      if (res.code === '200'){
        data.tableData = res.data?.list
        data.total = res.data?.total
      }else{
        ElMessage.error(res.msg)
      }
    })
  }

  const changePage = (pageNum) => {
    data.pageNum = pageNum
    load()
  }

  const handleAdd = () => {
    data.form = {}
    data.formVisible = true
  }

  const handleEdit = (row) => {
    data.form = JSON.parse(JSON.stringify(row))
    data.formVisible = true
  }

  const handleDelete = (id) => {
    ElMessageBox.confirm('The data cannot be recovered after deletion, are you sure to delete it?', 'Delete confirmation', { type: 'warning' }).then(res => {
      request.delete('/student/deleteById/' + id).then(res => {
        if (res.code === '200') {
          ElMessage.success('The operation was successful')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }).catch(err => {})
  }

  const add = () => {
    request.post('/student/add', data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success('The operation was successful')
        data.formVisible = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }

  const update = () => {
    request.put('/student/update', data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success('The operation was successful')
        data.formVisible = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }

  const save = () => {
    data.form.id ? update() : add()
  }

  const reset = () => {
    data.name = null
    load()
  }

  const handleImgSuccess = (res) => {
    data.form.avatar = res.data
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

  load()
  loadCollege()
</script>

<style>
.el-table .no-break-word .cell {
  white-space: normal;
  word-break: keep-all;
  overflow-wrap: break-word;
}
</style>