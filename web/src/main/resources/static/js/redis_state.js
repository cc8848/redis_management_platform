//Control switch
var t = 1;
function control_switch(task_name, directive) {
    var requestMap = {};
    requestMap.taskName = task_name;
    requestMap.directive = directive;

    $.ajax({
        url: "http://localhost:9090/redis/controlSwitch",
        type: 'POST',
        data: {
            reqJsonString: JSON.stringify(requestMap),
        },
        dataType: 'json',
        success: function (data) {
            if (data['returnCode'] === "0") {
                alert("success!");
            } else{
                alert("fail: " + data['message']);
            }
            document.getElementById("State").innerText = data['state'];
        }
    })
}


//跳转到详细信息页面
function linkToDetail(taskName) {
    var requestMap = {};
    requestMap.taskName = taskName;

    var tempForm = document.createElement("form");
    tempForm.action = "http://localhost:9090/redis/data";
    tempForm.method = "post";
    tempForm.style.display = "none";
    tempForm.target = "_blank";
    var dict = document.createElement("input");
    dict.name = "taskName";
    dict.value = taskName;
    tempForm.appendChild(dict);
    document.body.appendChild(tempForm);
    tempForm.submit();
    return tempForm;
}

