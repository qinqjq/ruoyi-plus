import request from '@/utils/request'

// 查询古树信息列列表
export function listAll(query) {
  return request({
    url: '/demo/tress/all',
    method: 'get',
    params: query
  })
}

// 查询古树信息列列表
export function listTress(query) {
  return request({
    url: '/demo/tress/list',
    method: 'get',
    params: query
  })
}

// 查询古树信息列详细
export function getTress(id) {
  return request({
    url: '/demo/tress/' + id,
    method: 'get'
  })
}

// 新增古树信息列
export function addTress(data) {
  return request({
    url: '/demo/tress',
    method: 'post',
    data: data
  })
}

// 修改古树信息列
export function updateTress(data) {
  return request({
    url: '/demo/tress',
    method: 'put',
    data: data
  })
}

// 删除古树信息列
export function delTress(id) {
  return request({
    url: '/demo/tress/' + id,
    method: 'delete'
  })
}

// 导出古树信息列
export function exportTress(query) {
  return request({
    url: '/demo/tress/export',
    method: 'get',
    params: query
  })
}
