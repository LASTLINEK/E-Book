<template>
  <div style="height: 100%">

    <el-container style="height: 100%;" height="vertical">
      <el-header>当前用户: {{ user }}
        <el-button @click="logout" style="float: right">退出登录</el-button>
      </el-header>
      <el-container>
        <el-aside width="200px">
          书名： {{ bookName }}
          <br>
          作者： {{ author }}
        </el-aside>
        <el-container>
          <el-main>{{ content }}</el-main>
          <el-footer>
            <el-button size="mini" @click="previousChapter()" v-bind:disabled="hasPrevious">上一章</el-button>
            <el-button size="mini" @click="nextChapter()" v-bind:disabled="hasNext">下一章</el-button>
          </el-footer>
        </el-container>
      </el-container>
    </el-container>

  </div>
</template>


<script>
import service from "@/service";

export default {
  name: "Read",
  data() {
    return {
      content: '',
      bookID: 1,
      page: 1,
      user: '',
      bookName: '',
      author: '',
      hasPrevious: Boolean,
      hasNext: Boolean
    }
  },

  methods: {
    previousChapter() {
      this.page = this.page - 1
      this.getContent()
    },
    nextChapter() {
      this.page = this.page + 1
      this.getContent()
    },
    logout(){
      service.logOut().then(res => {
      }).catch(err => {

      });
      this.$router.push('/login')
    },
    getContent() {
      service.bookContent(this.bookID, this.page).then(res => {
        this.content = res.texts
        this.bookName = res.book.name
        this.author = res.book.author
        this.hasPrevious = !res.hasPrev
        this.hasNext = !res.hasNext
      }).catch(err => {

      });
    }
  },

  created() {
    this.bookID = this.$route.params.id
    this.user = sessionStorage.getItem('name')
    this.getContent()
  }
}
</script>

<style scoped>
.el-header, .el-footer {
  background-color: #B3C0D1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  text-align: center;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
}

body > .el-container {
  margin-bottom: 40px;
}
#app,html,body,.el-container{
  height:100%;
}

</style>
