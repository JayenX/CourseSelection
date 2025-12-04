<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="Please enter the name of the college to search"></el-input>
      <el-button type="primary" @click="load">Search</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">Reset</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px" v-if="data.user.role === 'ADMIN'">
        <el-button type="primary" @click="handleAdd">New</el-button>
      </div>
      <el-table :data="data.tableData" stripe>
        <el-table-column label="Name" prop="name" class-name="no-break-word"></el-table-column>
        <el-table-column label="Introduction" prop="content" class-name="no-break-word"></el-table-column>
        <el-table-column label="Minimum credit requirements" prop="score"></el-table-column>
        <el-table-column label="Operation" align="center" width="160" v-if="data.user.role === 'ADMIN'">
          <template #default="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">Edit</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination background layout="prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" @current-change="load"/>
    </div>

    <el-dialog title="College information" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="120px" style="padding-right: 50px">
        <el-form-item label="Name" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Introduction" prop="content">
          <el-input type="textarea" :rows="4" v-model="data.form.content" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Minimum credit requirements" prop="score">
          <el-input v-model="data.form.score" autocomplete="off" />
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
  name: null
})

// Paginated queries
const load = () => {
  request.get('/college/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
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
  request.post('/college/add', data.form).then(res => {
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
  request.put('/college/update', data.form).then(res => {
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
    request.delete('/college/deleteById/' + id).then(res => {
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

<style>
.el-table .no-break-word .cell {
  white-space: normal;
  word-break: keep-all;
  overflow-wrap: break-word;
}
</style>