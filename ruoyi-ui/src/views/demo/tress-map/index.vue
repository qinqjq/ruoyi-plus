<template>
  <div class="app-container">
    <el-container>
      <el-header style="background: #c4c4ea">Header</el-header>
      <el-container>
        <el-aside width="500px" style="margin: 0;padding: 0">
          <el-table
            :data="tressList"
            :show-header="false"
            @cell-click="tableClock"
            style="width: 100%">
            <el-table-column
              type="index"
              width="15">
            </el-table-column>
            <el-table-column
              prop="treeName"
              label="姓名"
              width="100">
            </el-table-column>
            <el-table-column
              prop="address"
              label="地址">
            </el-table-column>
          </el-table>
        </el-aside>
        <el-main style="padding: 0">
          <baidu-map
            id="allmap"
            :scroll-wheel-zoom="true"
            @ready="mapReady">
            <bm-marker v-for="(item,i) in points" :zIndex="item.id" :position="item" :key="i" animation="BMAP_ANIMATION_DROP" @click="markerClick" :icon="icon"/>
          </baidu-map>
        </el-main>
      </el-container>
    </el-container>
    <el-dialog
      title="信息"
      :visible.sync="open"
      width="80%">

      <el-row>
        <el-col :span="18">
          <el-descriptions :column="2">
            <el-descriptions-item label="古树标号">
              {{form.treeNo}}
            </el-descriptions-item>
            <el-descriptions-item label="中文树名">
              {{form.treeName}}
            </el-descriptions-item>
            <el-descriptions-item label="阿拉丁名">
              {{form.treeNameE}}
            </el-descriptions-item>
            <el-descriptions-item label="别名">
              {{form.treeAlias}}
            </el-descriptions-item>
            <el-descriptions-item label="科">
              {{form.family}}
            </el-descriptions-item>
            <el-descriptions-item label="属">
              {{form.genus}}
            </el-descriptions-item>
            <el-descriptions-item label="类型">
              {{form.type}}
            </el-descriptions-item>
            <el-descriptions-item label="年龄">
              {{form.age}}
            </el-descriptions-item>
            <el-descriptions-item label="地址">
              {{form.address}}
            </el-descriptions-item>
            <el-descriptions-item label="详细地址">
              {{form.detailedAddress}}
            </el-descriptions-item>
            <el-descriptions-item label="管护单位">
              {{form.careUnit}}
            </el-descriptions-item>
            <el-descriptions-item label="古树状态">
              {{form.treeStatus}}
            </el-descriptions-item>
            <el-descriptions-item label="浏览量">
              {{form.viewsCount}}
            </el-descriptions-item>
            <el-descriptions-item label="高度">
              {{form.highly}}
            </el-descriptions-item>
            <el-descriptions-item label="直径(胸围)">
              {{form.diameter}}
            </el-descriptions-item>
            <el-descriptions-item label="生长势">
              {{form.growthVigor}}
            </el-descriptions-item>
            <el-descriptions-item label="平均冠幅">
              {{form.averageCanopy}}
            </el-descriptions-item>
            <el-descriptions-item label="东西冠幅">
              {{form.dongXiCanopy}}
            </el-descriptions-item>
            <el-descriptions-item label="南北冠幅">
              {{form.nanBeiCanopy}}
            </el-descriptions-item>
            <el-descriptions-item label="海拔">
              {{form.altitude}}
            </el-descriptions-item>
            <el-descriptions-item label="坡向">
              {{form.slopeDirection}}
            </el-descriptions-item>
            <el-descriptions-item label="坡度">
              {{form.slopeDegree}}
            </el-descriptions-item>
            <el-descriptions-item label="坡位">
              {{form.slopePosition}}
            </el-descriptions-item>
            <el-descriptions-item label="土壤名称">
              {{form.soilName}}
            </el-descriptions-item>
            <el-descriptions-item label="土壤紧密度">
              {{form.soilCompactness}}
            </el-descriptions-item>
            <el-descriptions-item label="生长环境">
              {{form.growthEnvironment}}
            </el-descriptions-item>
            <el-descriptions-item label="历史/详情">
              {{form.historyDetails}}
            </el-descriptions-item>
            <el-descriptions-item label="树木奇特性状">
              {{form.peculiarCharacter}}
            </el-descriptions-item>
          </el-descriptions>
        </el-col>
        <el-col :span="6"><el-image :src="form.pics"/></el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="open = false">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<style>
  #allmap{
    height: 600px;
    width: 100%;
    margin: 0 auto;
  }
</style>
<script>
import { listAll,listTress, getTress, delTress, addTress, updateTress, exportTress } from "@/api/demo/tress";
import BaiduMap from 'vue-baidu-map'
import theme6 from '@/assets/images/old-tress.png'

export default {
  name: "TressMap",
  data() {
    return {
      icon:{
        url: theme6,
        size: {width: 30, height: 30},
        opts: {
          imageSize: {width: 30, height: 30}
        }
      },
      point: "",
      points: [],
      tressList: [],
      map: {},
      open: false,
      form: {}
    };
  },
  created() {
  },
  methods: {
    mapReady({ BMap, map }) {
      this.map = map;
      // 选择一个经纬度作为中心点
      this.point = new BMap.Point(113.27, 23.13);
      map.centerAndZoom(this.point, 12);

      listAll().then(response => {
        this.tressList = response.data;
        const points = [];
        this.tressList.forEach((value, index) => {
          const point = {lng: value.longitude, lat: value.latitude, id: value.id};
          points.push(point);
        })
        this.points = points;
      });
    },
    markerClick({type,target}) {
      console.info(type);
      console.info(target);
      const a = this.tressList.filter((a) => {
        return a.id == target.zIndex
      });
      console.info(a);
      if (a.length > 0) {
        this.form = a[0];
      }
      this.open = true;
    },
    tableClock(row, column, cell, event) {
      var point = new BMap.Point(row.longitude, row.latitude);
      this.map.centerAndZoom(point,12);
    }
  }
};
</script>
