// 正式环境调用service（实际开发请查看该部分代码）
import service from '../util/request'
// mock数据直接调用axios
import axios from 'axios'

const qs = require('qs')
const baseUrl = 'http://localhost:8081/api/'

export default  {
  login: async (params: Object) => {
    console.log(params)
    return await service.post(`${baseUrl}login`, qs.stringify({params})).then(res => res).catch(err => err)
  },
  apply: async (params: Object) => {
    console.log(params)
    return await service.post(`${baseUrl}apply`, qs.stringify({params})).then(res => res).catch(err => err)
  },
  deploy: async (params: Object) => {
    console.log(params)
    return await service.post(`${baseUrl}deploy`, qs.stringify({params})).then(res => res).catch(err => err)
  },
  Getdeploy: async () => {
    return await service.get(`${baseUrl}deploy`, qs.stringify()).then(res => res).catch(err => err)
  },
  test: async (params: Object) => {
    console.log(params)
    return await service.post(`${baseUrl}test`, qs.stringify({params})).then(res => res).catch(err => err)
  },
  Gettest: async () => {
    return await service.get(`${baseUrl}test`, qs.stringify()).then(res => res).catch(err => err)
  },
  //流程审批
  approve: async () => {
    return await service.get(`${baseUrl}approve`, qs.stringify()).then(res => res).catch(err => err)
  },
  Proapprove: async (params: Object) => {
    console.log(params)
    return await service.post(`${baseUrl}approve`, qs.stringify({params})).then(res => res).catch(err => err)
  },
  review: async () => {
    return await service.get(`${baseUrl}DeployReview`, qs.stringify()).then(res => res).catch(err => err)
  },
  Proreview: async (params: Object) => {
    console.log(params)
    return await service.post(`${baseUrl}DeployReview`, qs.stringify({params})).then(res => res).catch(err => err)
  },
  testReview: async () => {
    return await service.get(`${baseUrl}testReview`, qs.stringify()).then(res => res).catch(err => err)
  },
  ProtestReview: async (params: Object) => {
    console.log(params)
    return await service.post(`${baseUrl}testReview`, qs.stringify({params})).then(res => res).catch(err => err)
  },
  // 修改
  change: async () => {
    return await service.get(`${baseUrl}change`, qs.stringify()).then(res => res).catch(err => err)
  },
  commit: async (params: Object) => {
    console.log(params)
    return await service.post(`${baseUrl}change`, qs.stringify({params})).then(res => res).catch(err => err)
  },
  getAdminList: async (params: any) => {
    console.log(params)
    return await axios.get(`${baseUrl}admin`, {params}).then(res => res.data).catch(err => err)
  },

  postAdminList: async (params: any) => {
    return await axios.post(`${baseUrl}admin`, { params }).then(res => res).catch(err => err)
  },

  putAdminList: async (params: any) => {
    return await axios.put(`${baseUrl}admin`, { params }).then(res => res).catch(err => err)
  },

  deleteAdminList: async (params: any) => {
    return await axios.delete(`${baseUrl}admin`, { params }).then(res => res).catch(err => err)
  },

  getProjectList: async (params: any) => {
    return await service.post(`${params.path}`, qs.stringify({params})).then(res => res).catch(err => err)
  },

  deleteProject: async (params: any) => {
    return await service.post(`/deleteProject`, qs.stringify({params})).then(res => res).catch(err => err)
  },

  getUserList: async (params: any) => {
    return await service.get(`/members`, qs.stringify({params})).then(res => res).catch(err => err)
  },

  postUserList: async (params: any) => {
    return await service.post(`/members`, qs.stringify({params})).then(res => res).catch(err => err)
  },

  putUserList: async (params: any) => {
    console.log(params)
    return await service.put(`/members`, qs.stringify({params})).then(res => res).catch(err => err)
  },

  putRightList: async (params: any) => {
    console.log(params)
    return await service.put(`/rights`, qs.stringify({params})).then(res => res).catch(err => err)
  },
  deleteUserList: async (params: any) => {
    console.log(params)
    return await service.post(`/deleteMembers`, qs.stringify({params})).then(res => res).catch(err => err)
  },

  getRightsList: async (params: any) => {
    return await service.get(`${params.path}`, qs.stringify({params})).then(res => res).catch(err => err)
  },

  postProjectList: async (params: any) => {
    return await axios.post(`${params.path}`, { params }).then(res => res).catch(err => err)
  },

  putProjectList: async (params: any) => {
    return await axios.put(`${params.path}`, { params }).then(res => res).catch(err => err)
  },

  deleteProjectList: async (params: any) => {
    return await axios.delete(`${params.path}`, { params }).then(res => res).catch(err => err)
  }
}
