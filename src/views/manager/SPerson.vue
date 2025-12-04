<template>
  <div style="width: 50%">
    <div class="card" style="padding: 30px">
      <el-form :model="data.user" label-width="100px" style="padding-right: 50px">
        <div style="margin: 20px 0; text-align: center">
          <el-upload :show-file-list="false" class="avatar-uploader" :action="uploadUrl" :on-success="handleFileUpload">
            <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </div>
        <el-form-item label="Username">
          <el-input disabled v-model="data.user.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Name">
          <el-input v-model="data.user.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Gender" prop="sex">
          <el-select v-model="data.user.sex" placeholder="Please select a gender" style="width: 100%">
            <el-option label="Male" value="Male"></el-option>
            <el-option label="Female" value="Female"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Student ID">
          <el-input v-model="data.user.code" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="College">
          <el-input v-model="data.user.collegeName" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="Credits">
          <el-input v-model="data.user.score" autocomplete="off" disabled/>
        </el-form-item>
        <div style="text-align: center">
          <el-button type="primary" @click="save">Save</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue"
import request from "@/utils/request";
import {ElMessage} from "element-plus";

// The address of the interface to which the file is uploaded
const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'

const data = reactive({
  user: {},
})

const handleFileUpload = (file) => {
  data.user.avatar = file.data
}

const emit = defineEmits(["updateUser"])
// Store the currently modified user information in the backend database
const save = () => {
  request.put('/student/update', data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('The update was successful')
      //Store the updated user information in the cache
      localStorage.setItem('system-user', JSON.stringify(data.user))
      emit('updateUser')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadStudent = () => {
  let user = JSON.parse(localStorage.getItem('system-user') || '{}')
  request.get('/student/selectById/' + user.id ).then(res => {
    if (res.code === '200') {
      data.user = res.data
      localStorage.setItem('system-user', JSON.stringify(res.data))
    }else {
      ElMessage.error(res.msg)
    }
  })
}

loadStudent()

</script>

<style scoped>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>