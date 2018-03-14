//跳转到详细信息页面
function linkToDetail(taskName, ifOpenNew) {
    var requestMap = {};
    requestMap.taskName = taskName;

    var tempForm = document.createElement("form");
    tempForm.action = "http://localhost:9090/redis/redisData";
    tempForm.method = "post";
    tempForm.style.display = "none";
    if (ifOpenNew === "1") {
        tempForm.target = "_blank";
    }
    var dict = document.createElement("input");
    dict.name = "taskName";
    dict.value = taskName;
    tempForm.appendChild(dict);
    document.body.appendChild(tempForm);
    tempForm.submit();
    return tempForm;
}
