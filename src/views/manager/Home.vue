<template>
  <div>
    <div class="card" style="line-height: 30px; margin-bottom: 5px">
      <div>Welcome，{{ data.user.name }}!Have a nice day!</div>
    </div>
    <div style="display: flex">
      <div class="card" style="flex: 60%">
        <div style="font-size: 18px; font-weight: bold; padding: 10px 10px 20px 10px">System Announcements</div>
        <el-timeline>
          <el-timeline-item
              v-for="(item, index) in data.noticeData"
              :key="index"
              :timestamp="item.time"
          >
            <strong>{{ item.title }}:</strong> <br>{{ item.content }}
          </el-timeline-item>
        </el-timeline>
      </div>
      <div style="flex: 40%"></div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  noticeData:[]
})

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200'){
      data.noticeData = res.data
    }else {
      ElMessage.error(res.msg)
    }
  })
}

loadNotice()

</script>