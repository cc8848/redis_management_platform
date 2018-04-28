function init(used_memory_lua_human, used_memory_rss_human, used_memory_human, used_memory_peak_human, abscissa) {
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    option = {
        title: {
            text: '堆叠区域图'
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
                data : [abscissa[6], abscissa[5], abscissa[4], abscissa[3], abscissa[2],
                    abscissa[1], abscissa[0]]
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
//切换
function switchover(task_name, ifOpenNew) {
    var requestMap = {};
    requestMap.taskName = task_name;

    var tempForm = document.createElement("form");
    tempForm.action = "http://localhost:9090/redis/memory";
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
//单位
function change_abscissa(unit, ifOpenNew) {
    var tempForm = document.createElement("form");
    tempForm.action = "http://localhost:9090/redis/memory";
    tempForm.method = "post";
    tempForm.style.display = "none";
    if (ifOpenNew === "1") {
        tempForm.target = "_blank";
    }
    var dict = document.createElement("input");
    dict.name = "taskName";
    dict.value = $("#taskName").val();
    tempForm.appendChild(dict);
    dict.name = "period";
    dict.value = unit;
    tempForm.appendChild(dict);
    document.body.appendChild(tempForm);
    tempForm.submit();
    return tempForm;
}