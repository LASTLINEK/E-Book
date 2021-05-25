<template>
  <el-dialog title="编辑Book" :visible.sync="dialogFormVisible" v-dialogDrag width="35%">
    <el-form :model="form">
      <el-form-item label="Book ID" :label-width="formLabelWidth">
        <el-input v-model="form.id" autocomplete="off" class="formItem" placeholder="请输入Book id" disabled></el-input>
      </el-form-item>
      <el-form-item label="书名" :label-width="formLabelWidth">
        <el-input v-model="form.name" autocomplete="off" class="formItem" placeholder="请输入书名"></el-input>
      </el-form-item>
      <el-form-item label="作者" :label-width="formLabelWidth">
        <el-input v-model="form.author" autocomplete="off" class="formItem" placeholder="请输作者"></el-input>
      </el-form-item>
      <el-form-item label="简介" :label-width="formLabelWidth">
        <el-input v-model="form.note" autocomplete="off" class="formItem" placeholder="请输入简介"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button size="medium" @click="dialogFormVisible = false" >取 消</el-button>
      <el-button size="medium" type="primary" @click="editAdmin" :loading="loading">编辑</el-button>
    </div>
  </el-dialog>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator'
import service from '@/service/index'

interface Edit {
  form: Object
  formLabelWidth: String
  dialogFormVisible: Boolean
  loading: Boolean
}

@Component
export default class AddAdmin extends Vue implements Edit{
  @Prop() private editVisible!: Boolean
  @Prop() private row !: Object

  formLabelWidth = '120px'
  loading = false

  // 计算属性，拿到要修改的数据
  get form() {
    return this.row
  }

  // 计算属性，获得父组件传值
  get dialogFormVisible() {
    return this.editVisible
  }

  // set进行父组件状态的修改
  set dialogFormVisible(val) {
    !val && this.$emit('update:editVisible', false)
  }

  // 编辑数据并修改
  public editBook() {
    let params = this.form
    this.loading = true
    service.editBook(this.form['id'], this.form).then(res => {
      this.loading = false
      this.dialogFormVisible = false
      if (res.code == 200) {
        this.$message({
          message: '成功',
          type: 'success'
        })
      }
      this.$emit('flushList')  // 修改后刷新数据
    })
  }
}
</script>

<style lang="less" scoped>
.formItem {
  width: 200px;
  float: left
}
</style>
