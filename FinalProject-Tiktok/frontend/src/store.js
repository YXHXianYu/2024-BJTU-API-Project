import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const mutations = {
  // 将数据存入sessionStorage
  saveDataToSessionStorage(state, data) {
    state.data = data;
    sessionStorage.setItem('data', JSON.stringify(data));
  }
};

const getters = {
  // 从sessionStorage中获取数据
  getDataFromSessionStorage: state => {
    if (!state.data) {
      // 如果state中没有数据，则尝试从sessionStorage中读取
      const data = sessionStorage.getItem('data');
      if (data) {
        state.data = JSON.parse(data);
      }
    }
    return state.data;
  }
};

const state = {
  data: null
};

export default {
  state,
  mutations,
  actions,
  getters
};