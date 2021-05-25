<template>
  <div>
    <el-table
      :data="tableData"
      highlight-current-row
      :header-cell-style="{background:'rgba(255,218,185,0.4)',color:'#2F4F4F'}"
    >
      <el-table-column
        label="Book ID"
        prop="id"
        align="center"
        min-width="150">
      </el-table-column>
      <el-table-column
        label="书名"
        prop="name"
        align="center"
        min-width="150">
      </el-table-column>
      <el-table-column
        label="作者"
        prop="author"
        align="center"
        min-width="150">
      </el-table-column>
      <el-table-column
        label="简介"
        prop="note"
        align="center"
        min-width="150">
      </el-table-column>
      <el-table-column label="操作" width="250" align="center">
        <template slot-scope="scope">
          <el-button type="info" size="mini" @click="toRead(scope.$index, scope.row)">Read</el-button>
          <el-button type="danger" size="mini" @click="uploadBook(scope.$index, scope.row)" icon="el-icon-delete">上传</el-button>
          <el-button type="danger" size="mini" @click="downloadBook(scope.$index, scope.row)" icon="el-icon-delete">下载</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>


import service from '@/service/index'


export default {

  data() {
    return {
      tableData: [],
      id: 0,
  }
  },
  methods: {
    toRead(){
      this.$router.push('/books/' + this.id + '/read')
    },
    uploadBook(){

    },
    downloadBook(){

    }
  },
  created() {
    this.id = this.$route.params.id;
    service.book(this.id).then(res => {
      this.tableData = [{
        'id': res.id,
        'name': res.name,
        'author': res.author,
        'note': res.note
      }]
      console.log(this.tableData)
    }).catch(err => {

    });
  }
}
</script>

<style scoped>

</style>
