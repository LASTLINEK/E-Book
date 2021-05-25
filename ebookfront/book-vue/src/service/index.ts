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
  bookContent: async (id, page) => {
    console.log(id)
    return await service.get(`${baseUrl}books/${id}/read/${page}`).then(res => res).catch(err => err)
  },
  logOut: async () => {
    return await service.get(`/logout`).then(res => res).catch(err => err)
  },
  deleteBook: async (id) => {
    return await service.delete(`${baseUrl}books/${id}`).then(res => res).catch(err => err)
  },
  createBook: async (param) => {
    return await service.post(`${baseUrl}books/add`, param).then(res => res).catch(err => err)
  },
  // 修改
  editBook: async (id, book) => {
    return await service.put(`${baseUrl}books/${id}`, book).then(res => res).catch(err => err)
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
