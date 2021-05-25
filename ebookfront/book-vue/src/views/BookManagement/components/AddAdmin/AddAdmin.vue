<template>
  <el-dialog title="添加Book" :visible.sync="dialogFormVisible" v-dialogDrag width="35%">
    <el-form :model="form">
      <el-form-item label="名字" :label-width="formLabelWidth">
        <el-input v-model="form.name" autocomplete="off" class="formItem" placeholder="请输入书名"></el-input>
      </el-form-item>
      <el-form-item label="作者" :label-width="formLabelWidth">
        <el-input v-model="form.author" autocomplete="off" class="formItem" placeholder="请输入作者"></el-input>
      </el-form-item>
      <el-form-item label="简介" :label-width="formLabelWidth">
        <el-input v-model="form.note" autocomplete="off" class="formItem" placeholder="请输入简介"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button size="medium" @click="dialogFormVisible = false" >取 消</el-button>
      <el-button size="medium" type="primary" @click="addAdmin">添加</el-button>
    </div>
  </el-dialog>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator'
import service from '@/service/index'

interface Add {
  form: Object
  formLabelWidth: String
  dialogFormVisible: Boolean
}

@Component
export default class AddAdmin extends Vue implements Add{
  @Prop() private visible!: Boolean

  form = {
    name: '',
    author: '',
    note: '',
  }
  formLabelWidth = '120px'


  // 通过计算属性获取父级组件传值，并进行form表单清空
  get dialogFormVisible() {
    if (this.visible) {
      for (let key in this.form) {
        this.form[key] = ''
      }
    }
    return this.visible
  }

  set dialogFormVisible(val) {
    !val && this.$emit('update:visible', false)  // 更新父组件状态，注意.sync修饰符
  }

  public addAdmin() {
    service.createBook(this.form).then(res => {
      this.dialogFormVisible = false
      console.log(res)
      let code = res.code
      if (code == 200) {
        this.$message({
          message: '增加成功',
          type: 'success'
        })
      }
      this.$emit('getAdmin')
    })
  }
}
</script>

<style lang="less" scoped>
.formItem {
  width: 250px;
  float: left;
}
</style>
