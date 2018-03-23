function init(connected_clients) {
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    option = {
        title: {
            text: 'Clients'
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

//切换
function switchover(task_name, ifOpenNew) {
    var requestMap = {};
    requestMap.taskName = task_name;

    var tempForm = document.createElement("form");
    tempForm.action = "http://localhost:9090/redis/clients";
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