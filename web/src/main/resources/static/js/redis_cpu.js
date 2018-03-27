function init(used_cpu_sys, abscissa) {
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    option = {
        xAxis: {
            type: 'category',
            data: [abscissa[6], abscissa[5], abscissa[4], abscissa[3], abscissa[2],
                abscissa[1], abscissa[0]]
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

//切换
function switchover(task_name, ifOpenNew) {
    var tempForm = document.createElement("form");
    tempForm.action = "http://localhost:9090/redis/cpu";
    tempForm.method = "post";
    tempForm.style.display = "none";
    if (ifOpenNew === "1") {
        tempForm.target = "_blank";
    }
    var dict = document.createElement("input");
    dict.name = "taskName";
    dict.value = task_name;
    tempForm.appendChild(dict);
    dict.name = "period";
    dict.value = document.getElementById('unit').value;
    tempForm.appendChild(dict);
    document.body.appendChild(tempForm);
    tempForm.submit();
    return tempForm;
}

//单位
function change_abscissa(unit, ifOpenNew) {
    var tempForm = document.createElement("form");
    tempForm.action = "http://localhost:9090/redis/cpu";
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