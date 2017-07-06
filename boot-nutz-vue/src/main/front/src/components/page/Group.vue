<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="fa fa-home"></i> 首页</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="fa fa-object-group"></i> 分组</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="fa fa-list"></i> 分组列表</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-row>
            <el-col :span="6">
                <el-input placeholder="请输入内容" v-model="pager.paras.key" icon="search">
                    <div slot="append">
                        <el-button type="primary" icon="search" @click=" pager.pager.pageNumber = 1 ;doSearch()">GO</el-button>
                    </div>
                </el-input>
            </el-col>
            <el-col :span="6" :offset="12">
                <el-button type="primary" icon="plus" @click="addEditShow = true">添加分组</el-button>
            </el-col>
        </el-row>
        <el-table :data="pager.dataList" border style="width: 100%">
            <el-table-column prop="id" label="ID" sortable>
            </el-table-column>
            <el-table-column prop="name" label="名称">
            </el-table-column>
            <el-table-column prop="delete" label="状态">
                <template scope="scope">
                    <el-tag :type="scope.row.delete  ? 'danger' : 'success'" close-transition>{{scope.row.delete ? '禁用' : '启用'}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="description" label="描述">
            </el-table-column>
            <el-table-column label="操作">
                <template scope="scope">
                    <el-dropdown>
                        <el-button type="primary">
                            操作
                            <i class="el-icon-caret-bottom el-icon--right"></i>
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item>
                                <div @click="handleEdit(scope.$index,scope.row)">
                                    <i class="fa fa-edit"></i> 编辑分组</div>
                            </el-dropdown-item>
                            <el-dropdown-item v-show="!scope.row.installed">
                                <div @click="handleDelete(scope.$index,scope.row)">
                                    <i :class="scope.row.delete  ? 'fa fa-toggle-on':'fa fa-toggle-off'"></i> {{scope.row.delete ? '启用分组' : '禁用分组'}}</div>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </template>
            </el-table-column>
        </el-table>
        <el-row>
            <el-col :span="6" :offset="18">
                <el-pagination style="float:right" layout="prev, pager, next" :total="pager.pager.recordCount" :page-size="pager.pager.pageSize" :current-page.sync="pager.pager.pageNumber" v-show="pager.pager.pageCount != 0" @current-change="changePage">
                </el-pagination>
            </el-col>
        </el-row>
        <!-- 弹框区域-->
        <el-dialog :title="group.id == 0 ? '添加分组' : '编辑分组' " :visible.sync="addEditShow" size="tiny">
            <el-form :model="group" :rules="checkGroup" ref="groupForm">
                <el-form-item label="名称" :label-width="formLabelWidth" prop="name">
                    <el-input v-model="group.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="描述" :label-width="formLabelWidth" prop="description">
                    <el-input v-model="group.description" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="addEditShow = false ; user = {installed:false}">取 消</el-button>
                <el-button type="primary" @click="saveOrUpdateGroup('groupForm')">确 定</el-button>
            </div>
        </el-dialog>
    
    </div>
</template>

<script>
import axios from 'axios';
import moment from 'moment'
export default {
    data() {
        return {
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
            },
            addEditShow: false,
            group: {
                id: 0,
                name: '',
                description: '',
                installed: false
            },
            checkGroup: {
                name: [
                    { required: true, message: '请输入分组名称', trigger: 'blur' }
                ],
                description: [
                    { required: true, message: '请输入分组描述', trigger: 'blur' }
                ]
            },
            formLabelWidth: '120px'
        }
    },
    watch: {},
    methods: {
        changePage() {
            if (this.pager.paras.key) {
                this.doSearch();
            } else {
                this.loadData();
            }
        },
        doSearch() {
            this.get('/group/search?page=' + this.pager.pager.pageNumber + '&key=' + this.pager.paras.key, result => {
                this.pager = result.data.pager;
            })
        },
        saveOrUpdateGroup(formName) {
            this.$refs[formName].validate(valid => {
                if (valid) {
                    let url = this.group.id ? '/group/edit' : '/group/add'
                    this.postBody(url, this.group, result => {
                        this.changePage();
                        this.addEditShow = false;
                    })
                } else {
                    return false;
                }
            })
        },
        handleEdit(index, row) {
            let id = row.id;
            this.get('/group/' + id, result => {
                this.group = result.data.group;
                this.addEditShow = true;
            })
        },
        handleDelete(index, row) {
            let id = row.id;
            this.$confirm('确认' + (row.delete ? '启用' : '禁用') + '分组?', '操作确认', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.get('/group/toggle/' + id + '/' + !row.delete, result => {
                    this.$message({
                        type: 'success',
                        message: (row.delete ? '启用' : '禁用') + '成功!'
                    });
                    window.setTimeout(() => {
                        this.changePage();
                    }, 2000)
                })
            }).catch(() => {
            });
        },
        loadData() {
            this.get('/group/list?page=' + this.pager.pager.pageNumber, result => {
                this.pager = result.data.pager;
                this.pager.paras = { key: '' }
            })
        }
    },
    mounted: function () {
        this.loadData();
    }
}
</script>
<style>
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