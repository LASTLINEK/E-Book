// 正式环境调用service（实际开发请查看该部分代码）
import service from '../util/request'
// mock数据直接调用axios
import axios from 'axios'

const qs = require('qs')
const baseUrl = '/api/'

export default  {
  login: async (params: Object) => {
    console.log(params)
    return await service.post(`/login`, qs.stringify(params)).then(res => res).catch(err => err)
  },
  book: async (params: Object) => {
    console.log(params)
    return await service.get(`${baseUrl}books/${params}`).then(res => res).catch(err => err)
  },
  apply: async (params: Object) => {
    console.log(params)
    return await service.post(`${baseUrl}apply`, qs.stringify({params})).then(res => res).catch(err => err)
  },
  //流程审批
  approve: async () => {
    return await service.get(`${baseUrl}approve`, qs.stringify()).then(res => res).catch(err => err)
  },
  review: async () => {
    return await service.get(`${baseUrl}DeployReview`, qs.stringify()).then(res => res).catch(err => err)
  },
  // 修改
  change: async () => {
    return await service.get(`${baseUrl}change`, qs.stringify()).then(res => res).catch(err => err)
  },
  commit: async (params: Object) => {
    console.log(params)
    return await service.post(`${baseUrl}change`, qs.stringify({params})).then(res => res).catch(err => err)
  },

  postAdminList: async (params: any) => {
    return await axios.post(`${baseUrl}admin`, { params }).then(res => res).catch(err => err)
  },

  putAdminList: async (params: any) => {
    return await axios.put(`${baseUrl}admin`, { params }).then(res => res).catch(err => err)
  },
  getProjectList: async () => {
    return await service.get(`${baseUrl}books`).then(res => res).catch(err => err)
  },

  deleteProject: async (params: any) => {
    return await service.post(`/deleteProject`, qs.stringify({params})).then(res => res).catch(err => err)
  },
}
