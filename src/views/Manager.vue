<template>
  <div>
    <div style="height: 60px; background-color: #fff; display: flex; align-items: center; border-bottom: 1px solid #ddd">
      <div style="flex: 1">
        <div style="padding-left: 20px; display: flex; align-items: center">
          <img src="@/assets/imgs/logo.png" alt="" style="width: 40px">
          <div style="font-weight: bold; font-size: 24px; margin-left: 5px">Course Selection Management System</div>
        </div>
      </div>
      <div style="width: fit-content; padding-right: 10px; display: flex; align-items: center;">
        <img style="width: 40px; height: 40px; border-radius: 50%" :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="">
        <span style="margin-left: 5px">{{ data.user.name }}</span>
      </div>
    </div>

    <div style="display: flex">
      <div style="width: 245px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
        <el-menu
            router
            style="border: none"
            :default-active="router.currentRoute.value.path"
            :default-openeds="['/home', '2' ,'3']"
        >
          <el-menu-item index="/home">
            <el-icon><HomeFilled /></el-icon>
            <span>Home Page</span>
          </el-menu-item>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><Promotion /></el-icon>
              <span>Information Management</span>
            </template>
            <el-menu-item index="/notice" v-if="data.user.role === 'ADMIN'"><el-icon><Bell /></el-icon><span>Notice Information</span></el-menu-item>
            <el-menu-item index="/college"><el-icon><OfficeBuilding /></el-icon><span>College Information</span></el-menu-item>
            <el-menu-item index="/speciality"><el-icon><DataBoard /></el-icon><span>Speciality Information</span></el-menu-item>
            <el-menu-item index="/course"><el-icon><Notebook /></el-icon><span>Course Information</span></el-menu-item>
            <el-menu-item index="/choice"><el-icon><Collection /></el-icon><span>Selection Information</span></el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Avatar /></el-icon>
              <span>User Management</span>
            </template>
            <el-menu-item index="/admin"><el-icon><User /></el-icon><span>Admin Information</span></el-menu-item>
            <el-menu-item index="/teacher"><el-icon><User /></el-icon><span>Teacher Information</span></el-menu-item>
            <el-menu-item index="/student"><el-icon><User /></el-icon><span>Student Information</span></el-menu-item>
          </el-sub-menu>
          <el-menu-item v-if="data.user.role === 'ADMIN'" index="/person"><el-icon><UserFilled /></el-icon><span>Personal Data</span></el-menu-item>
          <el-menu-item v-if="data.user.role === 'TEACHER'" index="/tPerson"><el-icon><UserFilled /></el-icon><span>Personal Data</span></el-menu-item>
          <el-menu-item v-if="data.user.role === 'STUDENT'" index="/sPerson"><el-icon><UserFilled /></el-icon><span>Personal Data</span></el-menu-item>
          <el-menu-item index="/password">
            <el-icon><Tools /></el-icon>
            <span>Change Password</span>
          </el-menu-item>
          <el-menu-item index="login" @click="logout">
            <el-icon><CircleCloseFilled /></el-icon>
            <span>Log out</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div style="flex: 1; width: 0; background-color: #f8f8ff; padding: 10px">
        <router-view @updateUser="updateUser" />
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}')
})

if (!data.user?.id) {
  ElMessage.error('Please log in！')
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('system-user') || '{}')
}

const logout = () => {
  ElMessage.success('The exit was successful')
  localStorage.removeItem('system-user')
  router.push('/login')
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #e0edfd !important;
}
.el-menu-item:hover {
  color: #1967e3;
}
:deep(th)  {
  color: #333;
}
</style>