//切换
function switchover(task_name, ifOpenNew) {
    var requestMap = {};
    requestMap.taskName = task_name;

    var tempForm = document.createElement("form");
    tempForm.action = "http://localhost:9090/redis/data";
    tempForm.method = "post";
    tempForm.style.display = "none";
    if (ifOpenNew === "1") {
        tempForm.target = "_blank";
    }
    var dict = document.createElement("input");
    dict.name = "taskName";
    dict.value = task_name;
    tempForm.appendChild(dict);
    document.body.appendChild(tempForm);
    tempForm.submit();
    return tempForm;
}

function init_all(connected_clients, used_cpu_sys, used_memory_lua_human, used_memory_rss_human, used_memory_human,
                  used_memory_peak_human) {
    init_clients(connected_clients);
    init_cpu(used_cpu_sys);
    init_memory(used_memory_lua_human, used_memory_rss_human, used_memory_human, used_memory_peak_human);
}
function init_clients(connected_clients) {
    var dom = document.getElementById("clients");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    option = {
        title: {
            text: 'Clients',
            top: '10%'
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            data: [connected_clients[6], connected_clients[5], connected_clients[4], connected_clients[3],
                connected_clients[2], connected_clients[1], connected_clients[0]],
            type: 'line',
        }]
    };
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
}

function init_cpu(used_cpu_sys) {
    var dom = document.getElementById("cpu");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    option = {
        title: {
            text: 'CPU',
            top: '10%'
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            data: [used_cpu_sys[6], used_cpu_sys[5], used_cpu_sys[4], used_cpu_sys[3], used_cpu_sys[2],
                used_cpu_sys[1], used_cpu_sys[0]],
            type: 'line',
            smooth: true
        }]
    };
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
}

function init_memory(used_memory_lua_human, used_memory_rss_human, used_memory_human, used_memory_peak_human) {
    var dom = document.getElementById("memory");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    option = {
        title: {
            text: 'Memory'
        },
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        legend: {
            // data:['邮件营销','联盟广告','视频广告','直接访问','搜索引擎']
            data:['引擎所占内存大小','驻集大小','内存总量','消耗峰值']
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : ['周一','周二','周三','周四','周五','周六','周日']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'引擎所占内存大小',
                type:'line',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[used_memory_lua_human[6], used_memory_lua_human[5], used_memory_lua_human[4],
                    used_memory_lua_human[3], used_memory_lua_human[2], used_memory_lua_human[1],
                    used_memory_lua_human[0]]
            },
            {
                name:'驻集大小',
                type:'line',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[used_memory_rss_human[6], used_memory_rss_human[5], used_memory_rss_human[4],
                    used_memory_rss_human[3], used_memory_rss_human[2], used_memory_rss_human[1],
                    used_memory_rss_human[0]]
            },
            {
                name:'内存总量',
                type:'line',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[used_memory_human[6], used_memory_human[5], used_memory_human[4], used_memory_human[3],
                    used_memory_human[2], used_memory_human[1], used_memory_human[0]]
            },
            {
                name:'消耗峰值',
                type:'line',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[used_memory_peak_human[6], used_memory_peak_human[5], used_memory_peak_human[4],
                    used_memory_peak_human[3], used_memory_peak_human[2], used_memory_peak_human[1],
                    used_memory_peak_human[0]]
            }
            // ,
            // {
            //     name:'搜索引擎',
            //     type:'line',
            //     stack: '总量',
            //     label: {
            //         normal: {
            //             show: true,
            //             position: 'top'
            //         }
            //     },
            //     areaStyle: {normal: {}},
            //     data:[820, 932, 901, 934, 1290, 1330, 1320]
            // }
        ]
    };
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
}

//翻页
function page_turning(page, type) {
    var requestMap = {};
    requestMap.page = page;

    var tempForm = document.createElement("form");
    tempForm.action = "http://localhost:9090/redis/" + type;
    tempForm.method = "post";
    tempForm.style.display = "none";
    var dict = document.createElement("input");
    dict.name = "page";
    dict.value = page;
    tempForm.appendChild(dict);
    document.body.appendChild(tempForm);
    tempForm.submit();
    return tempForm;
}

//切换
function linkToDetail(task_name, type, key) {
    var requestMap = {};
    requestMap.taskName = task_name;
    requestMap.key = key;

    var tempForm = document.createElement("form");
    tempForm.action = "http://localhost:9090/redis/" + type;
    tempForm.method = "post";
    tempForm.style.display = "none";
    tempForm.target = "_blank";
    var dict = document.createElement("input");
    dict.name = "taskName";
    dict.value = task_name;
    tempForm.appendChild(dict);
    dict.name = "key";
    dict.value = key;
    tempForm.appendChild(dict);
    document.body.appendChild(tempForm);
    tempForm.submit();
    return tempForm;
}