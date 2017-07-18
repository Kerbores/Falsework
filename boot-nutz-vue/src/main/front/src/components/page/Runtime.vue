<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="fa fa-home"></i> 首页</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="fa fa-camera"></i> 监控</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="fa fa-server"></i> 运行环境</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-tabs v-model="activeName2" type="card" @tab-click="handleClick">
            <el-tab-pane label="服务器状态" name="dynamic">
                <el-row :gutter="20">
                    <el-col :span="16">
                        <IEcharts class="echarts" :option="line"></IEcharts>
                    </el-col>
                    <el-col :span="8">
                        <IEcharts class="echarts" :option="gauge"></IEcharts>
                    </el-col>
                </el-row>
            </el-tab-pane>
            <el-tab-pane label="告警配置" name="config">告警配置</el-tab-pane>
            <el-tab-pane label="服务器信息" name="info">服务器信息</el-tab-pane>
        </el-tabs>
    </div>
    <!--sql详情-->
</template>

<script>
import IEcharts from 'vue-echarts-v3';
import axios from 'axios';
export default {
    components: {
        IEcharts
    },
    data() {
        return {
            ni: 0,
            no: 0,
            dr: 0,
            dw: 0,
            activeName2: 'dynamic',
            line: {
                color: ["#20a0ff", "#13CE66", "#F7BA2A", "#FF4949",'#540070', '#84ccd3'],
                title: {
                    text: '主机实时性能曲线'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    y: 'bottom',
                    data: ['CPU', 'JVM', 'RAM', 'SWAP', 'NETWORK IN', 'NETWORK OUT']
                },
                xAxis: [{
                    type: 'category',
                    boundaryGap: true,
                    data: []
                }],
                yAxis: [{
                    type: 'value',
                    scale: true,
                    name: '百分比',
                    max: 100,
                    min: 0,
                    boundaryGap: [0.2, 0.2]
                }, {
                    type: 'value',
                    scale: true,
                    max: 2500,
                    min: 0,
                    name: ' p/s',
                    boundaryGap: [0.2, 0.2]
                }],
                series: [{
                    name: 'CPU',
                    type: 'line',
                    smooth: true,
                    data: []
                }, {
                    name: 'JVM',
                    type: 'line',
                    smooth: true,
                    data: []
                }, {
                    name: 'RAM',
                    type: 'line',
                    smooth: true,
                    data: []
                },
                {
                    name: 'SWAP',
                    type: 'line',
                    smooth: true,
                    yAxisIndex: 0,
                    data: []
                },
                {
                    name: 'NETWORK IN',
                    type: 'line',
                    smooth: true,
                    yAxisIndex: 1,
                    data: []
                }, {
                    name: 'NETWORK OUT',
                    type: 'line',
                    smooth: true,
                    yAxisIndex: 1,
                    data: []
                }
                ]
            },
            gauge: {
                title: {
                    text: '主机实时性能仪表盘'
                },
                tooltip: {
                    formatter: "{a} <br/>{c} {b}"
                },
                series: [{
                    name: 'CPU',
                    type: 'gauge',
                    z: 3,
                    min: 0,
                    max: 100,
                    splitNumber: 10,
                    radius: '50%',
                    axisLine: { // 坐标轴线
                        lineStyle: { // 属性lineStyle控制线条样式
                            width: 10
                        }
                    },
                    axisTick: { // 坐标轴小标记
                        length: 15, // 属性length控制线长
                        lineStyle: { // 属性lineStyle控制线条样式
                            color: 'auto'
                        }
                    },
                    splitLine: { // 分隔线
                        length: 20, // 属性length控制线长
                        lineStyle: { // 属性lineStyle（详见lineStyle）控制线条样式
                            color: 'auto'
                        }
                    },
                    title: {
                        textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                            fontWeight: 'bolder',
                            fontSize: 20,
                            fontStyle: 'italic'
                        }
                    },
                    detail: {
                        textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                            fontWeight: 'bolder'
                        }
                    },
                    data: [{
                        value: 40,
                        name: '%'
                    }]
                },
                {
                    name: 'Disk Read',
                    type: 'gauge',
                    center: ['20%', '55%'], // 默认全局居中
                    radius: '40%',
                    min: 0,
                    max: 120,
                    endAngle: 70,
                    splitNumber: 6,
                    axisLine: { // 坐标轴线
                        lineStyle: { // 属性lineStyle控制线条样式
                            width: 8
                        }
                    },
                    axisTick: { // 坐标轴小标记
                        length: 12, // 属性length控制线长
                        lineStyle: { // 属性lineStyle控制线条样式
                            color: 'auto'
                        }
                    },
                    splitLine: { // 分隔线
                        length: 20, // 属性length控制线长
                        lineStyle: { // 属性lineStyle（详见lineStyle）控制线条样式
                            color: 'auto'
                        }
                    },
                    pointer: {
                        width: 5
                    },
                    title: {
                        offsetCenter: [0, '-30%'], // x, y，单位px
                    },
                    detail: {
                        textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                            fontWeight: 'bolder'
                        }
                    },
                    data: [{
                        value: 1.5,
                        name: 'M/s'
                    }]
                },
                {
                    name: 'Disk Write',
                    type: 'gauge',
                    center: ['80%', '55%'], // 默认全局居中
                    radius: '40%',
                    min: 0,
                    max: 120,
                    startAngle: 110,
                    endAngle: -45,
                    splitNumber: 6,
                    axisLine: { // 坐标轴线
                        lineStyle: { // 属性lineStyle控制线条样式
                            width: 8
                        }
                    },
                    axisTick: { // 坐标轴小标记
                        length: 12, // 属性length控制线长
                        lineStyle: { // 属性lineStyle控制线条样式
                            color: 'auto'
                        }
                    },
                    splitLine: { // 分隔线
                        length: 20, // 属性length控制线长
                        lineStyle: { // 属性lineStyle（详见lineStyle）控制线条样式
                            color: 'auto'
                        }
                    },
                    pointer: {
                        width: 5
                    },
                    title: {
                        offsetCenter: ['30%', 0], // x, y，单位px
                    },
                    detail: {
                        textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                            fontWeight: 'bolder'
                        }
                    },
                    data: [{
                        value: 5.5,
                        name: 'M/s'
                    }]
                },
                ]
            },
        };
    },
    methods: {
        handleClick(tab, event) {
            console.log(tab, event);
        }
    },
    mounted() {
        const self = this;
        setInterval(function () {
            axios.get(baseUrl + '/metrics').then(resp => {
                let res = resp.data;
                self.line.xAxis[0].data.push(new Date().toLocaleTimeString()); //时间戳
                self.line.series[0].data.push(res['cpu.usage']);//cpu
                self.line.series[1].data.push(res['jvm.usage']);
                self.line.series[2].data.push(res['mem.user.percent']);
                self.line.series[3].data.push(res['swap.usage']);
                // self.line.series[4].data.push(res['disk.io']);
                self.line.series[4].data.push(res['tcp.in.segs'] - (self.ni == 0 ? res['tcp.in.segs'] : self.ni));
                self.ni = res['tcp.in.segs'];
                self.line.series[5].data.push(res['tcp.out.segs'] - (self.no == 0 ? res['tcp.out.segs'] : self.no));
                self.no = res['tcp.out.segs']

                self.gauge.series[0].data[0].value = res['cpu.usage'].toFixed(2);
                self.gauge.series[1].data[0].value = ((res['disk.read'] - (self.dr == 0 ? res['disk.read'] : self.dr)) / 1024 / 1024 / 2).toFixed(2);
                self.dr = res['disk.read'];
                self.gauge.series[2].data[0].value = ((res['disk.write'] - (self.dw == 0 ? res['disk.write'] : self.dw)) / 1024 / 1024 / 2).toFixed(2);
                self.dw = res['disk.write'];
            })
        }, 2000)
    }
};
</script>

<style>
.echarts {
    float: left;
    width: 100%;
    height: 450px;
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
