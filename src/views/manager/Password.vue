<template>
  <div style="width: 50%">
    <div class="card" style="padding: 30px">
      <el-form :model="data.user" label-width="200px" style="padding-right: 50px">
        <el-form-item label="Original password">
          <el-input v-model="data.user.password" show-password />
        </el-form-item>
        <el-form-item label="New password">
          <el-input v-model="data.user.newPassword" show-password />
        </el-form-item>
        <el-form-item label="Confirm the new password">
          <el-input v-model="data.user.confirmPasword" show-password />
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
import router from "@/router";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
})

// Store the currently modified user information in the backend database
const save = () => {
  if (data.user.newPassword !== data.user.confirmPasword) {
    ElMessage.error('Confirm that the new password is incorrect')
    return
  }
  request.put('/updatePassword', data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('The password was changed')
      // Clear the cache
      localStorage.removeItem('system-user')
      router.push('/login')
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>