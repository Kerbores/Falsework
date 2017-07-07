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
            </el-breadcrumb>
        </div>
        <el-row>
            <el-col :span="6">
                <el-input placeholder="请输入内容" v-model="pager.paras.key">
                    <el-button type="primary" slot="append" icon="search" @click=" pager.pager.pageNumber = 1 ;doSearch()">GO</el-button>
                </el-input>
            </el-col>
            <el-col :span="6" :offset="12">
                <el-button type="primary" icon="plus" @click="goAdd">添加机构</el-button>
            </el-col>
        </el-row>
        <el-table :data="pager.dataList" border style="width: 100%">
            <el-table-tree-column :remote="remote" file-icon="icon icon-file" folder-icon="icon icon-folder" parentKey="parentId" prop="id" label="ID"></el-table-tree-column>
            <el-table-column prop="name" label="机构名称">
            </el-table-column>
            <el-table-column prop="description" label="机构描述">
            </el-table-column>
             <el-table-column prop="address" label="机构地址">
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
                                    <i class="fa fa-edit"></i> 编辑机构</div>
                            </el-dropdown-item>
                            <el-dropdown-item v-show="!scope.row.installed">
                                <div @click="handleDelete(scope.$index,scope.row)">
                                    <i class="fa fa-trash-o"></i> 删除机构</div>
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
            }
        }
    },
    watch: {},
    methods: {
        goAdd(){
            this.$router.push({ name: 'oaoe', params: { id: 0 }});
        },
        remote(row, callback) {
            this.get('/branch/sub/' + row.id, result => {
                const data = [];
                result.data.branchs.forEach(item => {
                    item.children = [{}];
                    item.depth = row.depth ? row.depth + 1 : 1;
                    data.push(item)
                })
                // row.children = data;
                callback(data);
            })
        },
        changePage() {
            if (this.pager.paras.key) {
                this.doSearch();
            } else {
                this.loadData();
            }
        },
        doSearch() {
            this.get('/branch/search?page=' + this.pager.pager.pageNumber + '&group=' + this.groupId + '&key=' + this.pager.paras.key, result => {
                console.log(result);
                this.pager = result.data.pager;
                this.pager.dataList.forEach(item => {
                    item.children = [{}];
                    item.depth = 1;
                });
            })
        },
        handleEdit(index, row) {
            this.$router.push({ name: 'oaoe', params: { id: row.id }});
        },
        handleDelete(index, row) {
            let id = row.id;
            this.$confirm('确认删除机构数据?', '删除确认', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.get('/branch/delete/' + id, result => {
                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                    window.setTimeout(() => {
                        this.changePage();
                    }, 2000)
                })
            }).catch(() => {
            });
        },
        loadData() {
            this.get('/branch/list?page=' + this.pager.pager.pageNumber, result => {
                this.pager = result.data.pager;
                this.pager.paras = { key: '' };
                this.pager.dataList.forEach(item => {
                    item.children = [{}];
                    item.depth = 1;
                });
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