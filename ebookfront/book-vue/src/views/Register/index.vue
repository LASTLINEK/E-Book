<template>
  <div class="login">
    <vue-particles
      color="#dedede"
      :particleOpacity="0.8"
      :particlesNumber="100"
      shapeType="circle"
      :particleSize="2"
      linesColor="#409EFF"
      :linesWidth="2"
      :lineLinked="true"
      :lineOpacity="0.4"
      :linesDistance="150"
      :moveSpeed="3"
      :hoverEffect="true"
      hoverMode="grab"
      :clickEffect="true"
      clickMode="push"
    >
    </vue-particles>
    <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="0px" class="loginForm">
      <h1 class="loginTitle">全流程协同管理系统</h1>
      <el-form-item prop="name">
        <el-input type="text" v-model="ruleForm.name" placeholder="账号" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="pass">
        <el-input type="password" v-model="ruleForm.pass" placeholder="密码" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="pass">
        <el-input type="password" v-model="ruleForm.pass" placeholder="确认密码" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button class="loginButton" type="info" @click="submitForm('ruleForm')" :loading="logining">确认注册</el-button>
        <el-button class="registerButton" type="info" @click="gotoLogin">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import service from '@/service/index'
import { mapGetters, mapActions } from 'vuex'
import { asyncRoutes } from '@/router/index'

export default {
  data() {
    return {
      logining: false,
      ruleForm: {
        name: '',
        pass: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        pass: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      },
      remember: false
    };
  },

  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let params = Object.assign({}, this.ruleForm)
          this.logining = true;
          service.login(params).then(res => {
            let { code, msg = '', role = '', result = {} } = res['data'];
            if (true) {
              this.$message.success("注册成功")
              this.$router.push('/login')
              this.$store.dispatch('app/UpdateRememberPass', this.remember)
            } else {
              this.$message({
                message: msg,
                type: 'error',
                duration: 1000
              })
            }
            this.logining = false
          }).catch(err => {
            this.$message({
              message: err,
              type: 'error',
              duration: 1000
            })
            console.log(err)
          });
        } else {
          this.$message({
            message: 'error submit!!',
            type: 'error',
            duration: 1000
          })
          return false;
        }
      });
    },
    gotoLogin() {
      this.$router.push('/login')
    },

    // 判断是否是移动端打开
    _isMobile() {
      let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
      return flag;
    }

  },

  mounted() {
    if (this._isMobile()) {
      this.$store.dispatch('app/UpdateIsFold', true)  // 移动端打开则折叠侧边栏
    }
  }
}
</script>

<style scoped lang="less">
.login {
  font-family: Verdana, Arial, Helvetica, sans-serif;
  font-weight: bold;
  display: flex;
  flex-flow: column nowrap;
  align-items: center;
  justify-content: center;
  height: 100%;
  background-color: #1a1a1a;
  //background-image: url('../../../public/img/background.jpg');
  background-size: 100% 100%;
  &Form {
    -webkit-border-radius: 20px;
    border-radius: 20px;
    -moz-border-radius: 20px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 50px 20px 50px;
    background-image: linear-gradient(to right, rgb(120, 133, 146), rgb(99, 105, 123));
    border: 1px solid #eaeaea;
    // box-shadow: 0 0 5px #AFEEEE;
    position: relative;
    z-index: 9;
  }
  &Title {
    margin: 0 0 20px 0;
    text-align: center;
    font-size: 1.8rem;
  }
  &Button {
    width: 50%;
  }
  &Rem {
    display: flex;
    flex-flow: row nowrap;
    align-items: flex-start;
    margin-bottom: 20px;
    // border: 1px solid red
  }
  /*粒子特效*/
  #particles-js{
    width: 100%;
    height: 100%;
    position: absolute;
  }
}

</style>
