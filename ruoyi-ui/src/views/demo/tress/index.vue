<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="中文树名" prop="treeName">
        <el-input
          v-model="queryParams.treeName"
          placeholder="请输入中文树名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="别名" prop="treeAlias">
        <el-input
          v-model="queryParams.treeAlias"
          placeholder="请输入别名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input
          v-model="queryParams.age"
          placeholder="请输入年龄"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['demo:tress:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['demo:tress:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['demo:tress:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
		  :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['demo:tress:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tressList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" v-if="false"/>
      <el-table-column label="古树标号" align="center" prop="treeNo" />
      <el-table-column label="图片" align="center" prop="pics">
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src=scope.row.pics
            fit="scale-down"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="中文树名" align="center" prop="treeName" />
      <el-table-column label="阿拉丁名" align="center" prop="treeNameE" />
      <el-table-column label="别名" align="center" prop="treeAlias" />
      <el-table-column label="年龄" align="center" prop="age" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="坡向" align="center" prop="slopeDirection" />
      <el-table-column label="土壤名称" align="center" prop="soilName" />
      <el-table-column label="树木奇特性状" align="center" prop="peculiarCharacter" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['demo:tress:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['demo:tress:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改古树信息列对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="古树标号" prop="treeNo">
          <el-input v-model="form.treeNo" placeholder="请输入古树标号" />
        </el-form-item>
        <el-form-item label="图片">
          <imageUpload v-model="form.pics"/>
        </el-form-item>
        <el-form-item label="中文树名" prop="treeName">
          <el-input v-model="form.treeName" placeholder="请输入中文树名" />
        </el-form-item>
        <el-form-item label="阿拉丁名" prop="treeNameE">
          <el-input v-model="form.treeNameE" placeholder="请输入阿拉丁名" />
        </el-form-item>
        <el-form-item label="别名" prop="treeAlias">
          <el-input v-model="form.treeAlias" placeholder="请输入别名" />
        </el-form-item>
        <el-form-item label="科" prop="family">
          <el-input v-model="form.family" placeholder="请输入科" />
        </el-form-item>
        <el-form-item label="属" prop="genus">
          <el-input v-model="form.genus" placeholder="请输入属" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-input v-model="form.type" placeholder="请输入类型" />
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="form.age" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="详细地址" prop="detailedAddress">
          <el-input v-model="form.detailedAddress" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="管护单位" prop="careUnit">
          <el-input v-model="form.careUnit" placeholder="请输入管护单位" />
        </el-form-item>
        <el-form-item label="古树状态">
          <el-radio-group v-model="form.treeStatus">
            <el-radio
              v-for="dict in treeStatusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="浏览量" prop="viewsCount">
          <el-input v-model="form.viewsCount" placeholder="请输入浏览量" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="请输入经度" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="请输入纬度" />
        </el-form-item>
        <el-form-item label="高度" prop="highly">
          <el-input v-model="form.highly" placeholder="请输入高度" />
        </el-form-item>
        <el-form-item label="直径(胸围)" prop="diameter">
          <el-input v-model="form.diameter" placeholder="请输入直径(胸围)" />
        </el-form-item>
        <el-form-item label="生长势" prop="growthVigor">
          <el-input v-model="form.growthVigor" placeholder="请输入生长势" />
        </el-form-item>
        <el-form-item label="平均冠幅" prop="averageCanopy">
          <el-input v-model="form.averageCanopy" placeholder="请输入平均冠幅" />
        </el-form-item>
        <el-form-item label="东西冠幅" prop="dongXiCanopy">
          <el-input v-model="form.dongXiCanopy" placeholder="请输入东西冠幅" />
        </el-form-item>
        <el-form-item label="南北冠幅" prop="nanBeiCanopy">
          <el-input v-model="form.nanBeiCanopy" placeholder="请输入南北冠幅" />
        </el-form-item>
        <el-form-item label="海拔" prop="altitude">
          <el-input v-model="form.altitude" placeholder="请输入海拔" />
        </el-form-item>
        <el-form-item label="坡向" prop="slopeDirection">
          <el-input v-model="form.slopeDirection" placeholder="请输入坡向" />
        </el-form-item>
        <el-form-item label="坡度" prop="slopeDegree">
          <el-input v-model="form.slopeDegree" placeholder="请输入坡度" />
        </el-form-item>
        <el-form-item label="坡位" prop="slopePosition">
          <el-input v-model="form.slopePosition" placeholder="请输入坡位" />
        </el-form-item>
        <el-form-item label="土壤名称" prop="soilName">
          <el-input v-model="form.soilName" placeholder="请输入土壤名称" />
        </el-form-item>
        <el-form-item label="土壤紧密度" prop="soilCompactness">
          <el-input v-model="form.soilCompactness" placeholder="请输入土壤紧密度" />
        </el-form-item>
        <el-form-item label="生长环境" prop="growthEnvironment">
          <el-input v-model="form.growthEnvironment" placeholder="请输入生长环境" />
        </el-form-item>
        <el-form-item label="历史/详情" prop="historyDetails">
          <el-input v-model="form.historyDetails" placeholder="请输入历史/详情" />
        </el-form-item>
        <el-form-item label="树木奇特性状" prop="peculiarCharacter">
          <el-input v-model="form.peculiarCharacter" placeholder="请输入树木奇特性状" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTress, getTress, delTress, addTress, updateTress, exportTress } from "@/api/demo/tress";
import ImageUpload from '@/components/ImageUpload';

export default {
  name: "Tress",
  components: {
    ImageUpload,
  },
  data() {
    return {
	  //按钮loading
	  buttonLoading: false,
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 古树信息列表格数据
      tressList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 古树状态字典
      treeStatusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        treeName: undefined,
        treeAlias: undefined,
        age: undefined,
        address: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("tree_status").then(response => {
      this.treeStatusOptions = response.data;
    });
  },
  methods: {
    /** 查询古树信息列列表 */
    getList() {
      this.loading = true;
      listTress(this.queryParams).then(response => {
        this.tressList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 古树状态字典翻译
    treeStatusFormat(row, column) {
      return this.selectDictLabel(this.treeStatusOptions, row.treeStatus);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        treeNo: undefined,
        pics: undefined,
        treeName: undefined,
        treeNameE: undefined,
        treeAlias: undefined,
        family: undefined,
        genus: undefined,
        type: undefined,
        age: undefined,
        address: undefined,
        detailedAddress: undefined,
        careUnit: undefined,
        treeStatus: "0",
        viewsCount: undefined,
        longitude: undefined,
        latitude: undefined,
        createTime: undefined,
        updateTime: undefined,
        highly: undefined,
        diameter: undefined,
        growthVigor: undefined,
        averageCanopy: undefined,
        dongXiCanopy: undefined,
        nanBeiCanopy: undefined,
        altitude: undefined,
        slopeDirection: undefined,
        slopeDegree: undefined,
        slopePosition: undefined,
        soilName: undefined,
        soilCompactness: undefined,
        growthEnvironment: undefined,
        historyDetails: undefined,
        peculiarCharacter: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加古树信息列";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getTress(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改古树信息列";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateTress(this.form).then(response => {
              this.buttonLoading = false;
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTress(this.form).then(response => {
              this.buttonLoading = false;
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除古树信息列编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.loading = true;
          return delTress(ids);
        }).then(() => {
          this.loading = false;
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有古树信息列数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportTress(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
