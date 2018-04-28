//跳转到详细信息页面
function linkToDetail(task_name, ifOpenNew) {
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

