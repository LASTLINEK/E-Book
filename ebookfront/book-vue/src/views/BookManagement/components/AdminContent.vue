<template>
  <div class="content">
    <el-table
    :data="tableData"
    highlight-current-row
    :header-cell-style="{background:'rgba(255,218,185,0.4)',color:'#2F4F4F'}"
    @sort-change='sortChange'
    v-loading="listLoading"
    border>
      <el-table-column
        label="Book ID"
        prop="id"
        align="center"
        min-width="150"
        sortable='custom'>
        <template slot-scope='scope'>
        <router-link v-bind:to="'/books/'+scope.row.id">{{scope.row.id}}</router-link>
        </template>
      </el-table-column>
      <el-table-column
        label="Book Name"
        prop="name"
        align="center"
        min-width="150">
        <template slot-scope='scope'>
          <router-link v-bind:to="'/books/'+scope.row.id">{{scope.row.name}}</router-link>
        </template>
      </el-table-column>
      <el-table-column
        label="Author"
        prop="author"
        align="center"
        min-width="150">
      </el-table-column>
      <el-table-column
        label="Note"
        prop="note"
        align="center"
        min-width="150">
      </el-table-column>
      <el-table-column label="Edit" width="250" align="center">
        <template slot-scope="scope">
          <el-button type="info" size="mini" @click="editBook(scope.$index, scope.row)">edit</el-button>
          <el-button type="danger" size="mini" @click="handleDel(scope.$index, scope.row)" icon="el-icon-delete">delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑子组件，点击编辑按钮可见 -->
    <edit-admin :editVisible.sync="editVisible" :row="row" @flushList='flushList'></edit-admin>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import EditAdmin from './EditAdmin/EditAdmin.vue'
import service from '@/service/index'
import ro from "element-ui/src/locale/lang/ro";

@Component({
  components: { EditAdmin }
})
export default class AdminContent extends Vue{
  @Prop() private tableData!: Array<object>
  @Prop() private listLoading!: Boolean

  editVisible = false
  focused = false
  row = {}

  // 页面点击排序按钮
  public sortChange(column) {
    let { prop, order } = column
    if (order === 'descending') {
      this.tableData.sort((a, b) => b["id"] - a["id"])
    } else {
      this.tableData.sort((a, b) => a["id"] - b["id"])
    }
  }

  public handleDel(index, row) {
    let params = row.id
    service.deleteBook(params).then(res => {
      let code = res.code
      let msg = ''
      console.log(code)
      if (code === 200) {
        msg = '删除成功'
      } else {
        msg = '删除失败'
      }
      this.$message({
        message: msg,
        type: 'success'
      })
      this.$emit('contentFlush')
    })
  }

  public editBook(index, row) {
    this.row = row
    this.editVisible = true
  }

  // 当修改完成需要刷新数据（会返回最顶层）
  public flushList() {
    this.$emit('contentFlush')
  }
}
</script>

<style lang="less" scoped>
.content {
  flex: 1 0 auto;
  border: 1px solid rgba(	176,196,222, 1);
  width: 100%;
  margin: 20px 0;
  overflow: auto;
}

.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

.el-table {
  width: 100%;
  text-align: center
}
</style>
