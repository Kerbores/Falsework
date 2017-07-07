<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="fa fa-home"></i> 首页</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="fa fa-bank"></i> 机构</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="fa fa-list"></i> 机构列表</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i :class="branch.id == 0 ? 'fa fa-plus':'fa fa-edit'"></i> {{this.branch.id == 0 ? '添加机构':'编辑机构'}}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-row>
            <el-col :span="20" :offset="2">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="line-height: 36px;">{{this.branch.id == 0 ? '添加机构':'编辑机构'}}</span>
                    </div>
                    <el-form ref="form" :model="branch" :rules="rules" label-width="80px">
                        <el-form-item label="上级" prop="parentId">
                            <el-tree :data="nodes" show-checkbox check-strictly lazy :load="loadNode" node-key="id" ref="tree" highlight-current :props="defaultProps" @check-change="check">
                            </el-tree>
                        </el-form-item>
                        <el-form-item label="机构名称" prop="name">
                            <el-input v-model="branch.name"></el-input>
                        </el-form-item>
                        <el-form-item label="机构描述" prop="description">
                            <el-input type="textarea" v-model="branch.description"></el-input>
                        </el-form-item>
                        <el-form-item label="机构地址">
                            <b-map-component :ak="ak" @notify="notify" :center='mapCenter'></b-map-component>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitForm('form')">提交</el-button>
                            <el-button @click="resetForm('form')">重置</el-button>
                        </el-form-item>
                    </el-form>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import axios from 'axios';
import moment from 'moment'
import BMapComponent from '../bdmap/BaiduMap.vue'
export default {
    data() {
        return {
            defaultProps: {
                children: 'children',
                label: 'name'
            },
            ak: 'CRHkMGE7Db1USNSyFXqVDmdv',
            nodes: [],
            branch: {
                name: '',
                description: '',
                parentId: 0
            },
            rules: {
                name: [
                    { required: true, message: '请输入活动名称', trigger: 'blur' },
                    { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
                ],
                description: [
                    { required: true, message: '请选择活动区域', trigger: 'change' }
                ]
            },
            pager: {
                dataList: [],
                pager: {
                    pageCount: 0,
                    pageNumber: 1,
                    pageSize: 15,
                    recordCount: 0
                },
                paras: {
                    key: ''
                }
            }
        }
    },
    computed: {
        mapCenter() {
            return {
                lng: this.branch.longitude,
                lat: this.branch.latitude
            }
        }
    },
    methods: {
        check(node, s, l) {
            if (this.$refs.tree.getCheckedNodes().length > 1) {
                this.$message('只能选择一个父节点');
                this.$refs.tree.setChecked(node, false);

            }
            if (node.id == this.branch.id) {
                this.$message('不能选择自己作为父节点');
                this.$refs.tree.setChecked(node, false);
                return;
            }
            this.branch.parentId = node.id;
        },
        loadNode(node, resolve) {
            if (node.data.id) {
                this.get('/branch/sub/' + node.data.id, result => {
                    resolve(result.data.branchs)
                })
            }
        },
        submitForm(formName) {
            this.$refs[formName].validate(valid => {
                if (valid) {
                    let url = this.branch.id != 0 ? '/branch/edit' : '/branch/add'
                    this.postBody(url, this.branch, result => {
                        this.$router.push({ name: "organization" });
                    })
                } else {
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        notify(rs) {
            this.$message('你选择的地址是:'+ rs.address);
            this.branch.address = rs.address;
            this.branch.longitude = rs.point.lng;
            this.branch.latitude = rs.point.lat;
        }
    },
    mounted() {
        this.get('/branch/top', result => {
            this.nodes = result.data.tops;
        });
        if (this.branch.id != 0) {
            this.get('/branch/' + this.branch.id, result => {
                this.branch = result.data.branch;
            });
        }
    },
    created() {
        this.branch.id = this.$route.params.id;
    },
    components: {
        BMapComponent
    }
}
</script>
<style>
.el-card__header {
    padding: 6px 20px;
}

.el-row {
    margin-bottom: 20px;
    &:last-child {
        margin-bottom: 0;
    }
}

.el-col {
    border-radius: 4px;
}

.bg-purple-dark {
    background: #99a9bf;
}

.bg-purple {
    background: #d3dce6;
}

.bg-purple-light {
    background: #e5e9f2;
}

.grid-content {
    border-radius: 4px;
    min-height: 36px;
}

.row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
}
</style>